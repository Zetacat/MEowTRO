package meowtro.eventSystem;

public abstract class Event {
    protected City city;
    protected String name;
    protected String happenedTimeString;   

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
