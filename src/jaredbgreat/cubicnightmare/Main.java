package jaredbgreat.cubicnightmare;

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
import jaredbgreat.cubicnightmare.appstates.AppStateSinglePlayer;
import jaredbgreat.cubicnightmare.appstates.AppStateStartScreen;
import jaredbgreat.cubicnightmare.appstates.EDifficulty;
import jaredbgreat.cubicnightmare.componenent.GeomorphManager;
import jaredbgreat.cubicnightmare.componenent.geomorph.GeomorphModel;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.util.debug.DLDProfile;
import jaredbgreat.cubicnightmare.util.debug.IProfiler;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Random;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {    
    public static final IProfiler  proflogger = new DLDProfile();
    private static AppStateSinglePlayer play;
    private static AppStateStartScreen start;
    EDifficulty difficulty;
    GeomorphManager geomanager;
    BulletAppState physics;
    Node worldNode;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(false);
        settings.setTitle("Cubic Nightmare");
        settings.setSettingsDialogImage("Interface/CubicNightmare.png");
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = device.getDisplayMode();
        settings.setFullscreen(true);
        settings.setFrameRate(mode.getRefreshRate());
        settings.setFrequency(mode.getRefreshRate());
        settings.setHeight(mode.getHeight());
        settings.setWidth(mode.getWidth());
        settings.setBitsPerPixel(mode.getBitDepth());
        settings.setVSync(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initPhysics();
        initGeomorphs();
        play  = new AppStateSinglePlayer();
        start = new AppStateStartScreen();
        stateManager.attach(start);
        //stateManager.attach(play);
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
    
    
    public void startGame() {
        difficulty = start.getDifficulty();
        if(stateManager.hasState(start)) {
            stateManager.detach(start);
        }
        if(stateManager.hasState(play)) {
            stateManager.detach(play);            
        }
        stateManager.attach(play = new AppStateSinglePlayer());
    }
    
    
    public void endGame() {
        if(stateManager.hasState(play)) {
            play.clearDungeon();
            stateManager.detach(play); 
        }
        stateManager.attach(start);
    }
    
    
    public EDifficulty getDifficulty() {
        return difficulty;
    }
    
    
    
    /*------------------------------------------------------------------------------------*/
    /*                          TEST SCENE STUFF BELOW                                    */
    /*------------------------------------------------------------------------------------*/
    
    
    
    private void makeTestScene() {
        addAnime(Vector3f.ZERO);
        addTableScene(Vector3f.ZERO);
    }
    
    
    public void makeTestScene(Vector3f center) {
        addTableScene(center);
        addTestGoblins(center);
    }
    
    
    private void addTableScene(Vector3f center) {                
        GeomorphModel tabletest = new GeomorphModel("setTable", 
                "Models/test/TableScene001s/TableScene001s.j3o");        
        rootNode.attachChild(tabletest.makeSpatialAt(3 + center.x, 0 + center.y, 2 + center.z));            
        tabletest = new GeomorphModel("setTable",  
                "Models/test/TableScene001s/TableScene001s-pbr.j3o");         
        rootNode.attachChild(tabletest.makeSpatialAt(3 + center.x, center.y, 0.5f + center.z));        
    }
    
    
    private void addAnime(Vector3f center) {
        GeomorphModel anime = new GeomorphModel("AnimeGirl", 
                "Models/test/Rin_2_(Native)/Rin_2_(Native).j3o");        
        rootNode.attachChild(anime.makeSpatialAt(center.x + 1, center.y, center.z));
        rootNode.attachChild(anime.makeSpatialAt(-3.5f + center.x, center.y + 0.01f, -0.8f + center.z));
    }
    
    
    private void addTestGoblins(Vector3f center) {
        GeomorphModel goblin = new GeomorphModel("Goblin", 
                "Models/Creatures/goblin/goblin001.glb");        
        rootNode.attachChild(goblin.makeSpatialAt(center.x + 1, center.y, center.z));
        //rootNode.attachChild(goblin.makeSpatialAt(-3.5f + center.x, center.y + 0.01f, -0.8f + center.z));
    }
    
}
