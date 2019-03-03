/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Child class of Ship with additional fields
 */
package seaport_park;

import java.util.HashMap;
import java.util.Scanner;

public class CargoShip extends Ship {

    private double cargoValue;
    private double cargoVolume;
    private double cargoWeight;

    //Constructor
    public CargoShip(Scanner s, HashMap<Integer, Dock> dockMap, HashMap<Integer,SeaPort> portMap) {
        super(s, dockMap, portMap);
        cargoWeight = s.nextDouble();
        cargoVolume = s.nextDouble();
        cargoValue = s.nextDouble();
    }
    
    //Getter & Setter methods
    public double getCargoWeight(){
        return this.cargoWeight;
    }
    public void setCargoWeight(double w){
        this.cargoWeight = w;
    }
    
    public double getCargoVolume(){
        return this.cargoVolume;
    }
    public void setCargoVolume(double v){
        this.cargoVolume = v;
    }
    
    public double getCargoValue(){
        return this.cargoValue;
    }
    public void setCargoValue(double vl){
        this.cargoValue = vl;
    }
    
    public String toString(){
        String st = " Cargo Ship: " + super.toString();
        st += "Cargo Weight: " + cargoWeight + " Cargo Volume: " + cargoVolume + "  Cargo Value: " + cargoValue +"\n";
        return st;
    }
}
