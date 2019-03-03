/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Dock class to retain to ship information on the dock
 */
package seaport_park;

import java.util.Scanner;

public class Dock extends Thing{
    private Ship ship;
    
    //Constructor
    public Dock(Scanner s){
        super(s);
       
    }
    //Getter & Setter methods
    
    public Ship getShip(){
        return ship;
    }
    
    public void setShip(Ship sh){
        this.ship = sh;
    }
    
    public String toString(){
        String st = "Dock: " + super.toString();
        st += "\nShip:" + ship;
        return st;
    }
}
