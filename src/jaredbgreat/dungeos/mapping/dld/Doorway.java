package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.mapping.tables.ECardinal;

/**
 *
 * @author jared
 */
// FIXME? Should this extend room?  Or should this and room extend an AbstractRoom class?
public class Doorway extends Room {
    final ECardinal heading;
    final Room[] connects;
    
    
    public Doorway(int startx, int endx, int startz, int endz, int starty, int endy, 
                ECardinal heading, Room from) {
        super(startx, endx, startz, endz, starty, endy);
        this.heading = heading;
        connects = new Room[2];
        connects[0] = from;
    }
    
    
    
}
