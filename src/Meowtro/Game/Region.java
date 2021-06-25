package Meowtro.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import Meowtro.Position;

public class Region {
    
    private List<List<Boolean>> positions = new ArrayList<List<Boolean>>();
    private int spawnRate = 0;
    private List<Integer> satisfications = new ArrayList<Integer>();
    // private List<Passenger> passengers = new ArrayList<Passenger>();
    // private List<Station> stations = new ArrayList<Station>();
    private int tranportedPassengerCount = 0;

    public Region(List<List<Boolean>> positions, int spawnRate) {
        this.positions = positions;
        this.spawnRate = spawnRate;
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

    public boolean containPosition(Position position) {
        return this.positions.get(position.i).get(position.j);
    }

    /****** MAIN ******/
    public static void main(String[] args) {

    }

}
