package jaredbgreat.cubicnightmare.mapping.dld;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.cubicnightmare.componenent.GeomorphManager;
import jaredbgreat.cubicnightmare.componenent.geomorph.GeomorphModel;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.mapping.tables.ECardinal;
import static jaredbgreat.cubicnightmare.mapping.tables.ECardinal.*;
import jaredbgreat.cubicnightmare.mapping.tables.Tables;
import java.util.ArrayList;
import java.util.List;

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
    private GeomorphModel pillar;
    int id;
    
    ArrayList<Doorway> exits;
    
    
    /**
     * Create the null room; this should never be called elsewhere.
     */
    private Room() {
        id = ix = iz = width = length = height = x1 = x2 = z1 = z2 = y1 = y2 = 0;
        centerx = centerz = 0.0f;
    }

    
    Room(int startx, int endx, int startz, int endz, int starty, int endy) {
        exits = new ArrayList<>();
        x1 = startx; x2 = endx;
        z1 = startz; z2 = endz;
        width  = x2 - x1 + 1; 
        length = z2 - z1 + 1;
        centerx = ((float)width / 2.0f) + (float)x1;
        centerz = ((float)length/ 2.0f) + (float)z1;
        ix = (int)centerx;
        iz = (int)centerz;
        height = 1;
        y1 = 0;
        y2 = endy;   
    }
    
    
    public int setID(int id) {
        return this.id = id;
    }
    
    
    public int getType() {
        return AreaType.ROOM.tid;
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
            if(dungeon.random.nextBoolean()) {
                int pos = dungeon.random.nextInt(getPerimeter());
                if(pos < width) {
                    dir = ECardinal.N;
                } else if(pos < (width + length)) {                
                    dir = ECardinal.E;
                } else if(pos < (width + width + length)) {                
                    dir = ECardinal.S;
                } else {
                    dir = ECardinal.W;
                }
            }
            addDoor(dungeon, dir);
        }
    }
    
    
    public Doorway addDoor(Dungeon dungeon, ECardinal dir) {
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
        int x = ndoor.doorx + ndoor.heading.incx;
        int z = ndoor.doorz + ndoor.heading.incz;
        if((x < 0) || (x > 63) || (z < 0) || (z > 63)) {
            return null;
        }
        int nextid = dungeon.map.room[x][z];
        if(nextid > 0) {
            if(dungeon.map.type[x][z] == AreaType.ROOM.tid) {
                ndoor.connects[1] = dungeon.areas.getRoom(dungeon.map.room[x][z]);
                exits.add(ndoor);
                ndoor.connects[1].exits.add(ndoor);
                return ndoor.connects[1];
            } else if(dungeon.map.type[x][z] == AreaType.DOORWAY.tid) {
                ndoor.connects[1] = dungeon.areas.getDoorway(dungeon.map.room[x][z]);
                Doorway odoor =  (Doorway)ndoor.connects[1];
                odoor.connects[1] = ndoor;
                exits.add(ndoor);
                return odoor.connects[0];                
            }
        }
        Room other = ndoor.makeOtherRoom(dungeon);
        if(other != null) {
            ndoor.connects[1] = other;
            exits.add(ndoor);
        }
        return other;
    }
    
    
    public Vector3f getCenterAsVec() {
        return new Vector3f(centerx * 3, (y1 + height / 2) * 3, centerz * 3);
    }
    
    
    public Vector3f getCenterAsVec(float h) {
        return new Vector3f(centerx * 3, (y1 + height / 2) * 3 + h, centerz * 3);
    }
    
    
    public float sqdist(Room other) {
        float dx = centerx - other.centerx, dz = centerz - other.centerz;
        return (dx * dx) + (dz * dz);
    }
    
    
    public float dist(Room other) {
        float dx = centerx - other.centerx, dz = centerz - other.centerz;
        return (float)Math.sqrt((dx * dx) + (dz * dz));
    }
    
    
    public String coordsAsString() {
        return "(" + ix + ", " + y1 + ", " + iz + ")";
     }

    public void setBaseGeomorph(int baseGeomorph) {
        this.baseGeomorph = baseGeomorph;
    }
    
    
    public void setPillar(HubRoom hub) {
        pillar = hub.getPillar();
    }
    
    
    public GeomorphModel getPillar() {
        return pillar;
    }
    
    
    
}
