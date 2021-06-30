package meowtro.metro_system.railway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import meowtro.Position;
import meowtro.game.obstacle.Obstacle;
import meowtro.metro_system.station.Station;

public class RectangularRailwayRealizer implements RailwayRealizer{

    private static boolean isInitialized = false;
    private static List<List<Boolean>> OccupancyMap;
    private static HashSet<Station> recordedStations = new HashSet<Station>(); 
    private static double nearbyThreshold = 8.0; 

    public List<Position> Nodes = new ArrayList<Position>(); 
    public HashMap<List<Position>, Obstacle> obsticleEndPoints = new HashMap<List<Position>, Obstacle>(); 
    private boolean isValid = false; 
    private boolean isIntersectedWithObstacle = false; 

    private Position start; 
    private Position end; 

    
    public RectangularRailwayRealizer(Station start, Station end, List<Station> allStations, List<Obstacle> obstacles){
        if (!isInitialized){
            initOccupancyMap(allStations);
        }

        for (Station s: allStations){
            if (!recordedStations.contains(s)){
                addStationToOccupancyMap(s);
                recordedStations.add(s); 
            }
        }
        removeStationInOccupancyMap(start);
        removeStationInOccupancyMap(end);

        this.isValid = false; 
        if (judgeLine()){
            this.isValid = true; 
        }else if (judgeLShape()){
            this.isValid = true; 
        }else if (judgeZShape()){
            this.isValid = true; 
        } 

        if (isValid){
            judgeObstacles(obstacles); 
        }

        addStationToOccupancyMap(start);
        addStationToOccupancyMap(end);
    }

    private static void initOccupancyMap(List<Station> allStations){
        OccupancyMap = new ArrayList<List<Boolean>>(); 
        OccupancyMap.add(new ArrayList<Boolean>()); 
        
        int height = 0; 
        int width = 0; 
        for (Station s: allStations){
            Position p = s.getPosition(); 
            if (p.i > height){
                height = (int)(p.i); 
            }
            if (p.j > width){
                width = (int)(p.j); 
            }
        }
        extendOccupancyMapHeight(height); 
        extendOccupancyMapWidth(width); 
        isInitialized = true; 
    }

    static void extendOccupancyMapHeight(int height){
        int oldHeight = OccupancyMap.size(); 
        int width = OccupancyMap.get(0).size(); 
        if (height <= oldHeight){
            return; 
        }
        for (int i = 0; i < height-oldHeight; i++){
            OccupancyMap.add(new ArrayList<Boolean>(Collections.nCopies(width, false))); 
        }
    }

    static void extendOccupancyMapWidth(int width){
        int oldWidth = OccupancyMap.get(0).size(); 
        if (width <= oldWidth){
            return; 
        }
        for (List<Boolean> row: OccupancyMap){
            row.addAll(Collections.nCopies(width - oldWidth, false)); 
        }
    }

    static void addStationToOccupancyMap(Station s){
        Position p = s.getPosition(); 
        for (int i = (int)(p.i - nearbyThreshold); i <= (int)(p.i + nearbyThreshold); i++){
            for (int j = (int)(p.j - nearbyThreshold); j <= (int)(p.j + nearbyThreshold); j++){
                if (i > 0 && i < OccupancyMap.size()){
                    if (j > 0 && j < OccupancyMap.get(0).size()){
                        OccupancyMap.get(i).set(j, true); 
                    }
                }
            }
        }
    }

    static void removeStationInOccupancyMap(Station s){
        Position p = s.getPosition(); 
        for (int i = (int)(p.i - nearbyThreshold); i <= (int)(p.i + nearbyThreshold); i++){
            for (int j = (int)(p.j - nearbyThreshold); j <= (int)(p.j + nearbyThreshold); j++){
                if (i > 0 && i < OccupancyMap.size()){
                    if (j > 0 && j < OccupancyMap.get(0).size()){
                        OccupancyMap.get(i).set(j, false); 
                    }
                }
            }
        }
    }

    private boolean isValidLine(Position a, Position b){
        double discriminant = (start.i - end.i) * (start.j - end.j); 
        boolean valid = false; 
        if (discriminant == 0){
            valid = true; 
            if (start.i == end.i){
                int i = (int) start.i; 
                for (int j = (int)Math.min(start.j, end.j); j <= (int)Math.max(start.j, end.j); j++){
                    if (OccupancyMap.get(i).get(j)){
                        valid = false; 
                        break; 
                    }
                }
            }else{
                int j = (int) start.j; 
                for (int i = (int)Math.min(start.i, end.i); i <= (int)Math.max(start.i, end.i); i++){
                    if (OccupancyMap.get(i).get(j)){
                        valid = false; 
                        break; 
                    }
                }
            }
        }
        return valid; 
    }

    private boolean judgeLine(){
        double discriminant = (start.i - end.i) * (start.j - end.j); 
        boolean valid = isValidLine(start, end); 
        if (discriminant == 0){
            this.Nodes.add(this.start); 
            this.Nodes.add(this.end); 
        }
        return valid; 
    }

    private boolean judgeLShape(){
        Position a = start; 
        Position b = end;
        if (start.j > end.j){
            a = end; 
            b = start; 
        } 

        Position turningPoint; 
        turningPoint = new Position(b.i, a.j); 
        if (isValidLine(a, turningPoint) && isValidLine(turningPoint, b)){
            this.Nodes.add(a); 
            this.Nodes.add(turningPoint); 
            this.Nodes.add(b); 
            return true; 
        }
        turningPoint = new Position(a.i, b.j); 
        if (isValidLine(a, turningPoint) && isValidLine(turningPoint, b)){
            this.Nodes.add(a); 
            this.Nodes.add(turningPoint); 
            this.Nodes.add(b); 
            return true; 
        }
        return false; 
    }

