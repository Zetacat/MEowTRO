package meowtro.eventSystem.disasterEvent;
import meowtro.game.City;
import java.util.List;
import meowtro.metro_system.railway.*;

public class EarthQuakeEvent extends DisasterEvent{
    private int remainTimeUnit;
    public EarthQuakeEvent(City city, String name, String happenedTimeString, int numOfDestroyRailway, int remainTimeUnit){
        super(city, name, happenedTimeString, numOfDestroyRailway);
        this.remainTimeUnit = remainTimeUnit;
    }
    public void trigger(){
        List<Line> allLines = this.city.getAllLines();
        for(Line l : allLines){
            List<Railway> allRailways = l.getRailways();
            for(Railway r: allRailways){
                r.setRemainTimeToLive(this.remainTimeUnit);
            }
        }
    }
}
