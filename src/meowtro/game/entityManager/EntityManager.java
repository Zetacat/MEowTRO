package meowtro.game.entityManager;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;
import meowtro.Position;
import meowtro.game.City;
import meowtro.game.obstacle.Obstacle;
import meowtro.metro_system.railway.Line;
import meowtro.metro_system.railway.LineColor;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.station.Station;
import meowtro.metro_system.train.Locomotive;

public abstract class EntityManager {
    protected Map<LineColor,Color> colorMap = Map.ofEntries(
        new AbstractMap.SimpleEntry<LineColor,Color>(LineColor.RED, Color.RED),
        new AbstractMap.SimpleEntry<LineColor,Color>(LineColor.BLUE, Color.BLUE),
        new AbstractMap.SimpleEntry<LineColor,Color>(LineColor.GREEN, Color.GREEN),
        new AbstractMap.SimpleEntry<LineColor,Color>(LineColor.YELLOW, Color.YELLOW),
        new AbstractMap.SimpleEntry<LineColor,Color>(LineColor.ORANGE, Color.ORANGE),
        new AbstractMap.SimpleEntry<LineColor,Color>(LineColor.BROWN, Color.BROWN)
    );

    public void build(Position position) {}
    public void build(City city, Position position) {}
    public void build(Station s1, Station s2, Line line, List<Station> allStations, List<Obstacle> obstacles){}
    public void build(Railway railway, Position position) {}

    public void destroy(Position position) {}
    public void destroy(Station station) {}
    public void destroy(Locomotive locomotive) {}
}
