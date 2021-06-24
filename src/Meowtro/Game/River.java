package Meowtro.Game;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class River extends Obstacle {

    public River(BufferedImage background, Color color) {
        super(background, color);

        if (Game.DEBUG) {
            System.out.println("River constructed.");
        }
    }
    
}
