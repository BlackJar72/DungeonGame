package jaredbgreat.dungeos.mapping.dld;

/**
 *
 * @author Jared Blackburn
 */
public enum Direction {
    
    W (1,  1,  0),
    N (2,  0,  1),
    E (4, -1,  0),
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
    
    
    public Direction complement() {
        switch(this) {
            case W: return E;
            case N: return S;
            case E: return W;
            case S: return N;
            default: return W;
        }
    }
    
    
    public static Direction complement(Direction dir) {
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
            
    
    public int addremove(int in) {
        return in & cbits;
    }
    
}
