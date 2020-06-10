package jaredbgreat.dungeos.componenet.geomorph;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Jared Blackburn
 */
public interface IGeomorph {

    IGeomorph setMaterials(String... mats);
    IGeomorph setMaterialsNm(String... mats);
    Node getModel(int variant);
    Node makeSpatialAt(int variant, float x, float y, float z);
    Node makeSpatialAt(int variant, Vector3f location);
    String getName();
    IGeomorph setID(int id);
    
}
