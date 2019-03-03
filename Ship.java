/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Parent Ship class for two children ship
 */
package seaport_park;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Ship extends Thing {

    private PortTime arrivalTime;
    private PortTime dockTime;
    private double draft;
    private double length;
    private double weight;
    private double width;
    private ArrayList<Job> jobs;
    private Dock dock;
    private SeaPort port;

    //Constructor
    public Ship(Scanner s, HashMap<Integer, Dock> dockMap, HashMap<Integer, SeaPort> portMap) {
        super(s);
        this.setDock(dockMap);
        this.setPort(portMap, dockMap);
        dockTime = new PortTime();
        arrivalTime = new PortTime();
        draft = s.nextDouble();
        length = s.nextDouble();
        weight = s.nextDouble();
        width = s.nextDouble();
        jobs = new ArrayList<>();
    }
    //Getter & Setter Methods

    public PortTime getDockTime() {
        return this.dockTime;
    }

    public void setDockTime(PortTime d) {
        this.dockTime = d;
    }

    public PortTime getArrivTime() {
        return this.arrivalTime;
    }

    public void setArrivTime(PortTime d) {
        this.arrivalTime = d;
    }

    public double getDraft() {
        return this.draft;
    }

    public void setDraft(double d) {
        this.draft = d;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double l) {
        this.length = l;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double wd) {
        this.width = wd;
    }

    public ArrayList<Job> getJob() {
        return this.jobs;
    }

    public void setJob(ArrayList<Job> j) {
        this.jobs = j;
    }

    public void setDock(HashMap<Integer, Dock> dockMap) {
        if (dockMap.containsKey(this.getParent())) {
            this.dock = dockMap.get(this.getParent());
        }
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Dock getDock() {
        return this.dock;
    }

    public void setPort(HashMap<Integer, SeaPort> portMap, HashMap<Integer, Dock> dockMap) {
        this.port = portMap.get(this.getParent());
        if (this.port == null) {
            Dock d = dockMap.get(this.getParent());
            this.port = portMap.get(d.getParent());
        }
    }

    public SeaPort getPort() {
        return this.port;
    }

    public String toString() {
        String st = super.toString();
        st += "\nWeight: " + weight + " Length: " + length + " Width: " + width + " Draft: " + draft + "\n";
        return st;
    }

}
