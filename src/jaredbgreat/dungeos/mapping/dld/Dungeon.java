package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;

/**
 *
 * @author jared
 */
public class Dungeon {
    private final GeomorphManager geoman;
    final Sizes size;    
    int[][] room;
    int[][] geomodel;
    int[] localModel;
    
    public Dungeon(GeomorphManager geoManager, Sizes size) {
        geoman = geoManager;
        this.size = size;
        room = new int[size.width][size.width];
        geomodel = new int[size.width][size.width];
        localModel = new int[2];
        localModel[0] = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        localModel[1] = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
    }
    
}
