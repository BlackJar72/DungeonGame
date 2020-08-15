package jaredbgreat.dungeos.mapping.osric;

import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import java.util.Random;

/**
 *
 * @author jared
 */
public class Dungeon {
    protected GeomorphManager geoman;
    Random random;
    Room room;
    
    public Dungeon(GeomorphManager geoman) {
        random = new Random();
        this.geoman = geoman;        
        int b = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        int a = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
        // A stand-in, but basically how I plan on doing main rooms.
        room = new Room(-2 - random.nextInt(2), 2 + random.nextInt(2), 
                        -2 - random.nextInt(2), 2 + random.nextInt(2));
        room.setGeomorph(a);
        room.addDoors();
        room.build(geoman);
    }
    
}
