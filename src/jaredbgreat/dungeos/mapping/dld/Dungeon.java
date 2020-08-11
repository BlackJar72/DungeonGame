package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.componenet.GeomorphManager;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jared
 */
public class Dungeon {
    private final GeomorphManager geoman;
    final Sizes size;    
    int[][] room;
    int[][] geomodel;  
    int[] localModel;
    
    List<Room> rooms;
    HubRoom[] nodes;
    int numNodes;
    
    Random random;
    
    public Dungeon(GeomorphManager geoManager, Sizes size) {
        random = new Random();
        geoman = geoManager;
        this.size = size;
        room = new int[size.width][size.width];
        geomodel = new int[size.width][size.width];
        rooms = new ArrayList<>(size.maxRooms);
        rooms.add(Room.NULL);
        nodes = new HubRoom[size.maxNodes];
        numNodes = nodes.length;
        localModel = new int[2];
        localModel[0] = Geomorphs.MORPHS.getGeomorphID("SimpleStone");
        localModel[1] = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
        makeNodes();
        connectNodes();
    }
    
    
    public DLDStyleMap makeMap() {
        return new DLDStyleMap(room, geomodel);
    }
    
    
    
    
	
	
	/**
	 * Creates all the nodes and store along with a list of node rooms.
	 */
	private void makeNodes() {
                nodes[0] = new HubRoom(size.width / 2, 0, size.width / 2, random, this);
		int i = 1;
		while(i < numNodes) {
			nodes[i] = new HubRoom(random.nextInt(size.width), 0, 
					random.nextInt(size.width), random, this);
			if(nodes[i].hubRoom != null) ++i;
		}
	}
	
	
	/**
	 * This will connect all the nodes with series of intermediate rooms by 
	 * callaing either connectNodesDensely or connectNodesSparcely, with a 
	 * 50% chance of each.
	 * 
	 * @throws Throwable
	 */
	private void connectNodes() {
		if(random.nextBoolean()) {
			connectNodesDensely();
		} else {
			connectNodesSparcely();
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
	private void connectNodesSparcely() {		
		HubRoom first, other;
		ArrayList<HubRoom> connected = new ArrayList<>(nodes.length), 
				        disconnected = new ArrayList<>(nodes.length);		
		connected.add(nodes[0]);
		for(int i = 1; i < nodes.length; i++) {
			disconnected.add(nodes[i]);
		}		
		while(!disconnected.isEmpty()) {
			if(rooms.size() >= size.maxRooms) {
				return;
			}
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
	private void connectNodesDensely() {		
		HubRoom first, other;
		for(int i = 0; i < nodes.length; i++) {
			first = nodes[i];
			for(int j = i + 1; j < nodes.length; j++) {
				other = nodes[j];
				if(rooms.size() >= size.maxRooms) {
					//DoomlikeDungeons.profiler.endTask("Connecting Nodes");
					return;
				}				
				if(other != first) {
					new Route(first, other).drawConnections(this);
				}
			}			
		}
	}
    
}
