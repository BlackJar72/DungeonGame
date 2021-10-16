package jaredbgreat.cubicnightmare.mapping.planner;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.cubicnightmare.Main;
import jaredbgreat.cubicnightmare.appstates.AppStateSinglePlayer;
import jaredbgreat.cubicnightmare.componenent.GeomorphManager;
import jaredbgreat.cubicnightmare.componenent.geomorph.GeomorphModel;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.componenent.pickups.BookStand;
import jaredbgreat.cubicnightmare.componenent.pickups.PickupEffectMap;
import jaredbgreat.cubicnightmare.entities.CubeMob;
import jaredbgreat.cubicnightmare.mapping.decorator.AreaZone;
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
    
    HubRoom[] hubRooms;
    AreaZone[] zones;
    Room endRoom;
    Vector3f playerStart;
    
    final List<CubeMob> mobs;    
    
    int b;
    int a;
    Room room;
    
    
    public Dungeon(AppStateSinglePlayer playing, GeomorphManager geoman, 
                    int level) {
        game = playing;
        random = new Random();
        this.geoman = geoman;
        size = Sizes.LARGE;
        playerStart = new Vector3f();
        mobs = new ArrayList<>();
        build(level + playing.getDifficulty().ordinal());
    }
    
    
    private void build(int level) { 
        Main.proflogger.startTask("Build");        
        boolean bad = true;
        
        while(bad) {
            map = new MapMatrix();
            areas = new Areas();
            plan(); 
            doorFixer(); 
            RoomBFS seeker = new RoomBFS(this);
            bad = !seeker.test();
            setupRoomThemes();
            connectHubsSparcely(seeker);
            map.populateDirs();
        }
        
        findPlayerStart();     
        
        map.buildMap(this);
        
        addMobs(level);  
        Main.proflogger.endTask("Build"); 
    }
    
    
    private void plan() {        
        makeHubRooms();        
        connectHubs();
        growthCycle();
    }
    
    
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
        hubRooms = new HubRoom[num];
        List<Sector> sectors = Sector.getShuffledList(random);
        int j = 0;
        for(int i = 0; (i < hubRooms.length) && ((i+j) < sectors.size()); i++) {            
            do {
                Sector sector = sectors.get(i + j);
                int x = random.nextInt(16) + (sector.x * 16);
                int z = random.nextInt(16) + (sector.z * 16);
                RoomSeed seed = new RoomSeed(x, 0, z);
                // Hubrooms are bigger than average
                hubRooms[i] = seed.growHubRoom(random.nextInt(5) + 4, 
                        random.nextInt(5) + 4, 1, this, null, room);
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
                } else {
                    j++;
                }
            } while (hubRooms[i] == null);
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
			other = disconnected.get(random
                                .nextInt(disconnected.size()));
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
        
        
        private void setupRoomThemes() {
            int nzones = 0;
            ArrayList<AreaZone> tzones = new ArrayList<>();
            for(int i = 0; i < hubRooms.length; i++) {
                if(hubRooms[i] != null) {
                    tzones.add(hubRooms[i].setAreaZone(this, 
                            Geomorphs.REGISTRY.getRandomID(random)));
                    hubRooms[i].setPillar(random);
                    nzones++;
                }
            }
            zones = new AreaZone[nzones];
            for(int i = 0; i < nzones; i++) {
                zones[i] = tzones.get(i);
            }
            int n = areas.getRoomList().size();
            for(int i = zones.length + 1; i < n; i++) {
                AreaZone.summateEffect(zones, areas.getRoom(i));
            }
            n = areas.getDoorways().size();
            for(int i = 0; i < n; i++) {
                AreaZone.summateEffect(zones, areas.getDoorway(i));
            }
        }
        
        
        public int getThemeIDforLoc(float x, float y, float z) {
            return AreaZone.summateEffect(zones, x, z).themeID;
        }
        
        
        public GeomorphModel getTPillarforLoc(float x, float y, float z) {
            return AreaZone.summateEffect(zones, x, z).getPillar();
        }
        
        
        public void findPlayerStart() {
            playerStart.set(hubRooms[0].centerx * 3, (float)hubRooms[0].y1 * 3, 
                        hubRooms[0].centerz * 3);
            float dist = 0;
            for(int i = 1; i < hubRooms.length; i++) {
               float d = playerStart.distanceSquared(new 
                         Vector3f(hubRooms[i].centerx * 3, hubRooms[i].y1 * 3, 
                                 hubRooms[i].centerz * 3));
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
            return new Vector3f(endRoom.centerx * 3, endRoom.y1 * 3, 
                    endRoom.centerz * 3);
        }
        
        
        public GeomorphManager getGeomorphManager() {
            return geoman;
        }
        
        
        public void addMobs(int level) {
            RoomList rl = areas.getRoomList();
            int n1 = rl.realSize();
            ArrayList<Room> l = new ArrayList<>(n1);
            for(int j = 0; j < n1;) {
                l.add(rl.get(++j));
            }
            l.remove(hubRooms[0]);
            Collections.shuffle(l);
            n1 = Math.min((n1 / 3) + level, l.size());
            for(int i = 0; i < n1; i++) {
                Room r = l.get(i);
                CubeMob cb = new CubeMob(game, this, game.getApplications().
                        getRootNode(), 
                game.getPhysics(), r.getCenterAsVec(), "DeathCube." + i);
                game.getPhysics().getPhysicsSpace().addCollisionListener(cb);
                mobs.add(cb);
            }
        }
        
        
        public void removeMobs() {
            PhysicsSpace physics = game.getPhysics().getPhysicsSpace();
            for(CubeMob mob : mobs) {
                mob.die(physics);
            }
            mobs.clear();
        }
        
        
        public void clear() {
            removeMobs();
            geoman.removeSpatials();
        }
        
        
        public void addPickups(PickupEffectMap effectMap, 
                    AppStateSinglePlayer game) {
            RoomList rl = areas.getRoomList();
            int n1 = rl.realSize();
            ArrayList<Room> l = new ArrayList<>(n1);
            for(int j = 0; j < n1;) {
                l.add(rl.get(++j));
            }
            l.remove(hubRooms[0]);
            l.remove(endRoom);
            Collections.shuffle(l);
            n1 = 5;
            //n1 = l.size();
            for(int i = 0; i < n1; i++) {
                Spatial s = addPickup(effectMap, l.get(i), "BookL" 
                        + game.getLevel() + "P" + i);
                GeomorphManager.manager.attachSpatial(s);
            }
        }
        
        
        public Spatial addPickup(PickupEffectMap effectMap, Room r, String name) {
            GeomorphModel model = Geomorphs.getDecor().getFromName("BookStand");
            Node node = model.makeSpatialAt(r.getCenterAsVec());
            effectMap.add(name, new BookStand(name, node));
            return node;
        }
    
    
}
