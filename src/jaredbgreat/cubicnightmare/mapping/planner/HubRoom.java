package jaredbgreat.cubicnightmare.mapping.planner;

import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorph;
import jaredbgreat.cubicnightmare.componenent.geomorph.GeomorphModel;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.mapping.decorator.AreaZone;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class HubRoom extends Room {  
    int themeID;
    AreaZone zone;
    GeomorphModel pillar;
    
    
    public HubRoom(int startx, int endx, int startz, int endz, int starty, int endy) {
        super(startx, endx, startz, endz, starty, endy);
    }
    
    
    public AreaZone setAreaZone(Dungeon dungeon, int themeID) {
        this.themeID = themeID;
        return zone = new AreaZone(this, themeID, dungeon.random.nextDouble() + 1, dungeon);        
    }
    
    
    public void setPillar(Random random) {
        pillar = ((Geomorph)Geomorphs.REGISTRY.getGeomorph(themeID)).getPillar(random);
    }
    
    
    public GeomorphModel getPillar() {
        return pillar;
    }
    
    
    public AreaZone getAreaZone() {
        return zone;
    }
}
