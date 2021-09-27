package jaredbgreat.gui;

import com.jme3.scene.Geometry;
import jaredbgreat.gui.IGuiCommand;

/**
 *
 * @author Jared Blackburn
 */
public final class RadioButton extends ToggleButton {
    public final IGuiCommand notifier = new IGuiCommand() {
        @Override
        public void execute() {
            notifyHolder();
        }
    };
    final ButtonList holder;
    private final int id;
    
    
    public RadioButton(int id, ButtonList holder, Geometry on, Geometry off) {
        this.id = id;
        this.holder = holder;
        super.attachGeometries(on, off);
    }
    
    
    void notifyHolder() {
        holder.beNotified(id);
    }
    
    
    public int getID() {
        return id;
    }
        
    
}
