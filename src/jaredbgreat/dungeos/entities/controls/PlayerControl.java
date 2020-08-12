package jaredbgreat.dungeos.entities.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import jaredbgreat.dungeos.Main;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;


/**
 *
 * @author Jared Blackburn
 */
public class PlayerControl extends AbstractEntityControl {
    private final BetterCharacterControl physics;
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
    private float sprint;
    private final Quaternion camq;

    
    public PlayerControl(AppStateSinglePlayer appState, BetterCharacterControl bcc) {
        super(appState);
        speed = 4.5f;
        sprint = 1.0f;
        hRotSpeed = 100f;
        vRotSpeed = 100f;
        movement = new Vector3f();
        heading  = new Vector3f();
        relativeForward = -1f;
        relativeBackward = 1f;
        spatialAngle = 0;
        camAngle = 0;
        camHeight = height = 1.6f;
        camq = new Quaternion();
        physics = bcc;
        physics.setDuckedFactor(0.65f);
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        //System.out.println(spatial.getWorldTranslation());
        Main app = (Main) game.getApplication();
        movement.set(0, 0, 0);
        movement.addLocal(dAxis, 0, wAxis);
        if(movement.lengthSquared() > 0) {
            Quaternion q = spatial.getLocalRotation();
            if(physics.isDucked()) {
                movement.set(q.mult(movement.normalizeLocal().multLocal(speed * 0.5f)));
            } else {
                movement.set(q.mult(movement.normalizeLocal().multLocal(speed * sprint)));
            }
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
        wAxis = dAxis = 0f;
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
    
    
    public void setSprint(boolean on) {
        if(on) {
            sprint = 3.0f;
        } else {
            sprint = 1.0f;
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
 
    
}
