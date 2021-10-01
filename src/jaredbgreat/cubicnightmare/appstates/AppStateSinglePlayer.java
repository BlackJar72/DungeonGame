package jaredbgreat.cubicnightmare.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import jaredbgreat.boulders.util.MaterialMaker;
import jaredbgreat.cubicnightmare.Main;
import jaredbgreat.cubicnightmare.componenent.GeomorphManager;
import jaredbgreat.cubicnightmare.componenent.geomorph.GeomorphModel;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.entities.Player;
import jaredbgreat.cubicnightmare.mapping.dld.Dungeon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author jared
 */
public class AppStateSinglePlayer extends BaseAppState {
    private static final int TO_WIN = 12;
    GeomorphManager geomanager;
    AssetManager assetman;
    BaseAppState controls;
    BulletAppState physics;
    Node phynode;
    AudioNode[] sounds;
    Random random;
    Main app;
    Node rootnode; 
    
    Player player; 
    volatile VolatileVec playerPos;    
    EDifficulty difficulty;
    
    Geometry hud;
    BitmapFont font;
    BitmapFont smallfont;
    BitmapFont bigfont;
    BitmapText healthtxt;
    BitmapText leveltxt;
    BitmapText scoretxt;
    BitmapText deathtxt;
    BitmapText wintxt;
    StringBuilder healthstr;
    StringBuilder levelstr;
    StringBuilder scorestr;
    
    Dungeon dungeon;
    static final List<Light> LIGHTS = new ArrayList<>();
    Spatial startMarker, finishMarker;
    long specialTimer;
    boolean gameOver;
    boolean endless;
    int level;
        
    
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
        level = 1;
        gameOver = false;
        difficulty = app.getDifficulty();
        dungeon = new Dungeon(this, geomanager, level);
        player = new Player(this, phynode, physics, 
                dungeon.getPlayerStart().add(new Vector3f(0f, 0.2f, 0f)));
        getPhysics().getPhysicsSpace().addCollisionListener(player.getControl());
        app.getStateManager().attach(new AppStateFirstPerson(player));
        defineSounds();
                
        setupTexts();
        
