package jaredbgreat.dungeos.componenent;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
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
    
    
    public void attachSpatial(Spatial spatial, Node node) {
        node.attachChild(spatial);
        RigidBodyControl spphy = new RigidBodyControl(0f);
        spatial.addControl(spphy);
        physics.getPhysicsSpace().add(spphy);        
    }
    
    
    public void attachNode(Node spatial) {
        rootnode.attachChild(spatial);        
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
    
    public Node getPhysicsNode() {
        return phynode;
    }
    
    public BulletAppState getPhysics() {
        return physics;
    }
    
    
    
    /*----------------------------------------------------------------------------------*/
    /*                   PRIMATIVES (mostly for testing) BELOW                          */
    /*----------------------------------------------------------------------------------*/
    
    
    
    public void line(Vector3f p1, Vector3f p2) {
        Mesh ml = new Line(p1, p2);
        Geometry gl = new Geometry("Line");
        gl.setMesh(ml);
        Material mat = new Material(assetman, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        gl.setMaterial(mat);
        rootnode.attachChild(gl);
    }
    
    
    public void line(Vector3f p1, Vector3f p2, ColorRGBA c) {
        Mesh ml = new Line(p1, p2);
        Geometry gl = new Geometry("Line");
        gl.setMesh(ml);
        Material mat = new Material(assetman, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", c);
        gl.setMaterial(mat);
        rootnode.attachChild(gl);
    }
    
    
    public Node dummyCube(float size, ColorRGBA c) {
        if(c == null) c = ColorRGBA.White;
        Geometry cube = new Geometry("Cube");
        cube.setMesh(new Box(Vector3f.ZERO, size, size, size));
        Material mat = new Material(assetman, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", c);
        cube.setMaterial(mat);
        cube.setLocalTranslation(0f, size, 0f);
        Node model = new Node();
        model.attachChild(cube);
        return model;
    }
    
    
}
