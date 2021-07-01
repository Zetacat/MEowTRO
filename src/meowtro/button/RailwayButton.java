package meowtro.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import meowtro.game.Game;
import meowtro.game.entityManager.RailwayManager;
import meowtro.game.onClickEvent.RailwayBuilder;
import meowtro.metro_system.railway.Line;

public class RailwayButton extends MyButton {
    private Button btn;
    public Button getButton() {
        return this.btn;
    }
    private Game game;
    private RailwayManager railwayManager;
    private Line line;

    public RailwayButton(int cost, Game game, RailwayManager railwayManager, Line line) {
        this.cost = cost;
        this.btn = new Button();
        this.game = game;
        this.railwayManager = railwayManager;
        this.line = line;
        
        btn.setText("Build Railway");
        btn.setLayoutX(100);
        btn.setLayoutY(150);
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
        RailwayBuilder b = new RailwayBuilder(this.railwayManager, this.game, this.line);
        this.game.setNowEvent(b);
    }
}
