package jaredbgreat.cubicnightmare.mapping.dld;

/**
 *
 * @author jared
 */
public enum AreaType {
    
    ROOM (0),
    DOORWAY (1),
    TUNNEL (2);
    // TODO? Other area types?
    
    public final int tid;
    
    
    AreaType(int tid) {
        this.tid = tid; // This could use getOrdinal(), but would be less flexible
    }
    
    
    public static AreaType fromID(int tid) {
        // This could probably be optimized
        switch(tid) {
            case 0: return ROOM;
            case 1: return DOORWAY;
            case 2: return TUNNEL;
            default: return ROOM;
        }
    }
    
    
    public Room getRoom(Areas areas, int index) {
        return areas.getArea(tid, index);
    }
    
    
    public RoomList getList(Areas areas) {
        return areas.getList(tid);
    }
}
