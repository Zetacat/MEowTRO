package meowtro.game.entityManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.paint.Color;
import meowtro.Position;
import meowtro.game.City;
import meowtro.game.Game;
import meowtro.game.Region;
import meowtro.metro_system.Direction;
import meowtro.metro_system.railway.LineColor;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.station.Station;
import meowtro.metro_system.train.Locomotive;

public class LocomotiveManager extends EntityManager {
    private Game game;

    public LocomotiveManager(Game game) {
        this.game = game;
    }

    public void build(Position position) {}
    public void build(City city, Position position) {}
    public void build(Railway railway, Position position) {
        Locomotive locomotive = new Locomotive(railway, position, Direction.FORWARD, this.colorMap.get(railway.getLine().getColor()));
        railway.getLine().addLocomotive(locomotive);
    }

    public void destroy(Position position) {}
    public void destroy(Station station) {}
    public void destroy(Locomotive locomotive) {
        locomotive.destroy();
    }
}
