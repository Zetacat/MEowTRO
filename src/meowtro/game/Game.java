package meowtro.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import meowtro.metro_system.station.Station;
import meowtro.game.entityManager.StationManager;
import meowtro.game.onClickEvent.OnClickEvent;
import meowtro.timeSystem.TimeLine;
import meowtro.Position;

public class Game {
    
    private static Config config = null;
    private City city = null;
    public City getCity() {
        return this.city;
    }
    // private Stack<OnClickEvent> onClickEventStack = new Stack<OnClickEvent>();
    // private EventTrigger eventTrigger = null;
    // private GameTerminateChecker gameTerminatChecker = null;
    // private History history = null;
    // private ReplayVideoPage replayVideoPage = null;
    // private ExitPage exitPage = null;
    private int globalSatisfaction = 0;
    private static int balance = 0;
    public static Random randomGenerator = new Random();
    public static boolean DEBUG = true;

    private int maxStationNum;
    public int getMaxStationNum() {
        return this.maxStationNum;
    }
    private ArrayList<String> iconPaths;
    public ArrayList<String> getIconPaths() {
        return this.iconPaths;
    }

    public static ArrayList<String> listFiles(String dir) {
        File file = null;
        ArrayList<String> files = new ArrayList<>();
        try {
            file = new File(dir);
            for (String s : file.list()) {
                files.add(dir+"/"+s);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return files;
    }

    public Game(Config config) {
        Game.config = config;
        Game.setBalance(Integer.parseInt(config.get("balance.default")));
        Game.randomGenerator.setSeed(Long.parseLong(config.get("game.random.seed")));
        this.startTimeLine();
    }

    public static int getBalance() {
        return Game.balance;
    }

    public static void setBalance(int newBalance) {
        Game.balance = newBalance;
    }

    public void setCity(City city) {
        this.city = city;
        this.iconPaths = listFiles("./image/icon/");
        this.maxStationNum = this.iconPaths.size();
        this.city.setGame(this);
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

    public void start(StationManager stationManager) {
        // construct station at two randomly selected region
        List<Region> regions2AddStation = this.city.getNRandomRegions(2);
        for (Region region: regions2AddStation) {
            Position newStationPosition = region.getRandomPositionInRegion();
            stationManager.build(this.city, newStationPosition);
        }

        // run game
        for (int i = 0; i < 5; i++) {
            this.update();
        }
    }

    public static void setToyConfig(){
        config = new Config("../resources/defaultConfig.properties", "../resources/defaultConfig.properties"); 
    }

    private OnClickEvent nowEvent;
    public OnClickEvent getNowEvent() {
        return this.nowEvent;
    }
    public void setNowEvent(OnClickEvent event) {
        this.nowEvent = event;
    }

    public void onClick(Position position) {
        this.nowEvent.conduct(position);
    }

    private List<Object> objectToBeRemoved = new ArrayList<>();
    public List<Object> getObjectToBeRemoved() {
        return this.objectToBeRemoved;
    }
    public void deleteObject(Object o) {
        this.objectToBeRemoved.add(o);
    }
    public void resetObjectToBeRemoved() {
        this.objectToBeRemoved.clear();
    }

    public void stationOnClick(Station station) {
        this.nowEvent.conduct(station);
    }

}
