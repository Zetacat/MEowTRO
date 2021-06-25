package meowtro.metro_system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class Line {
    private LinkedList<Railway> railways = new LinkedList<Railway>(); 
    private List<Locomotive> locomotives = new ArrayList<Locomotive>(); 
    private LineColor color; 

    public Line(LineColor color){
        this.color = color; 
    }

    public void addRailway(Railway r){
        if (r.end == railways.getFirst().start){
            railways.add(0, r); 
            return; 
        }
        for (Railway ref_r: railways){
            if (ref_r.end == r.start){
                railways.add(railways.indexOf(ref_r)+1, r); 
                return; 
            }
        }
        if (Game.DEBUG){
            System.out.println("Can't add railway to Line! ")
        }
    }

    public void addLocomotive(Locomotive l){
        locomotives.add(l); 
    }

    public LineColor getColor(){
        return color; 
    }

    public void destroyAll(){
        // TODO
    }

    public boolean isCircular(){
        if (railways.getFirst().start == railways.getLast().end){
            return true; 
        }
        return false; 
    }

    public HashSet<Direction> getDirections(){
        HashSet<Direction> result = new HashSet<Direction>(); 
        for (Locomotive l: locomotives){
            if (!result.contains(l.getDirection())){
                result.add(l.getDirection()); 
                if (result.size() >= 2){
                    break; 
                }
            }
        }
        return result; 
    }
}
