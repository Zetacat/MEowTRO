package meowtro.eventSystem.disasterEvent;
import meowtro.game.City;

public class EarthQuakeEvent extends DisasterEvent{
    public EarthQuakeEvent(City city, String name, String happenedTimeString, int numOfDestroyRailway){
        super(city, name, happenedTimeString, numOfDestroyRailway);
    }
}
