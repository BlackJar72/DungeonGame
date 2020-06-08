package jaredbgreat.dungeos.mapping;

/**
 *
 * @author Jared Blackburn
 */
public class AbstractMap {
    protected int[][] room;
    protected int[][] geomodel;
    
    
    public void simpleRefineMap() {
        for(int i = 0; i < room.length; i++) 
            for(int j = 0; j < room[i].length; j++) {
                geomodel[i][j] += (findRotationFromBorders(i, j) << 16);
            }
    }
    
    
    protected int findRotationFromBorders(int x, int z) {
        int out = 0;
        if(((x - 1) < 0) || (room[x - 1][z] != room[x][z])) out+= 1;
        if(((z + 1) == room[x].length) || (room[x][z + 1] != room[x][z])) out+= 8;
        if(((x + 1) == room.length) || (room[x + 1][z] != room[x][z])) out+= 4;
        if(((z - 1) < 0) || (room[x][z - 1] != room[x][z])) out+= 2;
        return out;
    }
    
}
