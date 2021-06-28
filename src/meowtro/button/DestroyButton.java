package meowtro.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import meowtro.game.Game;
import meowtro.game.onClickEvent.Destroyer;

public class DestroyButton extends MyButton {
    private Button btn;
    public Button getButton() {
        return this.btn;
    }
    private Game game;

    public DestroyButton(int cost, Game game) {
        this.cost = cost;
        this.btn = new Button();
        this.game = game;
        btn.setText("Destroy");
        btn.setLayoutX(600);
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
        Destroyer d = new Destroyer(this.game);
        game.setNowEvent(d);
    }
}
