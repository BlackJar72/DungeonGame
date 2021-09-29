package jaredbgreat.cubicnightmare.entities;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.cubicnightmare.appstates.AppStateSinglePlayer;
import jaredbgreat.cubicnightmare.entities.controls.PlayerControl;

/**
 *
 * @author jared
 */
public class Player extends AbstractEntity {
    public static final String NAME = "player";
    AppStateSinglePlayer game;
    PlayerControl player;
    BetterCharacterControl control;
    Geometry visual;
    private static int score;
    boolean alive;
    int health;
    
    public Player(AppStateSinglePlayer playgame, Node parent, BulletAppState physics, Vector3f playerStart) {     
        game = playgame;
        spatial = new Node();
        spatial.setLocalTranslation(playerStart);
        control = new BetterCharacterControl(0.5f, 1.8f, 150f);
        spatial.addControl(control);
        physics.getPhysicsSpace().add(control);
        control.setJumpForce(new Vector3f(0f, 750f, 0f));
        control.setGravity(new Vector3f(0f, -9.8f, 0f));  
        player = new PlayerControl(playgame, control); 
        spatial.addControl(player);
        spatial.setName(NAME);
        ((SimpleApplication)playgame.getApplication()).getRootNode().attachChild(spatial);
        alive = true;
        health = 20;
        score = 0;
    }

    public Player(AppStateSinglePlayer playgame, Node phynode, BulletAppState physics) {    
        spatial = new Node();
        spatial.setLocalTranslation(0, 0, 0);
        control = new BetterCharacterControl(0.5f, 1.8f, 150f);
        spatial.addControl(control);
        physics.getPhysicsSpace().add(control);
        control.setJumpForce(new Vector3f(0f, 750f, 0f));
        control.setGravity(new Vector3f(0f, -9.8f, 0f));  
        player = new PlayerControl(playgame, control); 
        spatial.addControl(player);
        ((SimpleApplication)playgame.getApplication()).getRootNode().attachChild(spatial);
        alive = true;
        score = 0;
    }
    
    
    public void giveTorch(PointLight t1, PointLight t2) {
        player.giveTorch(t1, t2);
    }
    

    public static int getScore() {
        return score;
    }
    

    public static void addScore(int points) {
        score += points;
    }
    

    public static void resetScore() {
        score = 0;
    }
    
    
    public void addSpatial(Spatial child) {
        ((Node)spatial).attachChild(child);
    }
    
    
    public PlayerControl getControl() {
        return player;
    }
    
    
    public void die() {
        alive = false;
        Node node = (Node)spatial;
        if(node.hasChild(visual)) {
            node.detachChild(visual);
        }
        alive = false;
        spatial.removeControl(control);
        spatial.removeControl(player);
        game.declareEnd();
    }
    
    
    public boolean isAlive() {
        return alive;
    }
    
    
    @Override
    public Vector3f getLocation() {
        return player.getLocation();
    }
    
    
    public int beHurt() {
        health--;
        if(health < 1) {
            die();
        }        
        return health;
    }
    
    
    public void movePlayer(Vector3f location) {
        control.warp(location);
    }
    
}
