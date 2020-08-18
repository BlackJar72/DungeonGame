package jaredbgreat.dungeos.mapping.osric;

import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import static jaredbgreat.dungeos.mapping.osric.ECardinal.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class Room {
    int id, geomorph, x1, x2, z1, z2, y, h;
    List<Spatial> tiles;
    List<Doorway> doors;
    
    public Room(int x1, int x2, int z1, int z2) {
        this(x1, x2, z1, z2, 0, 1);
    }
    
    public Room(int x1, int x2, int z1, int z2, int y, int h) {
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
        this.y  = y;
        this.h  = h;
    }
    
    
    /**
     * This will add the room to a List and assign itself an id 
     * equal to it's index in the list.
     * 
     * @param list
     * @return 
     */
    public int addToList(List<Room> list) {
        list.add(this);        
        return id = list.indexOf(this);
    }
    
    
    public int getArea() {
        return (x2 - x1) * (z2 - z1);
    }
    
    
    public int getVolume() {
        return (x2 - x1) * (z2 - z1) * h;
    }
    
    
    /**
     * This uses AABB collision detection to determine if this room 
     * and another room "other" overlap each other.
     * 
     * @param other
     * @return true if the rooms overlap
     */
    public boolean overlaps(Room other) {
        return (((x1 < other.x2) && (x1 > other.x1)) || ((x2 > other.x1) && (x2 < other.x2)))
                && (((z1 < other.z2) && (z1 > other.z1)) || ((z2 > other.z1) && (z2 < other.z2)));
    }
    
    
    /**
     * This will try to remove overlap with another room, and will return a 
     * boolean telling if a valid (decently sized and reasonably placed) room 
     * remains.  Note the this does not test for if the initial interest is 
     * still valid, that will need to be done separately.
     * 
     * @param other
     * @return 
     */
    public boolean removeOverlap(Room other, Dungeon dungeon) {
        // Find the centers of the rooms.
        float x  = (((float)(x2 - x1)) / 2.0f);
        float z  = (((float)(z2 - z1)) / 2.0f);
        float ox = (((float)(other.x2 - other.x1)) / 2.0f);
        float oz = (((float)(other.z2 - other.z1)) / 2.0f);
        // If one room is too inside the other, based on a center in the other, 
        // just fail this.
        if(((ox < x2) && (ox > x1) && (oz < z2) && (oz > z1))
          ||((x < other.x2) && (x > other.x1) && (z < other.z2) && (z > other.z1)))
            return false;
        //OK, now determine what changes to make an if they are feasible.
        float dx = x - ox, dz = z - oz;
        if(dungeon.random.nextBoolean()) {
            removeOverlapX(other, dx);
        } else {
            removeOverlapZ(other, dz);
        }
        int sx = x2 - x1, sz = z2 - z1;
        // FIXME? Is this too small a size constraint?  (Each dim >= 2; room at least 2x3)
        return ((sx * sx) > 1) && ((sz * sz) > 1);// && ((sx * sz) > 5);
    }
    
    
    private void removeOverlapX(Room other, float dx) {
        if(dx < 0.0f) {
            x2 = other.x2 - 1;
        } else {
            x1 = other.x1 + 1;
        }
    }
    
    
    private void removeOverlapZ(Room other, float dz) {
        if(dz < 0.0f) {
            z2 = other.z2 - 1;
        } else {
            z1 = other.z1 + 1;
        }
    }
    
    
    public void setGeomorph(int geo) {
        geomorph = geo;
    }
    
    
    /**
     * This adds doorways to the room, meaning points of exit; 
     * this does not actually add doors (which may come later 
     * as an occation addition to an exit.
     * 
     * FIXME: This is only a stand-in to test the door adding 
     * and removal system.
     */
    public void addDoors() {
        doors = new ArrayList<>();
        doors.add(new Doorway((x2 - x1 - 1) / 2, z2 - z1 - 1, S));
    }
    
    
    
    /**
     * Builds the room into the world; this should not be 
     * called until after all other processing for the room 
     * has been done.
     * 
     * @param geoman 
     */
    public void build(GeomorphManager geoman) {
        tiles = new ArrayList<>((((x2 - x1) * (z2 - z1)) * 4) / 3);
        int sx   = x2 - x1, sz = z2 - z1;
        int[] tids = new int[sx * sz];
        for(int i = 0; i < tids.length; i++ ) {
            tids[i] = geomorph |= 96 << 16;
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
        if(doors != null) for(Doorway door : doors) {
            int location = door.z * sx + door.x;
            tids[location] 
                    = door.add(tids[location]);
        }
        for(int i = 0; i < tids.length; i++) {
            Spatial tile = Geomorphs.REGISTRY.makeSpatialAt(tids[i], 
                   (x1 + (i % sx)) * 3, 0, (z1 + (i / sx)) * 3);
            tiles.add(tile);
            geoman.attachSpatial(tile);
        }
    }
    
    
    public void cleanup() {
        // TODO: Detatch all tiles, removing any physic and other controls
    }
    
    
}
