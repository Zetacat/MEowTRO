package Meowtro.Game;

import Meowtro.Position;

public class Passenger {
    
    Region birthRegion = null;
    Position position = null;
    long spawnTime = 0;
    Station destinationStation = null;
    double walkingSpeed = Double.parseDouble(Game.getConfig().get("passenger.walking.speed"));
    Station currentStation = null;
    Car currentCar = null;
    int traveledStationCount = 0;

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
        this.die();
    }
    
    public void arriveDestination() {
        if (Game.DEBUG)
            System.out.println("Passenger arrive destination.");
        this.die();
    }

    private void die() {
        // TODO: passenger die

    }

    public void enterStation(Station station) {
        this.position = station.getPosition();
        // arrive station
        if (station == this.destinationStation) {
            this.arriveDestination();
        }
        // enter station and wait
        else {
            station.insertPassenger(this, -1);
        }
    }

    public void enterCar(Car car) {
        car.addPassenger(this);
        this.currentCar = car;
    }

    public int evaluateSatisfaction() {
        // TODO: evaluate satisfaction
        return 0;
    }

    private void walkTowardClosestStation() {
        Station closestStation = this.findClosestStationInRegion(this.birthRegion);
        Position closestStationPosition = closestStation.getPosition();
        double distance = this.position.l2distance(closestStationPosition);
        // enter station if able to reach
        if (distance < this.walkingSpeed) {
            this.enterStation(closestStation);
        }
        // otherwise, walk toward
        else {
            double ratio = this.walkingSpeed / distance;
            double newPositionI = this.position.i + (closestStationPosition.i - this.position.i) * ratio;
            double newPositionJ = this.position.j + (closestStationPosition.j - this.position.j) * ratio;
            this.position = new Position((int) Math.round(newPositionI), (int) Math.round(newPositionJ));
        }
    }
}
