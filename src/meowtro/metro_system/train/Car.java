package meowtro.metro_system.train;
import java.util.ArrayList;
import java.util.List;

import meowtro.game.*;
import meowtro.game.passenger.Passenger;

public class Car {
    private int level; 
    private int maxLevel; 
    private int capacity = 8; 
    private List<Passenger> passengers = new ArrayList<Passenger>(); 
    private Locomotive locomotive; 

    /**
    * Parse game config and set proper value. 
    */
    private void init(){
        // parse config
        this.capacity = Integer.valueOf(Game.getConfig().get("metro_system.car.car_capacity")); 
        this.maxLevel = Integer.valueOf(Game.getConfig().get("metro_system.car.max_level")); 
    }

    public Car(Locomotive locomotive){
        init(); 
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
