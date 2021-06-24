package meowtro.eventSystem;

public abstract class Event {
    protected Map map;
    protected String name;
    protected String happenedTimeString;   
    
    public abstract void trigger();
    public abstract String getHappenedTime();
}
