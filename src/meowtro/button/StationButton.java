package meowtro.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import meowtro.game.Game;
import meowtro.game.entityManager.StationManager;
import meowtro.game.onClickEvent.StationBuilder;

public class StationButton extends MyButton {
    private Button btn;
    public Button getButton() {
        return this.btn;
    }
    private Game game;
    private StationManager stationManager;

    public StationButton(int cost, Game game, StationManager stationManager) {
        this.cost = cost;
        this.btn = new Button();
        this.game = game;
        this.stationManager = stationManager;
        btn.setText("Build Station");
        btn.setLayoutX(100);
        btn.setLayoutY(50);
        btn.setOnAction(
            new EventHandler<ActionEvent>() {    
                @Override
                public void handle(ActionEvent event) {
                    onClick();
                }
            }
        );
    }
    protected void onClick() {
        StationBuilder b = new StationBuilder(this.stationManager, this.game);
        this.game.setNowEvent(b);
    }

}
