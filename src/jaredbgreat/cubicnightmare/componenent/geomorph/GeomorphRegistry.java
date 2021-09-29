package jaredbgreat.cubicnightmare.componenent.geomorph;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import jaredbgreat.cubicnightmare.util.Registry;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class GeomorphRegistry extends Registry<IGeomorph> {
    

    /**
     * This exists only to add deprecation; do not use this, use 
     * getGeomorph() instead (or getModel() for GeomorphModels).
     */      
    @Deprecated
    @Override
    public IGeomorph get(int index) {
        return super.get(index);
    }
    
    
    /**
     * This will return the stored geomorph for the id given; 
     * ids run from 0 to 65535. Anything out of this range is 
     * invalid, but will wrap-around.
     * 
     * @param id
     * @return 
     */
    public IGeomorph getGeomorph(int id) {
        return super.get(id % 0xffff);
    }
    
    
    /**
     * This will return the stored geomorph for the id given; 
     * ids run from 0 to 65535. This version takes a short, 
     * so a short can be passed without warning and no modulus 
     * is needed to restrain to the proper range.
     * 
     * @param id
     * @return 
     */
    public IGeomorph getGeomorph(short id) {
        return super.get(id);
    }
    
    
    /**
     * This can be used to get a model (node) using an array; 
     * its is not intended for explicit use with literals but 
     * is the perfered way when using array data containing 
     * the full id and rotation data (as well as any sub-id 
     * that might be added).
     * 
     * @param id
     * @return 
     */
    public Node getModel(int id) {
        return super.get(id & 0xffff).getModel((id >> 16) & 0xffff);
    }
        
    
    public Node makeSpatialAt(int id, float x, float y, float z)  {
        return super.get(id & 0xffff).makeSpatialAt((id >> 16) & 0xffff, x, y, z);
    }
    
    
    public Node makeSpatialAt(int id, Vector3f location) {
        return super.get(id & 0xffff).makeSpatialAt((id >> 16) & 0xffff, location);
    }
    
    
    /**
     * This will retrieve the GeomorphModel for the given id and rotation. 
     * This is good for use with literals and perhaps in generation algorithms.
     * 
     * @param id
     * @param rotation
     * @return 
     */
    public Node getModel(int id, int rotation) {
        return super.get(id & 0xffff).getModel(rotation);
    }
    
    
    /**
     * This will retrieve the GeomorphModel for the given id and rotation. 
     * This is good for use with literals and perhaps in generation algorithms.
     * 
     * @param id
     * @param rotation
     * @return 
     */
    public Node getModel(short id, byte rotation) {
        return super.get(id).getModel(rotation);
    }
    
    
    /**
     * This will calculate a full model ID from a geomorph ID 
     * and rotation.  This is intended to be used by generators 
     * to store generated map data into arrays.
     * 
     * @param geomorph
     * @param rotation
     * @return 
     */
    public int generateFullModelId(int geomorph, int rotation) {
        return (geomorph & 0xffff) + ((rotation & 0xff) >> 16);
    }
    
    
    /**
     * This will calculate a full model ID from a geomorph name 
     * and rotation.
     * 
     * @param name 
     * @param rotation
     * @return 
     */
    public int generateFullModelId(String name, int rotation) {
        return getID(name) + ((rotation & 0xff) >> 16);
    }
    
}
