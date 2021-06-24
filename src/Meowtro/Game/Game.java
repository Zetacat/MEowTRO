package Meowtro.Game;

public class Game {
    
    private static Config config = new Config("./config.properties");
    public static boolean DEBUG = true;

    public static Config getConfig() {
        return Game.config;
    }

}
