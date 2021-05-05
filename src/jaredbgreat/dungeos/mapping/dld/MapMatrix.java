package jaredbgreat.dungeos.mapping.dld;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;

/**
 *
 * @author jared
 */
/*
 I really need to decide, is this actually going to build the dungeon?  Or is a record keeping system 
 for use during generation, afterwhich the rooms will generate themselves?  The later would be work well 
 with a portal systems as well as ways to organize some other data during play.  *OR* I could use this 
 to build but attach what is built to the relevant rooms by id.S
*/
public class MapMatrix {
    private static final int BOUNDARY = -1; // Marks area around rooms where doorways appear
    int[][] room;
    int[][] geomorph; // Do I need this, or should a let rooms build themselves, as nodes?
    
    
    public MapMatrix() {
        this(64);
    }
    
    
    public MapMatrix(int size) {
        room     = new int[size][size];
        geomorph = new int[size][size];
    }
    
    
    public void addRoom(int id, int geo, int minx, int maxx, int minz, int maxz) {
        for(int i = minx; i <= maxx; i++) 
            for(int j = minz; j <= maxz; j++) {
                room[i][j] = id;
                geomorph[i][j] = geo;
            }
        int xboundl = Math.max(minx - 1, 0);
        int zboundl = Math.max(minz - 1, 0);
        int xboundh = Math.min(maxx + 1, room.length - 1);
        int zboundh = Math.min(maxz + 1, room.length - 1);
        for(int i = xboundl; i <= xboundh; i++) {
            if(room[i][zboundl] == 0) room[i][zboundl] = BOUNDARY;
            if(room[i][zboundh] == 0) room[i][zboundh] = BOUNDARY;
        }
        for(int i = zboundl; i <= zboundh; i++) {
            if(room[xboundl][i] == 0) room[xboundl][i] = BOUNDARY;
            if(room[xboundh][i] == 0) room[xboundh][i] = BOUNDARY;
        }
    }
    
    
    public void addDoor(int x, int z) {
        if((x > -1) && (x < (room.length - 1)) 
                    && (z > -1) && (z < (room[x].length - 1)) && (room[x][z] < 1)) {
            //FXIME: Specific room IDs for specific doors!
            room[x][z] = 1;
        }
    }
    
    
    public void buildMap(DLDungeon dungeon) {
        simpleRefineMap();
        for(int i = 0; i < geomorph.length; i++) {
            for(int j = 0; j < geomorph[i].length; j++) {
                //if(room[i][j] > -1) System.out.print(' ');
                //if(room[i][j] < 10) System.out.print(' ');
                //System.out.print(room[i][j]);
                if(room[i][j] > 0) {
                    Room theRoom = dungeon.rooms.get(room[i][j]);
                    Spatial tile = Geomorphs.REGISTRY.makeSpatialAt(geomorph[i][j], 
                            i * 3, theRoom.y1 * 3, j * 3);                
                    dungeon.geoman.attachSpatial(tile, theRoom.roomSpace);                
                }
            }
            //System.out.println();
        }
        for(int i = 1; i < dungeon.rooms.size(); i++) {
            dungeon.geoman.attachNode(dungeon.rooms.get(i).roomSpace);
        }
    }    
    
    
    
    private void simpleRefineMap() {
        for(int i = 0; i < room.length; i++) 
            for(int j = 0; j < room[i].length; j++) {
                geomorph[i][j] += (findRotationFromBorders(i, j) << 16);
            }
    }
    
    
    private int findRotationFromBorders(int x, int z) {
        int out = 96;
        if(((x - 1) <= 0) || (room[x - 1][z] < 1)) out+= 1;
        if(((z + 1) == room[x].length) || (room[x][z + 1] < 1)) out+= 8;
        if(((x + 1) == room.length) || (room[x + 1][z] < 1)) out+= 4;
        if(((z - 1) <= 0) || (room[x][z - 1] < 1)) out+= 2;
        return out;
    }
    
}
