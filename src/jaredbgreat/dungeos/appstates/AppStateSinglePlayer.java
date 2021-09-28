package jaredbgreat.dungeos.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.dungeos.Main;
import jaredbgreat.dungeos.componenent.GeomorphManager;
import jaredbgreat.dungeos.componenent.geomorph.Geomorphs;
import jaredbgreat.dungeos.entities.Player;
import jaredbgreat.dungeos.mapping.dld.Dungeon;
import java.util.ArrayList;
import java.util.List;
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
    volatile VolatileVec playerPos;    
    EDifficulty difficulty;
    
    BitmapFont font;
    BitmapFont bigfont;
    BitmapText healthtxt;
    StringBuilder healthstr;
    Dungeon dungeon;
    static final List<Light> LIGHTS = new ArrayList<>();
    Spatial startMarker, finishMarker;
    
    
    public static final class VolatileVec {
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
    
    
    public static final class FinalVec {
        public final float x, y, z;
        public FinalVec(float nx, float ny, float nz) {
            x = nx; y = ny; z = nz;
        }
        public FinalVec(Vector3f v) {
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
        playerPos = new VolatileVec();
    }

    
    @Override
    protected void onEnable() {
        difficulty = app.getDifficulty();
        dungeon = new Dungeon(this, geomanager);
        player = new Player(this, phynode, physics, dungeon.getPlayerStart());
        app.getStateManager().attach(new AppStateFirstPerson(player));
                
        setupTexts();
        
        // Lastly lights
        if(difficulty.alight > 0) {
            addFourPointLight(difficulty.alight);
        }
        if(difficulty.tbright > 0) {
            giveTorch(dungeon, player); 
        }
        addStartEndMarks(dungeon);
    }
    
    
    @Override
    public void update(float tpf) {
        if(player.getLocation().distanceSquared(dungeon.getLevelEndSpot()) < 0.707106781187f) {
            app.endGame();
        }
    }

    
    @Override
    protected void onDisable() {}
    
    
    
    @Override
    protected void cleanup(Application app) {}
    
    
    public void clearDungeon() {
        dungeon.clear();
        dungeon = null;
        removeAllLights();
    }
    
    
    private void setupTexts() {        
        font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");        
        bigfont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        healthtxt = new BitmapText(font);
        
        healthtxt.setText(healthstr = new StringBuilder("Health: 20"));
        healthtxt.setSize(font.getCharSet().getRenderedSize() * 2);
        healthtxt.move((app.getContext().getSettings().getWidth() 
                              - healthtxt.getLineWidth()) / 2, 
                      app.getContext().getSettings().getHeight() 
                              - healthtxt.getLineHeight() * 2,
                      0);
        app.getGuiNode().attachChild(healthtxt);
    }
    
    
    public void addLight(Light l) {        
        rootnode.addLight(l);
        LIGHTS.add(l);
    }
    
    
    public void removeAllLights() {
        for(Light l : LIGHTS) {
            rootnode.removeLight(l);
        }
        LIGHTS.clear();
        rootnode.detachChild(startMarker);        
        startMarker.removeFromParent();
        rootnode.detachChild(finishMarker);
        finishMarker.removeFromParent();
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
        addLight(p1);
        addLight(p2);
        addLight(p3);
        addLight(p4);        
    }
    
    
    private void addStartEndMarks(Dungeon dungeon) {
        ColorRGBA ec = ColorRGBA.White.add(ColorRGBA.Orange).add(ColorRGBA.Yellow);
        Vector3f plloc = dungeon.getLevelEndSpot();
        PointLight pLight = new PointLight(plloc.add(Vector3f.UNIT_Y), 
                (ColorRGBA.Red.mult(0.25f).add(ec.mult(0.25f)).mult(difficulty.genericFactor)));
        finishMarker = Geomorphs.getDecor().getFromName("EndPad").getSpatial();
        finishMarker.setLocalTranslation(plloc);
        GeomorphManager.manager.attachSpatial(finishMarker);
        pLight.setRadius(10);
        addLight(pLight);
        
        plloc = dungeon.getPlayerStart();
        pLight = new PointLight(plloc.add(Vector3f.UNIT_Y), 
                (ColorRGBA.Blue.add(ColorRGBA.White).mult(0.5f)).mult(difficulty.genericFactor));
        startMarker = Geomorphs.getDecor().getFromName("StartPad").getSpatial();
        startMarker.setLocalTranslation(plloc);
        GeomorphManager.manager.attachSpatial(startMarker);
        rootnode.attachChild(startMarker);
        pLight.setRadius(10);
        addLight(pLight);
    }
    
    
    private void giveTorch(Dungeon dungeon, Player player) {
        Vector3f plloc = player.getLocation();
        ColorRGBA lc = ColorRGBA.Yellow.add(ColorRGBA.White);
        lc.multLocal(difficulty.tbright);
        PointLight p1 = new PointLight(plloc, lc);
        p1.setRadius(difficulty.tmax1);
        addLight(p1);
        
        lc.addLocal(ColorRGBA.Orange.mult(0.4f));
        lc.multLocal(Math.max(difficulty.alight, 0.025f));
        PointLight p2 = new PointLight(plloc, lc);
        p2.setRadius(difficulty.tmax2);
        addLight(p2);
        
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
    
    
    public VolatileVec getPlayerPos() {
        return playerPos;
    }
    
    
    public void hurtPlayer() {
        int hp = player.beHurt();
        healthstr.delete(8, Integer.MAX_VALUE);
        healthstr.append(hp);
        healthtxt.setText(healthstr);
    }
}
