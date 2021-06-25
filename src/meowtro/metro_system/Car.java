package meowtro.metro_system;
import java.util.ArrayList;
import java.util.List;

public class Car {
    private int level; 
    private int capacity = 8; 
    private List<Passenger> passengers = new ArrayList<Passenger>(); 
    private Locomotive locomotive; 

    public Car(Locomotive locomotive){
        this.locomotive = locomotive; 
    }

    public int getLevel(){
        return level; 
    }

    public void setLevel(int level){
        this.level = level; 
    }

    public void addPassenger(Passenger p){
        if (passengers.size() < capacity){
            passengers.add(p); 
        }
    }

    public void removePassenger(Passenger p){
        passengers.remove(p); 
    }

    public List<Passenger> getPassengers(){
        return passengers; 
    }

    public Locomotive getLocomotive(){
        return locomotive; 
    }
}
