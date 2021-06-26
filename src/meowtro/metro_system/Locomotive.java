package meowtro.metro_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import meowtro.Position;
import meowtro.game.*;

public class Locomotive {

    private enum State{
        MOVING, 
        ARRIVE_DROP, 
        ARRIVE_GETON, 
    }

    private Station currentStation;  // Station | null
    private int level = 0; 
    private int currentSpeed; 
    private int carNum = 0; 
    private static Map<Integer, Integer> levelToMaxSpeed; 
    private static Map<Integer, Integer> levelToMaxCar; 
    private Railway railway; 
    private Position position; 
    private Direction direction; 
    private ArrayList<Car> cars = new ArrayList<Car>(); 

    private int takePassengerInterval = 10; 
    private int dropPassengerInterval = 10; 
    private int takePassengerCountdown = 0; 
    private int dropPassengerCountdown = 0; 
    private LinkedList<Passenger> getDownQueue = null; 

    private State state; 
    private int distThres = 8; 

    public static void init(){
        // TODO: import the setting from config
        levelToMaxSpeed = new HashMap<Integer, Integer>(); 
        levelToMaxSpeed.put(0, 4); 
        levelToMaxSpeed.put(1, 8); 
        levelToMaxSpeed.put(2, 12); 

        levelToMaxCar = new HashMap<Integer, Integer>(); 
        levelToMaxCar.put(0, 2); 
        levelToMaxCar.put(0, 4); 
        levelToMaxCar.put(0, 6); 
    }

    public Locomotive(Railway railway, Position position, Direction direction){
        this.railway = railway; 
        this.position = position; 
        this.direction = direction;
        this.state = State.MOVING; 
    }

    public Position getPosition(){
        return position; 
    }

    public int getMaxSpeed(){
        return levelToMaxSpeed.get(level); 
    }

    public Station getSourceStation(){
        if (direction == Direction.FORWARD){
            return railway.start; 
        }
        return railway.end; 
    }

    public Station getDestinationStation(){
        if (direction == Direction.FORWARD){
            return railway.end; 
        }
        return railway.start; 
    }

    public Direction getDirection(){
        return direction; 
    }

    public Line getLine(){
        return railway.getLine(); 
    }

    public int getSpeed(){
        return currentSpeed; 
    }

    public void addCar(Car car){
        this.cars.add(car); 
    }

    public int getMaxCarNumber(){
        return levelToMaxCar.get(level); 
    }

    public void turnAround(){
        if (direction == Direction.BACKWARD){
            this.direction = Direction.FORWARD; 
        }else{
            this.direction = Direction.BACKWARD; 
        }
    }

    public void setSpeed(int speed){
        this.currentSpeed = speed; 
    }

    public void setLevel(int l){
        this.level = l; 
    }

    public int getLevel(){
        return level; 
    }

    private boolean assignPassengerToCar(Passenger p){
        for (Car c: cars){
            if (!c.isFull()){
                c.addPassenger(p);
                p.enterCar(c);
            }
        }
        return false; 
    }


    private LinkedList<Passenger> getAllPassenger(){
        LinkedList<Passenger> result = new LinkedList<Passenger>(); 
        for (Car c: cars){
            result.addAll(c.getPassengers()); 
        }
        return result; 
    }
    

    public void arrive(Station s){
        this.currentStation = s; 
    }

    public void depart(){
        this.takePassengerCountdown = 0; 
        this.dropPassengerCountdown = 0; 
        currentStation.locomotiveDepart(this);
        this.state = State.MOVING; 
        this.railway = currentStation.getNextRailway(railway); 
        this.currentStation = null; 
        // TODO: handle speed
    }

    public Station getCurrentStation() {
        return this.currentStation;
    }

    public Station getNextDstStation(){
        if (currentStation != null){
            return currentStation.getNextRailway(railway).getNextStation(direction); 
        }
        return null; 
    }


    public void tryTakePassenger(List<Passenger> stationQueue){
        if (stationQueue.size() == 0){
            depart();
        }
        if (takePassengerCountdown == 0){
            for (Passenger p: stationQueue){
                if (p.willingToGetOn(this)){
                    boolean success = assignPassengerToCar(stationQueue.get(0)); 
                    if (success){
                        stationQueue.remove(0); 
                    }else{
                        // Cars are full
                        depart();
                    }
                    break; 
                }
            }
            this.takePassengerCountdown = takePassengerInterval; 
        }else{
            this.takePassengerCountdown -= 1; 
        }
    }


    private void dropPassenger(Passenger p){
        for(Car c: cars){
            if (c.getPassengers().contains(p)){
                c.removePassenger(p); 
                p.enterStation(currentStation);
            }
        }
    }


    public void tryDropPassenger(){
        if (getDownQueue.size() == 0){
            this.state = State.ARRIVE_GETON; 
            return; 
        }
        if (takePassengerCountdown == 0){
            while (getDownQueue.size() > 0){
                Passenger p = getDownQueue.removeFirst(); 
                if (!(p.willingToGetOn(this))){
                    dropPassenger(p); 
                    this.takePassengerCountdown = takePassengerInterval; 
                    break; 
                }
            }
            // no passenger want to get down
            this.state = State.ARRIVE_GETON; 
        }else{
            this.takePassengerCountdown -= 1; 
        }
    }

    public void destroy(){
        for (Car c: cars){
            c.destroy();
        }
        cars.clear(); 
    }

    private int dist(Position p1, Position p2){
        return (int) Math.round(Math.sqrt( Math.pow(p1.i - p2.i, 2.0) + Math.pow(p1.j - p2.j, 2.0) )); 
    }

    public void update(){
        if (state == State.MOVING){
            // update current position
            this.position = railway.moveLocomotive(this); 

            // update current station
            if (dist(this.position, railway.getNextStation(direction).getPosition()) < distThres){
                railway.getNextStation(direction).locomotiveArrive(this);
                this.currentStation = railway.getNextStation(direction); 
                if (currentStation.getNextRailway(railway) == railway){
                    turnAround();
                }
                this.currentSpeed = 0; 
                this.state = State.ARRIVE_DROP; 
                this.getDownQueue = getAllPassenger(); 
            }
        }
        else if (state == State.ARRIVE_DROP){
            tryDropPassenger(); 
        }
        else if (state == State.ARRIVE_GETON){
            tryTakePassenger(currentStation.getPassengerQueue()); 
        }
        else{
            // error
            if (true)
                System.out.println("Locomotive state error");
        }
    }
}
