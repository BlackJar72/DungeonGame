package jaredbgreat.dungeos.componenet.geomorph;

import java.util.StringTokenizer;

/**
 *
 * @author Jared Blackbrun
 */
public class Geomorph {
    public static final int NUM_ROTATIONS = 16; // This may or may not be used.
    String name;
    int id;
    private GeomorphModel[] rotations;
    
    public Geomorph(String name, GeomorphModel ... models)  {
        this.name = name;
        rotations = models;
    }
    
    
    Geomorph setID(int id) {
        this.id = id;
        return this;
    }
    
    
    public GeomorphModel getRotation(int rotation) {
        return rotations[rotation % rotations.length];
    }
    
    
    public Geomorph setMaterials(String ... mats) {
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
            for(GeomorphModel rotation : rotations) {
                rotation.setMaterial(mesh, path, spec);
            }
        }
        return this;
    }
    
}
