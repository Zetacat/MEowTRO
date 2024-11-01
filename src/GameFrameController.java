import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import meowtro.game.*;
import meowtro.game.entityManager.CarManager;
import meowtro.game.entityManager.LocomotiveManager;
import meowtro.game.entityManager.RailwayManager;
import meowtro.game.entityManager.StationManager;
import javafx.scene.input.MouseEvent;
import meowtro.Position;
import meowtro.PlayTime;
import meowtro.button.*;
import meowtro.metro_system.railway.*;
import meowtro.metro_system.train.*;
import meowtro.metro_system.station.Station;
import javafx.animation.AnimationTimer;
import meowtro.game.passenger.Passenger;
import meowtro.timeSystem.*;
import javafx.scene.Node;
public class GameFrameController {
    private AnimationTimer timer;
    private AnimationTimer innerTimer;
    private long formerTimeStamp_cmd;
    private long duration_cmd = 99;
    private long formerTimeStamp_animate;
    private long duration_animate;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    public ProgressBar satisficBar;
    
    public Text gameCalenderTime; 
    public Text balanceText;
    public Text message;

    @FXML
    void initialize() {

    }
    @FXML
    private Button buildStationButton;

    // @FXML
    // private AnchorPane myMap;
    @FXML
    private Pane myMap;

