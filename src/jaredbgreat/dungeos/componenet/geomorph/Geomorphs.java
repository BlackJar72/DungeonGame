package jaredbgreat.dungeos.componenet.geomorph;

/**
 *
 * @author Jared Blacburn
 */
public class Geomorphs {
    public static final GeomorphRegistry REGISTRY = new GeomorphRegistry();
    public static final Geomorphs MORPHS = new Geomorphs();
    static final GeomorphRegistry FLOORS   = new GeomorphRegistry();
    static final GeomorphRegistry WALLS    = new GeomorphRegistry();
    static final GeomorphRegistry CIELINGS = new GeomorphRegistry();
    
    
    SimpleGeomorph simpleFloor = makeGeomorph("SimpleFloor", 
            "Models/geomorphs/simple/Geomorph-blank-floor", 16)
            .setMaterials("Floor:Textures/stone-hr.png", "Wall:Textures/stone-hr.png");
    
    
    private Geomorphs() {
        register(simpleFloor);
    }
    
    
    private SimpleGeomorph makeGeomorph(String name, String rlbase, int num) {
        GeomorphModel[] models = new GeomorphModel[num];
        for(int i = 0; i < models.length; i++) {
            models[i] = new GeomorphModel(name, 
                rlbase + makePaddedInt(i, 2) + ".j3o");
        }
        return new SimpleGeomorph(name, models);
    }
    
    
    private SimpleGeomorph register(SimpleGeomorph geo) {
        REGISTRY.add(geo.name, geo);
        geo.setID(REGISTRY.getID(geo.name));
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
    
}
