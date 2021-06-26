package meowtro.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import meowtro.timeSystem.TimeLine;

public class Game {
    
    private static Config config = null;
    private City city = null;
    // private Stack<OnClickEvent> onClickEventStack = new Stack<OnClickEvent>();
    // private EventTrigger eventTrigger = null;
    // private GameTerminateChecker gameTerminatChecker = null;
    // private History history = null;
    // private ReplayVideoPage replayVideoPage = null;
    // private ExitPage exitPage = null;
    private int globalSatisfaction = 0;
    // private List<UIElement> uiElements = new ArrayList<UIElement>();
    private static int balance = 10; //Integer.parseInt(Game.config.get("balance.default"));
    public static Random randomGenerator = new Random();
    public static boolean DEBUG = true;

    public Game(Config config, City city) {
        Game.config = config;
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

    public static int getBalance() {
        return Game.balance;
    }

    public static void setBalance(int newBalance) {
        Game.balance = newBalance;
    }

    public static void setToyConfig(){
        config = new Config("../resources/defaultConfig.properties", "../resources/defaultConfig.properties"); 
    }
}
