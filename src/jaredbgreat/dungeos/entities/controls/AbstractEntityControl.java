package jaredbgreat.dungeos.entities.controls;

import com.jme3.scene.control.AbstractControl;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;

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