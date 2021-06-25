package meowtro.metro_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Locomotive {
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

    public static void init(){
        // TODO: import the setting from config
        levelToMaxSpeed = new HashMap<Integer, Integer>(); 
        levelToMaxSpeed.put(0, 20); 
        levelToMaxSpeed.put(1, 30); 
        levelToMaxSpeed.put(2, 40); 

        levelToMaxCar = new HashMap<Integer, Integer>(); 
        levelToMaxCar.put(0, 2); 
        levelToMaxCar.put(0, 4); 
        levelToMaxCar.put(0, 6); 
    }

    public Locomotive(Railway railway, Position position, Direction direction){
        this.railway = railway; 
        this.position = position; 
        this.direction = direction;
    }

    public Station getSourceStation(){
        if (direction == Direction.FORWARD){
            //
        }
    }

    public Station getDestinationStation(){
        //
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

    public void update(){
        this.level = level + 1; 
    }
}
