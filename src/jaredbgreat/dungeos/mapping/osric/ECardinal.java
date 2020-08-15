package jaredbgreat.dungeos.mapping.osric;

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
    
}
