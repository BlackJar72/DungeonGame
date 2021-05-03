package jaredbgreat.dungeos.mapping.osric;

import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.tables.Tables;
import java.util.Random;

/**
 *
 * @author jared
 */
public class RTDungeon {
    protected GeomorphManager geoman;
    Random random;
    Room room;
    
    public RTDungeon(GeomorphManager geoman) {
        random = new Random();
        this.geoman = geoman;        
        int b = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        int a = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
        // A stand-in, but basically how I plan on doing main rooms.
        room = new Room(-2 - random.nextInt(2), 2 + random.nextInt(2), 
                        -2 - random.nextInt(2), 2 + random.nextInt(2));
        //int[] s = Tables.getRoomSize(random);
        //System.out.println(s[0] +  " x " + s[1]);
        //room = new Room(-(s[0] / 2), (s[0] / 2)  + (s[0] % 2), 
        //                -(s[1] / 2) ,(s[1] / 2)  + (s[1] % 2));
        room.setGeomorph(a);
        room.addDoors();
        room.build(geoman);
    }
    
    
    public Random getRandom() {
        return random;
    }
    
}
