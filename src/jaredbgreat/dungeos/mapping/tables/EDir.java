package jaredbgreat.dungeos.mapping.tables;

import com.jme3.math.Vector3f;

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
        
    EDir(int bits, int incx, int incz){//, float a) {
        this.bits  = bits;
        this.cbits = ~(bits << 16);
        this.incx  = incx;        
        this.incz  = incz;
        cost = (float)Math.sqrt((incx * incx) + (incz * incz));
        sx = ((float)incx) / cost;
        sz = ((float)incz) / cost;
        vec = new Vector3f(sx, 0, sz);
    }
}
