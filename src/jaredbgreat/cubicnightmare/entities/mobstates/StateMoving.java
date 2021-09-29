package jaredbgreat.cubicnightmare.entities.mobstates;

import jaredbgreat.cubicnightmare.entities.IMob;

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
