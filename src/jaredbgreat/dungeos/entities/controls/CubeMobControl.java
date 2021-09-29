package jaredbgreat.dungeos.entities.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.math.Ray;
import com.jme3.renderer.ViewPort;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;
import jaredbgreat.dungeos.appstates.EDifficulty;
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
    private final float speed;
    private float height;
    private final float hRotSpeed;
    private final float vRotSpeed;
    private float wAxis;
    private final float relativeForward;
    private final float relativeBackward;
    private float spatialAngle;
    private final float walk;
    private boolean moving;
    private boolean attacking;
    private boolean justHit;
    private long nextHit;
    private int turnfails;
    EDifficulty diff;

    
    public CubeMobControl(AppStateSinglePlayer appState, BetterCharacterControl bcc) {
        super(appState);
        random = new Random();
        diff = appState.getApplications().getDifficulty();
        speed = diff.mobSpeed * diff.genericFactor;
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
        moving = true;
        justHit = false;
        nextHit = 0;
        turnfails = 0;
    }
    

    @Override
    protected void controlUpdate(float f) {
        if(!attacking) {
            attacking = canSeePlayer();
        }
        move(f);
    }  
    
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {/*Do Nothing*/}
    
    
    private void move(float f) {
        if(justHit && (System.currentTimeMillis() > nextHit)) {
            justHit = false;
        }
        movement.set(0, 0, speed);
        Quaternion q = spatial.getLocalRotation();
        movement.set(q.mult(movement));
        if(justHit) {
            physics.setWalkDirection(movement.negate());
        } else {
            physics.setWalkDirection(movement);
        }
        float d = distToPlayer();
        if(attacking) {
            if((d > 4) && (random.nextInt(256) == 0)) attacking = false;
        } else {
            if(random.nextInt(256) == 0) spatialAngle = random.nextFloat() * FastMath.TWO_PI;
        }
        heading.set(Vector3f.UNIT_Z).negate();
        if(attacking) {            
            physics.setViewDirection(toPlayer());
        } else {
            physics.setViewDirection(new Quaternion()
                    .fromAngleNormalAxis(spatialAngle, Vector3f.UNIT_Y).mult(heading, heading));        
        }
        if((d > 1) && (previous.distance(spatial.getLocalTranslation()) < (3 * f * diff.genericFactor))) {
            if((distToPlayer() > 4)) attacking = false;
            if(random.nextInt(3) < turnfails) {
                spatialAngle += FastMath.PI;
                if(spatialAngle > FastMath.TWO_PI) {
                    spatialAngle -= FastMath.TWO_PI;
                }
                attacking = false;
                turnfails = 0;
            } else {                
                spatialAngle = random.nextFloat() * FastMath.TWO_PI;
                turnfails++;
            }
        } else {
            turnfails = Math.max(0, --turnfails);
        }
        previous.set(spatial.getLocalTranslation());        
    }
    
    
    private boolean canSeePlayer() {
        Vector3f ploc = new Vector3f();
        game.getPlayerPos().get(ploc);
        Vector3f mloc = physics.getSpatial().getLocalTranslation();
        Vector3f tdir  = ploc.subtract(mloc);
        if(tdir.lengthSquared() > 5184) return false; // Too far (over 24^2 units)
        Vector3f vdir = physics.getViewDirection();
        if(tdir.dot(vdir) < 0) return false; // Behind mob
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(mloc, vdir);
        game.getApplications().getRootNode().collideWith(ray, results);
        
        // If it collides with something other than it has hit a wall in the way
        // TODO: When/if the player has a model this must change!
        if(results.size() < 1) return true;
        String name = results.getCollision(0).getGeometry().getName();
        if(name.equals("Cube") && results.size() > 1) {
            return (results.getCollision(1).getDistance() > tdir.length());
        }
        return (results.getCollision(0).getDistance() > tdir.length());
    }
    
    
    private float angToPlayer() {
        Vector3f ploc = new Vector3f();
        game.getPlayerPos().get(ploc);
        Vector3f mloc = physics.getSpatial().getLocalTranslation();
        Vector3f tdir  = ploc.subtract(mloc);
        float tan = tdir.x / tdir.z;
        return FastMath.atan(tan);
    }
    
    
    private Vector3f toPlayer() {
        Vector3f ploc = new Vector3f();
        game.getPlayerPos().get(ploc);
        Vector3f mloc = physics.getSpatial().getLocalTranslation();
        ploc.subtractLocal(mloc);
        ploc.setY(0f);
        ploc.normalizeLocal().multLocal(speed);
        return ploc;
    }
    
    
    private float distToPlayer() {
        Vector3f ploc = new Vector3f();
        game.getPlayerPos().get(ploc);
        Vector3f mloc = physics.getSpatial().getLocalTranslation();
        return ploc.subtract(mloc).length();
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
    
    
    public void setJustHit(long when) {
        justHit = true;
        nextHit = when;
    }
    
}
