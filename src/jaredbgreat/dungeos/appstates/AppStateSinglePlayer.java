package jaredbgreat.dungeos.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import jaredbgreat.dungeos.Main;
import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.entities.Player;
import jaredbgreat.dungeos.mapping.TestMap;
import jaredbgreat.dungeos.mapping.dld.Dungeon;
import java.util.Random;

/**
 *
 * @author jared
 */
public class AppStateSinglePlayer extends BaseAppState {
    GeomorphManager geomanager;
    AssetManager assetman;
    BaseAppState controls;
    BulletAppState physics;
    Node phynode;
    Random random;
    Main app;
    Node rootnode; 
    
    Player player; // wtf is this?!? Its not the object I assign to it!!!
    volatile volatileVec playerPos; // "volatile," it does nothing!
    
    
    public static final class volatileVec {
        volatile float x, y, z;
        public void set(float nx, float ny, float nz) {
            x = nx; y = ny; z = nz;
        }
        public void set(Vector3f v) {
            x = v.x; y = v.y; z = v.z;
        }
        public void get(Vector3f v) {
            v.x = x; v.y = y; v.z = z;
        }
    }

    
    @Override
    protected void initialize(Application ap) { 
        app = (Main)ap;
        geomanager = GeomorphManager.manager;
        physics = geomanager.getPhysics();
        assetman = app.getAssetManager();
        rootnode = app.getRootNode();
        phynode = geomanager.getPhysicsNode();
        playerPos = new volatileVec();
    }

    
    @Override
    protected void onEnable() {
        
        app.getFlyByCamera().setMoveSpeed(10);
        // Create world from map
        //TestMap testmap = new TestMap();
        //testmap.build(); 
        //TestMap testmap = new TestMap();
        //testmap.build(); 
        Dungeon dungeon = new Dungeon(this, geomanager);
                
        //player = new Player(this, phynode, physics);
        player = new Player(this, phynode, physics, dungeon.getPlayerStart());
        app.getStateManager().attach(new AppStateFirstPerson(player));
                
        app.makeTestScene(dungeon.getPlayerStart());
        
        // Lastly lights
        addFourPointLight(0.5f);
        //giveTorch(dungeon, player);
        
        addbasicTestLights(dungeon);       
        addStartEndMarks(dungeon);
    }

    
    @Override
    protected void onDisable() {}

    
    @Override
    protected void cleanup(Application app) {}
    
    
    
    
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
        rootnode.addLight(p1);
        rootnode.addLight(p2);
        rootnode.addLight(p3);
        rootnode.addLight(p4);        
    }
    
    private void addbasicTestLights(Dungeon dungeon) {
        //addFourPointLight(0.15f);
        Vector3f plloc = dungeon.getLevelEndSpot().add(new Vector3f(0, 2.5f, 0));
        PointLight pLight = new PointLight(plloc, (ColorRGBA.White
                    .add(ColorRGBA.Orange).add(ColorRGBA.Yellow))
                .multLocal(0.20f));
        Mesh lb = new Sphere(8, 8, 0.1f);
        Material lm = new Material(assetman, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lm.setColor("Color", ColorRGBA.White
                    .add(ColorRGBA.Orange).add(ColorRGBA.Yellow).mult(0.3f));
        Geometry lg = new Geometry("Light", lb);
        lg.setMaterial(lm);
        lg.setLocalTranslation(plloc);
        pLight.setRadius(10);
        rootnode.attachChild(lg);
        rootnode.addLight(pLight);
    }
    
    private void addbasicTestLights() {
        //addFourPointLight(0.15f);
        Vector3f plloc = new Vector3f(0, 2.5f, 0);
        PointLight pLight = new PointLight(plloc, (ColorRGBA.White
                    .add(ColorRGBA.Orange).add(ColorRGBA.Yellow))
                .multLocal(0.20f));
        Mesh lb = new Sphere(8, 8, 0.1f);
        Material lm = new Material(assetman, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lm.setColor("Color", ColorRGBA.White
                    .add(ColorRGBA.Orange).add(ColorRGBA.Yellow).mult(0.3f));
        Geometry lg = new Geometry("Light", lb);
        lg.setMaterial(lm);
        lg.setLocalTranslation(plloc);
        pLight.setRadius(10);
        rootnode.attachChild(lg);
        rootnode.addLight(pLight);
    }
    
    private void addStartEndMarks(Dungeon dungeon) {
        Vector3f plloc = dungeon.getLevelEndSpot().add(new Vector3f(0, 4.5f, 0));
        PointLight pLight = new PointLight(plloc, ColorRGBA.Red);
        Mesh lb = new Sphere(8, 8, 0.1f);
        Material lm = new Material(assetman, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lm.setColor("Color", ColorRGBA.Red);
        Geometry lg = new Geometry("Light", lb);
        lg.setMaterial(lm);
        lg.setLocalTranslation(plloc);
        pLight.setRadius(10);
        rootnode.attachChild(lg);
        rootnode.addLight(pLight);
        
        plloc = dungeon.getPlayerStart().add(new Vector3f(0, 4.5f, 0));
        pLight = new PointLight(plloc, ColorRGBA.Blue);
        lb = new Sphere(8, 8, 0.1f);
        lm = new Material(assetman, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lm.setColor("Color", ColorRGBA.Blue);
        lg = new Geometry("Light", lb);
        lg.setMaterial(lm);
        lg.setLocalTranslation(plloc);
        pLight.setRadius(10);
        rootnode.attachChild(lg);
        rootnode.addLight(pLight);
    }
    
    private void giveTorch(Dungeon dungeon, Player player) {
        Vector3f plloc = player.getLocation();
        ColorRGBA lc = ColorRGBA.Yellow.add(ColorRGBA.White);
        lc.multLocal(0.2f);
        PointLight p1 = new PointLight(plloc, lc);
        p1.setRadius(10f);
        rootnode.addLight(p1);
        
        lc.addLocal(ColorRGBA.Orange.mult(0.4f));
        lc.multLocal(0.025f);
        PointLight p2 = new PointLight(plloc, lc);
        p2.setRadius(30f);
        rootnode.addLight(p2);
        
        player.giveTorch(p1, p2);
    }
    
    
   public BulletAppState getPhysics() {
       return physics;
   }
    
    
    public Main getApplications() {
        return app;
    }
    
    
    /**
     * Returns the player object. 
     * 
     * THIS SEEMS TO BE BROKEN!!! WHATEVER  OBJECT IT 
     * RETURNS IS ***NOT*** THE PLAYER!!!
     * 
     * Damn you broken-a** JME3!
     * 
     * @return player
     */
    public Player getPlayer() {
        return player;
    }
    
    
    public void setPlayerPos(Vector3f pos) {
        playerPos.set(pos);
    }
    
    
    public volatileVec getPlayerPos() {
        return playerPos;
    }
}
