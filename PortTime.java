/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Port Time class. No use for Project 1.
 */
package seaport_park;

public class PortTime {
    private int time;
    
    public PortTime(){
        time = 0;
    }
    
    //Getter & Setter Methods
    public int getTime(){
        return this.time;
    }
    public void setTime(int t){
        this.time = t;
    }
    
    public String toString(){
        String st = "Time: " + time;
        return st;
    }
}
