package meowtro.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import meowtro.game.Game;
import meowtro.game.entityManager.CarManager;
import meowtro.game.onClickEvent.CarBuilder;

public class CarButton extends MyButton {
    private Button btn;
    public Button getButton() {
        return this.btn;
    }
    private Game game;
    private CarManager carManager;

    public CarButton(int cost, Game game, CarManager carManager) {
        this.cost = cost;
        this.btn = new Button();
        this.game = game;
        this.carManager = carManager;
        
        btn.setText("Build car");
        btn.setLayoutX(100);
        btn.setLayoutY(250);
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
        CarBuilder b = new CarBuilder(this.carManager, this.game);
        this.game.setNowEvent(b);
    }
}
