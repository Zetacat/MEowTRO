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
import meowtro.Position;
import meowtro.button.DestroyButton;
import meowtro.button.MyButton;
import meowtro.button.StationButton;
import meowtro.game.Config;
import meowtro.game.Game;
import meowtro.game.GameFactory;
import meowtro.game.Region;
import meowtro.game.entityManager.StationManager;
import meowtro.game.passenger.Passenger;
import meowtro.metro_system.station.Station;

public class Main extends Application {
    private Pane root;
    private List<MyButton> buttons;
    private AnimationTimer timer;
    private AnimationTimer innerTimer;
    private long formerTimeStamp_cmd;
    private long duration_cmd = 999999;
    private long formerTimeStamp_animate;
    private long duration_animate = 999999;
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        GameFactory gameFactory = new GameFactory();
        Config config = new Config("./resources/defaultConfig.properties", "./resources/localconfig/localConfig1.properties");
        Game game = gameFactory.createGame(config);
        
        StationManager sm = new StationManager(game);
        
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
        
        Scene scene = new Scene(root, 640, 480);
        primaryStage.setTitle("MEwoTRO");
        primaryStage.setScene(scene);
        primaryStage.show();

        this.timer = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                // System.out.printf("Command Loop Time: %d %n", currentNanoTime);
                // implement timer
                if (currentNanoTime-formerTimeStamp_cmd > duration_cmd) {
                    // conduct command
                    for (Region region : game.getCity().getRegions()) {
                        for (Station station : region.getStations()) {
                            if (!root.getChildren().contains(station.getImage())) {
                                System.out.printf("station name: %s%n", station.getIconPath());
                                root.getChildren().add(station.getImage());
                            }
                        }
                    }
                    for (Object o : game.getObjectToBeRemoved()) {
                        if (root.getChildren().contains(o)) {
                            root.getChildren().remove(o);
                        }
                    }
                    game.resetObjectToBeRemoved();
                    System.out.printf("object in pane: %d%n", root.getChildren().size());
                    // static refresh
                    formerTimeStamp_cmd = currentNanoTime;
                }
            }
        };
        this.timer.start();
        
        this.innerTimer = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                // System.out.printf("Animation Loop Time: %d %n", currentNanoTime);
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
    }
    
    public static void main(String[] args) {        
        launch(args);
    }
}
