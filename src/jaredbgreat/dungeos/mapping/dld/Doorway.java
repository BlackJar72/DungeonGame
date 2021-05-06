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
    
    
    public Doorway(int x, int z, int y, ECardinal dir, Room from) {
        super(x, x, z, z, y, y + 1);
        heading = dir;
        connects = new Room[2];
        connects[0] = from;
    }
    
    
    @Override
    public int getType() {
        return 1;
    }
    
}
