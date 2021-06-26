package meowtro.eventSystem;

public abstract class Event {
    protected Map map;
    protected String name;
    protected String happenedTimeString;   

    Event(Map map, String name, String happenedTimeString){
        this.map = map;
        this.name = name;
        this.happenedTimeString = happenedTimeString;
    }
    public abstract void trigger();
    public String getHappenedTime(){
        return this.happenedTimeString;
    }
}
