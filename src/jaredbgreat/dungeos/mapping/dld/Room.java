package jaredbgreat.dungeos.mapping.dld;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.tables.ECardinal;
import static jaredbgreat.dungeos.mapping.tables.ECardinal.*;
import jaredbgreat.dungeos.mapping.tables.Tables;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jared
 */
public class Room {
    public static final Room NULL_ROOM = new Room();
    
    Node roomSpace = new Node();
    
    final int ix, iz, width, length, height, x1, x2, z1, z2, y1, y2;
    final float centerx, centerz;
    private int baseGeomorph;
    int id;
    
    ArrayList<Doorway> exits;
    
    
    /**
     * Create the null room; this should never be called elsewhere.
     */
    private Room() {
        id = ix = iz = width = length = height = x1 = x2 = z1 = z2 = y1 = y2 = 0;
        centerx = centerz = 0.0f;
    }

    
    Room(int startx, int endx, int startz, int endz) {
        exits = new ArrayList<>();
        x1 = startx; x2 = endx;
        z1 = startz; z2 = endz;
        width = x2 - x1; 
        length = z2 - z1;
        centerx = ((float)width / 2.0f) + (float)x2;
        centerz = ((float)length / 2.0f) + (float)z2;
        ix = (int)centerx;
        iz = (int)centerz;
        height = 1;
        y1 = 0;
        y2 = y1 + height;        
    }

    
    Room(int startx, int endx, int startz, int endz, int starty, int endy) {
        exits = new ArrayList<>();
        x1 = startx; x2 = endx;
        z1 = startz; z2 = endz;
        width = x2 - x1; 
        length = z2 - z1;
        centerx = ((float)width / 2.0f) + (float)x2;
        centerz = ((float)length / 2.0f) + (float)z2;
        ix = (int)centerx;
        iz = (int)centerz;
        height = 1;
        y1 = 0;
        y2 = endy;     
    }

    
    Room(int centerx, int centerz, int lowy, int width, int length, int height, boolean nothing) {
        exits = new ArrayList<>();
        this.width = width; 
        this.length = length;
        this.centerx = ix = centerx;
        this.centerz = iz = centerz;        
        x1 = centerx - (width / 2); 
        x2 = centerx + (width / 2) + (width % 2);
        z1 = centerz - (length / 2); 
        z2 = centerz + (length / 2) + (length % 2);
        this.height = height;
        y1 = lowy;
        y2 = y1 + height;        
    }
    
    
    public int setID(int id) {
        return this.id = id;
    }
    
    
    public int getType() {
        return 0;
    }
    
    
    public int getBaseGeomorph() {
        return baseGeomorph;
    }
    
    
    public void buildIn(MapMatrix map) {
        map.addRoom(id, baseGeomorph, 0, x1, x2, z1, z2);
    }

    
    public int getArea() {
        return width * height;
    }

    
    public int getPerimeter() {
        return (width + height) * 2;
    }

    
    void setGeomorph(int geo) {
        baseGeomorph = geo;
    }
    
    
    public void addDoors(Dungeon dungeon) {
        int num = Tables.getNumberOfDoors(dungeon.random, this);
        for(int i = 0; i < num; i++) {
            ECardinal dir = ECardinal.getRandom(dungeon.random);
            addDoor(dungeon, dir);
        }
    }
    
    
    public void addDoors(Dungeon dungeon, boolean hubroom) {
        int num = Tables.getNumberOfDoors(dungeon.random, this);
        if(hubroom) num = Math.max(num, dungeon.random.nextInt(4) + 2);
        for(int i = 0; i < num; i++) {
            ECardinal dir = ECardinal.getRandom(dungeon.random);
            addDoor(dungeon, dir);
        }
    }
    
    
    private Doorway addDoor(Dungeon dungeon, ECardinal dir) {
        int x, z;
        switch(dir) {
            case W:
                x = x1 + dir.incx;
                z = z1 + dungeon.random.nextInt(length + 1);
                break;
            case N:
                x = x1 + dungeon.random.nextInt(width + 1);
                z = z2 + dir.incz;
                break;
            case E:
                x = x2 + dir.incx;
                z = z1 + dungeon.random.nextInt(length + 1);
                break;
            case S:
                x = x1 + dungeon.random.nextInt(width + 1);
                z = z1 + dir.incz;
                break;
            default:
                throw new AssertionError(dir.name());        
        }
        Doorway d = dungeon.map.addDoor(x, z, y1, dir, this, dungeon);
        if(d != null) {
            exits.add(d);
        }
        return d;
    }
    
    
    
    
    
    
    
    /**
     * Builds the room into the world; this should not be 
     * called until after all other processing for the room 
     * has been done.
     * 
     * This is for testing in early development; a better, more 
     * general system using the whole map will come later.
     * 
     * @param geoman 
     */
    @Deprecated
    public void fastBuild(GeomorphManager geoman) {        
        List<Spatial> tiles = new ArrayList<>();
        tiles = new ArrayList<>((((x2 - x1) * (z2 - z1)) * 4) / 3);
        int sx   = x2 - x1, sz = z2 - z1;
        int[] tids = new int[sx * sz];
        for(int i = 0; i < tids.length; i++ ) {
            tids[i] = baseGeomorph |= 96 << 16;
        }
        for(int i = 0; i < sz; i++) {
            tids[i * sx] 
                    = W.addWall(tids[i * sx]);
            tids[(i * sx) + sx - 1] 
                    = E.addWall(tids[(i * sx) + sx - 1]);
        }
        for(int j = 0; j < sx; j++) {
            tids[j] 
                    = N.addWall(tids[j]);
            tids[j + ((sz - 1) * sx)] 
                    = S.addWall(tids[j + ((sz - 1) * sx)]);
        }
        //if(doors != null) for(Doorway door : doors) {
        //    int location = door.z * sx + door.x;
        //    tids[location] 
        //}
        for(int i = 0; i < tids.length; i++) {
            Spatial tile = Geomorphs.REGISTRY.makeSpatialAt(tids[i], 
                   (x1 + (i % sx)) * 3, 0, (z1 + (i / sx)) * 3);
            tiles.add(tile);
            geoman.attachSpatial(tile, roomSpace);
        }
        geoman.attachNode(roomSpace);
    }
    

    public Room connector(Dungeon dungeon, ECardinal dir, Route aThis) {
        Doorway ndoor = addDoor(dungeon, dir);
        if(ndoor == null) return null;
        return ndoor.makeOtherRoom(dungeon);
    }
    
    
}
