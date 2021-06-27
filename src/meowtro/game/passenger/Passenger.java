package meowtro.game.passenger;

import java.util.List;
import meowtro.Position;
import meowtro.game.Game;
import meowtro.game.Region;
import meowtro.metro_system.*;
import meowtro.metro_system.station.Station;
import meowtro.metro_system.train.Car;
import meowtro.metro_system.train.Locomotive;
import meowtro.timeSystem.TimeLine;

public class Passenger {
    
    private Region birthRegion = null;
    protected Position position = null;
    private long spawnTime = 0;
    private long lifeTimeLimit = Long.parseLong(Game.getConfig().get("passenger.life.time.limit").strip());
    protected Station destinationStation = null;
    private double walkingSpeed = Double.parseDouble(Game.getConfig().get("passenger.walking.speed"));
    protected Station currentStation = null;
    private Car currentCar = null;
    private int traveledStationCount = 0;
    private boolean isDead = false;
    private int index = 0;
    private static int nextIndex = 0;

    private static int getNextIndex() {
        return (Passenger.nextIndex++);
    }

    public Passenger(Region birthRegion, Position position, Station destinationStation) {
        this.birthRegion = birthRegion;
        this.position = position;
        this.spawnTime = TimeLine.getInstance().getCurrentTotalTimeUnit();
        this.destinationStation = destinationStation;
        this.index = Passenger.getNextIndex();
        if (Game.DEBUG) {
            System.out.println(this.toString() + " constructed at " + position.toString() + ", dest: " + destinationStation.toString());
        }
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
        if (Game.DEBUG)
            System.out.println(this.toString() + " selfExplode");

        this.die(false);
    }
    
    public void arriveDestination() {
        if (Game.DEBUG)
            System.out.println(this.toString() + " arrive destination.");
        int ticket = Integer.parseInt(Game.getConfig().get("passenger.ticket.per.station")) * this.traveledStationCount;
        Game.setBalance(Game.getBalance() + ticket);
        this.die(true);
    }

    private void die(boolean arrivedDestination) {
        this.isDead = true; 
        if (Game.DEBUG){
            System.out.println(this.toString() + " die ('x_x')");
            return; 
        }
        this.birthRegion.removePassenger(this, arrivedDestination);
    }

    public void enterStation(Station station) {
        this.position = station.getPosition();
        this.currentCar = null;
        if (Game.DEBUG)
            System.out.printf(this.toString() + " entered station %s\n", station.name); 
        // arrive station
        if (station == this.destinationStation) {
            this.arriveDestination();
        }
        // enter station and wait
        else {
            station.insertPassenger(this, -1);
            this.currentStation = station;
        }
    }

    public void enterCar(Car car) {
        car.addPassenger(this);
        this.currentCar = car;
        this.currentStation = null;
    }

    public int evaluateSatisfaction() {
        // TODO: evaluate satisfaction
        return 0;
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
            this.position = new Position((int) Math.round(newPositionI), (int) Math.round(newPositionJ));
            if (Game.DEBUG)
                System.out.printf("Passenger move to %s\n", position.toString()); 
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
        if (this.isDead){
            return; 
        }

        // self explode if exceed life time limit
        if (TimeLine.getInstance().getCurrentTotalTimeUnit() - spawnTime > this.lifeTimeLimit) {
            this.selfExplode();
            return;
        }

        // walking passenger
        if (this.currentStation == null && this.currentCar == null) {
            this.walkTowardClosestStation();
        }

        if (Game.DEBUG)
            System.out.println(this.toString() + "move to " + this.position.toString());
    }

    @Override
    public String toString() {
        return String.format("passenger P%d", this.index);
    }
}
