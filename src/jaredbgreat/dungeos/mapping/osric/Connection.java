package jaredbgreat.dungeos.mapping.osric;

/**
 *
 * @author Jared Blackurn
 */
public class Connection {
    private final Room startRoom;
    private Room endRoom;
    private final Doorway startDoor;
    private Doorway endDoor;
    
    
    public Connection(Room startRoom, Doorway startDoor) {
        this.startRoom = startRoom;
        endRoom = null;
        this.startDoor = startDoor;
        endDoor = null;
    }
    
    
    
    
}
