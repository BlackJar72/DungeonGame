package jaredbgreat.dungeos.entities;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Jared Blacburn
 */
public interface IEntity {
    public Spatial getSpatial();
    public Vector3f getLocation();
    public Vector2f getLocation2d();
}
