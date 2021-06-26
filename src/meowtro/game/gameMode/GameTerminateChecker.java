package meowtro.game.gameMode;

import meowtro.game.Game;

public interface GameTerminateChecker {
    boolean gameIsEnded(Game game);
    String getGameMode();
}
