package meowtro.eventSystem;

import meowtro.timeSystem.TimeLine;
import java.util.List;


public class EventTrigger {
    // private Map map;
    private List<Event> yearEvents = null;
    private int currentEventIndex;
    // private int totalEventNum;
    EventTrigger(List<Event> yearEvents){
        // this.map = map;
        this.yearEvents = yearEvents;
        this.currentEventIndex = 0;
    }
    public void trigger(){
        if(this.yearEvents.size()==0){
            return;
        }
        Event comingEvent = this.yearEvents.get(this.currentEventIndex);
        TimeLine curreTimeLine = TimeLine.getInstance();
        if(curreTimeLine.matchCalenderwoYear(comingEvent.getHappenedTime())){
            comingEvent.trigger();
        }
        this.currentEventIndex += 1;
        if(this.currentEventIndex >= this.yearEvents.size()){
            this.currentEventIndex = 0;
        }
    }
}
