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
        
    }

}
