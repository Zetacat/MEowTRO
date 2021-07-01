package meowtro.game.entityManager;

import meowtro.Position;
import meowtro.game.Game;
import meowtro.metro_system.Direction;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.train.Locomotive;

public class LocomotiveManager extends EntityManager {
    private Game game;

    public LocomotiveManager(Game game) {
        this.game = game;
    }

    public void build(Railway railway, Position position) {
        Locomotive locomotive = new Locomotive(railway, position, Direction.FORWARD, this.colorMap.get(railway.getLine().getColor()), this.game);
        railway.getLine().addLocomotive(locomotive);
    }

    public void destroy(Locomotive locomotive) {
        locomotive.destroy();
    }
}