        addBedroom(dungeon.getPlayerStart());
        
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
        if(!gameOver && (player.getLocation().distanceSquared(dungeon.getLevelEndSpot()) < 0.707106781187f)) {
            nextLevel();
        } else if(gameOver && System.currentTimeMillis() > specialTimer) {
            app.endGame();
        }
    }
    
    public void declareDefeat() {
        Node gui = app.getGuiNode();
        healthstr.delete(8, Integer.MAX_VALUE);
        healthstr.append(0);
        healthtxt.setText(healthstr);
        gui.attachChild(deathtxt);
        gameOver = true;
        long now = System.currentTimeMillis();
        specialTimer = now + 1500;
        if(now > specialTimer) {
            app.endGame();
        }
    }
    
    public void declareVictory() {
        gameOver = true;
        Node gui = app.getGuiNode();
        gui.attachChild(wintxt);
        dungeon.removeMobs();
        player.win();
        gameOver = true;
        long now = System.currentTimeMillis();
        specialTimer = now + 2500;
        if(now > specialTimer) {
            app.endGame();
        }
    }

    
    @Override
    protected void onDisable() {}    
    
    
    @Override
    protected void cleanup(Application app) {}
    
    
    public void nextLevel() {
        level++;
        if(level > TO_WIN) {
            player.addScore(100);
            declareVictory();
        } else {
            player.getControl().walkingOff();
            playSound(0);
            clearDungeon();        
            dungeon = new Dungeon(this, geomanager, level);
            player.movePlayer(dungeon.getPlayerStart().add(new Vector3f(0f, 0.2f, 0f)));
            addStartEndMarks(dungeon);   
            // Lastly lights
            if(difficulty.alight > 0) {
                addFourPointLight(difficulty.alight);
            }
            if(difficulty.tbright > 0) {
                giveTorch(dungeon, player); 
            }
            levelstr.delete(7, Integer.MAX_VALUE);
            levelstr.append(level);
            leveltxt.setText(levelstr);
            player.addScore(100);
        }
    }
    
    
    public void updateScore(int score) {
        scorestr.delete(7, Integer.MAX_VALUE);
        scorestr.append(score);
        scoretxt.setText(scorestr);
    }
    
    
    public void clearDungeon() {
        dungeon.clear();
        dungeon = null;
        removeAllLights();
    }
    
    
    public EDifficulty getDifficulty() {
        return difficulty;
    }
    
    
    private void setupTexts() {        
        font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt"); 
        smallfont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");       
        bigfont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        healthtxt = new BitmapText(font);
        deathtxt = new BitmapText(bigfont);
        wintxt = new BitmapText(bigfont);
        leveltxt = new BitmapText(smallfont);
        scoretxt = new BitmapText(smallfont);
        
        int screenWidth = app.getContext().getSettings().getWidth();
        int screenHeight = app.getContext().getSettings().getHeight();
        int hudHeight = screenHeight  / 10;
        
        Mesh hudquad = new Quad(screenWidth, hudHeight);
        hud = new Geometry("hud", hudquad);
        Material mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), 
                "Interface/CNHud.png");
        hud.setMaterial(mat);
        app.getGuiNode().attachChild(hud);
        
        healthtxt.setText(healthstr = new StringBuilder("Health: 20"));
        healthtxt.setSize(Math.min(font.getCharSet().getRenderedSize() 
                * 2.5f, screenHeight / 20f));
        healthtxt.move((screenWidth - healthtxt.getLineWidth()) / 2, 
                       (hudHeight + healthtxt.getLineHeight()) / 2, 0);
        app.getGuiNode().attachChild(healthtxt); 
        
        leveltxt.setText(levelstr = new StringBuilder("Level: " + level));
        leveltxt.setSize(Math.min(smallfont.getCharSet().getRenderedSize() 
                * 2f, screenHeight / 25f));
        leveltxt.move((screenWidth - leveltxt.getLineWidth()) / 10, 
                       (hudHeight + leveltxt.getLineHeight()) / 2, 0);
        app.getGuiNode().attachChild(leveltxt);   
        
        scoretxt.setText("Score: 00000");
        scoretxt.setSize(Math.min(smallfont.getCharSet().getRenderedSize() 
                * 2f, screenHeight / 25f));
        scoretxt.move((screenWidth * 0.9f)  - scoretxt.getLineWidth(), 
                       (hudHeight + scoretxt.getLineHeight()) / 2, 0);
        scoretxt.setText(scorestr = new StringBuilder("Score: 0"));
        app.getGuiNode().attachChild(scoretxt); 
        
        deathtxt.setText("You Died!");
        deathtxt.setSize(bigfont.getCharSet().getRenderedSize() * 4);
        deathtxt.move((app.getContext().getSettings().getWidth() 
                              - deathtxt.getLineWidth()) / 2, 
                      app.getContext().getSettings().getHeight() 
                              - deathtxt.getLineHeight() * 2,
                      0); 
        
        wintxt.setText("You escapaed!\n   You Won!");
        wintxt.setSize(bigfont.getCharSet().getRenderedSize() * 4);
        wintxt.move((app.getContext().getSettings().getWidth() 
                              - wintxt.getLineWidth()) / 2, 
                      app.getContext().getSettings().getHeight() 
                              - wintxt.getLineHeight() * 2,
                      0);        
    }
    
    
    public void addLight(Light l) {        
        rootnode.addLight(l);
        LIGHTS.add(l);
    }
    
    
    private void addBedroom(Vector3f center) {        
        GeomorphModel bedroom = new GeomorphModel("test", 
                "Models/test/MyIRLBedroom/DetailBedroom-furnished.glb");
        bedroom.rotateTableScene();
        GeomorphManager.manager
                .attachSpatial(bedroom.makeSpatialAt(center.x, center.y + 0.01f, center.z - 3f)
                        .rotate(0, FastMath.PI, 0));
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
    
    
    private void defineSounds() {
        AudioNode tp   = makeSound(assetman, "Sounds/Spawn.wav");
        setSounds(tp);
    }
    
    
    private AudioNode makeSound(AssetManager assetman, String location) {
        AudioNode out = new AudioNode(assetman, location, AudioData.DataType.Buffer);
        out.setPositional(true);
        out.setLooping(false);
        out.setVolume(5);
        player.getSpatial().getParent().attachChild(out);
        return out;
    }
    
    
    public void setSounds(AudioNode... audio) {
        sounds = audio;
    }
    
    
    public void playSound(int i) {
        sounds[i].setLocalTranslation(player.getLocation());
        sounds[i].play(); // playInstance()?
    }
    
    
    public void playSound(int i, float loud) {
        sounds[i].setLocalTranslation(player.getLocation());
        sounds[i].setVolume(loud);
        sounds[i].play(); // playInstance()?
    }
    
    
    public void playSoundAt(int i, Vector3f location) {
        sounds[i].setLocalTranslation(location);
        sounds[i].play(); // playInstance()?
    }
    
    
    public void playSoundAt(int i, float loud, Vector3f location) {
        sounds[i].setLocalTranslation(location);
        sounds[i].setVolume(loud);
        sounds[i].play(); // playInstance()?
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
