package jaredbgreat.cubicnightmare.mapping.dld;

import jaredbgreat.cubicnightmare.mapping.decorator.AreaZone;

/**
 *
 * @author Jared Blackburn
 */
public class HubRoom extends Room {
    AreaZone zone;
    
    
    public HubRoom(int startx, int endx, int startz, int endz, int starty, int endy) {
        super(startx, endx, startz, endz, starty, endy);
    }
    
    
    public AreaZone setAreaZone(Dungeon dungeon, int themeID) {
        return zone = new AreaZone(this, themeID, dungeon.random.nextDouble() + 1, dungeon);        
    }    
    
    
    public AreaZone getAreaZone() {
        return zone;
    }
}
