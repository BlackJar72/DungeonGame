/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.mapping.dld;

/**
 *
 * @author jared
 */
public class Room {
    public static final Room NULL_ROOM = new Room();
    
    final int ix, iz, width, length, height, maxx, maxz, minx, minz;
    final float centerx, centerz;
    int id, geomorph;
    
    
    /**
     * Create the null room; this should never be called elsewhere.
     */
    private Room() {
        id = ix = iz = width = length = height = maxx = maxz = minx = minz = 0;
        centerx = centerz = 0.0f;
    }
    
    
    /**
     * Sets the room id for this room.  Usually this will be gained from the RoomList.
     * 
     * @param id
     * @return id (for chaining)
     */
    public int setID(int id) {
        return this.id = id;
    }
    
    
    public void buildIn(MapMatrix map) {
        for(int i = minx; i <= maxx; i++) 
            for(int j = minz; j <= maxz; j++) {
                
            }
    }
    
    
}
