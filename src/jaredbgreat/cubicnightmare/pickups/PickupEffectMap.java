package jaredbgreat.cubicnightmare.pickups;

import jaredbgreat.cubicnightmare.entities.Player;
import java.util.HashMap;
import java.util.Map;

/**
 * This will map effect from pickups by their name string to the effect they should have.
 * 
 * The idea is to register the Player as a collision event listener, and when detecting a collision 
 * route the effect through this, using the string that is not "player" (not equal to Player.NAME).
 * 
 * Objects that should not have an effect can return null and be throw away on that basis.
 * 
 * Effect names should be held here as static final String constants to be used both in setting 
 * up the map and in defining the spatials representing the pickups.
 * 
 * These will need to be done per item (not type), added during dungeon generation, and removed 
 * afterwards, so that the IPickupEffect implementations can reference the exact item (which 
 * will usually need to be removed after taking effect, so it cannot be used more than once.
 * 
 * @author Jared Blackburn
 */
public class PickupEffectMap {
    private final Map<String, IPickupEffect> MAP 
            = new HashMap<>();
    
    
    public void applyEffects(String name, Player player) {
        IPickupEffect effect = MAP.get(name);
        if(effect != null) {
            effect.effect(player);
        }
    }
    
    
    public void setup() {
        //TODO: Setup the effect list
    }
}
