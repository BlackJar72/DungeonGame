package jaredbgreat.cubicnightmare.entities.controls;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import jaredbgreat.cubicnightmare.Main;
import jaredbgreat.cubicnightmare.appstates.AppStateSinglePlayer;
import jaredbgreat.cubicnightmare.entities.Player;


/**
 *
 * @author Jared Blackburn
 */
public class PlayerControl extends AbstractEntityControl implements PhysicsCollisionListener {
    private final BetterCharacterControl physics;
    private final Vector3f position;
    private final Vector3f movement;
    private final Vector3f heading;
    private float speed;
    private float height;
    private float hRotSpeed;
    private float vRotSpeed;
    private float wAxis;
    private float dAxis;
    private float relativeForward;
    private float relativeBackward;
    private boolean firstPerson;
    private float spatialAngle;
    private float camAngle;
    private float camHeight;
    private float walk;
    private final Quaternion camq;
    private AudioNode walking;
    private AudioNode[] sounds;
    private boolean isWalking;
    
    
    private PointLight t1, t2;

    
    public PlayerControl(AppStateSinglePlayer appState, BetterCharacterControl bcc) {
        super(appState);
        speed = 8.0f * appState.getApplications().getDifficulty().genericFactor;
        walk = 1.0f;
        hRotSpeed = 100f;
        vRotSpeed = 100f;
        position = new Vector3f();
        movement = new Vector3f();
        heading  = new Vector3f();
        relativeForward = -1f;
        relativeBackward = 1f;
        spatialAngle = 0;
        camAngle = 0;
        camHeight = height = 1.5f;
        camq = new Quaternion();
        physics = bcc;
        physics.setDuckedFactor(0.65f);
        walking = makeSound(appState.getApplications().getAssetManager(), 
                "Sounds/Walk10.wav");
        isWalking = false;
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        game.setPlayerPos(position);
        position.set(spatial.getWorldTranslation());
        Main app = (Main) game.getApplication();
        movement.set(0, 0, 0);
        movement.addLocal(dAxis, 0, wAxis);
        if(movement.lengthSquared() > 0) {
            Quaternion q = spatial.getLocalRotation();
            if(physics.isDucked()) {
                movement.set(q.mult(movement.normalizeLocal().multLocal(speed * 0.5f * walk)));
            } else {
                movement.set(q.mult(movement.normalizeLocal().multLocal(speed * walk)));
            }
            boolean onGround = physics.isOnGround();
            if(!isWalking && onGround) {
                walking.play();
                isWalking = true;
            } else if(!onGround) {                
                walking.stop();
                isWalking = false;
            }
        } else if(isWalking){
            walking.stop();
            isWalking = false;
        }
        //System.out.println(movement);
        physics.setWalkDirection(movement);
        heading.set(Vector3f.UNIT_Z).negate();
        physics.setViewDirection(new Quaternion()
                .fromAngleNormalAxis(spatialAngle, Vector3f.UNIT_Y).mult(heading, heading));
        {
            Camera cam = app.getCamera();                  
            camq.fromAngleNormalAxis(camAngle, Vector3f.UNIT_X);
            cam.setLocation(spatial.getWorldTranslation().add(0, camHeight, 0));
            cam.setRotation((spatial.getWorldRotation().mult(camq)));
        }
        if(t1 != null) {
            t1.setPosition(position.add(Vector3f.UNIT_Y));
        }
        if(t2 != null) {
            t2.setPosition(position.add(Vector3f.UNIT_Y));
        }
        wAxis = dAxis = 0f;
    }
    
    
    public void giveTorch(PointLight t1, PointLight t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
    
    
    public Vector3f getLocation() {
        return position;
    }

    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    
    public void setMoveForward(float amount) {
        wAxis += amount;
    }
    
    
    public void setMoveBackward(float amount) {
        wAxis -= amount;
    }
    
    
    public void setMoveLeft(float amount) { 
        dAxis += amount;
    }
    
    
    public void setMoveRight(float amount) {
        dAxis -= amount;
    }
    
    
    public void setTurnRght(float value) {
        spatialAngle -= (value * hRotSpeed);
        if(spatialAngle < 0) {
            spatialAngle += FastMath.TWO_PI;
        }
    }
    
    
    public void setTurnLeft(float value) {
        spatialAngle += (value * hRotSpeed);
        if(spatialAngle > FastMath.TWO_PI) {
            spatialAngle -= FastMath.TWO_PI;
        }
    }
    
    
    public void setLookUp(float value) {
        camAngle = Math.max(camAngle - (value * vRotSpeed), -FastMath.HALF_PI);
    }
    
    
    public void setLookDown(float value) {
        camAngle = Math.min(camAngle + (value * vRotSpeed), FastMath.HALF_PI);
    }
    
    
    public void jump() {
        physics.jump();
    }
    
    
    public void setCrouch(boolean on) {
        physics.setDucked(on);
        if(on) {
            camHeight = height * 0.6f;
        } else {
            camHeight = height;
        }
    }
    
    
    public void setWalk(boolean on) {
        if(on) {
            walk = 0.5f;
        } else {
            walk = 1.0f;
        }
    }
    
    
    public void setFirstPerson() {
        firstPerson = true;
        relativeForward  =  1f;
        relativeBackward = -1f;
        hRotSpeed = 100f;
        vRotSpeed = 100f;
    }
    
    
    public void setThirdPerson() {
        firstPerson = false;
        relativeForward  = -1f;
        relativeBackward =  1f;
        hRotSpeed = 400f;
        vRotSpeed = 0f;
    }
    
    
    public void setSounds(AudioNode... audio) {
        sounds = audio;
    }
    
    
    public void playSound(int i) {
        sounds[i].setLocalTranslation(spatial.getLocalTranslation());
        sounds[i].play(); // playInstance()?
    }
    
    
    public void playSound(int i, float loud) {
        sounds[i].setLocalTranslation(spatial.getLocalTranslation());
        sounds[i].setVolume(loud);
        sounds[i].play(); // playInstance()?
    }
    
    
    private AudioNode makeSound(AssetManager assetman, String location) {
        AudioNode out = new AudioNode(assetman, location, AudioData.DataType.Buffer);
        out.setPositional(false);
        out.setLooping(true);
        out.setVolume(speed / 10f);
        return out;
    }
    
    
    public void walkingOff() {
        walking.stop();
        isWalking = false;
    }
    

    @Override
    public void collision(PhysicsCollisionEvent event) {        
        try{
                String A = event.getNodeA().getName();
            String B = event.getNodeB().getName();
            if(A == Player.NAME) {
                game.applyPickupEffect(B); 
            } else {
                game.applyPickupEffect(A); 
            } 
        } catch (NullPointerException e) {}
    }
 
    
}
