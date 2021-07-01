package meowtro.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import meowtro.game.Game;
import meowtro.game.entityManager.LocomotiveManager;
import meowtro.game.onClickEvent.LocomotiveBuilder;

public class LocomotiveButton extends MyButton {
    private Button btn;
    public Button getButton() {
        return this.btn;
    }
    private Game game;
    private LocomotiveManager locomotiveManager;

    public LocomotiveButton(int cost, Game game, LocomotiveManager locomotiveManager) {
        this.cost = cost;
        this.btn = new Button();
        this.game = game;
        this.locomotiveManager = locomotiveManager;
        
        btn.setText("Build Locomotive");
        btn.setLayoutX(100);
        btn.setLayoutY(200);
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
        LocomotiveBuilder b = new LocomotiveBuilder(this.locomotiveManager, this.game);
        this.game.setNowEvent(b);
    }
}
