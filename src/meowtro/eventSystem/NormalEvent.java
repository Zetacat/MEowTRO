package meowtro.eventSystem;
// import meowtro.timeSystem.TimeLine;
import meowtro.game.City;

public class NormalEvent extends Event{
    public NormalEvent(City city, String name, String happenedTimeString){
        super(city, name, happenedTimeString);
    }
    public void trigger(){
        // TimeLine curreTimeLine = TimeLine.getInstance();

    }
}
