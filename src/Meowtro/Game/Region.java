package Meowtro.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import Meowtro.Position;

public class Region {
    
    private List<List<Boolean>> positions = new ArrayList<List<Boolean>>();
    private City city = null;
    private double spawnRate = Double.parseDouble(Game.getConfig().get("spawn.rate.default"));
    private double CutInLineElderProb = Double.parseDouble(Game.getConfig().get("cut.in.line.elder.prob"));
    private List<Integer> satisfications = new ArrayList<Integer>();
    private List<Passenger> passengers = new ArrayList<Passenger>();
    private List<Station> stations = new ArrayList<Station>();
    private int tranportedPassengerCount = 0;

    public Region(List<List<Boolean>> positions, City city) {
        this.positions = positions;
        this.city = city;
        if (Game.DEBUG)
            System.out.println("Region constructed.");
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
            System.out.println("Region satisfaction = " + regionSatisfaction);
        return regionSatisfaction;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public boolean containPosition(Position position) {
        return this.positions.get(position.i).get(position.j);
    }

    public Passenger spawnPassenger() {
        // get the position of the new passenger
        Position newPassengerPosition = null;
        while (newPassengerPosition != null) {
            int i = Game.randomGenerator.nextInt(this.positions.size());
            int j = Game.randomGenerator.nextInt(this.positions.get(0).size());
            // the passenger should be in the region
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
            // valid new passenger position
            if (!atStation) {
                newPassengerPosition = new Position(i, j);
            }
        }

        // get random destination station, random passenger type, spawn passenger
        Station destinationStation = this.city.getRandomStationFromDifferentRegion(this);
        Passenger newPassenger = (
            Game.randomGenerator.nextDouble() < this.CutInLineElderProb?
            new CutInLineElder(this, Timeline.now(), newPassengerPosition, destinationStation): 
            new Passenger(this, Timeline.now(), newPassengerPosition, destinationStation)
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


    /****** MAIN ******/
    public static void main(String[] args) {

    }

}
