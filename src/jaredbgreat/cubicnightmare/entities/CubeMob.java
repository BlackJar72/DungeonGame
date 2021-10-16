package jaredbgreat.cubicnightmare.entities;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import jaredbgreat.cubicnightmare.appstates.AppStateSinglePlayer;
import jaredbgreat.cubicnightmare.appstates.EDifficulty;
import jaredbgreat.cubicnightmare.componenent.geomorph.GeomorphModel;
import jaredbgreat.cubicnightmare.entities.controls.CubeMobControl;
import jaredbgreat.cubicnightmare.mapping.planner.Dungeon;

/**
 *
 * @author Jared Blackburn
 */
public class CubeMob extends AbstractEntity implements PhysicsCollisionListener {
    private static final String A_DIE = "Die";
    private final AppStateSinglePlayer game;
    private final CubeMobControl mobControl;
    private final BetterCharacterControl physicsControl;
    private String name;
    private boolean alive;
    private final EDifficulty diff;
    private long attackCoolDown;
    
    public CubeMob(AppStateSinglePlayer game, Dungeon dungeon, Node parent, 
            BulletAppState physics, Vector3f startPos, String name) {
        this.game = game;
        this.name = name;
        diff = game.getApplications().getDifficulty();
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
        try {
            String a = pce.getNodeA().getName();
            String b = pce.getNodeB().getName();
            if(((a == Player.NAME) && (b == name)) || ((b == Player.NAME) && (a == name))) {            
                hurtPlayer();
            } 
        } catch (NullPointerException e) {}
    }
    
    
    private void hurtPlayer() {
        long t = System.currentTimeMillis();
        if(attackCoolDown < t) {
            game.hurtPlayer();
            attackCoolDown = t + diff.mobCooldown;
        }
        mobControl.setJustHit(attackCoolDown);
    }
    
    
    public void die(PhysicsSpace physics) {
        alive = false;
        Node parentNode = spatial.getParent();
        physics.removeCollisionListener(this);
        physics.removeAll(spatial);
        spatial.removeControl(mobControl);
        spatial.removeControl(physicsControl);
        parentNode.detachChild(spatial);
        spatial = null;
        name = "_nothing_";
    }
    
}
