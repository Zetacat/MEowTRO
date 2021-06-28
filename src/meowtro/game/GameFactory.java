package meowtro.game;

import meowtro.game.entityManager.StationManager;

public class GameFactory {
    
    public Game createGame(Config config) {
        Game game = new Game(config);
        City city = new City();
        game.setCity(city);

        return game;
    }

    /******* MAIN *******/
    public static void main(String[] args) {
        GameFactory gameFactory = new GameFactory();
        Config config = new Config("./resources/defaultConfig.properties", "./resources/localconfig/localConfig1.properties");
        Game game = gameFactory.createGame(config);
        game.start(new StationManager(game));
    }

}
