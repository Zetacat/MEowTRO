package Meowtro.Game;

import Meowtro.Position;

public class CutInLineElder extends Passenger {
    
    public CutInLineElder(Region birthRegion, long spawnTime, Position position, Station destinationStation) {
        super(birthRegion, spawnTime, position, destinationStation);
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
