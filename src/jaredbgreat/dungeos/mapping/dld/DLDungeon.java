/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.tables.Tables;
import java.util.Random;

/**
 *
 * @author jared
 */
public class DLDungeon {
    protected GeomorphManager geoman;
    Random random;
    RoomList rooms;
    MapMatrix map;
    Sizes size;
    
    Room[] nodeRooms;
    
    
    int b;
    int a;
    Room room;
    
    
    public DLDungeon(GeomorphManager geoman) {
        random = new Random();
        this.geoman = geoman;
        map = new MapMatrix();
        rooms = new RoomList(256);
        size = Sizes.LARGE;
        dummyRoom();
    }
    
    
    private void dummyRoom() {
        /*int*/ b = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        /*int*/ a = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
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
        //room.addDoors();
        room.fastBuild(geoman);   
        
        makeNodeRooms();
    }
    
    
    private void makeNodeRooms() {
        int num = 4 + random.nextInt(2) + random.nextInt(2);
        nodeRooms = new Room[num];
        int[] dims;
        for(int i = 0; i < nodeRooms.length; i++) {
            dims = Tables.getRoomSize(random);
            // FIXME: This can be done better!
            int x = random.nextInt(48) + 8;
            int z = random.nextInt(48) + 8;
            nodeRooms[i] = new Room(x, z, 0, dims[0], dims[1], 1);
            //For tesing only
            {
                switch(random.nextInt(2)) {
                    case 0:
                        nodeRooms[i].setGeomorph(b);
                        break;
                    case 1:
                    default:
                        nodeRooms[i].setGeomorph(a);
                        break;
                }            
                nodeRooms[i].fastBuild(geoman);
            }
        }
    }
    
    
    
    
}
