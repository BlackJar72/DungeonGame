package jaredbgreat.dungeos.mapping.dld;

import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public enum Direction {
    
    W (1, -1,  0),
    N (2,  0,  1),
    E (4,  1,  0),
    S (8,  0, -1);
    
    public final int bits;
    public final int cbits;
    public final int incx;
    public final int incz;
    
    Direction(int bits, int dx, int dz) {
        this.bits  =  bits;
        this.cbits = ~bits;
        this.incx = dx;
        this.incz = dz;
    }
    
    
    public Direction opposite() {
        switch(this) {
            case W: return E;
            case N: return S;
            case E: return W;
            case S: return N;
            default: return W;
        }
    }
    
    
    public static Direction opposite(Direction dir) {
        switch(dir) {
            case W: return E;
            case N: return S;
            case E: return W;
            case S: return N;
            default: return W;
        }
    }
            
    
    public int add(int in) {
        return in | bits;
    }
            
    
    public int remove(int in) {
        return in & cbits;
    }
            
    
    public int addWall(int in) {
        return in | (bits << 16);
    }
            
    
    public int removeWall(int in) {
        return in & (cbits << 16);
    }
            
    
    public int addWall(int in, boolean doIt) {
        if(doIt) {
            return in | (bits << 16);
        }
        return in;
    }
            
    
    public int removeWall(int in, boolean doIt) {
        if(doIt) {
            return in & (cbits << 16);
        }
        return in;
    }
    
    public boolean onX() {
        return ((this == W) || (this == E));
    }
    
    public Direction right() {
        switch(this) {
            case W: return N;
            case N: return E;
            case E: return S;
            case S: return W;
            default: return W;
        }
    }
    
    public Direction left() {
        switch(this) {
            case W: return S;
            case N: return W;
            case E: return N;
            case S: return E;
            default: return W;
        }
    }
    
    public static Direction randomX(Random random) {
        if(random.nextBoolean()) return W;
        return E;
    }
    
    
    public static Direction randomZ(Random random) {
        if(random.nextBoolean()) return N;
        return S;
    }
    
    
    public static Direction random(Random random) {
        switch(random.nextInt(4)) {
            case 0: return W;
            case 1: return N;
            case 2: return E;
            case 3: return S;
            default: return W;
        }}
    
}
