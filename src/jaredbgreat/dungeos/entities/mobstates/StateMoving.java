package jaredbgreat.dungeos.entities.mobstates;

import jaredbgreat.dungeos.entities.IMob;

/**
 *
 * 
 * @author Jared Blackburn
 */
public class StateMoving implements IMobState {

    @Override
    public boolean process(IMob mob) {
        return mob.move();
    }
    
}
