package jaredbgreat.dungeos.mapping.dld;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import static jaredbgreat.dungeos.mapping.dld.Direction.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a potential route between two rooms; its purpose 
 * is to find an actual concrete route and add it to the dungeon.  To do 
 * that it will produce a sequence of rooms from each end (start and 
 * finish), alternately adding a room to each in the general direction 
 * of the other until the squences connect at some point.
 * 
 * @author Jared Blackburn
 *
 */
public class Route {

	private final HubRoom start, finish;
	private Room current1, current2, temp;
	private float realXDist, realZDist;
	private final int bXDist;
	private final int bZDist;
        Direction dir1, dir2;
        private final boolean xMatch;
        private final boolean zMatch;
	private boolean finishTurn, complete, comp1, comp2;
	private final ArrayList<Room> side1;
	private final ArrayList<Room> side2;
	
	
	protected Route(HubRoom start, HubRoom finish) {
		this.start  = start;
		this.finish = finish;
		current1 = start.hubRoom;
		current2 = finish.hubRoom;
		side1 = new ArrayList<Room>();
		side1.add(current1);
		side2 = new ArrayList<Room>();
		side2.add(current2);
		finishTurn = false;
		complete = false;
		
		if(realXDist > 3.0f) {
			bXDist = current2.endx - current1.startx;
			xMatch = false;
		} else if(realXDist < -3.0f) {
			bXDist = current2.startx - current1.endx;
			xMatch = false;
		} else {
			bXDist = (int)realXDist;
			xMatch = true;
		}
		
		if(realZDist > 3.0f) {
			bZDist = current2.endz - current1.startz;
			zMatch = false;
		} else if(realZDist < -3.0f) {
			bZDist = current2.startz - current1.endz;
			zMatch = false;
		} else {
			bZDist = (int)realZDist;
			zMatch = true;
		}		
	}
	
	
	/**
	 * This will attempt to draw the connecting serious of rooms by 
	 * calling drawConnection until either the connection is flagged as 
	 * complete or the dungeon has reach the maximum number of rooms. 
	 * 
	 * Note that the connection may be flagged complete either because 
	 * an actual connection has been found or because both sequences 
	 * (that from the start and that from the
	 * 
	 * @param dungeon
	 */
	protected void drawConnections(Dungeon dungeon) {
		int limit = dungeon.size.maxRooms;
		while(!complete && (limit > 0)) {
			limit--;
			if(dungeon.rooms.size() >= dungeon.size.maxRooms) return;
			drawConnection(dungeon);			
			if(complete || (limit < 0)) return;
		}
	}
	
	
	/**
	 * This will try to add a room to one of the sequences.  Which sequence gets 
	 * this turn is determined by the internal finishTurn variable, which alternates 
	 * with each call.  The location of the new rooms will always be closer on either 
	 * the x or z axis, though which is determined randomly, though weighted to favor 
	 * the axis on which the distance is greatest.
	 * 
	 * The connection is considered complete when either the sequence of rooms from 
	 * either end meet or when each fails to add a room on consecutive turns (a 
	 * give-up condition).
	 * 
	 * @param dungeon
	 */
	public void drawConnection(Dungeon dungeon) {
		//System.out.println("Running drawConnections(Dungeon dungeon)");
		getGrowthDir(dungeon.random);
		int x = dungeon.random.nextInt(dungeon.size.width);
		int z = dungeon.random.nextInt(dungeon.size.width);
		int xdim = dungeon.random.nextInt(dungeon.size.maxRoomSize - 1) + 2;
		int zdim = dungeon.random.nextInt(dungeon.size.maxRoomSize - 1) + 2;
		if(close(dungeon.size.maxRoomSize - 1)) xdim = zdim = dungeon.size.maxRoomSize;
		if(finishTurn) {
			//dir1 = dir1.opposite();
			//dir2 = dir2.opposite();
			temp = current2.connector(dungeon, dir1, xdim, zdim, 0, this);
			if((temp == null) || side2.contains(temp)) 
                            temp = current2.connector(dungeon, dir2, xdim, zdim, 0, this);
			if((temp == null) || side2.contains(temp)) {
				comp2 = true; // for now, give up
			} else if(side1.contains(temp)) {
				complete = true; // Success!
			} else {
				side2.add(temp);
				current2 = temp;
			}
		} else {
			temp = current1.connector(dungeon, dir1, xdim, zdim, 0, this);
			if(temp == null|| side1.contains(temp)) 
                            temp = current1.connector(dungeon, dir2, xdim, zdim, 0, this);
			if(temp == null|| side1.contains(temp)) {
				comp1 = true; // for now, give up
			} else if(side2.contains(temp)) {
				complete = true; // Success!
			} else {
				side1.add(temp);
				current1 = temp;
			}
		}
		if(!complete) complete = comp1 && comp2;
		if(comp1) finishTurn = true;
		else if(comp2) finishTurn = false;
		else finishTurn = !finishTurn;
	}
	
	
	
	
	
