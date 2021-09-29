package jaredbgreat.cubicnightmare.entities.mobstates;

/**
 * This is a container class for various standard states a mobile entity 
 * (typically a monster) can be in.  Membership in one the contained lists 
 * repressents being in the relevant states.  Each list can thus be traversed 
 * separately, only running relevant AI or other code only on mobs it applies
 * to, as opposed to testing for all possible conditions separately on each 
 * possible mob.
 * 
 *
 * @author Jared Blackburn
 */
public class MobStates {
    
    public final MobStateList moving;
    
    
    public MobStates() {
        moving = new MobStateList(new StateMoving());
    }
    
}
