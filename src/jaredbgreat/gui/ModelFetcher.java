package jaredbgreat.boulders.util;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;

/**
 *
 * @author Jared Blackburn
 */
public class ModelFetcher {
    
    
    public static Spatial fetchStaticModel(AssetManager assetman, String name, 
                BulletAppState physics) {
        Spatial out = assetman.loadModel(name);
        // FIXME?  Do I need to keep track of these physics components and clean them up?
        RigidBodyControl rbc = new RigidBodyControl(0f);
        out.addControl(rbc);
        physics.getPhysicsSpace().add(out);
        return out;
    }
    
}
