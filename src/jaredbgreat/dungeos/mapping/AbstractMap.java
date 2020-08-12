package jaredbgreat.dungeos.mapping;

import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jared Blackburn
 */
public class AbstractMap implements ILevelMap {
    protected static GeomorphManager geoman;
    
    protected int[][] room;
    protected int[][] geomodel;    
    protected List<Spatial> tiles = new ArrayList<>();
    
    
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
    
    
    /**
     * A basic map building method; that this may be overridden 
     * to extend features is expects, however this provides the 
     * basics.  By basics I mean it places floors and wall, but 
     * not other objects (such as decorations).
     */
    // TODO: Handle wall endcaps.
    @Override
    public void build() {
        
        System.out.println();
        for(int i = 0; i < geomodel.length; i++) {
            for(int j = 0; j < geomodel[i].length; j++) {                
                System.out.print((room[i][j] & 0xffff) + ", ");
            }            
            System.out.println();
        }        
        System.out.println();
        
        float xoff = (room.length / 2);
        float zoff = (room[0].length / 2);        
        for(int i = 0; i < room.length; i++) 
            for(int j = 0; j < room[i].length; j++) {
                if(room[i][j] > 0) {
                    Spatial tile = Geomorphs.REGISTRY.makeSpatialAt(geomodel[i][j], 
                           (i - xoff) * 3, 0, (j - zoff) * 3);
                    tiles.add(tile);
                    geoman.attachSpatial(tile);
                }
        }
    }
}
