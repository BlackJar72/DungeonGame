/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.mapping.osric;

/**
 *
 * @author jared
 */
public class Doorway {
    /** Coordinates must be relative to the room, not the dungeon/world! */
    final int x, z, y;
    final ECardinal heading;
    
    
    public Doorway(int x, int y, int z, ECardinal direction)  {
        this.x = x;
        this.z = z;
        this.y = y;
        this.heading = direction;
    }
    
    
    public Doorway(int x, int z, ECardinal direction)  {
        this(x, 0, z, direction);
    }
    
    
    public int add(int in) {
        return heading.removeWall(in);
    }
    
}
