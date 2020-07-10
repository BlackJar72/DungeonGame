package jaredbgreat.dungeos.componenet.geomorph;

import jaredbgreat.dungeos.util.Registry;

/**
 *
 * @author Jared Blacburn
 */
public class Geomorphs {
    public static final GeomorphRegistry  REGISTRY = new GeomorphRegistry();
    public static final Geomorphs         MORPHS   = new Geomorphs();
    static final Registry<GeomorphModel>  FLOORS   = new Registry<>();
    static final Registry<GeomorphModel>  CIELINGS = new Registry<>();
    static final Registry<SimpleGeomorph> WALLS    = new Registry<>();
    
    
    private Geomorphs() {}
    
    
    public static void init() {
        MORPHS.addGeomorphs();        
    }
    
    
    private SimpleGeomorph makeSimpleGeomorph(String name, String rlbase, int starti, int endi) {
        GeomorphModel[] models = new GeomorphModel[endi];
        for(int i = 0; i < starti; i++) {
            models[i] = GeomorphModel.EMPTY;
        }
        for(int i = starti; i < models.length; i++) {
            models[i] = new GeomorphModel(name, 
                rlbase + makePaddedInt(i, 2) + ".j3o");
        }
        return new SimpleGeomorph(name, models);
    }
    
    
    private IGeomorph register(IGeomorph geo) {
        String name = geo.getName();
        REGISTRY.add(name, geo);
        geo.setID(REGISTRY.getID(name));
        return geo;
    }
    
    
    public int getGeomorphID(String name) {
        return REGISTRY.getID(name);
    }
    
    
    // FIXME: This should really be a static method in a utility class.
    private String makePaddedInt(int number, int digits) {
        String simpleNumber = Integer.toString(number);
        int padding = Math.max(0, digits - simpleNumber.length());
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < padding; i++) {
            builder.append("0");
        }
        builder.append(simpleNumber);
        return builder.toString();        
    }
    
    
    GeomorphModel getFloor(String name) {
        return FLOORS.getFromName(name);
    }
    
    
    GeomorphModel getCieling(String name) {
        return CIELINGS.getFromName(name);
    }
    
    
    SimpleGeomorph getWalls(String name) {
        return WALLS.getFromName(name);
    }
    
    
    private void addFloors() {
        FLOORS.add("SimpleStone", new GeomorphModel("SimpleStone", 
            "Models/geomorphs/floors/Geomorph-blank-floor.j3o")
                .setMaterials("Floor:Textures/stone-hr.png"));
        FLOORS.add("SimpleStoneDark", new GeomorphModel("SimpleStoneDark", 
            "Models/geomorphs/floors/Geomorph-blank-floor.j3o")
                .setMaterials("Floor:Textures/stone-dark-hr.png"));
        FLOORS.add("Concrete", GeomorphModel.makeSimpleFloor("Concrete")
                .setMaterials("Floor:Textures/stone-hr.png"));
    }
    
    
    private void addWalls() {
       WALLS.add("SimpleStone", makeSimpleGeomorph("SimpleFloor", 
            "Models/geomorphs/walls/simple/Geomorph-blank-wall", 1, 20)
            .setMaterials("Wall:Textures/stone-hr.png"));
       WALLS.add("SimpleStoneDark", makeSimpleGeomorph("SimpleFloorDark", 
            "Models/geomorphs/walls/simple/Geomorph-blank-wall", 1, 20)
            .setMaterials("Wall:Textures/stone-hr.png"));
       WALLS.add("RedBrick", makeSimpleGeomorph("RedBrick", 
            "Models/geomorphs/walls/brick/Geomorph-brick-floor", 1, 20));
    }
    
    
    private void addCielings() {
        CIELINGS.add("SimpleStone", new GeomorphModel("SimpleStone", 
            "Models/geomorphs/cielings/Geomorph-blank-ciel.j3o")
                .setMaterials("Floor:Textures/stone-hr.png"));
        CIELINGS.add("SimpleStoneDark", new GeomorphModel("SimpleStoneDark", 
            "Models/geomorphs/cielings/Geomorph-blank-ciel.j3o")
                .setMaterials("Floor:Textures/stone-dark-hr.png"));
        CIELINGS.add("Concrete", GeomorphModel.makeSimpleCieling("Concrete")
                .setMaterials("Cieling:Textures/stone-hr.png"));
        
    }
    
    
    private void addGeomorphs() {
        // First, setup components; these must be in place!
        addFloors();
        addCielings();
        addWalls();
        
        // Then, make the actual geomorphs!
        register(new Geomorph("SimpleStone", "SimpleStone", "SimpleStone", "SimpleStone"));
        register(new Geomorph("BrickNStone", "Concrete", "RedBrick", "Concrete"));
    }
}
