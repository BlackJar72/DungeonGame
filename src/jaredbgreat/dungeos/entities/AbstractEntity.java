package jaredbgreat.dungeos.entities;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author jared
 */
public class AbstractEntity implements IEntity {
    protected Spatial spatial;
    
    
    @Override
    public Vector3f getLocation() {
        return spatial.getWorldTranslation();
    }
    
    
    @Override
    public Vector2f getLocation2d() {
        Vector3f threeD = getLocation();
        return new Vector2f(threeD.x, threeD.z);
    }
    
    
    @Override
    public Spatial getSpatial() {
        return spatial;
    }
    
}
