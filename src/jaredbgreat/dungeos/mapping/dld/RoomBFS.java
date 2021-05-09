package jaredbgreat.dungeos.mapping.dld;

import com.jme3.math.Vector3f;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author jared
 */
public class RoomBFS {
    List<Room> hubs, found;
    Deque<Room> q;
    List<Room>[] reachable;
    boolean[] checked;
    DLDungeon dungeon;
    
    int c;
    
    
    public RoomBFS(DLDungeon dungeon) {
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
    
    
    public boolean map() {
        q.add(hubs.get(0));
        hubs.remove(0);
        search(reachable[0]);
        /*while(!hubs.isEmpty()) {
            q.add(hubs.get(0));
            hubs.remove(0);
            search(reachable[1]);
            link(reachable[0], reachable[1]);
        }*/
        return hubs.isEmpty();
    }
    
    
    private void search(List<Room> reached) {
        Room next;
        while(!hubs.isEmpty() && !q.isEmpty()) {
            next = q.poll();
            checked[next.id] = true;
            reached.add(next);
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
    

    private void link(List<Room> rooms1, List<Room> rooms2) {
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
    }
    
}
