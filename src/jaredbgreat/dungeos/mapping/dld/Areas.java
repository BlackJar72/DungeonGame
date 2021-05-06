package jaredbgreat.dungeos.mapping.dld;

/**
 *
 * @author jared
 */
public class Areas {
    
    private final RoomList[] data;
    private final RoomList rooms;
    private final RoomList doors;
    
    
    public Areas() {
        data = new RoomList[2];
        data[0] = rooms = new RoomList(256);
        data[1] = doors = new RoomList(256);        
    }
    
    
    public Room getArea(int list, int place) {
        return data[list].get(place);
    }
    
    
    public RoomList getList(int list) {
        return data[list];
    }
    
    
    public Room getRoom(int i) {
        return rooms.get(i);
    }
    
    
    public Room getDoorway(int i) {
        return doors.get(i);
    }
    
    
    public RoomList getRoomList() {
        return rooms;
    }
    
    
    public RoomList getDoorways() {
        return doors;
    }
    
}
