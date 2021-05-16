package jaredbgreat.dungeos.mapping.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class AreaTheme {
    private final List<RoomTheme> roomTypes, workspace;
    private final List<Integer> geomorphs;
    
    
    public AreaTheme() {
        roomTypes = new ArrayList<>();
        geomorphs = new ArrayList<>();
        workspace = new ArrayList<>();
    }
    
    
    public RoomTheme getRoomTheme(int width, int length, Random random) {
        workspace.clear();
        for(RoomTheme theme : roomTypes) {
            if(theme.canUse(width, length, random)) {
                workspace.add(theme);
            }
        }
        if(!workspace.isEmpty()) {
            return workspace.get(random.nextInt(workspace.size()));
        } else { // Failsafe,if no viable theme was found
            return roomTypes.get(random.nextInt(roomTypes.size()));
        }
    }
    
    
    public List<RoomTheme> getTypes() {
        return roomTypes;
    }
    
    
    public List<Integer> getGeos() {
        return geomorphs;
    }
    
}
