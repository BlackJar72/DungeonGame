package jaredbgreat.dungeos;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import jaredbgreat.dungeos.componenet.geomorph.GeomorphModel;
import jaredbgreat.dungeos.mapping.TestMap;

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
        
        makeTestScene();
        
        TestMap testmap = new TestMap();
        testmap.build(rootNode);
        
        
        // Lastly lights
        Vector3f ld = new Vector3f(1, -2, -3.5f).normalizeLocal();   
        DirectionalLight dirlight = new DirectionalLight(ld, (ColorRGBA.White
                .add((ColorRGBA.Yellow).mult(0.1f)))
                .multLocal(0.5f)); 

        AmbientLight aLight = new AmbientLight(ColorRGBA.White.mult(0.05f));
        PointLight pLight = new PointLight(new Vector3f(0, 2, 0), (ColorRGBA.White.add(ColorRGBA.Yellow))
                .multLocal(0.25f));
        rootNode.addLight(dirlight);
        rootNode.addLight(aLight);
        rootNode.addLight(pLight);
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
        //addFirstTestFloor();
        addBedroom();
        addAnime();
        //addTableScene();
    }
    
    private void addFirstTestFloor() {                
                GeomorphModel testgeo = new GeomorphModel("test", 
                "Models/test/Geomorph-blank-floor/Geomorph-blank-floor.j3o")
                .setMaterial("Floor", "Textures/stone-hr.png", 0.05f);        
        int ij;
        Node[] floorTiles = new Node[7 * 7];
        for(int i = 0; i < 7; i++) 
            for(int j = 0; j < 7; j++) {
                ij = (i * 7) + j;
                floorTiles[ij] = testgeo.makeSpatialAt((i - 3) * 3, 0, (j - 3) * 3);
                rootNode.attachChild(floorTiles[ij]);
                if(i == 3 & j == 3) {
                    floorTiles[ij].getChild("Floor")
                            .setMaterial(makeTexturedtMaterial(assetManager, 
                                         "Textures/granite-black-hr.jpg", 0.05f));
                }
        }
    }
    
    private void addTableScene() {                
        GeomorphModel tabletest = new GeomorphModel("setTable", 
                "Models/test/TableScene001s/TableScene001s.j3o");        
        rootNode.attachChild(tabletest.makeSpatialAt(0, 0, -1));            
        tabletest = new GeomorphModel("setTable", 
                "Models/test/TableScene001s/TableScene001s-pbr.j3o");         
        rootNode.attachChild(tabletest.makeSpatialAt(0, 0, -2.5f));        
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
}
