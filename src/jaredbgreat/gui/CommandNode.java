package jaredbgreat.gui;

import com.jme3.scene.Node;

/**
 * I special node capable of executing a command (IGuiCommad) when 
 * notified of an interation by one of its children.  The command 
 * must be supplied at in the constructor and cannot be changed 
 * afterward.
 * 
 * @author Jared Blackburn
 */
public final class CommandNode extends Node implements IGuiCommand {
    private final IGuiCommand command;
    
    public CommandNode(IGuiCommand command) {
        super();
        this.command = command;
    }
    
    
    public CommandNode(String name, IGuiCommand command) {
        super(name);
        this.command = command;
    }
    
    
    @Override
    public void execute() {
        command.execute();
    }
}
