package jaredbgreat.dungeos;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import jaredbgreat.dungeos.appstates.AppStateSinglePlayer;
import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.GeomorphModel;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import java.util.Random;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    GeomorphManager geomanager;
    BulletAppState physics;
    Node worldNode;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Dungeon Game");
        settings.setVSync(true);
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initPhysics();
        initGeomorphs();      
        stateManager.attach(new AppStateSinglePlayer());
        
        //makeTestScene();
    }
    
    
    private void initPhysics() {             
        physics = new BulletAppState();
        worldNode = new Node("worldNode");
        rootNode.attachChild(worldNode);        
        stateManager.attach(physics);
        geomanager = GeomorphManager.init(assetManager, worldNode, physics, worldNode);
    }
    
    
    private void initGeomorphs() { 
        GeomorphModel.setAssetManager(assetManager);
        Geomorphs.init();        
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
    
    
    
    private void makeTestScene() {
        addBedroom(Vector3f.ZERO);
        addAnime(Vector3f.ZERO);
        addTableScene(Vector3f.ZERO);
    }
    
    
    public void makeTestScene(Vector3f center) {
        addBedroom(center);
        //addAnime(center);
        addTableScene(center);
    }
    
    
    private void addTableScene(Vector3f center) {                
        GeomorphModel tabletest = new GeomorphModel("setTable", 
                "Models/test/TableScene001s/TableScene001s.j3o");        
        rootNode.attachChild(tabletest.makeSpatialAt(3 + center.x, 0 + center.y, 2 + center.z));            
        tabletest = new GeomorphModel("setTable",  
                "Models/test/TableScene001s/TableScene001s-pbr.j3o");         
        rootNode.attachChild(tabletest.makeSpatialAt(3 + center.x, center.y, 0.5f + center.z));        
    }
    
    
    private void addBedroom(Vector3f center) {        
        GeomorphModel bedroom = new GeomorphModel("test", 
                "Models/test/MyIRLBedroom-furnished/MyIRLBedroom-furnished.j3o");
        bedroom.rotateTableScene();
        GeomorphManager.manager
                .attachSpatial(bedroom.makeSpatialAt(-4 + center.x, center.y + 0.01f, -0.5f + center.z).rotate(0, FastMath.PI, 0));
    }
    
    
    private void addAnime(Vector3f center) {
        GeomorphModel anime = new GeomorphModel("AnimeGirl", 
                "Models/test/Rin_2_(Native)/Rin_2_(Native).j3o");        
        rootNode.attachChild(anime.makeSpatialAt(center.x + 1, center.y, center.z));
        rootNode.attachChild(anime.makeSpatialAt(-3.5f + center.x, center.y + 0.01f, -0.8f + center.z));
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
    
}
