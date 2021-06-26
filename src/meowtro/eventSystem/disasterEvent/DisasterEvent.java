package meowtro.eventSystem.disasterEvent;
import meowtro.eventSystem.Event;
import meowtro.game.City;

public abstract class DisasterEvent extends Event{
    protected int numOfDestroyRailway;
    public DisasterEvent(City city, String name, String happenedTimeString, int numOfDestroyRailway){
        super(city, name, happenedTimeString);
        this.numOfDestroyRailway = numOfDestroyRailway;
    }
    
}
