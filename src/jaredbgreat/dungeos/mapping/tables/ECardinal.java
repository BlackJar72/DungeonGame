package jaredbgreat.dungeos.mapping.tables;

import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public enum ECardinal {
    
    W (1, -1,  0),
    N (2,  0,  1),
    E (4,  1,  0),
    S (8,  0, -1);
    
    public final int bits, cbits, incx, incz;
    
    // I don't know if this is good; i.e., I don't know if 
    // values() generates a new (and mutable) array each time 
    // or just returns a version of this already stored.
    private static final ECardinal[] ALL_DIRS = ECardinal.values();
    
    
    ECardinal(int bits, int incx, int incz) {
        this.bits  = bits;
        this.cbits = ~(bits << 16);
        this.incx  = incx;
        this.incz  = incz;
    }
    
    
    public int addWall(int in) {
        return in | (bits << 16);
    }
    
    
    public int removeWall(int in) {
        return in & (cbits);
    }
    
    
    public static ECardinal getRandom(Random random) {
        return ALL_DIRS[random.nextInt(ALL_DIRS.length)];
    }
    
}