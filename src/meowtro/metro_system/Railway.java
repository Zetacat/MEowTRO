package meowtro.metro_system;

import java.util.ArrayList;

public class Railway {
    private Line line; 
    public Station start; 
    public Station end; 
    private int remainTimeToLive = Integer.MAX_VALUE; 
    private ArrayList<RailwayDecorator> railwayDecorators = new ArrayList<RailwayDecorator>(); 

    private int fragileThreshold = 240; 
    private int maxLimitedRemainTimeToLive = 500; 
    private int originalPrice = 1000; 

    public Railway(Station anchor, Line line){
        this.line = line; 

        for (Railway r: anchor.getRailways()){
            if (r.line == line){
                if (r.start == anchor){
                    this.end = anchor; 
                    this.start = null; 
                }
                else if (r.end == anchor){
                    this.end = null; 
                    this.start = anchor; 
                }
                else{
                    if (Game.DEBUG){
                        System.out.println("construct Railway() error");
                    }
                }
                break; 
            }
        }

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

    public void setRemainTimeToLive(int remainTime){
        this.remainTimeToLive = remainTime; 
    }

    public boolean isFragile(){
        return remainTimeToLive < fragileThreshold; 
    }

    public void fix(){

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

    public void update(){

    }
}
