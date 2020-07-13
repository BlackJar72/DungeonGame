package jaredbgreat.dungeos.mapping.dld;


/**
 *
 * @author Jared Blackburn
 */
public class Room {
    public static final Room roomNull = new Room();
    
    public int id;
    
    
    /**
     * A constructor specifically for the null room 
     * (areas not in a room).
     */
    private Room() {
        id = 0;
    }
    
}
