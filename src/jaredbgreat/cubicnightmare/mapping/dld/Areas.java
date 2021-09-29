package jaredbgreat.cubicnightmare.mapping.dld;

/**
 *
 * @author jared
 */
public class Areas {
    
    private final RoomList[] data;
    private final RoomList rooms;
    private final RoomList doors;
    private final RoomList halls;
    
    
    public Areas() {
        data = new RoomList[3];
        data[0] = rooms = new RoomList(256);
        data[1] = doors = new RoomList(256);
        data[2] = halls = new RoomList(256);        
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
    
    
    public Room getTunnel(int i) {
        return halls.get(i);
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
    
    
    public RoomList getTunnels() {
        return halls;
    }
    
}
