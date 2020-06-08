package jaredbgreat.dungeos.componenet.geomorph;

import java.util.StringTokenizer;

/**
 *
 * @author Jared Blackbrun
 */
public class Geomorph {
    String name;
    int id;
    private final GeomorphModel[] variantes;
    
    public Geomorph(String name, GeomorphModel ... models)  {
        this.name = name;
        variantes = models;
    }
    
    
    Geomorph setID(int id) {
        this.id = id;
        return this;
    }
    
    
    public GeomorphModel getRotation(int rotation) {
        return variantes[rotation % variantes.length];
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
            for(GeomorphModel rotation : variantes) {
                rotation.setMaterial(mesh, path, spec);
            }
        }
        return this;
    }
    
}
