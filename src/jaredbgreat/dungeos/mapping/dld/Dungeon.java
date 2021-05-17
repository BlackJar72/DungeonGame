package jaredbgreat.dungeos.mapping.dld;

import com.jme3.math.Vector3f;
import jaredbgreat.dungeos.Main;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;
import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import jaredbgreat.dungeos.entities.CubeMob;
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
    GeomorphManager geoman;
    AppStateSinglePlayer game;
    Random random;
    Areas areas;
    MapMatrix map;
    Sizes size;
    
    Room[] hubRooms;
    Room endRoom;
    Vector3f playerStart;
    
    
    int b;
    int a;
    Room room;
    
    
    public Dungeon(AppStateSinglePlayer playing, GeomorphManager geoman) {
        game = playing;
        random = new Random();
        this.geoman = geoman;
        size = Sizes.LARGE;
        playerStart = new Vector3f();
        build();
    }
    
    
    private void build() { 
        Main.proflogger.startTask("Build");        
        boolean bad = true;
        
        while(bad) {
            map = new MapMatrix();
            areas = new Areas();
            plan(); 
            doorFixer(); 
            RoomBFS seeker = new RoomBFS(this);
            bad = !seeker.test();
            for(Room r : areas.getRoomList()) {
                r.setGeomorph(random.nextInt(3));
            }
            connectHubsSparcely(seeker);
            map.populateDirs();
        }
        
        findPlayerStart();     
        
        map.buildMap(this);
        
        addMobs();  
        Main.proflogger.endTask("Build"); 
    }
    
    
    private void plan() {        
        makeHubRooms();        
        connectHubs();
        growthCycle();
    }
    
    
    //TODO: This is a prototype, not sure if its the best way!
    private void doorFixer() {
        ArrayList<Doorway> toRemove = new ArrayList<>();
        for(Room r : areas.getDoorways()) {
            Doorway dw = (Doorway)r;
            if(dw.connects[1] == null) {
                int fu = map.room[Math.min(63, Math.max(0, dw.doorx + dw.heading.incx))]
                        [Math.min(63, Math.max(0, dw.doorz + dw.heading.incz))];
                int es = map.type[Math.min(63, Math.max(0, dw.doorx + dw.heading.incx))]
                        [Math.min(63, Math.max(0, dw.doorz + dw.heading.incz))];
                if((fu < 1) || (es != 0) || (areas.getRoom(fu) == null)) {
                    toRemove.add(dw);
                } else {
                    Room du = areas.getRoom(fu);
                    dw.connects[1] = du;
                    if(!du.exits.contains(dw)) {
                        du.exits.add(dw);
                    }
                }
            }  
            if(dw.connects[0] == null) {
                int fu = map.room[Math.min(63, Math.max(0, dw.doorx - dw.heading.incx))]
                        [Math.min(63, Math.max(0, dw.doorz - dw.heading.incz))];
                int es = map.type[Math.min(63, Math.max(0, dw.doorx - dw.heading.incx))]
                        [Math.min(63, Math.max(0, dw.doorz - dw.heading.incz))];
                if((fu < 1) || (es != 0) || (areas.getRoom(fu) == null)) {
                    toRemove.add(dw);
                } else {
                    Room du = areas.getRoom(fu);
                    dw.connects[0] = du;
                    if(!du.exits.contains(dw)) {
                        du.exits.add(dw);
                    }                        
                }
            }              
        }
        for(Doorway dw : toRemove) {
            map.removeDoor(dw, this);
        }
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
                hubRooms[i].addDoors(this, false);
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
            connectHubsDensely();
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
		for(int i = 0; i < hubRooms.length; i++) {
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
	private void connectHubsSparcely(RoomBFS connector) {
		Room first, other;
		ArrayList<Room> connected = new ArrayList<>(hubRooms.length), 
				disconnected = new ArrayList<>(hubRooms.length);
                Room start = hubRooms[random.nextInt(hubRooms.length - 1) + 1];
		connected.add(start);
		for(int i = 1; i < hubRooms.length; i++) {
			disconnected.add(hubRooms[i]);
		}
                disconnected.remove(start);
		while(!disconnected.isEmpty()) {
			first = connected.get(random.nextInt(connected.size()));
			other = disconnected.get(random.nextInt(disconnected.size()));
			connector.connect(first, other);
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
        
        
        public void findPlayerStart() {
            playerStart.set(hubRooms[0].centerx * 3, (float)hubRooms[0].y1 * 3, 
                        hubRooms[0].centerz * 3);
            float dist = 0;
            for(int i = 1; i < hubRooms.length; i++) {
               float d = playerStart.distanceSquared(new 
                         Vector3f(hubRooms[i].centerx * 3, hubRooms[i].y1 * 3, hubRooms[i].centerz * 3));
               if(d > dist) {
                   dist = d;
                   endRoom = hubRooms[i];
               }
            }
        }
        
        
        public Vector3f getPlayerStart() {
            return playerStart;
        }
        
        
        public Vector3f getLevelEndSpot() {
            return new Vector3f(endRoom.centerx * 3, endRoom.y1 * 3, endRoom.centerz * 3);
        }
        
        
        public GeomorphManager getGeomorphManager() {
            return geoman;
        }
        
        
        public void addMobs() {
            int i = 0;
            for(Room r : areas.getRoomList()) {
                if((r.id > 1) && random.nextInt(3) == 0) {
                    CubeMob cb = new CubeMob(game, this, game.getApplications().getRootNode(), 
                            game.getPhysics(), r.getCenterAsVec(), "Cube" + i);
                    i++;
                }
            }
        }
    
    
}
