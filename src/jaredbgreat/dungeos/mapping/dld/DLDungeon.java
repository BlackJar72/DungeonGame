/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.osric.Room;
import jaredbgreat.dungeos.mapping.tables.Tables;
import java.util.Random;

/**
 *
 * @author jared
 */
public class DLDungeon {
    protected GeomorphManager geoman;
    Random random;
    Room room;
    
    
    public DLDungeon(GeomorphManager geoman) {
        random = new Random();
        this.geoman = geoman;
        dummyRoom();
    }
    
    
    private void dummyRoom() {
        int b = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        int a = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
        // A stand-in, but basically how I plan on doing main rooms.
        //room = new Room(-2 - random.nextInt(2), 2 + random.nextInt(2), 
        //                -2 - random.nextInt(2), 2 + random.nextInt(2));
        int[] s = Tables.getRoomSize(random);
        System.out.println(s[0] +  " x " + s[1]);
        room = new Room(-(s[0] / 2), (s[0] / 2)  + (s[0] % 2), 
                        -(s[1] / 2) ,(s[1] / 2)  + (s[1] % 2));
        switch(random.nextInt(2)) {
            case 0:
                room.setGeomorph(b);
                break;
            case 1:
            default:
                room.setGeomorph(a);
                break;
        }
        room.addDoors();
        room.build(geoman);        
    }
    
    
    
    
    
}
