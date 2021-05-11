package jaredbgreat.dungeos.componenent.geomorph;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;
import java.util.StringTokenizer;

/**
 *
 * @author Jared Blackburn
 */
public class GeomorphModel {
    private static final Node NULL = new Node("Empty");
    public  static final GeomorphModel EMPTY = new GeomorphModel(null, null);
    private static AssetManager assetman;
    final String name;
    Node template;
    // TODO: Other components like step sound or scripts
    
    public GeomorphModel(String name, String path) {
        this.name = name;
        if(path == null) {
            template = NULL;
        } else {
            Spatial model = assetman.loadModel(path);
            if(model instanceof Node) {
                template = (Node)model;
            } else {
                template = new Node();
                template.attachChild(model);
            }
            template.setName(name);
        }
    }
    
    
    public GeomorphModel(String name, String mname, Mesh mesh, Vector3f position) {
        this.name = name;
        if(mesh == null) {
            template = NULL;
        } else {
            Geometry geom = new Geometry(mname, mesh);
            geom.setName(name);
            geom.setLocalTranslation(position);
            template = new Node(name);
            template.attachChild(geom);
        }
    }
    
    
    static GeomorphModel makeSimpleFloor(String name) {
        return new GeomorphModel(name, "Floor", 
                new Box(1.5f, 0.115385f, 1.5f), new Vector3f(0, -0.115329f, 0));
    }
    
    
    static GeomorphModel makeSimpleCieling(String name) {
        return new GeomorphModel(name, "Cieling", 
                new Box(1.5f, 0.115385f, 1.5f), new Vector3f(0, 3.1331725f, 0));
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
    
    
    public GeomorphModel setMaterial(String name, String path, String normals, float specular) {
        //System.out.println(template.getChildren());
        Spatial component = template.getChild(name);
        // This fails quietly, since some variants in a group make lack some pieces;
        // e.g., the base form typically lacks any wall while others have walls.
        // i.e., this is expected and intended to be null and do nothing at times.
        if(component != null) {
            TangentBinormalGenerator.generate((Geometry)component);
            component.setMaterial(makeTexturedtMaterial(path, normals, specular));
        }
        return this;
    }
    
    
    public GeomorphModel setMaterials(String ... mats) {
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
            setMaterial(mesh, path, spec);
        }
        return this;
    }
    
    
    public GeomorphModel setMaterialsNm(String ... mats) {
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
            setMaterial(mesh, path, nml, spec);
        }
        return this;
    }
    
    
    public GeomorphModel setScale(Vector2f scale, String ... mats) {
        for(String mat : mats) {
            Spatial thing = template.getChild(mat);
            if((thing != null) && (thing instanceof Geometry)) {
                Geometry geom = (Geometry)thing;
                Mesh mesh = geom.getMesh();
                mesh.scaleTextureCoordinates(scale);
            }
        }
        return this;
    }
    
    
    // FIXME: Is this really the best way, or should I have it attach them, too?
    public Node makeSpatialAt(float x, float y, float z) {
        Node out = template.clone(true);
        System.out.println(out.getName());
        out.setLocalTranslation(x, y, z);
        return out;
    }
    
    
    public Node makeSpatialAt(Vector3f location) {
        Node out = template.clone(true);
        System.out.println(out.getName());
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
    
    
    public static Material makeTexturedtMaterial(String texture, String normal, float specular) {
        Material mat = new Material(assetman, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetman.loadTexture(texture));
        mat.setTexture("NormalMap", assetman.loadTexture(normal));
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
