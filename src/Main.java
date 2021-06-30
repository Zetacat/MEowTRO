import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import meowtro.PlayTime;
import meowtro.Position;
import meowtro.button.DestroyButton;
import meowtro.button.FastforwardButton;
import meowtro.button.LocomotiveButton;
import meowtro.button.MyButton;
import meowtro.button.PauseButton;
import meowtro.button.PlayButton;
import meowtro.button.RailwayButton;
import meowtro.button.StationButton;
import meowtro.game.Config;
import meowtro.game.Game;
import meowtro.game.GameFactory;
import meowtro.game.Region;
import meowtro.game.entityManager.LocomotiveManager;
import meowtro.game.entityManager.RailwayManager;
import meowtro.game.entityManager.StationManager;
import meowtro.game.passenger.Passenger;
import meowtro.metro_system.railway.Line;
import meowtro.metro_system.railway.LineColor;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.station.Station;
import meowtro.metro_system.train.Locomotive;

public class Main extends Application {
    private Pane root;
    private List<MyButton> buttons;
    private AnimationTimer timer;
    private AnimationTimer innerTimer;
    private long formerTimeStamp_cmd;
    private long duration_cmd = 99;
    private long formerTimeStamp_animate;
    private long duration_animate;
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        GameFactory gameFactory = new GameFactory();
        Config config = new Config("./resources/defaultConfig.properties", "./resources/localconfig/localConfig1.properties");
        Game game = gameFactory.createGame(config);
        
        StationManager sm = new StationManager(game);
        RailwayManager rm = new RailwayManager(game);
        LocomotiveManager lm = new LocomotiveManager(game);
        
        game.start(sm);

        this.root = new Pane();
        root.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    game.onClick(new Position((int) Math.round(event.getSceneY()), (int) Math.round(event.getSceneX())));
                }
            }
        );

        root.getChildren().add(new ImageView(new Image(new FileInputStream("./image/map_3.png"))));

        StationButton stationButton = new StationButton(10, game, sm);
        root.getChildren().add(stationButton.getButton());
        
        DestroyButton destroyButton = new DestroyButton(0, game);
        root.getChildren().add(destroyButton.getButton());
        
        // TEMPORARILY add a new line after game start
        Line line = new Line(game.getCity(), LineColor.BLUE);
        RailwayButton railwayButton = new RailwayButton(2, game, rm, line);
        root.getChildren().add(railwayButton.getButton());

        LocomotiveButton locomotiveButton = new LocomotiveButton(2, game, lm);
        root.getChildren().add(locomotiveButton.getButton());

        this.timer = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                duration_animate = PlayTime.duration_animate;
                System.out.printf("duration_animate: %d%n", duration_animate);

                // implement timer
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
                        }
                    }
                    for (Line line : game.getCity().getAllLines()) {
                        for (Locomotive locomotive : line.getLocomotives()) {
                            if (!root.getChildren().contains(locomotive.getImage())) {
                                root.getChildren().add(locomotive.getImage());
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
                if (currentNanoTime-formerTimeStamp_animate > duration_animate) {
                    // conduct command
                    game.update();
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

        PauseButton pauseButton = new PauseButton(0, "./image/button/pause.png", this.innerTimer);
        root.getChildren().add(pauseButton.getButton());

        PlayButton playButton = new PlayButton(0, "./image/button/play.png", this.innerTimer);
        root.getChildren().add(playButton.getButton());

        FastforwardButton ffButton = new FastforwardButton(0, "./image/button/fast_forward.png");
        root.getChildren().add(ffButton.getButton());

        Scene scene = new Scene(root, 640, 480);
        primaryStage.setTitle("MEwoTRO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {        
        launch(args);
    }
}
