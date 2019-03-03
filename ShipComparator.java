
package seaport_park;

import java.util.Comparator;

/*
 * File Name: ShipComparator.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: This class compare two ship objects by their dimensions and return a value for sorting.
 */
public class ShipComparator implements Comparator<Ship>{
    private String spec;
    
    public ShipComparator (String spec){
        this.spec = spec;
    }
    
    public int compare (Ship sh1, Ship sh2){
        switch(spec){
            case "Draft":
                if (sh1.getDraft() == sh2.getDraft()){
                    return 0;
                } else if ( sh1.getDraft() > sh2.getDraft()){
                    return 1;
                } else {
                    return -1;
                }
            case "Length":
                if (sh1.getLength() == sh2.getLength()){
                    return 0;
                } else if ( sh1.getLength() > sh2.getLength()){
                    return 1;
                } else {
                    return -1;
                }
            case "Weight":
                if (sh1.getWeight() == sh2.getWeight()){
                    return 0;
                } else if ( sh1.getWeight() > sh2.getWeight()){
                    return 1;
                } else {
                    return -1;
                }
            case "Width":
                if (sh1.getWidth() == sh2.getWidth()){
                    return 0;
                } else if ( sh1.getWidth() > sh2.getWidth()){
                    return 1;
                } else {
                    return -1;
                }
            case "Name":
                return sh1.getName().compareToIgnoreCase(sh2.getName());
        }
        return -1;
    }
}
