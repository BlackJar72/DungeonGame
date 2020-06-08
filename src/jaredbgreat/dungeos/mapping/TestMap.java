package jaredbgreat.dungeos.mapping;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jared Blackburn
 */
public class TestMap extends AbstractMap {
    List<Spatial> tiles = new ArrayList<>();
    
    public TestMap() {
        int f = Geomorphs.MORPHS.getGeomorphID("SimpleFloor");
        room = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
                           {0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                           {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 1, 2, 1, 1, 1, 1, 1},
                           {0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 0, 0, 0, 0, 1, 0, 0}
                          };
        geomodel = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, f, f, 0, 0, 0},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {f, f, f, f, f, f, f, f, f, f},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {0, 0, 0, f, f, f, f, f, f, f},
                               {0, 0, 0, 0, 0, 0, 0, f, 0, 0}
                          };
        simpleRefineMap();
    }
    
    
    public void build(Node parent) {
        float xoff = (room.length / 2);
        float zoff = (room[0].length / 2);        
        for(int i = 0; i < room.length; i++) 
            for(int j = 0; j < room[i].length; j++) {
                if(room[i][j] > 0) {
                    Spatial tile = Geomorphs.REGISTRY.getModel(geomodel[i][j])
                            .makeSpatialAt((i - xoff) * 3, 0, (j - zoff) * 3);
                    tiles.add(tile);
                    parent.attachChild(tile);
                }
        }
    }
    
}
