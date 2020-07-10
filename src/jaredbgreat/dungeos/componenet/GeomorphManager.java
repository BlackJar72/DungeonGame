package jaredbgreat.dungeos.componenet;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.mapping.AbstractMap;

/**
 *
 * @author jared
 */
public class GeomorphManager {    
    public static GeomorphManager manager;
    final AssetManager assetman;
    final BulletAppState physics;
    final Node phynode, rootnode;
    
    //TODO: A system for tracking spatials for removal
    
    
    public GeomorphManager(AssetManager assetManager, Node rootNode, 
            BulletAppState bullet, Node physicsNode) {
        assetman = assetManager;
        physics = bullet;
        rootnode = rootNode;
        phynode = physicsNode;        
    }
    
    
    /**
     * This will properly initialize the manager; 
     * it must be called before attempting to use 
     * the manager!
     * 
     * @param assetManager
     * @param rootNode
     * @param bullet
     * @param physicsNode
     * @return the static instance manager
     */
    public static GeomorphManager init(AssetManager assetManager, Node rootNode, 
            BulletAppState bullet, Node physicsNode) {
        manager = new GeomorphManager(assetManager, rootNode, bullet, physicsNode);
        AbstractMap.init(manager);
        return manager;
    }
    
    
    public void attachSpatial(Spatial spatial) {
        rootnode.attachChild(spatial);
        RigidBodyControl spphy = new RigidBodyControl(0f);
        spatial.addControl(spphy);
        physics.getPhysicsSpace().add(spphy);        
    }
    
    
    /**
     * This will remove the spatial from the scene and from 
     * physcis space (effectively removing it from the world).
     * 
     * The spatial must be provided; it must be tracked separately 
     * to know when/if to remove it.
     * 
     * @param spatial 
     */
    public void removeSpatial(Spatial spatial) {
        physics.getPhysicsSpace().removeAll(spatial);
        spatial.removeControl(RigidBodyControl.class);
        spatial.removeFromParent();
    }
        
    
    
}
