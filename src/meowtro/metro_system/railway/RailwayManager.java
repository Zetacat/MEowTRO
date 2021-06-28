package meowtro.metro_system.railway;

import java.util.ArrayList;
import java.util.List;

import meowtro.game.entityManager.*;
import meowtro.metro_system.station.Station;

public class RailwayManager extends EntityManager{
    List<Railway> railways = new ArrayList<Railway>(); 

    public void build(Station s1, Station s2, Line line){
        if (s1.isAdjTo(s2) || s2.isAdjTo(s1)){
            return; 
        }
        if (line.getRailways().size() != 0){
            if ((!line.getStations().contains(s1)) && (!line.getStations().contains(s1))){
                return; 
            }
        }
        new Railway(s1, s2, line);
    }
}
