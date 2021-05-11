package jaredbgreat.dungeos.entities.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.math.Ray;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;
import java.util.Random;

/**
 *
 * @author jared
 */
public class CubeMobControl extends AbstractEntityControl {
    private final BetterCharacterControl physics;
    private final Vector3f movement;
    private final Vector3f heading;
    private final Vector3f previous;
    private final Random random;
    private float speed;
    private float height;
    private float hRotSpeed;
    private float vRotSpeed;
    private float wAxis;
    private float relativeForward;
    private float relativeBackward;
    private float spatialAngle;
    private float walk;
    private boolean moving;

    
    public CubeMobControl(AppStateSinglePlayer appState, BetterCharacterControl bcc) {
        super(appState);
        random = new Random();
        speed = 6.0f;
        walk = 1.0f;
        hRotSpeed = 100f;
        vRotSpeed = 100f;
        movement = new Vector3f(0, 0, speed);
        heading  = new Vector3f();
        previous = new Vector3f();
        relativeForward = -1f;
        relativeBackward = 1f;
        spatialAngle = random.nextFloat() * FastMath.TWO_PI;
        physics = bcc;
        physics.setDuckedFactor(0.65f);
        moving = true;//false;
    }
    

    @Override
    protected void controlUpdate(float f) {
        move(f);
        if(canSeePlayer()) {
            System.out.println("I see you!");
            Material mat = new Material(game.getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);
            ((Node)spatial).getChild("Cube").setMaterial(mat);            
        }
    }  
    
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {/*Do Nothing*/}
    
    
    private void move(float f) {
        movement.set(0, 0, speed);
        Quaternion q = spatial.getLocalRotation();
        movement.set(q.mult(movement));
        physics.setWalkDirection(movement);
        if(random.nextInt(256) == 0) spatialAngle = random.nextFloat() * FastMath.TWO_PI;
        heading.set(Vector3f.UNIT_Z).negate();
        physics.setViewDirection(new Quaternion()
                .fromAngleNormalAxis(spatialAngle, Vector3f.UNIT_Y).mult(heading, heading));
        if(previous.distance(spatial.getLocalTranslation()) < (0.1 * f)) spatialAngle = random.nextFloat() * FastMath.TWO_PI;
        previous.set(spatial.getLocalTranslation());        
    }
    
    
    private boolean canSeePlayer() {
        Vector3f ploc = game.getApplication().getCamera().getLocation();
        Vector3f mloc = physics.getSpatial().getLocalTranslation();
        Vector3f tdir  = ploc.subtractLocal(mloc);
        //System.out.println(tdir.lengthSquared());
        //System.out.println(ploc);
        if(tdir.lengthSquared() > 5184) return false; // Too far (over 24^2 units)
        Vector3f vdir = physics.getViewDirection();
        tdir.normalize(); vdir.normalize();
        System.out.println(tdir.dot(vdir));
        if(tdir.dot(vdir) < 0) return false; // Behind mob
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(mloc, vdir);
        game.getApplications().getRootNode().collideWith(ray, results);
        // If it collides with something other than it has hit a wall in the way
        // TODO: When/if the player has a model this must change!
        return (results.size() > 1);        
    }
    
    
    public void startMoving() {
        moving = true;
    }
    
    
    public void stopMovint() {
        moving = false;
    }
    
    
    public void randomizeHeading() {
        spatialAngle = random.nextFloat() * FastMath.TWO_PI;
    }
    
    
}
