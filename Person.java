/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Person class with skills
 */
package seaport_park;

import java.util.Scanner;

public class Person extends Thing{
    private String skill;
    
    //Constructor
    public Person (Scanner s){
        super(s);
        skill = s.next();
    }
    
    //Getter & Setter methods
    public String getSkill(){
        return this.skill;
    }
    public void setSkill(String sk){
        this.skill = skill;
    }
    
    public String toString(){
        String st = "Person: " + super.toString() + " Skill: " + skill + "\n";
        return st;
    }
}
