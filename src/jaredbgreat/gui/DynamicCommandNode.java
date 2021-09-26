package jaredbgreat.gui;

import com.jme3.scene.Node;

/**
 * I special node capable of executing a command (IGuiCommad) when 
 * notified of an interation by one of its children.  While a command 
 * must be supplied to the constructor, it is possible to reset the 
 * command at a later time, allowing for the sematics to change 
 * dynamically at run time.
 * 
 * @author Jared Blackburn
 */
public class DynamicCommandNode extends Node implements IGuiCommand {
    private IGuiCommand command;
    
    public DynamicCommandNode(IGuiCommand command) {
        super();
        this.command = command;
    }
    
    
    public DynamicCommandNode(String name, IGuiCommand command) {
        super(name);
        this.command = command;
    }
    
    
    @Override
    public void execute() {
        command.execute();
    }
    
    
    public void setCommand(IGuiCommand command) {
        this.command = command;
    }
}
