package jaredbgreat.cubicnightmare.mapping.dld;

import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.mapping.tables.ECardinal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * 
 * 
 * @author jared
 */
// FIXME!!! This needs to be able to actually fix levels, not just 
//          generate them over-and over until finding a good one!!!
public class RoomBFS {
    List<Room> hubs, found;
    Deque<Room> q;
    List<Room>[] reachable;
    boolean[] checked;
    Dungeon dungeon;
    
    int c;
    
    
    public RoomBFS(Dungeon dungeon) {
        this.dungeon = dungeon;
        hubs = new ArrayList<>(dungeon.hubRooms.length);
        hubs.addAll(Arrays.asList(dungeon.hubRooms));
        found = new ArrayList<>(dungeon.hubRooms.length);
        q = new ArrayDeque<>();
        reachable = new ArrayList[2];
        reachable[0] = new ArrayList<>();
        reachable[1] = new ArrayList<>();
        checked = new boolean[dungeon.areas.getRoomList().size() + 1];
        c = Geomorphs.MORPHS.getGeomorphID("BrickNStone");
    }
    
    
    // FIXME!!! This needs to be able to actually fix levels, not just 
    //          generate them over-and over until finding a good one!!!
    public boolean test() {
       // Main.proflogger.startTask("RoomBFS.map()");
        q.add(hubs.get(0));
        hubs.remove(0);
        search(reachable[0]);
        //Main.proflogger.endTask("RoomBFS.map()");
        return hubs.isEmpty();
    }
    
    
    // FIXME!!! This needs to be able to actually fix levels, not just 
    //          generate them over-and over until finding a good one!!!
    public void map() {
       // Main.proflogger.startTask("RoomBFS.map()");
        q.add(hubs.get(0));
        hubs.remove(0);
        search(reachable[0]);
        while(!hubs.isEmpty()) {
            q.add(hubs.get(0));
            hubs.remove(0);
            search(reachable[1]);
            link(reachable[0], reachable[1]);
        } 
        //Main.proflogger.endTask("RoomBFS.map()");
    }
    
    
    private void search(List<Room> reached) {
        //Main.proflogger.startTask("RoomBFS.search()");
        Room next;
        while(!hubs.isEmpty() && !q.isEmpty()) {
            next = q.poll();
            if(!checked[next.id]) {
                reached.add(next);
                checked[next.id] = true;
                next.setGeomorph(c);
                if(hubs.contains(next)) {
                    found.add(next);
                    hubs.remove(next);
                }
                for(Doorway door : next.exits) {
                    Room r = door.getNeigbor(next);
                    if((r != null) && (!checked[r.id])) {
                        q.add(r);
                    } 
                }
            }
        }
        //Main.proflogger.endTask("RoomBFS.search()");
    }
    

    /**
     * Findings the closest of room from two lists by a comparing all room combinations. This 
     * is slow and best not used.
     */
    private void link(List<Room> rooms1, List<Room> rooms2) {
        //Main.proflogger.startTask("RoomBFS.link()");
        if(rooms1.isEmpty() || rooms2.isEmpty()) {
            return;
        }
        Room r1 = null, r2 = null;
        float dist = Float.MAX_VALUE;
        for(int i = 0; i < rooms1.size(); i++) {
            for(int j = 0; j < rooms2.size(); j++) {
                float d = rooms1.get(i).sqdist(rooms2.get(j));
                if(d < dist) {
                    dist = d;
                    r1 = rooms1.get(i);
                    r2 = rooms2.get(j);
                }
            }
        }
        dungeon.geoman.line(r1.getCenterAsVec(4f), r2.getCenterAsVec(4f));
        connect(r1, r2);
        rooms1.addAll(rooms2);
        rooms2.clear();
        //Main.proflogger.endTask("RoomBFS.link()");
    }
    
    
    public void connect(Room r1, Room r2) {
        if((r1 == null) || r2 == null) {
            return;
        }
        int x1 = r1.ix, x2 = r2.ix, z1 = r1.iz, z2 = r2.iz;
        int xstep = 0, zstep = 0;
        if(x1 != x2) xstep = (x2 - x1) / Math.abs(x2 - x1);
        if(z1 != z2) zstep = (z2 - z1) / Math.abs(z2 - z1);
        //Room last = r1;
        ECardinal heading;
        Tunnel tunnel = new Tunnel((x1 + x2) / 2, (z1 + z2) / 2, r1.y1);
        dungeon.areas.getTunnels().add(tunnel);
        tunnel.setID(dungeon.areas.getTunnels().realSize());
        if(dungeon.random.nextBoolean()) {
            if(xstep > 0) heading = ECardinal.E;
            else heading = ECardinal.W;
            for(int i = x1; i != x2; i += xstep) {
                int cr = dungeon.map.room[i][z1];
                if(cr > 0) {
                    //last = dungeon.areas.getArea(dungeon.map.type[i][z1], cr);
                } else {
                    //last = tunnel;
                    dungeon.map.addTunnelStep(i, z1, tunnel, heading);
                }
            }
            if(xstep > 0) heading = ECardinal.N;
            else heading = ECardinal.S;
            for(int j = z1; j != z2; j += zstep) { 
                int cr = dungeon.map.room[x2][j];
                if(cr > 0) {
                    //last = dungeon.areas.getArea(dungeon.map.type[x2][j], cr);
                } else {
                    //last = tunnel;
                    dungeon.map.addTunnelStep(x2, j, tunnel, heading);
                }                    
            }
        } else {
            if(xstep > 0) heading = ECardinal.N;
            else heading = ECardinal.S;
            for(int j = z1; j != z2; j += zstep) {   
                int cr = dungeon.map.room[x1][j];
                if(cr > 0) {
                    //last = dungeon.areas.getArea(dungeon.map.type[x1][j], cr);
                } else {
                    //last = tunnel;
                    dungeon.map.addTunnelStep(x1, j, tunnel, heading);                    
                }
            }            
            if(xstep > 0) heading = ECardinal.E;
            else heading = ECardinal.W;
            for(int i = x1; i != x2; i += xstep) {
                int cr = dungeon.map.room[i][z2];
                if(cr > 0) {
                    //last = dungeon.areas.getArea(dungeon.map.type[i][z2], cr);
                } else {                    
                    //last = tunnel;
                    dungeon.map.addTunnelStep(i, z2, tunnel, heading);
                }
            }                  
        }
        
    }
    
}
