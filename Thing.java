/*
 * File Name: Thing.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: This is the parent class for the objects that will be created in this project.
 */
package seaport_park;

import java.util.Scanner;

public class Thing implements Comparable<Thing> {

    private int index;
    private String name;
    private int parent;

    //Constructor
    public Thing(Scanner s) {
        name = s.hasNext() ? s.next() : "N/A";
        index = s.hasNextInt() ? s.nextInt() : 0;
        parent = s.hasNextInt() ? s.nextInt() : 0;
    }

    //Getter & Setter methods
    public int getIndex() {
        return index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int i) {
        this.parent = i;
    }

    @Override
    public int compareTo(Thing o) {
        return this.name.compareToIgnoreCase(o.getName());
    }
    
    public String toString (){
        return  name + " " + index ;
    }

}
