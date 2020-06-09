package jaredbgreat.dungeos.componenet.geomorph;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Jared Blackburn
 */
public class GeomorphModel {
    private static AssetManager assetman;
    final String name;
    Node template;
    // TODO: Other components like step sound or scripts
    
    public GeomorphModel(String name, String path) {
        this.name = name;
        Spatial model = assetman.loadModel(path);
        if(model instanceof Node) {
            template = (Node)model;
        } else {
            template = new Node();
            template.attachChild(model);
        } 
        template.setName(name);
        //System.out.println(name + ": " + template + " -> " +template.getChildren());
    }
    
    
    public GeomorphModel setMaterial(String name, String path, float specular) {
        //System.out.println(template.getChildren());
        Spatial component = template.getChild(name);
        // This fails quietly, since some variants in a group make lack some pieces;
        // e.g., the base form typically lacks any wall while others have walls.
        // i.e., this is expected and intended to be null and do nothing at times.
        if(component != null) {
            component.setMaterial(makeTexturedtMaterial(path, specular));
        }
        return this;
    }
    
    // FIXME: Is this really the best way, or should I have it attach them, too?
    public Node makeSpatialAt(float x, float y, float z) {
        Node out = template.clone(true);
        out.setLocalTranslation(x, y, z);
        return out;
    }
    
    
    public Node makeSpatialAt(Vector3f location) {
        Node out = template.clone(true);
        out.setLocalTranslation(location);
        return out;
    }
    
    
    public static void setAssetManager(AssetManager manager) {
        assetman = manager;
    }
    
    
    public static Material makeTexturedtMaterial(String texture, float specular) {
        Material mat = new Material(assetman, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetman.loadTexture(texture));
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", false);
        mat.setFloat("Shininess", specular);
        return mat;
    }
    
    
    public static Material fixtMaterial(String texture, float specular) {
        Material mat = new Material(assetman, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetman.loadTexture(texture));
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", false);
        mat.setFloat("Shininess", specular);
        return mat;
    }
    
    
    public void rotateTableScene() {        
        template.rotate(0, FastMath.HALF_PI, 0);
    }
}
