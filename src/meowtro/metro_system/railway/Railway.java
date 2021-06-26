package meowtro.metro_system.railway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import meowtro.Position;
import meowtro.game.Game;
import meowtro.metro_system.Direction;
import meowtro.metro_system.station.Station;
import meowtro.metro_system.train.Locomotive;

public class Railway {
    public int railwayID = -1; 
    private Line line; 
    public Station start; 
    public Station end; 
    private int remainTimeToLive = Integer.MAX_VALUE; 
    private List<Locomotive> locomotives = new ArrayList<Locomotive>(); 
    private ArrayList<RailwayDecorator> railwayDecorators = new ArrayList<RailwayDecorator>(); 

    private int fragileThreshold = 240; 
    private int maxLimitedRemainTimeToLive = Integer.MAX_VALUE; 
    private int originalPrice = 1000; 

    private int length; 
    private HashMap<Locomotive, Integer> positionsInAbstractLine = new HashMap<Locomotive, Integer>(); 


    /**
    * Parse game config and set proper value. 
    */
    public void init(){
        this.fragileThreshold = Integer.valueOf(Game.getConfig().get("metro_system.railway.fragile_threshold")); 
        this.originalPrice = Integer.valueOf(Game.getConfig().get("metro_system.railway.original_price")); 
    }


    public Railway(Station s1, Station s2, Line line){
        init();
        boolean DEBUG = true; 
        this.line = line; 

        if (s1.getAdjacents().contains(s2) || s2.getAdjacents().contains(s1)){
            if (DEBUG){
                System.out.println("Can't have two railways between two stations! ");
            }
            return; 
        }

        List<Railway> s1AdjRailways = s1.getRailwaysWithLine(line); 
        List<Railway> s2AdjRailways = s2.getRailwaysWithLine(line); 

        assert s1AdjRailways.size() <= 2 && s2AdjRailways.size() <= 2; 

        if (s1AdjRailways.size() >= 1 && s2AdjRailways.size() >= 1){
            // add a shortcut railway and destroy old
            assert s1.isEndStationInLine(line) && s2.isEndStationInLine(line); 
            for (Railway r: line.getRailwaysBetweenStations(s1, s2)){
                r.destroy();
            }
            s1AdjRailways = s1.getRailwaysWithLine(line); 
            s2AdjRailways = s2.getRailwaysWithLine(line); 
            
            // --S1 --this- S2---
            assert s1AdjRailways.size() <= 1 && s2AdjRailways.size() <= 1; 
            if (s1AdjRailways.size() == 1 && s1AdjRailways.size() == 1){
                Railway r1 = s1AdjRailways.get(0); 
                Railway r2 = s2AdjRailways.get(0); 
                if (r1.end == s1 && r2.start == s2){
                    this.end = s2; 
                    this.start = s1; 
                }else if (r1.start == s1 && r2.end == s2){
                    this.end = s1; 
                    this.start = s2; 
                }else{
                    // error
                    if (DEBUG){
                        System.out.println("Create shortcut railway error: mismatched directions");
                    }
                    return; 
                }
            }
        }
        if (s1AdjRailways.size() == 0 && s2AdjRailways.size() == 0){
            // brand new line
            assert line.getRailways().size() == 0; 
            this.start = s1; 
            this.end = s2;
        }
        else{
            // extend from one end
            Station endStationOfThisLine = null; 
            Station NewEndStation = null; 
            Railway endRailway = null; 
            if (s1AdjRailways.size() == 1){
                endStationOfThisLine = s1; 
                NewEndStation = s2; 
                endRailway = s1AdjRailways.get(0); 
            }else{
                endStationOfThisLine = s2; 
                NewEndStation = s1; 
                endRailway = s2AdjRailways.get(0); 
            }

            if (endRailway.start == endStationOfThisLine){
                this.end = endStationOfThisLine; 
                this.start = NewEndStation; 
            }
            else if (endRailway.end == endStationOfThisLine){
                this.end = NewEndStation; 
                this.start = endStationOfThisLine; 
            }
        }

        if (start != null){
            start.addRailway(this);
        }
        if (end != null){
            end.addRailway(this);
        }

        if (start == null && end == null){
            if (DEBUG)
                System.out.println("construct Railway() error");
            return; 
        }

        this.length = computeLenght(); 
        line.addRailway(this);
    }

    public Station getAdjacent(Station sourceStation){
        if (sourceStation != start && sourceStation != end){
            return null; 
        }
        if (line.isCircular()){
            if (sourceStation == start){
                if (line.getDirections().contains(Direction.FORWARD))
                    return end; 
            }else{
                if (line.getDirections().contains(Direction.BACKWARD))
                    return start; 
            }
            return null; 
        }else{
            if (sourceStation == start){
                return end; 
            }else{
                return start; 
            }
        }
    }

    public Line getLine(){
        return line; 
    }

    private int parsePositionToAbstractPosition(Position p){
        // TODO
        return p.i; 
    }

    private Position parseAbstractPositionToPosition(int ap){
        // TODO
        return new Position(ap, 0); 
    }

    public void addLocomotive(Locomotive l){
        locomotives.add(l); 
        if (l.getDirection() == Direction.FORWARD){
            positionsInAbstractLine.put(l, parsePositionToAbstractPosition(l.getPosition())); 
        }
        
    }

    public void removeLocomotive(Locomotive l){
        locomotives.remove(l); 
        positionsInAbstractLine.remove(l); 
    }

    public void setRemainTimeToLive(int remainTime){
        this.remainTimeToLive = remainTime; 
        this.maxLimitedRemainTimeToLive = remainTime; 
    }

    public boolean isFragile(){
        return remainTimeToLive < fragileThreshold; 
    }

    public void fix(){
        // TODO
    }

    public int getRemainPrice(){
        if (remainTimeToLive == Integer.MAX_VALUE){
            return originalPrice; 
        }
        return (remainTimeToLive / maxLimitedRemainTimeToLive) * originalPrice; 
    }

    public Station getNextStation(Direction directionToward){
        if (directionToward == Direction.FORWARD){
            return end; 
        }
        return start; 
    }

    public void destroy(){
        boolean DEBUG = true; 
        if (DEBUG){
            System.out.printf("Railway %d in Line %s destroyed\n", railwayID, line.getColor()); 
        }
        for (Locomotive l: locomotives){
            l.destroy();
            removeLocomotive(l);
        }

        if (start != null){
            start.removeRailway(this);
        }
        if (end != null){
            end.removeRailway(this);
        }

        line.removeRailways(this);
    }

    private int computeLenght(){
        // TODO
        return 800; 
    }

    public Position moveLocomotive(Locomotive l){
        int speed = l.getSpeed(); 
        int maxSpeed = l.getMaxSpeed(); 

        int orientation = 1; 
        if (l.getDirection() == Direction.BACKWARD){
            orientation = -1; 
        }
        
        int newAbstractPosition = positionsInAbstractLine.get(l) + (speed * orientation); 
        newAbstractPosition = Math.min(newAbstractPosition, length); 
        newAbstractPosition = Math.max(newAbstractPosition, 0); 

        l.setSpeed(maxSpeed); 
        return parseAbstractPositionToPosition(newAbstractPosition); 
    }

    public String toString(){
        return String.format("%s:%d", line.getColor().toString(), railwayID); 
    }

    public void update(){
        // just for check; normally this shouldn't happen
        if (start == null && end == null){
            destroy();
        }
    }
}
