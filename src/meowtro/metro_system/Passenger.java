package meowtro.metro_system;

public class Passenger {
    public void selfExplode(){
        System.out.println("Passenger exploded. ");
    }
    public int findShorestPath(Station src, Station dst){
        return ShortestPathCalculator.findShorestPath(src, dst); 
    }
}
