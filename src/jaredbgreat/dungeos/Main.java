package jaredbgreat.dungeos;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import jaredbgreat.dungeos.componenet.geomorph.GeomorphModel;
import jaredbgreat.dungeos.componenet.geomorph.Geomorphs;
import jaredbgreat.dungeos.mapping.TestMap;
import java.util.Random;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        GeomorphModel.setAssetManager(assetManager);
        Geomorphs.init();
        
        //makeTestScene();
        
        TestMap testmap = new TestMap();
        testmap.build(rootNode);
        
                
        // Lastly lights
        addbasicTestLights();
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
    public static Material makeTexturedtMaterial(AssetManager assetManager, 
            String texture, float specular) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture(texture));
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", false);
        mat.setFloat("Shininess", specular);
        return mat;
    }
    
    
    /*------------------------------------------------------------------------------------*/
    /*                          TEST SCENE STUFF BELOW                                    */
    /*------------------------------------------------------------------------------------*/
    
    
    private void addbasicTestLights() {
        addFourPointLight(0.1f);
        PointLight pLight = new PointLight(new Vector3f(0, 2.5f, 0), (ColorRGBA.White
                    .add(ColorRGBA.Orange).add(ColorRGBA.Yellow))
                .multLocal(0.20f));
        Mesh lb = new Sphere(8, 8, 0.1f);
        Material lm = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lm.setColor("Color", ColorRGBA.White
                    .add(ColorRGBA.Orange).add(ColorRGBA.Yellow).mult(0.3f));
        Geometry lg = new Geometry("Light", lb);
        lg.setMaterial(lm);
        lg.setLocalTranslation(0, 2.5f, 0);
        pLight.setRadius(10);
        rootNode.attachChild(lg);
        rootNode.addLight(pLight);
    }
    
    
    private void makeTestScene() {
        addBedroom();
        addAnime();
        addTableScene();
    }
    
    
    private void addTableScene() {                
        GeomorphModel tabletest = new GeomorphModel("setTable", 
                "Models/test/TableScene001s/TableScene001s.j3o");        
        rootNode.attachChild(tabletest.makeSpatialAt(3, 0, 2));            
        tabletest = new GeomorphModel("setTable", 
                "Models/test/TableScene001s/TableScene001s-pbr.j3o");         
        rootNode.attachChild(tabletest.makeSpatialAt(3, 0, 0.5f));        
    }
    
    
    private void addBedroom() {        
        GeomorphModel bedroom = new GeomorphModel("test", 
                "Models/test/MyIRLBedroom-furnished/MyIRLBedroom-furnished.j3o");
        bedroom.rotateTableScene();
        rootNode.attachChild(bedroom.makeSpatialAt(-4, 0.25f, -0.5f).rotate(0, FastMath.PI, 0));
    }
    
    
    private void addAnime() {
        GeomorphModel anime = new GeomorphModel("AnimeGirl", 
                "Models/test/Rin_2_(Native)/Rin_2_(Native).j3o");        
        rootNode.attachChild(anime.makeSpatialAt(1, 0, 0));
        rootNode.attachChild(anime.makeSpatialAt(-3.5f, 0.25f, -0.8f));
    }
    
    
    private void addExtraLights(int num) {
        Random rand = new Random();
        for(int i = 0; i < num; i++) {  
            PointLight pLight = new PointLight(new Vector3f(rand.nextInt(21) - 10, 
                        rand.nextFloat() + 2, 
                        rand.nextInt(21) - 10), 
                    (ColorRGBA.White.add(ColorRGBA.randomColor()))
                .multLocal(rand.nextFloat() / 10f));
            pLight.setRadius(rand.nextFloat() * 5 + 5);
            rootNode.addLight(pLight);
        }
    }
    
    
    private void addFourPointLight(float brightness) {
        DirectionalLight p1, p2, p3, p4;
        Vector3f d1, d2, d3, d4;
        d4 = new Vector3f(0, 1, 0);
        d1 = new Vector3f(0.866025403784f, -0.5f, 0);
        d2 = new Vector3f(-0.433012701892f, -0.5f, 0.75f).normalizeLocal();
        d3 = new Vector3f(-0.433012701892f, -0.5f, -0.75f).normalizeLocal();
        p1 = new DirectionalLight(d1, ColorRGBA.White.mult(brightness));
        p2 = new DirectionalLight(d2, ColorRGBA.White.mult(brightness));
        p3 = new DirectionalLight(d3, ColorRGBA.White.mult(brightness));
        p4 = new DirectionalLight(d4, ColorRGBA.White.mult(brightness / 2.0f));
        rootNode.addLight(p1);
        rootNode.addLight(p2);
        rootNode.addLight(p3);
        rootNode.addLight(p4);        
    }
}
