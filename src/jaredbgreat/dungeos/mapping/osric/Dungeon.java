package jaredbgreat.dungeos.mapping.osric;

import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;

/**
 *
 * @author jared
 */
public class Dungeon {
    protected GeomorphManager geoman;
    Room room;
    
    public Dungeon(GeomorphManager geoman) {
        this.geoman = geoman;        
        int b = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        int a = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
        room = new Room(-2, 1, -2, 2);
        room.setGeomorph(a);
        room.build(geoman);
    }
    
}
