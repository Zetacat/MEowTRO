package Meowtro.Game;

import java.util.Random;

public class Game {
    
    private static Config config = new Config("./config.properties");
    public static boolean DEBUG = true;
    public static Random randomGenerator = new Random();
    private static int balance = Integer.parseInt(Game.config.get("balance.default"));

    public static Config getConfig() {
        return Game.config;
    }

    public static int getBalance() {
        return Game.balance;
    }

    public static void setBalance(int newBalance) {
        Game.balance = newBalance;
    }

}
