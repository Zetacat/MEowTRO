package meowtro.game.onClickEvent;

import meowtro.Position;
import meowtro.game.City;
import meowtro.game.Game;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.station.Station;

public abstract class OnClickEvent {
    protected String name;
    protected Game game;
    protected City city;
    public String getName() {
        return this.name;
    }
    public Game getGame() {
        return this.game;
    }
    public City getCity() {
        return this.city;
    }
    public void conduct(Station station) {}
    public void conduct(Position position) {}
    public void conduct(Station s1, Station s2) {}
    public void conduct(Railway railway, Position position) {}
}
