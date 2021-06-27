package meowtro.game.entityManager;

import meowtro.Position;
import meowtro.game.City;
import meowtro.metro_system.station.Station;

public abstract class EntityManager {
    public void build(Position position) {}
    public void build(City city, Position position) {}

    public void destroy(Position position) {}
    public void destroy(Station station) {}
}
