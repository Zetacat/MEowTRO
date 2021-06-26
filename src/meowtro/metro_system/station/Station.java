package meowtro.metro_system.station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import meowtro.Position;
import meowtro.game.*;
import meowtro.metro_system.railway.Line;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.train.Locomotive;

public class Station {
    private City city; 
    private ArrayList<Railway> railways = new ArrayList<Railway>(); 
    private ArrayList<Passenger> queue = new ArrayList<Passenger>(); 
    private HashSet<Line> lines = new HashSet<Line>(); 
    private ArrayList<Locomotive> arrivedLocomotives = new ArrayList<Locomotive>(); 
    private int level = 0; 
    private Position position; 
    private Region region; 
    private int maxLineNum = 6; 


    public Station(City city, Position p){
        this.position = p; 
        this.level = 0; 
        this.city = city; 
    }


    private int getMaxLineNum(){
        return maxLineNum; 
    }


    private int getMaxQueueSize(){
        return level + 8; 
    }


    public Station(Position p){
        this.position = p; 
        this.level = 0; 
    }


    public void setRegion(Region r){
        this.region = r; 
    }


    public void addRailway(Railway r){
        this.railways.add(r); 
        this.lines.add(r.getLine()); 
        assert lines.size() <= getMaxLineNum(); 
    }


    public void removeRailway(Railway r){
        railways.remove(r); 
    }


    public List<Railway> getRailways(){
        return railways; 
    }


    public List<Railway> getRailwaysWithLine(Line l){
        ArrayList<Railway> result = new ArrayList<Railway>(); 
        for (Railway r: getRailways()){
            if (r.getLine() == l){
                result.add(r); 
            }
        }
        return result; 
    }


    public void removeLine(Line l){
        if (lines.contains(l)){
            for (Railway r: railways){
                if (r.getLine() == l){
                    railways.remove(r); 
                }
            }
        }
        lines.remove(l); 
    }


    public List<Line> getLines(){
        return new ArrayList<Line>(lines); 
    }


    public List<Passenger> getPassengerQueue(){
        return queue; 
    }


    public boolean isAdjTo(Station s){
        return getAdjacents().contains(s); 
    }


    public List<Station> getAdjacents(){
        ArrayList<Station> adjList = new ArrayList<Station>(); 
        for (Railway r: railways){
            Station adj = r.getAdjacent(this); 
            if (adj != null){
                adjList.add(adj); 
            }
        }
        return adjList; 
    }


    public Railway getNextRailway(Railway srcRailway){
        Line currentLine = srcRailway.getLine(); 
        for (Railway r: railways){
            if (r != srcRailway && r.getLine() == currentLine){
                return r; 
            }
        }
        // if can't find next railway, turn around
        return srcRailway; 
    }


    public void setPosition(Position p){
        this.position = p; 
    }


    public Position getPosition(){
        return position; 
    }


    public void insertPassenger(Passenger p, int index){
        // index = 0 or -1

        if (queue.size() >= getMaxQueueSize()){
            p.selfExplode();
            return; 
        }

        if (index < 0){
            index = queue.size() + index + 1; 
        }
        assert index >= 0 && index <= queue.size(); 
        queue.add(index, p);
    }


    public List<Locomotive> getArrivedLocomotives(){
        return arrivedLocomotives; 
    }


    public boolean isEndStationInLine(Line l){
        int lineCnt = 0; 
        for (Railway r: railways){
            if (r.getLine() == l){
                lineCnt += 1; 
            }
        }
        assert lineCnt >= 0 || lineCnt <= 2; 
        if (lineCnt == 1){
            return true; 
        }
        return false; 
    }


    public void locomotiveArrive(Locomotive l){
        this.arrivedLocomotives.add(l); 
    }


    public void locomotiveDepart(Locomotive l){
        this.arrivedLocomotives.remove(l); 
        l.depart(); 
    }


    public void destroy(){
        for (Passenger p: queue){
            p.selfExplode();
        }
        for (Line l: lines){
            l.destroyAll();
        }
        city.removeStation(this); 
    }


    public void update(){
        // for each arrived
    }
}
