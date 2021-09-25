package jaredbgreat.dungeos.entities;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;
import jaredbgreat.dungeos.componenent.geomorph.GeomorphModel;
import jaredbgreat.dungeos.entities.controls.CubeMobControl;
import jaredbgreat.dungeos.mapping.dld.Dungeon;

/**
 *
 * @author Jared Blackburn
 */
public class CubeMob extends AbstractEntity implements PhysicsCollisionListener{
    private static final String A_DIE = "Die";
    private final AppStateSinglePlayer game;
    private final CubeMobControl mobControl;
    private final BetterCharacterControl physicsControl;
    private Geometry visual;
    private final String name;
    private boolean alive;
    private long attackCoolDown;
    
    public CubeMob(AppStateSinglePlayer game, Dungeon dungeon, Node parent, 
            BulletAppState physics, Vector3f startPos, String name) {
        this.game = game;
        this.name = name;
        GeomorphModel cube = new GeomorphModel("DeathCube", 
                "Models/Creatures/deathcube/DeathCube.glb");        
        spatial = cube.getSpatial();
        spatial.setLocalTranslation(startPos);
        physicsControl = new BetterCharacterControl(0.5f, 1f, 150f);
        spatial.addControl(physicsControl);
        spatial.setName(name);
        physics.getPhysicsSpace().add(physicsControl);
        physicsControl.setJumpForce(new Vector3f(0f, 750f, 0f));
        physicsControl.setGravity(new Vector3f(0f, -9.8f, 0f));  
        mobControl = new CubeMobControl(game, physicsControl); 
        spatial.addControl(mobControl);
        ((SimpleApplication)game.getApplication()).getRootNode().attachChild(spatial);
        attackCoolDown = System.currentTimeMillis();
        alive = true;
    }
    
    
    public CubeMobControl getControl() {
        return mobControl;
    }
    
    
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void collision(PhysicsCollisionEvent pce) {
        String a = pce.getNodeA().getName();
        String b = pce.getNodeB().getName();
        if(((a == Player.NAME) && (b == name)) || ((b == Player.NAME) && (a == name))) {            
            hurtPlayer();
        }     
    }
    
    
    private void hurtPlayer() {
        long t = System.currentTimeMillis();
        if(attackCoolDown < t) {
            game.hurtPlayer();
            attackCoolDown = t + 1000;
        }
    }
    
}
