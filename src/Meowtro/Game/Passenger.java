package Meowtro.Game;

import Meowtro.Position;

public class Passenger {
    
    private Region birthRegion = null;
    protected Position position = null;
    private long spawnTime = 0;
    private long lifeTimeLimit = Long.parseLong(Game.getConfig().get("passenger.life.time.limit"));
    protected Station destinationStation = null;
    private double walkingSpeed = Double.parseDouble(Game.getConfig().get("passenger.walking.speed"));
    protected Station currentStation = null;
    private Car currentCar = null;
    private int traveledStationCount = 0;

    public Passenger(Region birthRegion, long spawnTime, Position position, Station destinationStation) {
        this.birthRegion = birthRegion;
        this.position = position;
        this.spawnTime = spawnTime;
        this.destinationStation = destinationStation;
        if (Game.DEBUG) {
            System.out.println("Passenger constructed.");
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
            System.out.println("Passenger selfExplode");

        this.die(false);
    }
    
    public void arriveDestination() {
        if (Game.DEBUG)
            System.out.println("Passenger arrive destination.");

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
        }
    }

    public void update() {
        // self explode if exceed life time limit
        if (Timeline.now() - spawnTime > this.lifeTimeLimit) {
            this.selfExplode();
            return;
        }

        // walking passenger
        if (this.currentStation == null && this.currentCar == null) {
            this.walkTowardClosestStation();
        }

        // traveling passenger
        else if (this.currentCar != null) {
            // TODO: update position for passengers traveling
        }
    }
}