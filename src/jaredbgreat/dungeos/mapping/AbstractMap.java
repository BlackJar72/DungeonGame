package jaredbgreat.dungeos.mapping;

import jaredbgreat.dungeos.componenet.GeomorphManager;

/**
 *
 * @author Jared Blackburn
 */
public class AbstractMap {
    protected static GeomorphManager geoman;
    
    protected int[][] room;
    protected int[][] geomodel;
    
    /**
     * This prepares the maps by assigning a GeomophManger 
     * to add the geomorphs through; it must be called before 
     * attempting to build any map of any kind.
     * 
     * @param geoManager 
     */
    public static void init(GeomorphManager geoManager) {
        geoman = geoManager;
    }
}
