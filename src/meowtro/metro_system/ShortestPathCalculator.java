package meowtro.metro_system;
import java.util.LinkedList;


public class ShortestPathCalculator{
    static int findShorestPath(Station src, Station dst){
        int inf = Integer.MAX_VALUE;
        
        LinkedList<Station> stationsToExplore = new LinkedList<Station>(); 
        LinkedList<Station> stationsExploring = new LinkedList<Station>(); 


        // initiallize
        stationsToExplore.add(src); 

        // BFS start
        int dist = 0; 
        while (stationsToExplore.size() > 0){

            // load stations in current level(level num = dist)
            while (stationsToExplore.size() > 0){
                Station s = stationsToExplore.getFirst(); 
                stationsToExplore.remove(s); 
                stationsExploring.add(s); 
            }

            // check whether dst is in this level
            while (stationsExploring.size() > 0){
                Station s = stationsExploring.getFirst(); 
                stationsExploring.remove(s); 
                if (s == dst){
                    return dist; 
                }
                
                stationsToExplore.addAll(s.getAdjacents()); 
            }
            dist += 1; 
        }

        return inf; 
    }
}