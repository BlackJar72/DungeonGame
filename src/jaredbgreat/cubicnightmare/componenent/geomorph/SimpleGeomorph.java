package jaredbgreat.cubicnightmare.componenent.geomorph;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.StringTokenizer;

/**
 *
 * @author Jared Blackbrun
 */
public class SimpleGeomorph implements IGeomorph {
    String name;
    int id;
    private final GeomorphModel[] variants;
    
    public SimpleGeomorph(String name, GeomorphModel ... models)  {
        this.name = name;
        variants = models;
    }
    
    
    @Override
    public SimpleGeomorph setID(int id) {
        this.id = id;
        return this;
    }
    

    public GeomorphModel getVariant(int variant) {
        return variants[((variant) & 0xffff) % variants.length];
    }
    
    
    @Override
    public SimpleGeomorph setMaterials(String ... mats) {
        String mesh, path;
        StringTokenizer tokens;
        float spec;
        for(String mat : mats) {
            tokens = new StringTokenizer(mat, ":");
            mesh = tokens.nextToken();
            path = tokens.nextToken();
            if(tokens.hasMoreTokens()) {
                spec = Float.parseFloat(tokens.nextToken());
            } else {
                spec = 0.01f;
            }
            for(GeomorphModel variant : variants) {
                variant.setMaterial(mesh, path, spec);
            }
        }
        return this;
    }
    
    
    @Override
    public SimpleGeomorph setMaterialsNm(String ... mats) {
        String mesh, nml, path;
        StringTokenizer tokens;
        float spec;
        for(String mat : mats) {
            tokens = new StringTokenizer(mat, ":");
            mesh = tokens.nextToken();
            path = tokens.nextToken();
            nml  = tokens.nextToken();
            if(tokens.hasMoreTokens()) {
                spec = Float.parseFloat(tokens.nextToken());
            } else {
                spec = 0.01f;
            }
            for(GeomorphModel variant : variants) {
                variant.setMaterial(mesh, path, nml, spec);
            }
        }
        return this;
    }
    

    @Override
    public Node getModel(int variant) {
        return getVariant(variant).template.clone(true);
    }

    @Override
    public Node makeSpatialAt(int variant, float x, float y, float z) {
        Node out = getVariant(variant).template.clone(true);
        out.setLocalTranslation(x, y, z);
        return out;
    }

    @Override
    public Node makeSpatialAt(int variant, Vector3f location) {
        Node out = getVariant(variant).template.clone(true);
        out.setLocalTranslation(location);
        return out;
    }
        
    
    @Override
    public String getName() {
        return name;
    }
    
}
