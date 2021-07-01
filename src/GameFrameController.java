import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class GameFrameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {

    }
    @FXML
    private Button buildStationButton;

    @FXML
    private AnchorPane myMap;

    public void buildStation(){
        System.out.println("You Pressed build Station!!");
    }
    public void buildOnMap(){
        this.myMap.getChildren().add(new Button("Test"));
    }
}
