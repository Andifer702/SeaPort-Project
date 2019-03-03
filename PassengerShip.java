/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Child class of Ship with additional fields
 */
package seaport_park;

import java.util.HashMap;
import java.util.Scanner;

public class PassengerShip extends Ship{
    private int numberOfOccupiedRooms;
    private int numberOfPassengers;
    private int numberOfRooms;
    
    //Constructor
    public PassengerShip (Scanner s, HashMap<Integer, Dock> dockMap, HashMap<Integer,SeaPort> portMap) {
        super(s, dockMap, portMap);
        numberOfPassengers = s.nextInt();
        numberOfRooms = s.nextInt();
        numberOfOccupiedRooms = s.nextInt(); 
    }
    
    //Getter & Setter methods
    public int getOccRooms(){
        return this.numberOfOccupiedRooms;
    }
    public void setOccRooms(int o){
        this.numberOfOccupiedRooms = 0;
    }
    
    public int getPassengers(){
        return this.numberOfPassengers;
    }
    public void setPassengers(int p){
        this.numberOfPassengers = p;
    }
    
    public int getRooms(){
        return this.numberOfRooms;
    }
    public void setRooms(int r){
        this.numberOfRooms = r;
    }
    
    public String toString(){
        String st = " Passenger Ship: " + super.toString();
        st += "Passengers: " + numberOfPassengers + " Rooms: " + numberOfRooms + " Occupied Rooms: " + numberOfOccupiedRooms + "\n";
        return st;
    }
}
