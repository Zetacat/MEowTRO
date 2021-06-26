package meowtro.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameFactory {
    
    public Game createGame(Config config) {
        // read image
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(Game.getConfig().get("image.path")));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        // create city
        City city = new City(image);

        return new Game(config, city);
    }

    /******* MAIN *******/
    public static void main(String[] args) {
        Config config = new Config("./defaultConfig.properties", "./localConfig.properties");
        GameFactory gameFactory = new GameFactory();
        gameFactory.createGame(config);
    }

}
