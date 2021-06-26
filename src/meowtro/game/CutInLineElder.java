package meowtro.game;

import meowtro.Position;
import meowtro.metro_system.*;

public class CutInLineElder extends Passenger {
    
    public CutInLineElder(Region birthRegion, Position position, Station destinationStation) {
        super(birthRegion, position, destinationStation);
    }

    @Override
    public void enterStation(Station station) {
        this.position = station.getPosition();
        // arrive station
        if (station == this.destinationStation) {
            this.arriveDestination();
        }
        // enter station and wait
        else {
            station.insertPassenger(this, 0);
            this.currentStation = station;
        }
    }
}
