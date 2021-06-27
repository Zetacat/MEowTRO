package meowtro.game;

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
        game.start();
    }

}
