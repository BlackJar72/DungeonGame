package jaredbgreat.cubicnightmare.entities.controls;

import com.jme3.scene.control.AbstractControl;
import jaredbgreat.cubicnightmare.appstates.AppStateSinglePlayer;

/**
 *
 * @author jared
 */
public abstract class AbstractEntityControl extends AbstractControl {
    AppStateSinglePlayer game;
    
    public AbstractEntityControl(AppStateSinglePlayer appState) {
        game = appState;
    }
    
    
}