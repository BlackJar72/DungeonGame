/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.cubicnightmare.mapping;

import com.jme3.scene.Spatial;
import jaredbgreat.cubicnightmare.componenent.GeomorphManager;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import static jaredbgreat.cubicnightmare.mapping.AbstractMap.geoman;

/**
 *
 * @author jared
 */
public interface ILevelMap {
    
    
    /**
     * A basic map building method; that this may be overridden 
     * to extend features is expects, however this provides the 
     * basics.  By basics I mean it places floors and wall, but 
     * not other objects (such as decorations).
     */
    // TODO: Handle wall endcaps.
    public void build();
    
}
