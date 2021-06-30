package meowtro.metro_system.train;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import meowtro.Position;
import meowtro.game.*;
import meowtro.game.passenger.Passenger;
import meowtro.metro_system.*;
import meowtro.metro_system.railway.*;
import meowtro.metro_system.station.*;

public class Locomotive {

    private enum State{
        MOVING, 
        ARRIVE_DROP, 
        ARRIVE_GETON, 
    }

    private Station currentStation;  // Station | null
    private int level = 0; 
    private int maxLevel; 
    private int currentSpeed; 
    private Map<Integer, Integer> levelToMaxSpeed; 
    private Map<Integer, Integer> levelToMaxCar; 
    private Railway railway; 
    private Position position; 
    private Direction direction; 
    private ArrayList<Car> cars = new ArrayList<Car>(); 

    private int takePassengerInterval = 10; 
    private int dropPassengerInterval = 10; 
    private int takePassengerCountdown = 0; 
    private int dropPassengerCountdown = 0; 
    private LinkedList<Passenger> getDownQueue = null; 
    private LinkedList<Passenger> getUpQueue = null; 
    private int index = 0;
    private static int nextIndex = 0;
    
    private State state; 
    private int distThres = 8; 

    private static int getNextIndex() {
        return (Locomotive.nextIndex++);
    }
    
    /**
    * Parse game config and set proper value. 
    */
    public void init(){
        this.maxLevel = Integer.valueOf(Game.getConfig().get("metro_system.locomotive.max_level")); 
        this.takePassengerCountdown = Integer.valueOf(Game.getConfig().get("metro_system.locomotive.take_passenger_interval")); 
        this.dropPassengerCountdown = Integer.valueOf(Game.getConfig().get("metro_system.locomotive.drop_passenger_interval")); 
        this.distThres = Integer.valueOf(Game.getConfig().get("metro_system.locomotive.dist_thres")); 

        this.levelToMaxSpeed = new HashMap<Integer, Integer>(); 
        this.levelToMaxCar = new HashMap<Integer, Integer>(); 

        int level = 0; 
        for (String s: Game.getConfig().get("metro_system.locomotive.level_to_maxspeed").split("_")){
            levelToMaxSpeed.put(level, Integer.valueOf(s)); 
            level += 1; 
        }
        if (level < maxLevel)
            System.out.println("ValueError: metro_system.locomotive.level_to_maxspeed < maxLevel"); 
        level = 0; 
        for (String s: Game.getConfig().get("metro_system.locomotive.level_to_maxcar").split("_")){
            levelToMaxCar.put(level, Integer.valueOf(s)); 
            level += 1; 
        }
        if (level < maxLevel)
            System.out.println("ValueError: metro_system.locomotive.level_to_maxspeed < maxLevel"); 
    }

    private Rectangle image;
    private void setImage(Color color) {
        this.image = new Rectangle();
        this.image.setHeight(20);
        this.image.setWidth(10);
        this.image.setFill(color);
        setImagePosition(this.position, this.image.getHeight()/2, this.image.getWidth()/2);
    }
    public Rectangle getImage() {
        return this.image;
    }
    public void setImagePosition(Position position, double shiftSizeX, double shiftSizeY) {
        this.image.setLayoutX(position.j-shiftSizeX);
        this.image.setLayoutY(position.i-shiftSizeY);
    }
    public void tuneImageSize(double size) {
        this.image.setHeight(size);
        this.image.setWidth(size);
    }

    public Locomotive(Railway railway, Position position, Direction direction, Color color){
        init();
        this.railway = railway; 
        this.position = position; 
        this.direction = direction;
        this.state = State.MOVING; 
        this.index = Locomotive.getNextIndex();
        railway.addLocomotive(this);
        if (Game.DEBUG){
            System.out.printf("Locomotive created at %s, railway %s\n", position.toString(), railway.toString());
        }
        setImage(color);
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
        if (Game.DEBUG){
            System.out.printf("Turned around\n"); 
        }
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
                p.enterCar(c);
                return true; 
            }
        }
        return false; 
    }


    public LinkedList<Passenger> getAllPassenger(){
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
        // if (Game.DEBUG){
        System.out.printf("Loco depart from station %s\n", currentStation.toString());
        // }
        this.takePassengerCountdown = 0; 
        this.dropPassengerCountdown = 0; 
        currentStation.locomotiveDepart(this);
        railway.removeLocomotive(this);
        this.railway = currentStation.getNextRailway(railway); 
        railway.locomotiveDepart(this);
        this.currentStation = null; 
        this.state = State.MOVING; 
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


    public void tryTakePassenger(){
        if (getUpQueue.size() == 0){
            depart();
            return; 
        }
        if (takePassengerCountdown == 0){
            Passenger p = getUpQueue.removeFirst(); 
            boolean success = assignPassengerToCar(p); 
            if (success){
                if (Game.DEBUG){
                    System.out.printf("...Passenger get on locomotive at station %s, %s\n", currentStation.name, position.toString());
                }
                currentStation.removePassenger(p); 
            } else{
                // Cars are full
                if (Game.DEBUG)
                    System.out.printf("...Cars are full or don't have any car\n");
                depart();
            }
            this.takePassengerCountdown = takePassengerInterval; 
        } else{
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
                if (p.willingToGetOff(this)){
                    dropPassenger(p); 
                    if (Game.DEBUG){
                        System.out.printf("Passenger get off locomotive at station %s, %s\n", currentStation.toString(), position.toString());
                    }
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


    public void update(){
        if (state == State.MOVING){
            // update current position
            this.position = railway.moveLocomotive(this);
            
            setImagePosition(this.position, this.image.getHeight()/2, this.image.getWidth()/2);

            // update current station
            if (railway.isArrived(this, railway.getNextStation(direction))){
                railway.getNextStation(direction).locomotiveArrive(this);
                this.currentStation = railway.getNextStation(direction);
                if (Game.DEBUG){
                    System.out.printf("arrived %s, %d waiting\n", currentStation.toString(), currentStation.getPassengerQueue().size()); 
                }
                
                if (currentStation.getNextRailway(railway) == railway){
                    turnAround();
                }
                this.currentSpeed = 0; 
                this.takePassengerCountdown = 0;
                this.dropPassengerCountdown = 0;
                this.getDownQueue = new LinkedList<Passenger>(
                                                getAllPassenger()
                                                .stream()
                                                .filter(p -> p.willingToGetOff(this))
                                                .collect(Collectors.toList())); 
                this.getUpQueue = new LinkedList<Passenger>(
                                                currentStation.getPassengerQueue()
                                                .stream()
                                                .filter(p -> p.willingToGetOn(this))
                                                .collect(Collectors.toList())); 
                this.state = State.ARRIVE_DROP; 
            }
        }
        else if (state == State.ARRIVE_DROP){
            if (Game.DEBUG)
                System.out.printf("Trying Dropping passenger...\n");
            tryDropPassenger(); 
        }
        else if (state == State.ARRIVE_GETON){
            if (Game.DEBUG)
                System.out.printf("Trying taking passenger...\n");
            tryTakePassenger();
        }
        else{
            // error
            if (true)
                System.out.println("Locomotive state error");
        }
    }

    public String toString() {
        return String.format("L%d", this.index);
    }
}
