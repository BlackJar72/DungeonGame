/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.tables.Tables;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jared
 */
public class Dungeon {
    protected GeomorphManager geoman;
    Random random;
    RoomList rooms;
    MapMatrix map;
    Sizes size;
    
    Room[] hubRooms;
    
    
    int b;
    int a;
    Room room;
    
    
    public Dungeon(GeomorphManager geoman) {
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
        
        makeHubRooms();
        
        map.buildMap(this);
    }
    
    
    private void makeHubRooms() {
        int num = 4 + random.nextInt(2) + random.nextInt(2);
        hubRooms = new Room[num];
        //int[] dims;
        List<Sector> sectors = Sector.getShuffledList(random);//Sector.getSectorList();
        //Collections.shuffle(sectors, random);
        for(int i = 0; i < hubRooms.length; i++) {
            //dims = Tables.getRoomSize(random);
            // FIXME: This can be done better!
            Sector sector = sectors.get(i);
            int x = random.nextInt(16) + (sector.x * 16);
            int z = random.nextInt(16) + (sector.z * 16);
            RoomSeed seed = new RoomSeed(x, 0, z);
            // Hubrooms are bigger than average
            hubRooms[i] = seed.growRoom(random.nextInt(5) + 4, random.nextInt(5) + 4, 
                    1, this, null, room);
            if(hubRooms[i] != null) {
                rooms.add(hubRooms[i]);
                hubRooms[i].setID(rooms.realSize());
                switch(random.nextInt(2)) {
                    case 0:
                        hubRooms[i].setGeomorph(b);
                        break;
                    case 1:
                    default:
                        hubRooms[i].setGeomorph(a);
                        break;
                }
                hubRooms[i].buildIn(map);
                hubRooms[i].addDoors(this, true);
            }
        }
    }
    
    
    
    
}
