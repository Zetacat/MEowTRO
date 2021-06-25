package Meowtro.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Region {
    
    private List<List<Boolean>> positions = new ArrayList<List<Boolean>>();
    private int spawnRate = 0;
    private List<Integer> satisfications = new ArrayList<Integer>();
    // private List<Passenger> passengers = new ArrayList<Passenger>();
    // private List<Station> stations = new ArrayList<Station>();
    private int tranportedPassengerCount = 0;

    public Region(BufferedImage background, Color color, int spawnRate) {
        // iterate all pixels, check if match color
        for (int r = 0; r < background.getHeight(); r++) {
            this.positions.add(new ArrayList<Boolean>());
            for (int c = 0; c < background.getWidth(); c++) {
                int pixel = background.getRGB(c, r);
                Color pixelColor = new Color(pixel);
                this.positions.get(r).add(color.equals(pixelColor)? true : false);
            }
        }
        // set spawnRate
        this.spawnRate = spawnRate;
    }

    public int getRegionSatisfaction() {
        // compute the average satisfaction of the last "region.satisfaction.window" passengers
        int satisfactionsSize = this.satisfications.size();
        if (satisfactionsSize == 0)
            return 0;
        
        // compute average
        int count = Math.min(satisfactionsSize, Integer.parseInt(Game.getConfig().get("region.satisfaction.window")));
        List<Integer> recentPassengerSatisfactions = this.satisfications.subList(satisfactionsSize - count, satisfactionsSize);
        OptionalDouble avg = recentPassengerSatisfactions.stream().mapToInt(Integer::intValue).average();
        return (int)Math.round(avg.getAsDouble());
    }

    /****** MAIN ******/
    public static void main(String[] args) {

    }

}
