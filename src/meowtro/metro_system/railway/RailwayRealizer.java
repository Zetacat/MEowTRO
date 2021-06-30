package meowtro.metro_system.railway;

import meowtro.Position;

public interface RailwayRealizer {
    public int parsePositionToAbstractPosition(); 
    public Position parseAbstractPositionToPosition(int abstractPosition); 
}
