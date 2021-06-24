package Meowtro.Game;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Mountain extends Obstacle {

    public Mountain(BufferedImage background, Color color) {
        super(background, color);
    }
    
    public static void main(String[] args) {
        // read image
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("image/map_1.png"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // test Mountain
        Color color = new Color(219, 231, 182);
        Mountain mountain = new Mountain(image, color);
        System.out.println("(200, 1000)[Green]: " + mountain.isBlocked(new Position(200, 1000)));
        System.out.println("(300, 1200)[Gray]: " + mountain.isBlocked(new Position(300, 1200)));
        System.out.println("(400, 900)[Blue]: " + mountain.isBlocked(new Position(400, 900)));
        System.out.println("(400, 700)[White]: " + mountain.isBlocked(new Position(400, 700)));
        System.out.println("(210, 576)[Border]: " + mountain.isBlocked(new Position(210, 576)));
    }

}
