package Meowtro.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.awt.Color;
import javax.imageio.ImageIO;
import Meowtro.Position;

public class City {
    
    private List<Region> regions = new ArrayList<Region>();
    private List<Obstacle> obstacles = new ArrayList<Obstacle>();
    // private List<Line> lines = new ArrayList<Line>();
    // private List<Station> stations = new ArrayList<Station>();
    private int totalTransportedPassenger = 0;
    

    public City(BufferedImage background) {
        // iterate the whole image to extract pixels of the same color
        Hashtable<Color, List<Position>> color2pixels = new Hashtable<Color, List<Position>>();
        for (int i = 0; i < background.getHeight(); i++) {
            for (int j = 0; j < background.getWidth(); j++) {
                int pixel = background.getRGB(j, i);
                Color pixelColor = new Color(pixel);
                if (!color2pixels.containsKey(pixelColor))
                    color2pixels.put(pixelColor, new ArrayList<Position>());
                color2pixels.get(pixelColor).add(new Position(i, j));
            }
        }

        // calculate area threshold, obstacle colors
        double areaThreshold = Double.parseDouble(Game.getConfig().get("region.background.ratio.threshold")) * background.getWidth() * background.getHeight();
        Hashtable<Color, Class<? extends Obstacle>> color2obstacle = new Hashtable<Color, Class<? extends Obstacle>>();
        for (String key: Game.getConfig().getAllKeys()) {
            if (key.startsWith("obstacle.") && key.endsWith(".rgb")) {
                // get color for the obstacle
                String[] rgbStr = Game.getConfig().get(key).split("\\.", 0);
                Color color = new Color(Integer.parseInt(rgbStr[0]), Integer.parseInt(rgbStr[1]), Integer.parseInt(rgbStr[2]));
                // get obstacle name
                String obstacleName = key.split("\\.", 0)[1];
                obstacleName = this.getClass().getPackage().getName() + "." + obstacleName.substring(0, 1).toUpperCase() + obstacleName.substring(1);
                // add <color, obstacle class> to color2obstacle
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends Obstacle> obstacleClass = (Class<? extends Obstacle>)Class.forName(obstacleName);
                    color2obstacle.put(color, obstacleClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        // iterate every color
        Iterator<Entry<Color, List<Position>>> iter = color2pixels.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Color, List<Position>> colorPixelsPair = iter.next();
            // skip colors with too less pixels (edges)
            if (colorPixelsPair.getValue().size() < areaThreshold)
                continue;
            
            // construct obstacle
            if (color2obstacle.containsKey(colorPixelsPair.getKey())) {
                Class<? extends Obstacle> obstacleClass = color2obstacle.get(colorPixelsPair.getKey());
                List<List<Boolean>> positions = this.positionList2Boolean2DList(colorPixelsPair.getValue(), background.getWidth(), background.getHeight());
                try {
                    this.obstacles.add(obstacleClass.getConstructor(List.class).newInstance(positions));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }         

            // construct region
            
        }

        if (Game.DEBUG)
            System.out.println("City constructed.");
    }

    private List<List<Boolean>> positionList2Boolean2DList(List<Position> positionsList, int width, int height) {
        // initialize boolean 2d list
        List<List<Boolean>> positions = new ArrayList<List<Boolean>>();
        for (int i = 0; i < height; i++) {
            positions.add(new ArrayList<Boolean>());
            for (int j = 0; j < width; j++) {
                positions.get(i).add(false);
            }
        }
        
        // set positions in positionsList to true
        for (Position position: positionsList) {
            positions.get(position.i).set(position.j, true);
        }
        return positions;
    }

    /****** MAIN ******/
    public static void main(String[] args) {

        // read image
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("image/map_1.png"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // test City
        City city = new City(image);

    }

}
