package jaredbgreat.dungeos.componenet.geomorph;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * This is a replacement for the original Geomorph classs to better
 * and more flexibly handle combinations of elements (notable floor 
 * wall combos, but maybe more later.
 * 
 * The plan goes like this (I think, so far):
 *      Byte 1: Floor type
 *      Byte 2: Wall type
 *      Byte 3: Wall possition/variants
 *      Byte 4: ??? (Reserved for future development
 * 
 * Hopefully that will do, I don't think I need more than 256 of 
 * each componenent.
 * 
 * @author Jared Blackburn
 */
public class Geomorph implements IGeomorph {
    // Now, should this hold geomorphs models?  Or handles (ints) into a registry?
    private GeomorphModel  floor;
    private SimpleGeomorph walls;
    private GeomorphModel  cieling;
    

    @Override
    public Geomorph setMaterials(String... mats) {
        //TODO
        return null;
    }
    
    
    @Override
    public Node getModel(int variant) {
        Node out = new Node();
        out.attachChild(walls.getVariant(variant & 31).template.clone(true));
        if((variant & 0x20) > 0) {
            out.attachChild(floor.template.clone(true));
        }
        if((variant & 0x40) > 0) {
            out.attachChild(cieling.template.clone());
        }
        return out;
    }
    

    @Override
    public Node makeSpatialAt(int variant, float x, float y, float z) {
        Node out = getModel(variant);
        out.setLocalTranslation(x, y, z);
        return out;
    }

    
    @Override
    public Node makeSpatialAt(int variant, Vector3f location) {
        Node out = getModel(variant);
        out.setLocalTranslation(location);
        return out;
    }
    
}
