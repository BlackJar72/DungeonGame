package jaredbgreat.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Jared Blackburn
 */
public class CommandFinder {
    private static final Vector3f DIR = Vector3f.UNIT_Z.negate();
    private static float FARTHEST = 65536f;
    InputManager input;
    Node baseNode;
    
    
    public CommandFinder(SimpleApplication app, Node node) {
        input = app.getInputManager();
        baseNode = node;
    }
    
    
    public CommandFinder(SimpleApplication app) {
        input = app.getInputManager();
        baseNode = app.getGuiNode();
    }
    
    
    public void processClick() {
        CollisionResults results = new CollisionResults();
        Vector2f cursorPos = input.getCursorPosition();        
        Vector3f start = new Vector3f(cursorPos.x, cursorPos.y, FARTHEST);
        Ray ray = new Ray(start, DIR);
        baseNode.collideWith(ray, results);
        if(results.size() > 0) {
            Spatial hit = results.getClosestCollision().getGeometry().getParent();
            if(hit instanceof IGuiCommand) {
                ((IGuiCommand) hit).execute();
            }           
        }
    }
}
