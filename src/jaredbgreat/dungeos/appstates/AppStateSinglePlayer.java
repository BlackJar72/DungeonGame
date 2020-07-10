package jaredbgreat.dungeos.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import jaredbgreat.dungeos.Main;
import jaredbgreat.dungeos.componenet.GeomorphManager;
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

    
    @Override
    protected void initialize(Application ap) {        
        app = (Main)ap;
        physics = new BulletAppState();
        app.getStateManager().attach(physics);
        assetman = app.getAssetManager();  
    }

    
    @Override
    protected void onEnable() {}

    
    @Override
    protected void onDisable() {}

    
    @Override
    protected void cleanup(Application app) {}
    
    
    
}