    private Game game=null;
    private StationManager statoionManager = null;
    private RailwayManager railwayManager = null;
    private LocomotiveManager locomotiveManager = null;
    private CarManager carManager = null;
    private MyButton blueRailwayButton, brownRailwayButton, greenRailwayButton, orangeRailwayButton, redRailwayButton, yellowRailwayButton;
    private MyButton stationButton, destroyButton, locomotiveButton, carButton, upgradeButton;
    private MyButton pauseButton, playButton, ffButton;
    public void setGame(Game game){
        this.game = game;
    }
    public void setManager(StationManager statoionManager, RailwayManager railwayManager, LocomotiveManager locomotiveManager, CarManager carManager){
        this.statoionManager = statoionManager;
        this.railwayManager = railwayManager;
        this.locomotiveManager = locomotiveManager;
        this.carManager = carManager;
    }
    public void addButton(){
        int railwayPrice = Integer.parseInt(Game.getConfig().get("price.railway"));
        int stationPrice = Integer.parseInt(Game.getConfig().get("price.station"));
        int locomotivePrice = Integer.parseInt(Game.getConfig().get("price.locomotive"));
        int carPrice = Integer.parseInt(Game.getConfig().get("price.car"));
        this.stationButton = new StationButton(stationPrice, game, statoionManager, "./image/button/station.png");
        this.destroyButton = new DestroyButton(0, game, "./image/button/remove.png");

        Line blueLine = new Line(game.getCity(), LineColor.BLUE);
        this.blueRailwayButton = new RailwayButton(railwayPrice, game, railwayManager, blueLine, "./image/button/railway.png");

        Line brownLine = new Line(game.getCity(), LineColor.PURPLE);
        this.brownRailwayButton = new RailwayButton(railwayPrice, game, railwayManager, brownLine, "./image/button/railway.png");

        Line greenLine = new Line(game.getCity(), LineColor.GREEN);
        this.greenRailwayButton = new RailwayButton(railwayPrice, game, railwayManager, greenLine, "./image/button/railway.png");

        Line orangeLine = new Line(game.getCity(), LineColor.ORANGE);
        this.orangeRailwayButton = new RailwayButton(railwayPrice, game, railwayManager, orangeLine, "./image/button/railway.png");

        Line redLine = new Line(game.getCity(), LineColor.RED);
        this.redRailwayButton = new RailwayButton(railwayPrice, game, railwayManager, redLine, "./image/button/railway.png");

        Line yellowLine = new Line(game.getCity(), LineColor.YELLOW);
        this.yellowRailwayButton = new RailwayButton(railwayPrice, game, railwayManager, yellowLine, "./image/button/railway.png");

        this.locomotiveButton = new LocomotiveButton(locomotivePrice, game, locomotiveManager, "./image/button/locomotive.png");
        this.carButton = new CarButton(carPrice, game, carManager, "./image/button/car.png");
        this.upgradeButton = new UpgradeButton(0, game, "./image/button/car.png");
    }
    public void setTimerAndButton(){
        Pane root = this.myMap;
        this.timer = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                duration_animate = PlayTime.duration_animate;
                balanceText.setText(Game.getBalance()+"$");
                // implement timer
                if (Game.textMessage.size() > 0){
                    String msg = Game.textMessage.poll();
                    message.setText(msg);
                    // System.out.println(msg);
                }
                else{
                    message.setText("");
                    
                }
                
                if (currentNanoTime-formerTimeStamp_cmd > duration_cmd) {
                    // conduct command
                    for (Region region : game.getCity().getRegions()) {
                        for (Station station : region.getStations()) {
                            if (!root.getChildren().contains(station.getImage())) {
                                root.getChildren().add(station.getImage());
                            }
                        }
                    }
                    for (Line line : game.getCity().getAllLines()) {
                        for (Railway railway : line.getRailways()) {
                            if (!root.getChildren().contains(railway.getImage())) {
                                root.getChildren().add(railway.getImage());
                            }
                            for (Node obstacle : railway.getObstacleImages()) {
                                if (!root.getChildren().contains(obstacle)) {
                                    root.getChildren().add(obstacle);
                                }
                            }
                        }
                    }
                    for (Line line : game.getCity().getAllLines()) {
                        for (Locomotive locomotive : line.getLocomotives()) {
                            if (!root.getChildren().contains(locomotive.getImage())) {
                                root.getChildren().add(locomotive.getImage());
                            }
                            for (Car car : locomotive.getCars()) {
                                if (!root.getChildren().contains(car.getImage())) {
                                    root.getChildren().add(car.getImage());
                                }
                            }
                        }
                    }
                    for (Object o : game.getObjectToBeRemoved()) {
                        if (root.getChildren().contains(o)) {
                            root.getChildren().remove(o);
                        }
                    }
                    game.resetObjectToBeRemoved();
                    // static refresh
                    formerTimeStamp_cmd = currentNanoTime;
                }
            }
        };
        this.timer.start();
        
        this.innerTimer = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                // implement timer
                if (game.gameIsEnded){
                    timer.stop();
                    message.setText("Game is End!!!");
                    message.setStyle("-fx-text-fill: #A1B56C");
                    innerTimer.stop();
                }
                if (currentNanoTime-formerTimeStamp_animate > duration_animate) {
                    // conduct command
                    game.update();
                    satisficBar.setProgress(Game.satisfactionBarRate);
                    TimeLine gameTimer = TimeLine.getInstance();
                    String calenderTime = gameTimer.getCalenderTime();
                    gameCalenderTime.setText(calenderTime);;
                    
                    for (Region region : game.getCity().getRegions()) {
                        for (Passenger passenger : region.getPassengers()) {
                            if (!root.getChildren().contains(passenger.getImage())) {
                                root.getChildren().add(passenger.getImage());
                            }
                        }
                    }
                    // animate refresh
                    formerTimeStamp_animate = currentNanoTime;
                }
            }
        };
        this.innerTimer.start();

        this.pauseButton = new PauseButton(0, "./image/button/pause.png", this.innerTimer);

        this.playButton = new PlayButton(0, "./image/button/play.png", this.innerTimer);

        this.ffButton = new FastforwardButton(0, "./image/button/fast_forward.png");
    }
    public void getPosOnAnchorPane(MouseEvent event){
        game.onClick(new Position((int) Math.round(event.getY()), (int) Math.round(event.getX())));
    }
    public void buildStation(){
        this.stationButton.onClick();
    }
    public void buildBlueRailway(){
        this.blueRailwayButton.onClick();
    }
    public void buildGreenRailway(){
        this.greenRailwayButton.onClick();
    }
    public void buildBrownRailway(){
        this.brownRailwayButton.onClick();
    }
    public void buildRedRailway(){
        this.redRailwayButton.onClick();
    }
    public void buildYellowRailway(){
        this.yellowRailwayButton.onClick();
    }
    public void buildOrangeRailway(){
        this.orangeRailwayButton.onClick();
    }
    public void addLocomotive(){
        this.locomotiveButton.onClick();
    }
    public void addCar(){
        this.carButton.onClick();
    }
    public void pause(){
        this.pauseButton.onClick();
    }
    public void fastForward(){
        this.ffButton.onClick();
    }
    public void play(){
        this.playButton.onClick();
    }
    public void destroy(){
        this.destroyButton.onClick();
    }
    public void levelup(){
        this.upgradeButton.onClick();
    }

}
