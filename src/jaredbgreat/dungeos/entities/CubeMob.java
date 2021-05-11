package jaredbgreat.dungeos.entities;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;
import jaredbgreat.dungeos.entities.controls.CubeMobControl;
import jaredbgreat.dungeos.mapping.dld.Dungeon;

/**
 *
 * @author Jared Blackburn
 */
public class CubeMob extends AbstractEntity implements PhysicsCollisionListener{
    private final AppStateSinglePlayer game;
    private final CubeMobControl mobControl;
    private final BetterCharacterControl physicsControl;
    private Geometry visual;
    private final String name;
    private boolean alive;
    
    public CubeMob(AppStateSinglePlayer game, Dungeon dungeon, Node parent, 
            BulletAppState physics, Vector3f startPos, String name) {
        this.game = game;
        this.name = name;
        spatial = dungeon.getGeomorphManager().dummyCube(0.65f, ColorRGBA.Red);
        spatial.setLocalTranslation(startPos);
        physicsControl = new BetterCharacterControl(0.65f, 1.3f, 150f);
        spatial.addControl(physicsControl);
        spatial.setName(name);
        physics.getPhysicsSpace().add(physicsControl);
        physicsControl.setJumpForce(new Vector3f(0f, 750f, 0f));
        physicsControl.setGravity(new Vector3f(0f, -9.8f, 0f));  
        mobControl = new CubeMobControl(game, physicsControl); 
        spatial.addControl(mobControl);
        ((SimpleApplication)game.getApplication()).getRootNode().attachChild(spatial);
        alive = true;
    }
    
    
    public CubeMobControl getControl() {
        return mobControl;
    }
    
    
    public void die() {
        Node node = (Node)spatial;
        // FIXME/TODO: Leave the visual, detatch the physics!
        if(node.hasChild(visual)) {
            node.detachChild(visual);
        }
        alive = false;
    }
    
    
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void collision(PhysicsCollisionEvent pce) {/*
        if((pce.getNodeA().getName() == name)) {            
            System.out.println(name + ": Ouch, I ran into " + pce.getNodeB().getName() + "!");
        } else if((pce.getNodeA().getName() == name)) {
            System.out.println(name + ": Ouch, I ran into " + pce.getNodeA().getName() + "!");
        }
    */
    }
    
}
