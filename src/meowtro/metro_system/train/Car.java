package meowtro.metro_system.train;
import java.util.ArrayList;
import java.util.List;

import meowtro.game.*;

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

    public void destroy(){
        for (Passenger p: passengers){
            p.selfExplode();
        }
        passengers.clear();
    }

    public boolean isFull() {
        assert passengers.size() <= capacity; 
        return passengers.size() == capacity;
    }
}
