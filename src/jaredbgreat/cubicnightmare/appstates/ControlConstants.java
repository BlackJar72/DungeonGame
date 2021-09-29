package jaredbgreat.cubicnightmare.appstates;

/**
 *
 * @author jared
 */
public class ControlConstants {   
    public static final String JUMP = "playerJump";
    public static final String DUCK = "playerDuck";  
    public static final String RUN = "playerRun";    
    public static final String TURN_RIGHT = "playerTurnRight";
    public static final String TURN_LEFT  = "playerTurnLeft";    
    public static final String LOOK_UP    = "playerLookUp";
    public static final String LOOK_DOWN  = "playerLookDown";    
    public static final String GO_RIGHT    = "playerGoRight";
    public static final String GO_LEFT     = "playerGoLeft";    
    public static final String GO_FORWARD  = "playerGoForward";
    public static final String GO_BACKWARD = "playerGoBackward";
    
    public static final String START_GAME = "startGame";
    public static final String END_GAME   = "endGame";
    public static final String GOTO_HELP  = "gotoHelp";
    public static final String GOTO_START = "gotoStart";
    
    public static final String TO_MENUS   = "goToMenus";
    
    public static String[] MAPPINGS = new String[]{
        TURN_LEFT, TURN_RIGHT, LOOK_UP, LOOK_DOWN,
        GO_LEFT, GO_RIGHT, GO_FORWARD, GO_BACKWARD,
        JUMP, DUCK, RUN, TO_MENUS
    };
    
}
