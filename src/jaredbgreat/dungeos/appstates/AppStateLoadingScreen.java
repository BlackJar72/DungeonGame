/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.dungeos.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Quad;
import jaredbgreat.boulders.util.MaterialMaker;
import jaredbgreat.dungeos.Main;

/**
 *
 * @author Jared Blackburn
 */
public class AppStateLoadingScreen extends BaseAppState  {
    Main app;
    Geometry background;

    @Override
    protected void initialize(Application ap) {   
        app = (Main)ap;
    }

    @Override
    protected void onEnable() {
        if(background == null) {
            int h = app.getContext().getSettings().getHeight();
            int w = app.getContext().getSettings().getWidth();        int mh = Math.min(((h - 96) / 2) + 64, (h / 2) - 96);
            Mesh backquad = new Quad(w, h);
            background = new Geometry("background", backquad);
            Material mat = MaterialMaker.makeGuiMaterial(app.getAssetManager(), 
                    "Interface/bricks-dark.png");
            background.setMaterial(mat);
        }
        app.getGuiNode().attachChild(background);
    }

    @Override
    protected void onDisable() {
        app.getGuiNode().detachChild(background);
    }

    @Override
    protected void cleanup(Application aplctn) {}
    
}
