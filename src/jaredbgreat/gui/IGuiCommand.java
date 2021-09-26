package jaredbgreat.gui;

/**
 * I simple encapsulation of behavior, intended to be used 
 * with command nodes.  It is also implemented by command 
 * nodes themselves to tag them as objects through which 
 * such a command can be executed.
 * 
 * @author Jared Blackburn
 */
public interface IGuiCommand {
    public void execute();
}
