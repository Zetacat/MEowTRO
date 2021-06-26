package meowtro.eventSystem.disasterEvent;
import meowtro.eventSystem.Event;
import meowtro.game.City;

public abstract class DisasterEvent extends Event{
    // protected int numOfDestroyRailway;
    protected int remainTimeUnit;
    public DisasterEvent(City city, String name, String happenedTimeString, int remainTimeUnit){
        super(city, name, happenedTimeString);
        this.remainTimeUnit = remainTimeUnit;
        // this.numOfDestroyRailway = numOfDestroyRailway;
    }
    
}
