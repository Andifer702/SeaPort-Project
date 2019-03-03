/*
 * File Name: World.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: This is the world class to contain all objects. 
 * Also has methods for searching and assigning objects for linking
 */
package seaport_park;

import java.util.ArrayList;
import java.util.Scanner;

public class World extends Thing {

    private ArrayList<SeaPort> ports;
    private PortTime time;

    //Constructor
    public World(Scanner s) {
        super(s);
        ports = new ArrayList<>();
    }

    //Getter & setter methods
    public ArrayList<SeaPort> getPorts() {
        return this.ports;
    }

    public void setPorts(ArrayList<SeaPort> p) {
        ports = p;
    }

    public PortTime getTime() {
        return this.time;
    }

    public void setTime(PortTime t) {
        time = t;
    }

    public void assignPort(SeaPort sp) {
        ports.add(sp);
    }

    public void assignDock(SeaPort sp, Dock d) {
        sp.getDock().add(d);
    }

    public void assignShip(SeaPort sp, Dock d, Ship s) {
        sp.getShip().add(s);
        if (d == null) { // if d is null that means the ship has not docked. 
            sp.getQue().add(s); //send to que ArrayList
        } else {
            d.setShip(s); //else ship is docked
        }
    }

    public void assignPerson(SeaPort sp, Person pr) {
        sp.getPerson().add(pr);
    }
    
    public void assignJob(Ship sh, Job jb){
        sh.getJob().add(jb);
    }

    //Search method for searching by name. Nest loop is created
    //For each port, each sub classes are looped to find a match.
    //Match found are added to the search result ArrayList.
    public ArrayList<Thing> searchByName(String s) {
        ArrayList<Thing> searchResult = new ArrayList<>();
        for (SeaPort sp : ports) {
            if (sp.getName().equalsIgnoreCase(s)) {
                searchResult.add(sp);
            }
            for (Dock d : sp.getDock()) {
                if (d.getName().equalsIgnoreCase(s)) {
                    searchResult.add(d);
                }
            }
            for (Ship sh : sp.getShip()) {
                if (sh.getName().equalsIgnoreCase(s)) {
                    searchResult.add(sh);
                }
            }
            for (Person pr : sp.getPerson()) {
                if (pr.getName().equalsIgnoreCase(s)) {
                    searchResult.add(pr);
                }
            }
        }
        return searchResult;
    }
    
    //Search method for searching by index. Nest loop is created
    //For each port, each sub classes are looped to find a match.
    //Match found are added to the search result ArrayList.
    /*
    public ArrayList<Thing> searchByIndex(String s){
        ArrayList<Thing> searchResult = new ArrayList<>();
        int x = Integer.parseInt(s);
        for (SeaPort sp : ports){
            if (sp.getIndex() == x){
                searchResult.add(sp);
            }
            for (Dock d : sp.getDock()){
                if (d.getIndex() == x){
                    searchResult.add(d);
                }
            }
            for (Ship sh : sp.getShip()){
                if (sh.getIndex() == x){
                    searchResult.add(sh);
                }
            }
            for (Person pr : sp.getPerson()){
                if (pr.getIndex() == x){
                    searchResult.add(pr);
                }
            }
        }
        return searchResult;
    }
*/
    
    //Search method for searching by skill. Nest loop is created
    //For each port, all Perons are filtered to find a person with the matching skill
    //Person instance with the matching skill is added to the result.
    public ArrayList<Thing> searchBySkill(String s) {
        ArrayList<Thing> searchResult = new ArrayList<>();
        for (SeaPort sp : ports) {
            for (Person pr : sp.getPerson()) {
                if (pr.getSkill().equalsIgnoreCase(s)) {
                    searchResult.add(pr);
                }
            }
        }
        return searchResult;
    }
    @Override
    public String toString() {
        String st = "\n\nWorld: ";
        for (SeaPort sp : ports) {
            st += "\n" + sp;
        }
        return st;
    } // end method toString

}
