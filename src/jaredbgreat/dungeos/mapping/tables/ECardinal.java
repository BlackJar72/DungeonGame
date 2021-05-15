package jaredbgreat.dungeos.mapping.tables;

import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public enum ECardinal {
    
    W (1, -1,  0, EDir.W),
    N (2,  0,  1, EDir.N),
    E (4,  1,  0, EDir.E),
    S (8,  0, -1, EDir.S);
    
    public final int bits, cbits, incx, incz;
    public final EDir dir;
    
    // I don't know if this is good; i.e., I don't know if 
    // values() generates a new (and mutable) array each time 
    // or just returns a version of this already stored.
    private static final ECardinal[] ALL_DIRS = ECardinal.values();
    
    
    ECardinal(int bits, int incx, int incz, EDir dir) {
        this.bits  = bits;
        this.cbits = ~(bits << 16);
        this.incx  = incx;
        this.incz  = incz;
        this.dir   = dir;
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
    
    
    public ECardinal clockwise() {
        return ALL_DIRS[(ordinal() + 1) % 4];
    }
    
    
    public ECardinal clockwise(int turns) {
        return ALL_DIRS[(ordinal() + turns) % 4];
    }
    
    
    public ECardinal opposite() {
        return ALL_DIRS[(ordinal() + 2) % 4];
    }
    
    
    public static ECardinal fromOrdinal(int value) {
        return ALL_DIRS[value];
    }
    
}
