package meowtro.game.entityManager;

import java.util.List;

import meowtro.Position;
import meowtro.game.City;
import meowtro.game.obstacle.Obstacle;
import meowtro.metro_system.railway.Line;
import meowtro.metro_system.station.Station;

public abstract class EntityManager {
    public void build(Position position) {}
    public void build(City city, Position position) {}
    public void build(Station s1, Station s2, Line line, List<Station> allStations, List<Obstacle> obstacles){}

    public void destroy(Position position) {}
    public void destroy(Station station) {}
}
