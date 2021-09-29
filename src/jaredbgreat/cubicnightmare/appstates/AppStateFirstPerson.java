package jaredbgreat.cubicnightmare.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector3f;
import jaredbgreat.cubicnightmare.Main;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.DUCK;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.GO_BACKWARD;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.GO_FORWARD;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.GO_LEFT;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.GO_RIGHT;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.JUMP;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.LOOK_DOWN;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.LOOK_UP;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.RUN;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.TO_MENUS;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.TURN_LEFT;
import static jaredbgreat.cubicnightmare.appstates.ControlConstants.TURN_RIGHT;
import jaredbgreat.cubicnightmare.entities.Player;
import jaredbgreat.cubicnightmare.entities.controls.PlayerControl;

/**
 *
 * @author Jared Blackburn
 */
public class AppStateFirstPerson extends BaseAppState implements ActionListener, AnalogListener {  
    Main app;
    Player player;
    PlayerControl control;
    
    
    
    public AppStateFirstPerson(Player player) {
        super();
        this.player = player;
        this.control = player.getControl();
    }

    
    @Override
    protected void initialize(Application ap) {
        app = (Main)ap;
        //app.setDisplayStatView(false);
        //app.setDisplayFps(false);
        nixMyControls();
        setControls(app.getInputManager());
        float ar = (float)app.getContext().getSettings().getWidth() 
                        / (float)app.getContext().getSettings().getHeight();
        app.getCamera().setFrustumPerspective(37.5f * ar, ar, 0.25f, 1000f);
    }
    

    @Override
    protected void onEnable() {}
    
    
    @Override
    public void update(float tpf){}
    
    @Override
    protected void onDisable() {}

    
    @Override
    protected void cleanup(Application ap) {
        unsetControls(app.getInputManager());
    }
    
    
    /*---------------------------------------------------------------------------------*/
    /*                                    CONTROLS                                     */
    /*---------------------------------------------------------------------------------*/
    
    
    protected void setControls(InputManager inputMan) {
        inputMan.addMapping(GO_FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputMan.addMapping(GO_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputMan.addMapping(GO_LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputMan.addMapping(GO_RIGHT, new KeyTrigger(KeyInput.KEY_D));
        
        inputMan.addMapping(TURN_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputMan.addMapping(TURN_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputMan.addMapping(LOOK_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));
        inputMan.addMapping(LOOK_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));
        
        inputMan.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE)); 
        inputMan.addMapping(DUCK, new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputMan.addMapping(RUN, new KeyTrigger(KeyInput.KEY_LCONTROL));
        
        inputMan.addMapping(TO_MENUS, new KeyTrigger(KeyInput.KEY_ESCAPE));
        
        inputMan.addListener(this, ControlConstants.MAPPINGS);
    }
    
    
    
    
    protected void unsetControls(InputManager inputMan) {
        inputMan.deleteMapping(GO_FORWARD);
        inputMan.deleteMapping(GO_BACKWARD);
        inputMan.deleteMapping(GO_LEFT);
        inputMan.deleteMapping(GO_RIGHT);
        
        inputMan.deleteMapping(TURN_LEFT);
        inputMan.deleteMapping(TURN_RIGHT);
        inputMan.deleteMapping(LOOK_UP);
        inputMan.deleteMapping(LOOK_DOWN);
        
        inputMan.deleteMapping(JUMP);
        inputMan.removeListener(this);
    }

    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(null != name) switch (name) {
            case ControlConstants.JUMP:
                control.jump();
                break;
            case ControlConstants.DUCK:
                control.setCrouch(isPressed);
                break;
            case ControlConstants.RUN:
                control.setWalk(isPressed);
                break;
            case ControlConstants.TO_MENUS:
                app.endGame();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {  
        if(null != name) switch (name) {
            case ControlConstants.GO_FORWARD:
                control.setMoveForward(tpf * value);
                break;
            case ControlConstants.GO_BACKWARD:
                control.setMoveBackward(tpf * value);
                break;
            case ControlConstants.GO_RIGHT:
                control.setMoveRight(tpf * value);
                break;
            case ControlConstants.GO_LEFT:
                control.setMoveLeft(tpf * value);
                break;
            case ControlConstants.TURN_RIGHT:
                control.setTurnRght(tpf * value);
                break;            
            case ControlConstants.TURN_LEFT:
                control.setTurnLeft(tpf * value);
                break;
            case ControlConstants.LOOK_UP:
                control.setLookUp(tpf * value);
                break;
            case ControlConstants.LOOK_DOWN:
                control.setLookDown(tpf * value);
                break;
            default:
                break;
        }
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
        //inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.removeListener(app.getFlyByCamera());
        app.getCamera().setLocation(new Vector3f(0f, 0.5f, 0f));
    }
    
}
