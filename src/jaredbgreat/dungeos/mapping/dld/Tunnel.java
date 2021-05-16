package jaredbgreat.dungeos.mapping.dld;

/**
 *
 * @author jared
 */
public class Tunnel extends Room {
    
    public Tunnel(int startx, int startz, int starty) {
        this(startx, startz, starty, starty + 1);
    }
    
    public Tunnel(int startx, int startz, int starty, int height) {
        super(startx, startx, startz, startz, starty, starty + height);
    }
    
    
    public int getType() {
        return AreaType.TUNNEL.tid;
    }
    
}
