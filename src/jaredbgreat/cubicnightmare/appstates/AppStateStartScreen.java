package jaredbgreat.cubicnightmare.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import jaredbgreat.boulders.util.MaterialMaker;
import jaredbgreat.cubicnightmare.Main;
import jaredbgreat.gui.ButtonList;
import jaredbgreat.gui.CommandFinder;
import jaredbgreat.gui.CommandNode;
import jaredbgreat.gui.IGuiCommand;

/**
 *
 * @author Jared Blackburn
 */
public class AppStateStartScreen extends BaseAppState implements ActionListener {
    private static final String CLICK_GUI = "clickGui";
    private static final Trigger LEFT_CLICK 
            = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private static final Trigger RIGHT_CLICK 
            = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
    ButtonList difflist;
    CommandFinder interpreter;
    Main app;

    @Override
    protected void initialize(Application ap) {    
        app = (Main)ap;
        interpreter = new CommandFinder(app);
    }

    
    @Override
    protected void onEnable() {  
        nixMyControls(); // Game wide, does not need reversing
        app.getFlyByCamera().setDragToRotate(true);
        
        int h = app.getContext().getSettings().getHeight();
        int w = app.getContext().getSettings().getWidth();
        
        int mh = Math.min(((h - 96) / 2) + 64, (h / 2) - 96);
        Mesh backquad = new Quad(w, h);
        Geometry background = new Geometry("background", backquad);
        Material mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), 
                "Interface/MenuTitle.png");
        background.setMaterial(mat);
        app.getGuiNode().attachChild(background);
        
        addMainButtons(h, w, mh);
        addDifficultyButtons(h, w);
        
        app.getInputManager().addMapping(CLICK_GUI, LEFT_CLICK);
        app.getInputManager().addListener(this, CLICK_GUI);
    }

    
    @Override
    protected void onDisable() {
        app.getInputManager().removeListener(this);
        app.getGuiNode().detachAllChildren();
        app.getFlyByCamera().setDragToRotate(false);
    }
        

    @Override
    protected void cleanup(Application ap) {    
        app.getInputManager().deleteMapping(CLICK_GUI);
    }
    

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if((name == CLICK_GUI) && !isPressed) {
            interpreter.processClick();
        }
    }
    
    
    public EDifficulty getDifficulty() {
        return EDifficulty.setCurrent(difflist.getSelected());
    }
    
    
    private void addMainButtons(int h, int w, int mh) {
        Material mat;
        
        CommandNode startNode = new CommandNode(new IGuiCommand() {
            @Override
            public void execute() {
                app.startGame();
            }            
        });        
        startNode.setLocalTranslation((w - 224) / 2, mh + 64, 1);
        app.getGuiNode().attachChild(startNode);
        Mesh startquad = new Quad(224, 96);
        Geometry startButton = new Geometry("startButton", startquad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/StartGame.png");
        startButton.setMaterial(mat);
        startNode.attachChild(startButton);
        
        CommandNode quitNode = new CommandNode(new IGuiCommand() {
            @Override
            public void execute() {
                app.stop();
            }            
        });        
        quitNode.setLocalTranslation((w - 224) / 2, mh - 64, 1);
        app.getGuiNode().attachChild(quitNode);
        Mesh quitquad = new Quad(224, 96);
        Geometry quitButton = new Geometry("quitButton", quitquad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/QuitGame.png");
        quitButton.setMaterial(mat);
        quitNode.attachChild(quitButton);        
    }
    
    
    private void addDifficultyButtons(int h1, int w1) {
        difflist = new ButtonList();
        Node gui = app.getGuiNode();
        int bw = Math.min((w1 / 2) - 112, 256);
        int bh = (72 * bw) / 256;
        int w = w1 - bw;
        int h = Math.min((h1 / 2) + bh, (h1 / 2));
        Material mat;
        Geometry bon, boff;
        Mesh quad;
        Node node;
        
        quad = new Quad(bw, bh);
        bon = new Geometry("lookingOn", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Looking-on.png");
        bon.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        quad = new Quad(bw, bh);        
        boff = new Geometry("lookingOff", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Looking-off.png");
        boff.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        difflist.addButton(bon, boff);
        node = difflist.getNode(0);
        node.setLocalTranslation(w, h, 1);
        gui.attachChild(node);
        h -= bh;
        
        quad = new Quad(bw, bh);
        bon = new Geometry("EasyOn", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Easy-on.png");
        bon.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        quad = new Quad(bw, bh);        
        boff = new Geometry("EasyOff", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Easy-off.png");
        boff.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        difflist.addButton(bon, boff);
        node = difflist.getNode(1);
        node.setLocalTranslation(w, h, 1);
        gui.attachChild(node);
        h -= bh;
        
        quad = new Quad(bw, bh);
        bon = new Geometry("normalOn", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Normal-on.png");
        bon.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        quad = new Quad(bw, bh);        
        boff = new Geometry("normalOff", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Normal-off.png");
        boff.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        difflist.addButton(bon, boff);
        node = difflist.getNode(2);
        node.setLocalTranslation(w, h, 1);
        gui.attachChild(node);
        h -= bh;
        
        quad = new Quad(bw, bh);
        bon = new Geometry("hardOn", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Hard-on.png");
        bon.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        quad = new Quad(bw, bh);        
        boff = new Geometry("hardOff", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Hard-off.png");
        boff.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        difflist.addButton(bon, boff);
        node = difflist.getNode(3);
        node.setLocalTranslation(w, h, 1);
        gui.attachChild(node);
        h -= bh;
        
        quad = new Quad(bw, bh);
        bon = new Geometry("horrorOn", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Horror-on.png");
        bon.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        quad = new Quad(bw, bh);        
        boff = new Geometry("horrorOff", quad);
        mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), "Interface/Horror-off.png");
        boff.setMaterial(mat);
        bon.setLocalTranslation(Vector3f.ZERO);
        difflist.addButton(bon, boff);
        node = difflist.getNode(4);
        node.setLocalTranslation(w, h, 1);
        gui.attachChild(node);
        
        difflist.setSelected(2);
    }
    
    
    
    private void nixMyControls() {
        InputManager inputManager = app.getInputManager();
        inputManager.deleteTrigger(CameraInput.FLYCAM_RISE, new KeyTrigger(KeyInput.KEY_Q));
        inputManager.deleteTrigger(CameraInput.FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_Z));
        inputManager.deleteTrigger(CameraInput.FLYCAM_FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.deleteTrigger(CameraInput.FLYCAM_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.deleteTrigger(CameraInput.FLYCAM_STRAFELEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.deleteTrigger(CameraInput.FLYCAM_STRAFERIGHT, new KeyTrigger(KeyInput.KEY_D));
        inputManager.deleteTrigger(CameraInput.FLYCAM_LEFT, 
                new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.deleteTrigger(CameraInput.FLYCAM_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.deleteTrigger(CameraInput.FLYCAM_RIGHT, 
                new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.deleteTrigger(CameraInput.FLYCAM_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.deleteTrigger(CameraInput.FLYCAM_DOWN, 
                new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.deleteTrigger(CameraInput.FLYCAM_UP, new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.deleteTrigger(CameraInput.FLYCAM_UP, 
                new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.deleteTrigger(CameraInput.FLYCAM_UP, new KeyTrigger(KeyInput.KEY_UP));
        inputManager.deleteTrigger(CameraInput.FLYCAM_ZOOMIN, 
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.deleteTrigger(CameraInput.FLYCAM_ZOOMOUT, 
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)); 
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.removeListener(app.getFlyByCamera());
        app.getCamera().setLocation(new Vector3f(0f, 0.5f, 0f));
    }
    
}
