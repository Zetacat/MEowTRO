package meowtro.game.entityManager;

import java.util.ArrayList;

import meowtro.Position;
import meowtro.game.City;
import meowtro.game.Game;
import meowtro.game.Region;
import meowtro.metro_system.station.Station;

public class StationManager extends EntityManager {
    private Game game;
    private int stationNum = 0;
    public int getStationNum() {
        return stationNum;
    }
    private ArrayList<String> iconPaths;

    public StationManager(Game game) {
        this.game = game;
        this.iconPaths = game.getIconPaths();
    }

    public void build(City city, Position position) {
        if (this.stationNum >= game.getMaxStationNum() || this.iconPaths.size() == 0) {
            // throw exception
        } else {
            String iconPath = this.iconPaths.get(0);
            this.iconPaths.remove(iconPath);
            Station station = new Station(city, position, iconPath);
            station.setManager(this);
            Region region = city.getRegionByPosition(position);
            region.addStation(station);
            this.stationNum ++;
        }
    }

    public void destroy(Station station) {
        if (this.stationNum > 1) {
            this.iconPaths.add(station.getIconPath());
            station.destroy();
            this.stationNum --;
        }
    }
}
