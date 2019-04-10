/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: This is the SeaPort class to connect all related classes to a Sea Port.
 */
package seaport_park;

import java.util.ArrayList;
import java.util.Scanner;

public class SeaPort extends Thing {

    private ArrayList<Dock> docks;
    private ArrayList<Ship> que;
    private ArrayList<Ship> ships;
    private ArrayList<Person> persons;

    //Constructor
    public SeaPort(Scanner s) {
        super(s);
        docks = new ArrayList<>();
        que = new ArrayList<>();
        ships = new ArrayList<>();
        persons = new ArrayList<>();
    }

    //Getter & Setter methods
    public ArrayList<Dock> getDock() {
        return this.docks;
    }

    public void setDock(ArrayList<Dock> d) {
        this.docks = d;
    }

    public ArrayList<Ship> getQue() {
        return this.que;
    }

    public void setQue(ArrayList<Ship> q) {
        this.que = q;
    }

    public ArrayList<Ship> getShip() {
        return this.ships;
    }

    public void setShip(ArrayList<Ship> s) {
        this.ships = s;
    }

    public ArrayList<Person> getPerson() {
        return this.persons;
    }

    public void setPerson(ArrayList<Person> p) {
        this.persons = p;
    }

    //checks to see if the Port contains the required workers to complete the job
    //Regardless of whether they are currently availble or not
    
    public synchronized boolean checkForWorkers(ArrayList<String> requirements) {
        int counter = 0;
        int reqCounter = 0;
        ArrayList<Person> qualifiedWorkers = new ArrayList<>();
        outerLoop:
        for (String requirement : requirements) {
            for (Person worker : this.getPerson()) {
                //To prevent adding same worker more than twice for a job that requires multiple person with same skill.
                if (requirement.equals(worker.getSkill()) && !qualifiedWorkers.contains(worker)) {               
                    qualifiedWorkers.add(worker);
                    counter++;
                    continue outerLoop;
                }
            }
        }
        reqCounter = requirements.size();
        if (counter == reqCounter) {
            return true;
        } else {
            return false;
        }
    }

    //This method is for iterating through the job requirement and the availble workers
    //The method first checks to make sure that the required resources are available at the port.
    //If a worker is found and also available, it is added to the ArrayList and boolean value isWorking is updated
    //However, if a worker is currently not available due to being used for another job, all workers
    //are released back to the pool and the method returns null.
    
    public synchronized ArrayList<Person> getResources(Job jb) {
        ArrayList<Person> activeWorkers = new ArrayList<>();
        boolean requirementsMet = true;
        if (this.checkForWorkers(jb.getRequirements())) { //initial check to see if the job can be done
            outerLoop:
            for (String requirement : jb.getRequirements()) {
                for (Person worker : this.getPerson()) {
                    //if worker hs the required skill and is available for work
                    //worker added to ArrayList
                    if (worker.getSkill().equals(requirement) && !worker.getIsWorking()) { 
                        worker.setIsWorking(true);
                        activeWorkers.add(worker);
                        jb.getWorkerTextArea().append(worker.getName() + " acquired for " + jb.getName() + "\n");
                        continue outerLoop;
                    }
                }
                //unless all workers required for the job is added
                //requirements are not met
                requirementsMet = false;
                break;
            }
        }

        if (requirementsMet) {
            return activeWorkers;
        } else { //release all workers
            activeWorkers.forEach((worker) -> {
                jb.getResourceTextArea().append("Name:" + worker.getName() + "/Skill:" + worker.getSkill() + " released to back to port and is available.\n");
            });
            returnResources(activeWorkers);
            return null;
        }
    }

    //This method is for returning all workers back to the available resource pool
    public synchronized void returnResources(ArrayList<Person> workers) {
        if (workers != null) {
            workers.forEach((worker) -> {
                worker.setIsWorking(false);
            });
        }
    }

    public String toString() {
        String st = "\n\nSeaPort: " + super.toString() + "\n";
        for (Dock md : docks) {
            st += "\n" + md;
        }
        st += "\n\n --- List of all ships in que:";
        for (Ship ms : que) {
            st += "\n" + ms;
        }
        st += "\n\n --- List of all ships:";
        for (Ship ms : ships) {
            st += "\n" + ms;
        }
        st += "\n\n --- List of all persons:";
        for (Person mp : persons) {
            st += "\n" + mp;
        }
        return st;
    } // end method toString   

}
