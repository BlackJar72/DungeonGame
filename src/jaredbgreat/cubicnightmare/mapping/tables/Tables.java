package jaredbgreat.cubicnightmare.mapping.tables;


import jaredbgreat.cubicnightmare.mapping.planner.Room;
import java.util.Random;

/**
 * A collection static functions serving as tables for 
 * the generation process.  Basically, most of the 
 * top-level random rolls for content types are here 
 * (though plenty of randomness is to be used down 
 * the line in fleshing out the meaning of the 
 * results).  
 * 
 * @author Jared Blackburn
 */
public final class Tables {
    
    
    /**
     * The produces dimensions to attempt for a new room, 
     * with a strong favoring for certain common room
     * sizes and shapes.  Specifically, 25% of rooms will 
     * have fully randomized dimensions, while 75% will 
     * use certain commonly used sizes with a preference 
     * for squares and for smaller rooms.
     * 
     * This is derived from the dice based random dungeons 
     * tables found in the OSRIC tabletop RPG, which are 
     * in turn ultimately based on ideas from the 1st 
     * edition AD&D Dungeon Masters Gude by TSR inc.  
     * Chnages made here are that there area no "special" 
     * (weirdly shaped) rooms or rooms that would be no 
     * wider than a hallway (and thus indistiguishable). 
     * Instead, such content was replaced with true rng 
     * rolls for some more varied length/width combinations.
     * 
     * 
     * @param random
     * @return 
     */
    public static int[] getRoomSize(Random random) {
        final int[] out = new int[2];
        // Yeah, I'm using a switch, at least for now;
        // I may optimize it with a true (array-based) 
        // table later (FIXME).
        switch(random.nextInt(20)) {
            case 0:
            case 1:
            case 2:
                out[0] = 2; out[1] = 2;
                break;
            case 3:
            case 4:
                out[0] = 3; out[1] = 3;
                break;
            case 5:
            case 6:
                out[0] = 4; out[1] = 4;
                break;
            case 7:
                out[0] = 2; out[1] = 3;
                break;
            case 8:
                out[0] = 3; out[1] = 2;
                break;
            case 9:
                out[0] = 2; out[1] = 4;
                break;
            case 10:
                out[0] = 4; out[1] = 2;
                break;
            case 11:
                out[0] = 4; out[1] = 5;
                break;
            case 12:
                out[0] = 5; out[1] = 4;
                break;
            case 13:
                out[0] = 4; out[1] = 6;
                break;
            case 14:
                out[0] = 6; out[1] = 4;
                break;
            case 15:
            case 16:
            case 17:
                out[0] = random.nextInt(5) + 2;
                out[1] = random.nextInt(5) + 2; 
                break;
            default: // Case 18-19
                out[0] = random.nextInt(7) + 2;
                out[1] = random.nextInt(7) + 2; 
                break;                
        }        
        return out;
    }
    
    
    /**
     * Gets a number of exits for a room -- may be increased to 
     * minimum of one if the room is an intermediary of a route.
     * 
     * This is likely to change; it is based on systems used in 
     * quickly creating dungeons on the fly for TTRPGs.  I this 
     * will be mixed with other things.
     * 
     * This is derived from the dice based random dungeons 
     * tables found in the OSRIC tabletop RPG, which are 
     * in turn ultimately based on ideas from the 1st 
     * edition AD&D Dungeon Masters Gude by TSR inc.  
     * Chnages made here are that there area no "special" 
     * (weirdly shaped) rooms or rooms that would be no 
     * wider than a hallway (and thus indistiguishable). 
     * Instead, such content was replaced with true rng 
     * rolls for some more varied length/width combinations.
     * 
     * @param dungeon
     * @param room
     * @return 
     */
    public static int getNumberOfDoors(Random random, Room room) {
        int out = 1;        
        switch(random.nextInt(20)) {
            case 0:
            case 1:
            case 2:
            case 3:
                if(room.getArea() > 6) {
                    out = 2;
                } else {
                    out = 1;
                }
                break;
            case 4:
            case 5:
            case 6:
                if(room.getArea() > 6) {
                    out = 3;
                } else {
                    out = 2;
                }
                break;
            case 7:
            case 8:
                if(room.getArea() > 6) {
                    out = 4;
                } else {
                    out = 3;
                }
                break;
            case 9:
            case 10:
            case 11:
                if(room.getArea() > 10) {
                    out = 1;
                } else {
                    out = 0;
                }
                break;
            case 12:
            case 13:
            case 14:
                if(room.getArea() > 15) {
                    out = 1;
                } else {
                    out = 0;
                }
                break;
            case 15:
            case 16:
            case 17:
            case 18:
                out = random.nextInt(4) + 1;
                break;
            default:
                out = 1;
        }        
        return Math.max(Math.min(out, ((room.getPerimeter() + 1) / 3) - 1), 0);
    }
    
}
