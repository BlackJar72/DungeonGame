/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.tables.Tables;
import java.util.ArrayList;
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
    Areas areas;
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
        areas = new Areas();
        size = Sizes.LARGE;
        build();
    }
    
    
    private void build() {        
        dummyRoom();
        
        makeHubRooms();
        
        connectHubs();
        growthCycle();
        
        map.buildMap(this);
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
                RoomList rl = areas.getList(0);
                rl.add(hubRooms[i]);
                hubRooms[i].setID(rl.realSize());
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
    
    
    public void growthCycle() {
        ArrayList<Doorway> toUse;
        ArrayList<Room> newRooms = new ArrayList<>();
        toUse = (ArrayList<Doorway>)areas.getDoorways().clone();
        Collections.copy(areas.getDoorways(), toUse);
        for(Doorway d : toUse) {
            Room r = d.makeOtherRoom(this);
            if(r != null) {
                newRooms.add(r);
            }
        }
        while(!newRooms.isEmpty()) {
            toUse.clear();
            for(Room r : newRooms) {
                toUse.addAll(r.exits);
            }
            newRooms.clear();
            if(!toUse.isEmpty()) {
                for(Doorway d : toUse) {
                    Room r = d.makeOtherRoom(this);
                    if(r != null) {
                        newRooms.add(r);
                    }
                }
            }
        }
    }
	
	/**
	 * This will connect all the nodes with series of intermediate rooms by 
	 * callaing either connectNodesDensely or connectNodesSparcely, with a 
	 * 50% chance of each.
	 * 
	 * @throws Throwable
	 */
	private void connectHubs() {
		if(random.nextBoolean()) {
			connectHubsDensely();
		} else {
			connectHubsSparcely();
		}
	}
	
	
	/**
	 * This will attempt to connect all nodes based on the logic that 
	 * if B can be reached from A, and C can be reached from B, then 
	 * C can be reached from A (by going through B if no other route 
	 * exists).
	 * 
	 * Specifically, it will connect the first node to one random other 
	 * node, and then connect a random node already connected to the 
	 * first with one that has not been connected, until all nodes have 
	 * attempted a connects.  Note that this does not guarantee connections 
	 * as the attempt to place a route between any two nodes may fail.
	 * 
	 * @throws Throwable
	 */
	private void connectHubsSparcely() {
		Room first, other;
		ArrayList<Room> connected = new ArrayList<>(hubRooms.length), 
				disconnected = new ArrayList<>(hubRooms.length);		
		connected.add(hubRooms[0]);
		for(int i = 1; i < hubRooms.length; i++) {
			disconnected.add(hubRooms[i]);
		}		
		while(!disconnected.isEmpty()) {
			first = connected.get(random.nextInt(connected.size()));
			other = disconnected.get(random.nextInt(disconnected.size()));
			new Route(first, other).drawConnections(this);
			connected.add(other);
			disconnected.remove(other);
                }
	}
	
	
	/**
	 * This will attempt to make one connects between every two pairs of 
	 * nodes by first connecting the first node to all others directly, 
	 * then each successive node to every node with a higher index.  As 
	 * nodes with a lower index will already have attempted a connects 
	 * this is not repeated.  Note that this does not guarantee connections 
	 * as the attempt to place a route between any two nodes may fail.
	 * 
	 * @throws Throwable
	 */
	private void connectHubsDensely() {
		Room first, other;
		for(int i = 0; i < hubRooms.length; i++) {
			first = hubRooms[i];
			for(int j = i + 1; j < hubRooms.length; j++) {
				other = hubRooms[j];		
				if(other != first) {
					new Route(first, other).drawConnections(this);
				}
			}			
		}
	}
    
    
}
