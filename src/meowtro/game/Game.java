package meowtro.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import meowtro.timeSystem.TimeLine;

public class Game {
    
    private static Config config = new Config("./config.properties");
    private City city = null;
    private Stack<OnClickEvent> onClickEventStack = new Stack<OnClickEvent>();
    private EventTrigger eventTrigger = null;
    private GameTerminateChecker gameTerminatChecker = null;
    private History history = null;
    private ReplayVideoPage replayVideoPage = null;
    private ExitPage exitPage = null;
    private int globalSatisfaction = 0;
    private List<UIElement> uiElements = new ArrayList<UIElement>();
    private static int balance = Integer.parseInt(Game.config.get("balance.default"));
    public static Random randomGenerator = new Random();
    public static boolean DEBUG = true;

    public Game(Config config, City city,  EventTrigger eventTrigger, GameTerminateChecker gameTerminateChecker, 
                History history, ReplayVideoPage replayVideoPage, ExitPage exitPage) {
        Game.config = config;
        this.city = city;
        this.eventTrigger = eventTrigger;
        this.gameTerminatChecker = gameTerminateChecker;
        this.history = history;
        this.replayVideoPage = replayVideoPage;
        this.exitPage = exitPage;
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
        while (!this.onClickEventStack.empty()) {
            // TODO: onclick event
        }

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

}
