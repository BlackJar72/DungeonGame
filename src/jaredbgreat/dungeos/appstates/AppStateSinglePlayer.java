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
import jaredbgreat.dungeos.mapping.dld.DLDungeon;
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
    Player player; 

    
    @Override
    protected void initialize(Application ap) { 
        app = (Main)ap;
        geomanager = GeomorphManager.manager;
        physics = geomanager.getPhysics();
        assetman = app.getAssetManager();
        rootnode = app.getRootNode();
        phynode = geomanager.getPhysicsNode();
    }

    
    @Override
    protected void onEnable() {
        
        // Create world from map
        //TestMap testmap = new TestMap();
        //testmap.build(); 
        //TestMap testmap = new TestMap();
        //testmap.build(); 
        DLDungeon dungeon = new DLDungeon(geomanager);
                
        //player = new Player(this, phynode, physics);
        player = new Player(this, phynode, physics, dungeon.getPlayerStart());
        app.getStateManager().attach(new AppStateFirstPerson(player));
        //app.getFlyByCamera().setMoveSpeed(10);
                
        // Lastly lights
        addFourPointLight(0.15f);
        addbasicTestLights(dungeon);       
        
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
    
    private void addbasicTestLights(DLDungeon dungeon) {
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
    
    
   
    
    
    public Main getApplications() {
        return app;
    }
    
    
    /**
     * Returns the player object.
     * 
     * @return player
     */
    public Player getPlayer() {
        return player;
    }
    
}
