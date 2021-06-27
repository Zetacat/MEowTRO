package meowtro.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import meowtro.timeSystem.TimeLine;

public class Game {
    
    private static Config config = null;
    private City city = null;
    private int globalSatisfaction = 0;
    private static int balance = 0;
    public static Random randomGenerator = new Random();
    public static boolean DEBUG = true;

    public Game(Config config) {
        Game.config = config;
        Game.setBalance(Integer.parseInt(config.get("balance.default")));
    }

    public static int getBalance() {
        return Game.balance;
    }

    public static void setBalance(int newBalance) {
        Game.balance = newBalance;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public static Config getConfig() {
        return Game.config;
    }

    public static void debug() {
        Game.DEBUG = true;
    }

    public void startTimeLine() {
        TimeLine.getInstance().reset();
    }

    public void onClick() {
        // TODO: on click
    }

    public void saveRecord() {
        // TODO: save record
    }

    public void generatePopupMessage(String Message) {
        // TODO: generate popup message
    }

    public void update() {

        // on click events
        // while (!this.onClickEventStack.empty()) {
        //     // TODO: onclick event
        // }

        // TODO: check event

        // update city
        this.city.update();
        this.globalSatisfaction = this.city.getGlobalStatisfaction();
    }


    public static void setToyConfig(){
        config = new Config("../resources/defaultConfig.properties", "../resources/defaultConfig.properties"); 
    }
}
