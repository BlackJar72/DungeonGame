package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.mapping.AbstractMap;

/**
 *
 * @author jared
 */
public class DLDStyleMap extends AbstractMap {
    
    public DLDStyleMap(int[][] rooms, int[][] geos) {
        room = rooms;
        geomodel = geos;
    }
    
    
}
