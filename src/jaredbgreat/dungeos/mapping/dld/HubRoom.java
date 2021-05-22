package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.mapping.decorator.AreaZone;

/**
 *
 * @author Jared Blackburn
 */
public class HubRoom extends Room {
    AreaZone zone;
    
    
    public HubRoom(int startx, int endx, int startz, int endz, int starty, int endy) {
        super(startx, endx, startz, endz, starty, endy);
    }
    
    
    public void setAreaZone(Dungeon dungeon, int themeID) {
        zone = new AreaZone(this, themeID, dungeon.random.nextDouble() + 1, dungeon);        
    }
    
}
