package meowtro.game;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import meowtro.Position;
import meowtro.game.passenger.CutInLineElder;
import meowtro.game.passenger.Passenger;
import meowtro.metro_system.*;
import meowtro.metro_system.station.Station;

public class Region {
    
    private List<List<Boolean>> positions = new ArrayList<List<Boolean>>();
    private City city = null;
    private double spawnRate = Double.parseDouble(Game.getConfig().get("spawn.rate.default"));
    private double CutInLineElderProb = Double.parseDouble(Game.getConfig().get("cut.in.line.elder.prob"));
    private List<Integer> satisfications = new ArrayList<Integer>();
    private List<Passenger> passengers = new ArrayList<Passenger>();
    private List<Station> stations = new ArrayList<Station>();
    private int tranportedPassengerCount = 0;
    private int index = 0;
    private static int nextIndex = 0;

    private static int getNextIndex() {
        return (Region.nextIndex++);
    }

    public Region(List<List<Boolean>> positions, City city) {
        this.positions = positions;
        this.city = city;
        this.index = Region.getNextIndex();
        if (Game.DEBUG)
            System.out.println(this.toString() + " constructed");
    }

    public int getRegionSatisfaction() {
        // compute the average satisfaction of the last "region.satisfaction.window" passengers
        int satisfactionsSize = this.satisfications.size();
        if (satisfactionsSize == 0)
            return 0;
        
        // compute average
        int count = Math.min(satisfactionsSize, Integer.parseInt(Game.getConfig().get("region.satisfaction.window")));
        List<Integer> recentPassengerSatisfactions = this.satisfications.subList(satisfactionsSize - count, satisfactionsSize);
        OptionalDouble avgPassengerSatisfactions = recentPassengerSatisfactions.stream().mapToInt(Integer::intValue).average();
        int regionSatisfaction = (int)Math.round(avgPassengerSatisfactions.getAsDouble());

        if (Game.DEBUG)
            System.out.println(this.toString() + " satisfaction = " + regionSatisfaction);
        return regionSatisfaction;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public boolean containPosition(Position position) {
        return this.positions.get(position.i).get(position.j);
    }

    public Position getRandomPositionInRegion() {
        Position newPosition = null;
        while (newPosition == null) {
            int i = Game.randomGenerator.nextInt(this.positions.size());
            int j = Game.randomGenerator.nextInt(this.positions.get(0).size());
            // should be in the region
            if (!this.positions.get(i).get(j))
                continue;
            // the passenger should not be at a station
            boolean atStation = false;
            for (Station station:this.stations) {
                Position stationPosition = station.getPosition();
                if (i == stationPosition.i && j == stationPosition.j) {
                    atStation = true;
                    break;
                }
            }
            if (!atStation)
                newPosition = new Position(i, j);
        }
        return newPosition;
    }

    public Passenger spawnPassenger() {
        // get the position of the new passenger
        Position newPassengerPosition = this.getRandomPositionInRegion();

        // get random destination station, random passenger type, spawn passenger
        Station destinationStation = this.city.getRandomStationFromDifferentRegion(this);
        Passenger newPassenger = (
            Game.randomGenerator.nextDouble() < this.CutInLineElderProb?
            new CutInLineElder(this, newPassengerPosition, destinationStation): 
            new Passenger(this, newPassengerPosition, destinationStation)
        );
        this.passengers.add(newPassenger);
        return newPassenger;
    }

    public void removeStation(Station station) {
        this.stations.remove(station);
    }

    public void addStation(Station newStation) {
        this.stations.add(newStation);
    }

    public void addSatisfactionScoreFromPassenger(Passenger passenger) {
        this.satisfications.add(passenger.evaluateSatisfaction());
    }

    public void removePassenger(Passenger passenger, boolean arrivedDestination) {
        if (arrivedDestination) {
            this.tranportedPassengerCount += 1;
            this.city.addTotalTransportedPassengerCount();
        }
        this.addSatisfactionScoreFromPassenger(passenger);
        this.passengers.remove(passenger);
    }



    public void update() {
        // update passengers
        for (Passenger passenger: this.passengers)
            passenger.update();
        
        // update stations
        for (Station station: this.stations)
            station.update();
        
        // spawn passenger on the probability of spawnRate
        if (Game.randomGenerator.nextDouble() < this.spawnRate)
            this.spawnPassenger();
    }

    @Override
    public String toString() {
        return String.format("R%d", this.index);
    }

    /****** MAIN ******/
    // public static void main(String[] args) {
    //     config = new Config("./defaultConfig.properties", "./localConfig.properties")
    // }
}