	// Helper methods
	
	/**
	 * @return ture if the terminal rooms in both sequences overlap on the x axis.
	 */
	private boolean xOverlap() {
		return ((current1.endx > current2.startx) && (current2.endx > current1.startx));	
	}
	
	
	/**
	 * @return true if the terminal rooms in both sequences overlap on the z axis. 
	 */
	private boolean zOverlap() {
		return ((current1.endz > current2.startz) && (current2.endz > current1.startz));	
		}
	
	
	/**
	 * @return true if the room edges align on x and the rooms overlap on z
	 */
	private boolean touchesOnX() {
		if(!zOverlap()) return false;
		return ((current1.startx == current2.endx) || (current1.endx == current2.startx));
	}
	
	
	/**
	 * @return true if the room edges align on z and overlap on x.
	 */
	private boolean touchesOnZ() {
		if(!xOverlap()) return false;
		return ((current1.startz == current2.endz) || (current1.endz == current2.startz));
	}
	
	
	/**
	 * @return true if the the terminal rooms in each sequence are directly adjacent
	 */
	private boolean touching() {
		return (touchesOnX() || touchesOnZ());
	}
	
	
	/**
	 * This will determine which wall is touching between the terminal rooms on each 
	 * sequence.  Specifically this finds which direction to move from the terminal room in
	 * the sequence to that in the finish sequence.
	 * 
	 * @return the direction from the start sequence to the finish sequence.
	 */
	private int touchDir() {
		if(zOverlap()) {
			if(current1.endx == current2.startx) return 0;
			if(current1.startx == current2.endx) return 2;
		} else if(xOverlap()) {
			if(current1.endz == current2.startz) return 1;
			if(current1.startz == current2.endz) return 3;
		}
		return -1; // Not touching
	}
	
	
	/**
	 * This determines if the terminal rooms in the each sequence are close, that is
	 * if they are close enough to fill the gap with a single room.  This is used to 
	 * prevent potential connections are not ruined by placing an room whose target 
	 * size is too short to connect but leave too small a gap for another room.
	 * 
	 * @param range
	 * @return
	 */
	private boolean close(int range) {
		if(Math.abs(current1.startx - current2.endx) > range) return false; 
		if(Math.abs(current2.startx - current1.endx) > range) return false;
		if(Math.abs(current1.startz - current2.endz) > range) return false;
		return Math.abs(current2.startz - current1.endz) <= range;
	}
	
	
	/**
	 * This uses randomness and the coordinate of the terminal rooms in each 
	 * sequence to determine in which direction a new rooms should be added.
	 * 
	 * The direction is picked in such a way that sequences attempt to "grow" 
	 * toward each other while also maintaining a degree of randomness.
	 * 
	 * @param random
	 */
	private void getGrowthDir(Random random) {
		boolean posX = (current1.x < current2.x);
		boolean posZ = (current1.z < current2.z);
		if(xOverlap()) {
			if(posZ) {
				dir1 = N;
				dir2 = Direction.randomX(random);
			} else {
				dir1 = S;
				dir2 = Direction.randomX(random);
			}
		} else if (zOverlap()) {
			if(posX) {
				dir1 = W;
				dir2 = Direction.randomZ(random);
			}
			else {
				dir1 = E;
				dir2 = Direction.randomZ(random);
			}
		} else if (random.nextInt((int)(Math.abs(realXDist) + Math.abs(realZDist) + 1)) 
				> (int)(Math.abs(realXDist))) {
			if(posX) {
				dir1 = W;
				if(posZ) dir2 = N;
				else dir2 = S;
			}
			else {
				dir1 = E;
				if(posZ) dir2 = N;
				else dir2 = S;
			}
		} else {
			if(posZ) {
				dir1 = N;
				if(posX) dir2 = W;
				else dir2 = E;
			}
			else {
				dir1 = S;
				if(posX) dir2 = W;
				else dir2 = E;			
			}
		}
	}
	

    
    
}
