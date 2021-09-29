package jaredbgreat.cubicnightmare.mapping.tables;

import com.jme3.math.Vector3f;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public enum EDir {
    
     W (1,   -1,  0),
    NW (2,   -1,  1),
     N (4,    0,  1),
    NE (8,    1,  1),
     E (16,   1,  0),
    SE (32,   1, -1),
     S (64,   0, -1),
    SW (128, -1, -1);
        
    
    public final int bits, cbits, incx, incz;
    public final float cost, sx, sz;
    public Vector3f vec;
    
    // I don't know if this is good; i.e., I don't know if 
    // values() generates a new (and mutable) array each time 
    // or just returns a version of this already stored.
    private static final EDir[] ALL_DIRS = EDir.values();
    
        
    EDir(int bits, int incx, int incz){//, float a) {
        this.bits  = bits;
        this.cbits = ~bits;
        this.incx  = incx;        
        this.incz  = incz;
        cost = (float)Math.sqrt((incx * incx) + (incz * incz));
        sx = ((float)incx) / cost;
        sz = ((float)incz) / cost;
        vec = new Vector3f(sx, 0, sz);
    }
    
    
    public static EDir fromCardinal(ECardinal inDir) {
        return inDir.dir;
    }
    
    
    public static int fromCardinalBits(int inBits) {
        return (inBits ^ 1) + ((inBits ^ 2) << 1) + ((inBits ^ 4) << 2) + ((inBits ^ 8) << 3);
    }
    
    
    public static int fromWallData(int inBits) {
        // Get block directions from walls, correcting for how geomorph data is stored.
        inBits = fromCardinalBits(inBits >> 16);
        // Block corners where either wall is blocked; should be equivalent to:
        // inBits |= ((inBits & 1) << 1) | ((inBits & 4) >> 1);
        // inBits |= ((inBits & 1) << 1) | ((inBits & 4) >> 1);
        // inBits |= ((inBits & 4) << 1) | ((inBits & 16) >> 1);
        // inBits |= ((inBits & 16) << 1) | ((inBits & 64) >> 1);
        // inBits |= ((inBits & 64) << 1) | ((inBits & 1) << 7);
        inBits |= (inBits << 1) | (inBits >> 1);        
        inBits |= ((inBits & 1) << 7);
        // Return the the bitset for non-blocked directions.
        return (~inBits) & 255;
    }
    
    
    public int add(int in) {
        return in | bits;
    }
    
    
    public static int add(int n, EDir d) {
        return n | d.bits;
    }
    
    
    public int remove(int in) {
        return in & (cbits);
    }
    
    
    public static int remove(int n, EDir d) {
        return n & (d.cbits);
    }
    
    
    public static EDir getRandom(Random random) {
        return ALL_DIRS[random.nextInt(ALL_DIRS.length)];
    }
    
    
    public EDir clockwise() {
        return ALL_DIRS[(ordinal() + 1) % 8];
    }
    
    
    public EDir clockwise(int turns) {
        return ALL_DIRS[(ordinal() + turns) % 8];
    }
    
    
    public EDir opposite() {
        return ALL_DIRS[(ordinal() + 4) % 8];
    }
    
    
    public static EDir fromOrdinal(int value) {
        return ALL_DIRS[value];
    }
    
    
    public boolean canGo(int dirs) {
        return (dirs & bits) > 0;
    }
    
    
    public static boolean canGo(EDir dir, int dirs) {
        return (dirs & dir.bits) > 0;
    }
    
    
    public float canGoCost(int dirs) {
        if((dirs & bits) > 0) return cost;
        return -1f; // Less than 0 means can not go!
    }
    
    
    public static float canGoCost(EDir dir, int dirs) {
        if((dirs & dir.bits) > 0) return dir.cost;
        return -1f; // Less than 0 means can not go!
    }
    
}
