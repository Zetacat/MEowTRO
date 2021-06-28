package meowtro.eventSystem.holidayEvent;
import meowtro.eventSystem.Event;
import meowtro.game.City;

public abstract class HolidayEvent extends Event{
    protected float growthRate;
    public HolidayEvent(City city, String happenedTimeString, float growthRate){
        super(city, happenedTimeString);
        this.growthRate = growthRate;
    }
}
