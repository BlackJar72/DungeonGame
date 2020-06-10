package jaredbgreat.dungeos.componenet.geomorph;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This is a replacement for the original Geomorph classs to better
 * and more flexibly handle combinations of elements (notable floor 
 * wall combos, but maybe more later.
 * 
 * @author Jared Blackburn
 */
public class Geomorph implements IGeomorph {
    int id;
    String name;
    private final GeomorphModel  floor;
    private final SimpleGeomorph walls;
    private final GeomorphModel  cieling;
    
    
    public Geomorph(String name, GeomorphModel floor, SimpleGeomorph walls, GeomorphModel cieling) {
        this.name    = name;
        this.floor   = floor;
        this.walls   = walls;
        this.cieling = cieling;
    }
    
    
    public Geomorph(String name, String floor, String walls, String cieling) {
        this.name    = name;
        this.floor   = Geomorphs.MORPHS.getFloor(floor);
        this.walls   = Geomorphs.MORPHS.getWalls(walls);
        this.cieling = Geomorphs.MORPHS.getCieling(cieling);
    }
    
    
    @Override
    public IGeomorph setID(int id) {
        this.id = id;
        return this;
    }
    

    @Deprecated
    @Override
    /**
     * This is deprected; you should not use this method for 
     * compound geomorphs (represented by this class), but 
     * instead set it on each component separately before 
     * combining them.
     */
    public Geomorph setMaterials(String... mats) {
        floor.setMaterials(mats);
        walls.setMaterials(mats);
        cieling.setMaterials(mats);
        return this;
    }
    
    
    @Override
    public Node getModel(int variant) {
        System.out.println(variant + " -> " + (variant & 31));
        Node out = new Node();
        if((variant & 0x31) > 0) {
            out.attachChild(walls.getVariant(variant & 31).template.clone(true));
        }
        if((variant & 0x20) > 0) {
            out.attachChild(floor.template.clone(true));
        }
        if((variant & 0x40) > 0) {
            out.attachChild(cieling.template.clone());
        }
        return out;
    }
    

    @Override
    public Node makeSpatialAt(int variant, float x, float y, float z) {
        Node out = getModel(variant);
        out.setLocalTranslation(x, y, z);
        return out;
    }

    
    @Override
    public Node makeSpatialAt(int variant, Vector3f location) {
        Node out = getModel(variant);
        out.setLocalTranslation(location);
        return out;
    }
        
    
    @Override
    public String getName() {
        return name;
    }
    
}
