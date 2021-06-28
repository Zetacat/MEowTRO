package meowtro.game;
import meowtro.eventSystem.*;
import meowtro.eventSystem.disasterEvent.EarthQuakeEvent;
import meowtro.eventSystem.disasterEvent.FireEvent;
import meowtro.eventSystem.holidayEvent.RushHourEvent;

import java.util.List;
import java.util.ArrayList;
public class GameFactory {
    private List<Event> creatEvents(Config config, City city){
        String eventsSetting = config.get("eventSystem.allEvents");
        String[] eventsStr = eventsSetting.split("\\r?\\n");
        List<Event> allEvents = new ArrayList<>();
        for(int i=0; i<eventsStr.length; i++){
            if(eventsStr[i].length()==0){
                continue;
            }
            String[] eventInfo = eventsStr[i].split("$");
            if(eventInfo[0].equals("FireEvent")){
                allEvents.add(new FireEvent(city, eventInfo[1], Integer.parseInt(eventInfo[2])));
            }
            else if(eventInfo[0].equals("EarthQuakeEvent")){
                allEvents.add(new EarthQuakeEvent(city, eventInfo[1], Integer.parseInt(eventInfo[2])));
            }
            else if(eventInfo[0].equals("RushHourEvent")){
                allEvents.add(new RushHourEvent(city, eventInfo[1], Double.parseDouble(eventInfo[2])));
            }
            else{
                System.out.println("Wrong event config:\n"+eventsStr[i]);
            }
        }
        return allEvents;

    }
    public Game createGame(Config config) {
        City city = new City();
        EventTrigger eventTrigger = new EventTrigger(creatEvents(config, city));
        Game game = new Game(config, eventTrigger);
        
        game.setCity(city);

        return game;
    }

    /******* MAIN *******/
    public static void main(String[] args) {
        
    }

}
