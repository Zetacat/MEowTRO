package meowtro.eventSystem;
import meowtro.game.City;
public abstract class Event {
    protected City city;
    protected String name;
    protected String happenedTimeString;  //"MM-DD HH:MM:SS"

    Event(City city, String name, String happenedTimeString){
        this.city = city;
        this.name = name;
        this.happenedTimeString = happenedTimeString;
    }
    public abstract void trigger();
    public String getHappenedTime(){
        return this.happenedTimeString;
    }
}
