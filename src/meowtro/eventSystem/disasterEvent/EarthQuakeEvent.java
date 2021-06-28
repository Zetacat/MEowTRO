package meowtro.eventSystem.disasterEvent;
import meowtro.game.City;
import java.util.List;
import meowtro.metro_system.railway.*;

public class EarthQuakeEvent extends DisasterEvent{
    // private int remainTimeUnit;
    public EarthQuakeEvent(City city, String happenedTimeString, int remainTimeUnit){
        super(city, happenedTimeString, remainTimeUnit);
        this.name = "EarthQuakeEvent";
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