    private List<Boolean> ORAlongAxis(List<List<Boolean>> oMap, int axis){
        assert axis == 0 || axis == 1; 
        List<Boolean> result = new ArrayList<Boolean>(); 
        if (axis == 1){
            for (List<Boolean> row: oMap){
                boolean value = false; 
                for (boolean o: row){
                    if (o == true){
                        value = true; 
                        break; 
                    }
                }
                result.add(value); 
            }
        }else{
            int height = OccupancyMap.size(); 
            int width = OccupancyMap.get(0).size(); 
            for (int j = 0; j < width; j++){
                boolean value = false; 
                for (int i = 0; i < height; i++){
                    if (oMap.get(i).get(j) == true){
                        value = true; 
                        break; 
                    }
                }
                result.add(value); 
            }
        }
        return result; 
    }

    private boolean judgeZShape(){
        // search along axis 1
        int startIdx = (int) (Math.abs(start.j + end.j) / 2); 
        int maxOffset = (int) (Math.abs(start.j - end.j) / 2) - (int)(nearbyThreshold * 1.5); 
        int offset = 0; 
        List<Boolean> oMap = ORAlongAxis(OccupancyMap, 0); 
        int resultIdx = -1; 
        while (offset < maxOffset && resultIdx < 0){
            for (int i = -1; i <= 1; i+=2){
                int index = startIdx + offset * i; 
                if (oMap.get(index) == false){
                    // found it
                    resultIdx = index; 
                    break; 
                }
            }
            offset ++; 
        }
        if (resultIdx >= 0){
            Nodes.add(start); 
            Nodes.add(new Position(start.i, resultIdx)); 
            Nodes.add(new Position(end.i, resultIdx)); 
            Nodes.add(end); 
            return true; 
        }
        // search along axis 0
        startIdx = (int) (Math.abs(start.i + end.i) / 2); 
        maxOffset = (int) (Math.abs(start.i - end.i) / 2) - (int)(nearbyThreshold * 1.5); 
        offset = 0; 
        oMap = ORAlongAxis(OccupancyMap, 1); 
        resultIdx = -1; 
        while (offset < maxOffset && resultIdx < 0){
            for (int i = -1; i <= 1; i+=2){
                int index = startIdx + offset * i; 
                if (oMap.get(index) == false){
                    // found it
                    resultIdx = index; 
                    break; 
                }
            }
            offset ++; 
        }
        if (resultIdx >= 0){
            Nodes.add(start); 
            Nodes.add(new Position(resultIdx, start.j)); 
            Nodes.add(new Position(resultIdx, end.j)); 
            Nodes.add(end); 
            return true; 
        }
        return false; 
    }

    private void judgeLineIntersectedWithObstacle(Position a, Position b, List<Obstacle> obstacles){
        double discriminant = (start.i - end.i) * (start.j - end.j); 
        assert discriminant == 0; 
        
        if (start.i == end.i){
            int i = (int) start.i; 
            boolean isRecordingIntersection = false; 
            List<Position> ObstacleStartEndPair = null; 
            Obstacle currentObstacle = null; 
            for (int j = (int)Math.min(start.j, end.j); j <= (int)Math.max(start.j, end.j); j++){
                for (Obstacle obs: obstacles){
                    if (obs.getPositions().get(i).get(j)){
                        if (!isRecordingIntersection){
                            isRecordingIntersection = true; 
                            currentObstacle = obs; 
                            ObstacleStartEndPair = new ArrayList<Position>(); 
                            ObstacleStartEndPair.add(new Position(i, j)); 
                            break; 
                        }
                    }else{
                        if (isRecordingIntersection){
                            isRecordingIntersection = false; 
                            ObstacleStartEndPair.add(new Position(i, j)); 
                            assert (ObstacleStartEndPair.size() == 2); 
                            obsticleEndPoints.put(ObstacleStartEndPair, currentObstacle); 
                            ObstacleStartEndPair = null; 
                            currentObstacle = null; 
                        }
                    }
                }
            }
        }else{
            int j = (int) start.j; 
            boolean isRecordingIntersection = false; 
            List<Position> ObstacleStartEndPair = null; 
            Obstacle currentObstacle = null; 
            for (int i = (int)Math.min(start.i, end.i); i <= (int)Math.max(start.i, end.i); i++){
                for (Obstacle obs: obstacles){
                    if (obs.getPositions().get(i).get(j)){
                        if (!isRecordingIntersection){
                            isRecordingIntersection = true; 
                            currentObstacle = obs; 
                            ObstacleStartEndPair = new ArrayList<Position>(); 
                            ObstacleStartEndPair.add(new Position(i, j)); 
                            break; 
                        }
                    }else{
                        if (isRecordingIntersection){
                            isRecordingIntersection = false; 
                            ObstacleStartEndPair.add(new Position(i, j)); 
                            assert (ObstacleStartEndPair.size() == 2); 
                            obsticleEndPoints.put(ObstacleStartEndPair, currentObstacle); 
                            ObstacleStartEndPair = null; 
                            currentObstacle = null; 
                        }
                    }
                }
            }
        } 
    }

    private void judgeObstacles(List<Obstacle> obstacles){
        if (obstacles.size() == 0){
            this.isIntersectedWithObstacle = false; 
            return;
        }
        for (int i = 0; i < Nodes.size()-1; i++){
            judgeLineIntersectedWithObstacle(Nodes.get(i), Nodes.get(i+1), obstacles); 
        }
    }

    public boolean isValidRailway(){
        return isValid; 
    }

    public boolean isIntersectedWithObstacle(){
        return isIntersectedWithObstacle; 
    }

    @Override
    public int parsePositionToAbstractPosition(Position p) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Position parseAbstractPositionToPosition(int abstractPosition) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
