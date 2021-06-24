package Meowtro.Game;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.Color;

public abstract class Obstacle {
    
    protected List<List<Boolean>> positions = new ArrayList<List<Boolean>>();
    
    public Obstacle(BufferedImage background, Color color) {
        // iterate all pixels, check if match color
        for (int r = 0; r < background.getHeight(); r++) {
            this.positions.add(new ArrayList<Boolean>());
            for (int c = 0; c < background.getWidth(); c++) {
                int pixel = background.getRGB(c, r);
                Color pixelColor = new Color(pixel);
                this.positions.get(r).add(color.equals(pixelColor)? true : false);
            }
        }
    }

    public Boolean isBlocked(Position position) {
        return this.positions.get(position.i).get(position.j);
    }
    
}
