package meowtro.metro_system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import meowtro.game.City;


public class Line {
    private City city; 
    private LinkedList<Railway> railways = new LinkedList<Railway>(); 
    private List<Locomotive> locomotives = new ArrayList<Locomotive>(); 
    private HashSet<Station> stations = new HashSet<Station>(); 
    private LineColor color; 

    public Line(City city, LineColor color){
        this.city = city; 
        this.color = color; 
    }

    public Line(LineColor color){
        this.color = color; 
    }

    private void reIndexRailways(){
        int index = 0; 
        for (Railway r: railways){
            r.railwayID = index; 
            index += 1; 
        }
    }

    public void addRailway(Railway r){
        boolean DEBUG = true; 
        if (railways.size() == 0){
            railways.add(r); 
            r.railwayID = 0; 
        }
        else if (r.end == railways.getFirst().start){
            railways.add(0, r); 
            reIndexRailways(); 
        }
        else{
            for (Railway ref_r: railways){
                if (ref_r.end == r.start){
                    railways.add(railways.indexOf(ref_r)+1, r); 
                    reIndexRailways(); 
                    return; 
                }
            }
        }
        if (! railways.contains(r)){
            if (DEBUG){
                System.out.println("Can't add railway to Line! "); 
            }
            return; 
        }

        reIndexRailways(); 
        if (r.start != null){
            stations.add(r.start); 
        }
        if (r.end != null){
            stations.add(r.end); 
        }
    }

    public Set<Station> getStations(){
        return stations; 
    }

    public List<Railway> getRailways(){
        return railways; 
    }

    public void removeRailways(Railway r){
        railways.remove(r); 
    }

    public void addLocomotive(Locomotive l){
        locomotives.add(l); 
    }

    public LineColor getColor(){
        return color; 
    }

    public List<Railway> getRailwaysBetweenStations(Station s1, Station s2){
        List<Railway> result = new ArrayList<Railway>(); 
        if (s1 == s2){
            return result; 
        }
        boolean startCollecting = false; 
        for (Railway r: railways){
            if (r.start == s1 || r.start == s2){
                startCollecting = true; 
            }
            if (startCollecting){
                result.add(r); 
            }
            if (r.end == s1 || r.end == s2){
                startCollecting = false; 
                break; 
            }
        }
        return result; 
    }

    public List<Station> getStationsBetweenStations(Station s1, Station s2){
        // TODO
        return null; 
    }

    public void destroyAll(){
        for (Railway r: railways){
            r.destroy();
        }
        this.railways.clear();

        for (Locomotive l: locomotives){
            l.destroy(); 
        }
        this.locomotives.clear(); 
        // city.removeLine(); 
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

    public String toString(){
        String s = ""; 
        for (Railway r: railways){
            s = s + r.toString() + " "; 
        }
        return s; 
    }
}
