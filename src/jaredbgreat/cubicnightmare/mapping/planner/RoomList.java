package jaredbgreat.cubicnightmare.mapping.planner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * @author jared
 */
public class RoomList extends ArrayList<Room> {

    private int numRooms;


    public RoomList(int slots) {
        super(slots);
        add(0, Room.NULL_ROOM);
        numRooms = 0;
    }


    /**
     * The number real rooms actually in the list; the null
     * room is not counted.
     * 
     * @return the number of rooms in the list
     */
    public int realSize() {
            return numRooms;
    }


    /**
     * Returns true if only the null room is present.
     * 
     * @return true if no "real" rooms are in the list
     */
    public boolean isReallyEmpty() {
            return (numRooms == 0);
    }

    @Override
    public ListIterator listIterator() {
        return super.listIterator(1);
    }
	
	
    @Override
    public Iterator iterator() {
        return super.listIterator(1);
    }
	
	
    @Override
    public boolean add(Room room) {
            boolean out = super.add(room);
            if(out) {
                    numRooms++;
            }
            return out;
    }



    @Override
    public Room remove(int index) {
            System.err.println("WARNING! Trying to remove room from list "
                         + "(Rooms cannot be removed)!");
            return null; // Rooms cannot be removed
    }
    
    
	
}
