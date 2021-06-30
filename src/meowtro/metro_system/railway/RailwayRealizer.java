package meowtro.metro_system.railway;

import meowtro.Position;

public interface RailwayRealizer {
    public int parsePositionToAbstractPosition(Position p); 
    public Position parseAbstractPositionToPosition(double abstractPosition); 
}
