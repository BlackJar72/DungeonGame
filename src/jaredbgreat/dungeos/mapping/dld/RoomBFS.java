package jaredbgreat.dungeos.mapping.dld;

import com.jme3.math.Vector3f;
import jaredbgreat.dungeos.Main;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
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
    public boolean map() {
       // Main.proflogger.startTask("RoomBFS.map()");
        q.add(hubs.get(0));
        hubs.remove(0);
        search(reachable[0]);
        // TODO:  Have the link method actually link areas, then bring this back.
        /*while(!hubs.isEmpty()) {
            q.add(hubs.get(0));
            hubs.remove(0);
            search(reachable[1]);
            link(reachable[0], reachable[1]);
        }*/ 
        //Main.proflogger.endTask("RoomBFS.map()");
        return hubs.isEmpty();
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
    

    @Deprecated
    /**
     * Findings the closest of room from two lists by a comparing all room combinations. This 
     * is slow and best not used.
     */
    private void link(List<Room> rooms1, List<Room> rooms2) {
        //Main.proflogger.startTask("RoomBFS.link()");
        System.out.println(rooms1.size() + " * " + rooms2.size() + " = " + (rooms1.size() * rooms2.size()));
        if(rooms1.isEmpty() || rooms2.isEmpty()) {
            System.out.println("Flaming FUCK!!! " + rooms1.isEmpty() + "   " + rooms2.isEmpty());
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
                    System.out.println(d);
                }
            }
        }
        dungeon.geoman.line(r1.getCenterAsVec(4f), r2.getCenterAsVec(4f));
        rooms1.addAll(rooms2);
        rooms2.clear();
        //Main.proflogger.endTask("RoomBFS.link()");
    }
    
}
