package jaredbgreat.dungeos.mapping.dld;

/**
 *
 * @author jared
 */
/*
 I really need to decide, is this actually going to build the dungeon?  Or is a record keeping system 
 for use during generation, afterwhich the rooms will generate themselves?  The later would be work well 
 with a portal systems as well as ways to organize some other data during play.
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
        int xboundh = Math.min(maxx + 1, room.length);
        int zboundh = Math.min(maxz + 1, room.length);
        for(int i = xboundl; i <= xboundh; i++) {
            if(room[i][zboundl] > 0) room[i][zboundl] = BOUNDARY;
            if(room[i][zboundh] > 0) room[i][zboundh] = BOUNDARY;
        }
        for(int i = zboundl; i <= zboundh; i++) {
            if(room[xboundl][i] > 0) room[xboundl][i] = BOUNDARY;
            if(room[xboundh][i] > 0) room[zboundh][i] = BOUNDARY;
        }
    }
    
}
