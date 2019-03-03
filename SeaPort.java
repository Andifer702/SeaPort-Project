/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: This is the SeaPort class to connect all related classes to a Sea Port.
 */
package seaport_park;

import java.util.ArrayList;
import java.util.Scanner;

public class SeaPort extends Thing{
    private ArrayList<Dock> docks;
    private ArrayList<Ship> que;
    private ArrayList<Ship> ships;
    private ArrayList<Person> persons;
    
    //Constructor
    public SeaPort (Scanner s){
        super(s);
        docks = new ArrayList<>();
        que = new ArrayList<>();
        ships = new ArrayList<>();
        persons = new ArrayList<>();
    }
    
    //Getter & Setter methods
    
    public ArrayList<Dock> getDock(){
        return this.docks;
    }
    
    public void setDock(ArrayList<Dock> d){
        this.docks = d;
    }
    
     public ArrayList<Ship> getQue(){
        return this.que;
    }
     
      public void setQue(ArrayList<Ship> q){
        this.que = q;
    }
     
      public ArrayList<Ship> getShip(){
        return this.ships;
    }
      
       public void setShip(ArrayList<Ship> s){
        this.ships = s;
    }
      
       public ArrayList<Person> getPerson(){
        return this.persons;
    }
       
        public void setPerson(ArrayList<Person> p){
        this.persons = p;
    }
       
    public String toString () {
      String st = "\n\nSeaPort: " + super.toString() + "\n";
      for (Dock md: docks) st += "\n" + md;
      st += "\n\n --- List of all ships in que:";
      for (Ship ms: que ) st += "\n" + ms;
      st += "\n\n --- List of all ships:";
      for (Ship ms: ships) st += "\n" + ms;
      st += "\n\n --- List of all persons:";
      for (Person mp: persons) st += "\n" + mp;
      return st;
   } // end method toString   
    
    
}
