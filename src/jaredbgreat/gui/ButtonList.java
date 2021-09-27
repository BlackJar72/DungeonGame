package jaredbgreat.gui;

import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jared Blackburn
 */
public class ButtonList {
    private final List<RadioButton> buttons;
    private int selected;
    
    
    public ButtonList() {
        buttons = new ArrayList<>();
        selected = -1; // dummy state
    }
    
    
    
    void beNotified(int id) {
        selected = id;
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setState(i == selected);
        }
    }
    
    
    public void addButton(Geometry on, Geometry off) {        
        buttons.add(new RadioButton(buttons.size(), this, on, off));
    }
    
    
    public int getSelected() {
        return selected;
    }
}
