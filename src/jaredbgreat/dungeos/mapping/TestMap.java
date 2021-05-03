package jaredbgreat.dungeos.mapping;

import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;

/**
 * Purely a test class giveing a hypothetical small sections 
 * of tiles to hand coded to test dungeon assembly and 
 * reandering.
 * 
 * @author Jared Blackburn
 */
public class TestMap extends AbstractMap {
    
    public TestMap() {
        int b = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        int a = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
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
                               {0, 0, 0, 0, 0, a, a, 0, 0, 0},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {b, b, b, a, a, a, a, a, a, a},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {0, 0, 0, a, a, a, a, a, a, a},
                               {0, 0, 0, 0, 0, 0, 0, b, 0, 0}
                          };
        simpleRefineMap();
    }
    
    
    private void simpleRefineMap() {
        for(int i = 0; i < room.length; i++) 
            for(int j = 0; j < room[i].length; j++) {
                geomodel[i][j] += (findRotationFromBorders(i, j) << 16);
            }
    }
    
    
    private int findRotationFromBorders(int x, int z) {
        int out = 96;
        if(((x - 1) <= 0) || (room[x - 1][z] != room[x][z])) out+= 1;
        if(((z + 1) == room[x].length) || (room[x][z + 1] != room[x][z])) out+= 8;
        if(((x + 1) == room.length) || (room[x + 1][z] != room[x][z])) out+= 4;
        if(((z - 1) <= 0) || (room[x][z - 1] != room[x][z])) out+= 2;
        return out;
    }
    
}
