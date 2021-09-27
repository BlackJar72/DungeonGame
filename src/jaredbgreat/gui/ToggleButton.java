package jaredbgreat.gui;


import com.jme3.scene.Geometry;


/**
 *
 * @author Jared Blackburn
 */
public class ToggleButton extends DynamicCommandNode {
    public static final IGuiCommand EMPTY_COMMAND = new IGuiCommand() {
        @Override
        public void execute() {/*Do Nothing*/}
    };
    
    
    private boolean state;
    private Geometry on, off;
    

    public ToggleButton() {
        super(EMPTY_COMMAND);
        state = false;
    }
    

    public ToggleButton(String name) {
        super(name, EMPTY_COMMAND);
        state = false;
    }
    

    public ToggleButton(IGuiCommand command) {
        super(command);
        state = false;
    }
    
    
    public void attachGeometries(Geometry on, Geometry off) {
        this.on  = on;
        this.off = off;
        setGeometry();
    }    
    

    @Override
    public void execute() {
        flip();
        super.execute();
    }
    
    
    public void flip() {
        state = !state;
        setGeometry();
    }
    
    
    public void set() {
        state = true;
        setGeometry();
    }
    
    
    public void unset() {
        state = false;
        setGeometry();
    }
    
    
    public void setState(boolean state) {
        this.state = state;
        setGeometry();
    }
    
    
    private void setGeometry() {
        if(state) {
            detachAllChildren();
            attachChild(on);
        } else {
            detachAllChildren();
            attachChild(off);
        }
    }
    
    
    public boolean getState() {
        return state;
    }
    
    
    public boolean getOn() {
        return state;
    }
    
    
    public boolean getOff() {
        return !state;
    }
    
}
