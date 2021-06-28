package meowtro.game.passenger;

import java.io.FileInputStream;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import meowtro.Position;
import meowtro.game.Game;
import meowtro.game.Region;
import meowtro.metro_system.*;
import meowtro.metro_system.station.Station;
import meowtro.metro_system.train.Car;
import meowtro.metro_system.train.Locomotive;
import meowtro.timeSystem.TimeLine;

public class Passenger {
    
    protected enum State {
        WALKING,
        AT_STATION,
        TRAVELING,
        ARRIVED
    }

    protected Region birthRegion = null;
    protected Position position = null;
    protected long spawnTime = 0;
    protected static long lifeTimeLimit = Long.parseLong(Game.getConfig().get("passenger.life.time.limit").strip());
    protected Station destinationStation = null;
    protected double walkingSpeed = Double.parseDouble(Game.getConfig().get("passenger.walking.speed"));
    protected static int expectedTimePerStation = Integer.parseInt(Game.getConfig().get("passenger.expected.time.per.station"));
    protected Station currentStation = null;
    protected Car currentCar = null;
    protected int traveledStationCount = 0;
    protected State state;
    protected int index = 0;
    protected static int nextIndex = 0;
    
    protected static int getNextIndex() {
        return (Passenger.nextIndex++);
    }

    private ImageView image;
    private void setImage() {
        try {
            Image img = new Image(new FileInputStream(this.destinationStation.getIconPath()));
            this.image = new ImageView(img);
            this.image.setPickOnBounds(true);
            this.image.setFitHeight(10);
            this.image.setFitWidth(10);
            setImagePosition();
        } catch (Exception e) {
            System.out.println("Image doesn't exist!");
        }
    }
    public ImageView getImage() {
        return this.image;
    }
    private void setImagePosition() {
        this.image.setLayoutX(this.position.i);
        this.image.setLayoutY(this.position.j);
    }


    public Passenger(Region birthRegion, Position position, Station destinationStation) {
        this.birthRegion = birthRegion;
        this.position = position;
        this.spawnTime = TimeLine.getInstance().getCurrentTotalTimeUnit();
        this.destinationStation = destinationStation;
        this.index = Passenger.getNextIndex();
        this.state = State.WALKING;
        if (Game.DEBUG) {
            System.out.println(this.toString() + " constructed at " + position.toString() + ", dest: " + destinationStation.toString());
        }
        setImage();
    }
    
    public Station findClosestStationInRegion(Region region) {
        double closestDistance = Double.MAX_VALUE;
        Station closestStation = null;
        for (Station station: this.birthRegion.getStations()) {
            double currentDistance = station.getPosition().l2distance(this.position);
            if (currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestStation = station;
            }
        }
        return closestStation;
    }

    public int findShortestPath(Station station1, Station station2) {
        return ShortestPathCalculator.findShortestPath(station1, station2);
    }

    public void selfExplode() {
        System.out.printf("passenger_%d is exploding%n", this.index);
        if (this.state == State.AT_STATION)
            this.currentStation.removePassenger(this);
        else if (this.state == State.TRAVELING)
            this.currentCar.removePassenger(this);
        this.die(false);

        if (Game.DEBUG)
            System.out.println(this.toString() + " self exploded");
    }
    
    public void arriveDestination() {
        if (Game.DEBUG)
            System.out.println(this.toString() + " arrive destination");
        
        this.state = State.ARRIVED;
        int ticket = Integer.parseInt(Game.getConfig().get("passenger.ticket.per.station")) * this.traveledStationCount;
        Game.setBalance(Game.getBalance() + ticket);
        this.die(true);
    }

    private void die(boolean arrivedDestination) {
        this.birthRegion.removePassenger(this, arrivedDestination);
    }

    public void enterStation(Station station) {
        this.position = station.getPosition();
        this.currentCar = null;
        this.traveledStationCount += 1;
        this.state = State.AT_STATION;
        
        // arrive station
        if (station == this.destinationStation) {
            this.arriveDestination();
        }
        // enter station and wait
        else {
            this.currentStation = station;
            station.insertPassenger(this, -1);
        }
    }

    public void enterCar(Car car) {
        car.addPassenger(this);
        this.currentCar = car;
        this.currentStation = null;
        this.state = State.TRAVELING;
    }

    public int evaluateSatisfaction() {
        if (this.state != State.ARRIVED)
            return 0;
        double timeSpent = (double)(TimeLine.getInstance().getCurrentTotalTimeUnit() - this.spawnTime);
        double expectedTravelTime = (double)(Passenger.expectedTimePerStation * this.traveledStationCount);
        return (int) Math.round(expectedTravelTime / timeSpent);
    }

    private void walkTowardClosestStation() {
        Station closestStation = this.findClosestStationInRegion(this.birthRegion);
        // do not move if no station in region
        if (closestStation == null)
            return;

        // move
        Position closestStationPosition = closestStation.getPosition();
        double distance = this.position.l2distance(closestStationPosition);
        if (distance < this.walkingSpeed) {
            // enter station if able to reach
            this.enterStation(closestStation);
        }
        else {
            // otherwise, walk toward
            double ratio = this.walkingSpeed / distance;
            double newPositionI = this.position.i + (closestStationPosition.i - this.position.i) * ratio;
            double newPositionJ = this.position.j + (closestStationPosition.j - this.position.j) * ratio;
            this.position = new Position(newPositionI, newPositionJ);
            // this.position = new Position((int) Math.round(newPositionI), (int) Math.round(newPositionJ));
            setImagePosition();
        }

        if (Game.DEBUG) {
            if (this.state == State.WALKING)
                System.out.println(this.toString() + " moving toward " + closestStation.toString() + " now at " + this.position.toString());
        }
    }

    public boolean willingToGetOn(Locomotive locomotive) {
        // calculate the shortest path of all candidate paths
        List<Station> adjacentStation = locomotive.getCurrentStation().getAdjacents();
        int shortestPath = Integer.MAX_VALUE;
        for (Station adjStation: adjacentStation) {
            int pathThroughAdj2dest = ShortestPathCalculator.findShortestPath(adjStation, this.destinationStation);
            if (pathThroughAdj2dest < shortestPath)
                shortestPath = pathThroughAdj2dest;
        }

        // calculate the shortest path of current path
        int currentShortestPath = ShortestPathCalculator.findShortestPath(locomotive.getNextDstStation(), this.destinationStation);
        return (currentShortestPath <= shortestPath);
    }

    public boolean willingToGetOff(Locomotive locomotive){
        if (locomotive.getCurrentStation() == destinationStation){
            return true; 
        }
        return !willingToGetOn(locomotive);  
    }

    public void update() {

        // self explode if exceed life time limit
        if (TimeLine.getInstance().getCurrentTotalTimeUnit() - spawnTime > Passenger.lifeTimeLimit) {
            this.selfExplode();
            return;
        }

        // walking passenger
        if (this.state == State.WALKING) {
            this.walkTowardClosestStation();
        }
    }

    @Override
    public String toString() {
        return String.format("P%d(%d)", this.index, this.spawnTime);
    }
}
