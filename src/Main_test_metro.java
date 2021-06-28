import meowtro.*;
import meowtro.game.*;
import meowtro.game.entityManager.*;

public class Main_test_metro {
    
    public static void main(String[] args) {
        GameFactory gameFactory = new GameFactory();
        Config config = new Config("./resources/defaultConfig.properties", "./resources/localconfig/localConfig1.properties");
        Game game = gameFactory.createGame(config);
        StationManager stationManager = new StationManager(game);
        game.start(new StationManager(game));

        for (int i = 0; i < 20; i++) {

            // if (i == 5) {
            //     Position newStationPosition = game.getCity().getNRandomRegions(1).get(0).getRandomPositionInRegion();
            //     stationManager.build(game.getCity(), newStationPosition);
            // }

            game.update();
        }
    }

}
