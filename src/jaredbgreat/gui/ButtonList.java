package jaredbgreat.gui;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
        // Do not allow this to remain in an invalid state if at least one 
        // button exists.
        if(buttons.size() == 1) {
            beNotified(0);
        }
    }
    
    
    public int getSelected() {
        return selected;
    }
    
    
    public void setSelected(int value) {
        if((value > -1) && (value < buttons.size())) {
            selected = value;
            for(int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setState(i == selected);
            }
        }
    }
    
    
    public RadioButton getNode(int i) {
        return buttons.get(i);
    }
    
    
    
}
