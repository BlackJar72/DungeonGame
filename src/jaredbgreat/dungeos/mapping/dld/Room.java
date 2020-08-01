package jaredbgreat.dungeos.mapping.dld;


/**
 *
 * @author Jared Blackburn
 */
public class Room {
    public static final Room NULL = new Room();
    
    public final int id;
    public final int x, z;
    public final int geomorph;
    private int sizex, sizez;
    private int startx;
    private int endx;
    private int startz;
    private int endz;
    
    
    /**
     * A constructor specifically for the null room 
     * (areas not in a room).
     */
    private Room() {
        id = 0;
        // Other fields are irrelevant as this "room" is not actually 
        // built, being a null room representing unused space.
        x = z = 0;        
        geomorph = 0;
    }
    
    
    public Room(int id, int startx, int endx, int startz, int endz, int geomorph) {
        this.id = id;
        this.geomorph = geomorph;
        makePrebounded(startx, endx, startz, endz);
        this.x  = startz + sizex / 2;
        this.z  = startz + sizez / 2; 
    }
    
    
    private void makePrebounded(int startx, int endx, int startz, int endz) {
        this.sizex = endx - startx;
        this.sizez = endz - startz;
        // Find the bounds of the room; yes its biased positive
        this.startx = x - sizex / 2;
        this.endx   = x + sizex / 2 + sizex % 2;
        this.startz = z - sizez / 2;
        this.endz   = z + sizez / 2 + sizez % 2;
    }
    
    
    public void build(int[][] rooms, int[][] geos) {
        for(int i = startx; i <= endx; i++) 
            for(int j = startz; j <= endz; j++) {
                rooms[i][j] = id;
                geos[i][j] = geomorph;
            }
        // TODO: Find doors
        for(int i = startz; i <= endz; i++) {
            geos[startx][i] += 1 << 16;
            geos[endx][i]   += 4 << 16;
        }
        for(int i = startx; i <= endx; i++) {
            geos[i][startz] += 2 << 16;
            geos[i][endz]   += 8 << 16;
        }
    }
    
}
