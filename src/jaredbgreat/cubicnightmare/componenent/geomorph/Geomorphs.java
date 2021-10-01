package jaredbgreat.cubicnightmare.componenent.geomorph;

import jaredbgreat.cubicnightmare.util.Registry;
import java.util.Random;

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
    static final Registry<GeomorphModel>  DECOR    = new Registry<>();
    
    
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
    
    
    private SimpleGeomorph makeSimpleGeomorph(String name, String rlbase, String ext, int starti, int endi) {
        GeomorphModel[] models = new GeomorphModel[endi];
        for(int i = 0; i < starti; i++) {
            models[i] = GeomorphModel.EMPTY;
        }
        for(int i = starti; i < models.length; i++) {
            models[i] = new GeomorphModel(name, 
                rlbase + makePaddedInt(i, 2) + ext);
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
    
    
    GeomorphModel getDecor(String name) {
        return DECOR.getFromName(name);
    }
    
    
    /**
     * Methods calls to add decor models.
     */
    private void addDecor() {
        // Gameplay objects 
        DECOR.add("StartPad", new GeomorphModel("StartPad", 
            "Models/geomorphs/gameplay/StartPad.glb")); 
        DECOR.add("EndPad", new GeomorphModel("EndPad", 
            "Models/geomorphs/gameplay/EndPad.glb"));
        DECOR.add("BookStand", new GeomorphModel("BookStand", 
            "Models/geomorphs/gameplay/ItemStandwBook.glb"));
    }
    
    
    /**
     * Methods calls to add floor sections.
     */
    private void addFloors() {
        // Basic and unworked stone 
        FLOORS.add("SimpleStone", new GeomorphModel("SimpleStone", 
            "Models/geomorphs/floors/Geomorph-blank-floor.j3o")
                .setMaterials("Floor:Textures/stone-hr.png"));
        FLOORS.add("SimpleStoneDark", new GeomorphModel("SimpleStoneDark", 
            "Models/geomorphs/floors/Geomorph-blank-floor.j3o")
                .setMaterials("Floor:Textures/stone-dark-hr.png"));
        FLOORS.add("SimpleDarkStone", new GeomorphModel("SimpleDarkStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/darkstone-hr.png:Textures/NoiseNormal.png"));
        FLOORS.add("SimpleGneiss", new GeomorphModel("SimpleGneiss", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/gneiss-hr.jpg:Textures/NoiseNormal.png"));
        FLOORS.add("SimpleMudstone", new GeomorphModel("SimpleMudstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/mudstone.png:Textures/NoiseNormal.png"));
        FLOORS.add("CobbleStone", new GeomorphModel("CobbleStone", 
            "Models/geomorphs/floors/Geomorph-cobble-nml-floor.glb"));
        
        // Marble
        FLOORS.add("SimpleMarble", new GeomorphModel("SimpleMarble", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/marble.png"));
        FLOORS.add("SimpleMarbleBlack", new GeomorphModel("SimpleMarbleBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/marble-black.png"));
        
        //Granites
        FLOORS.add("SimpleGraniteBlack", new GeomorphModel("SimpleGraniteBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/granite-black-hr.jpg:Textures/NoiseNormal.png"));
        FLOORS.add("SimpleGraniteGreen", new GeomorphModel("SimpleGraniteGreen", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/granite-green-hr.jpg"));
        FLOORS.add("SimpleFancyStone", new GeomorphModel("SimpleFancyStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/FancyStone-01-hr.jpg:Textures/NoiseNormal.png"));
        FLOORS.add("SimpleFancyStoneLt", new GeomorphModel("SimpleFancyStoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/FancyStone-01-lt-hr.jpg"));
        
        //Sandstones
        FLOORS.add("SimpleSandstoneLt", new GeomorphModel("SimpleSandstoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/Sandstone-lt-hr.png:Textures/NoiseNormal.png"));
        FLOORS.add("SimpleSandstone", new GeomorphModel("SimpleSandstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/Sandstone-hr.png:Textures/NoiseNormal.png"));
        FLOORS.add("SimpleSandstoneRed", new GeomorphModel("SimpleSandstoneRed", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/Sandstone-red-hr.jpg:Textures/NoiseNormal.png"));
        
        //Smooth Sandstones
        FLOORS.add("SmoothSandstoneLt", new GeomorphModel("SimpleSandstoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/Sandstone-lt-hr.png"));
        FLOORS.add("SmoothSandstone", new GeomorphModel("SimpleSandstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/Sandstone-hr.png"));
        FLOORS.add("SmoothSandstoneRed", new GeomorphModel("SimpleSandstoneRed", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/Sandstone-red-hr.jpg"));
        
        //Exotic Stone
        FLOORS.add("SimpleBlueStone", new GeomorphModel("SimpleBlueStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/blueball.png"));
        FLOORS.add("SimpleLapisLazuli", new GeomorphModel("SimpleLapisLazuli", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/BlueStone.png"));
        FLOORS.add("SimpleRedBlack", new GeomorphModel("SimpleRedBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/redball.png:Textures/NoiseNormal.png"));
        
        
        // Concrete
        FLOORS.add("Concrete01", new GeomorphModel("Concrete01", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/concrete-01.png"));
        FLOORS.add("Concrete03", new GeomorphModel("Concrete03", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/concrete-03.png"));
        FLOORS.add("Concrete04", new GeomorphModel("Concrete04", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/concrete-04.png"));
        
        // Non-Stone
        FLOORS.add("SimpleDirt", new GeomorphModel("SimpleDirt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/dirt-01.png"));
        FLOORS.add("SimpleOldWood", new GeomorphModel("SimpleOldWood", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterialsNm("Floor:Textures/oldwood-01b-sm.png:Textures/oldwood-01b-sm-nml.png"));
        FLOORS.add("SimpleCarpet01", new GeomorphModel("SimpleCarpet01", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/Carpet-01.png"));
        FLOORS.add("SimpleCarpet02", new GeomorphModel("SimpleCarpet02", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/carpet-02.png"));
        FLOORS.add("SimpleGrassGreen", new GeomorphModel("SimpleGrassGreen", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/grass-02b.png"));
        FLOORS.add("SimpleGrassBrown", new GeomorphModel("SimpleGrassBrown", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-floor.gltf")
                .setMaterials("Floor:Textures/grass-02c.png"));
        
    }
    
    
    /**
     * Methods calls to add Wall sections.
     */
    private void addWalls() {
        // Basic and unworked stone
       WALLS.add("SimpleStone", makeSimpleGeomorph("SimpleFloor", 
            "Models/geomorphs/walls/simple/Geomorph-blank-wall", 1, 20)
            .setMaterials("Wall:Textures/stone-hr.png"));
       WALLS.add("SimpleStoneDark", makeSimpleGeomorph("SimpleFloorDark", 
            "Models/geomorphs/walls/simple/Geomorph-blank-wall", 1, 20)
            .setMaterials("Wall:Textures/stone-dark-hr.png"));
       WALLS.add("SimpleDarkStone", makeSimpleGeomorph("SimpleDarkStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/darkstone-hr.png"));
       WALLS.add("SimpleGneiss", makeSimpleGeomorph("SimpleGneiss", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/gneiss-hr.jpg"));
       WALLS.add("SimpleMudstone", makeSimpleGeomorph("SimpleMudstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/mudstone.png"));
       
        // Marble
       WALLS.add("SimpleMarble", makeSimpleGeomorph("SimpleFloorMarble", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/marble.png"));
       WALLS.add("SimpleMarbleBlack", makeSimpleGeomorph("SimpleFloorMarbleBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/marble-black.png"));
       
       //Granites
       WALLS.add("SimpleGraniteBlack", makeSimpleGeomorph("SimpleGraniteBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/granite-black-hr.jpg"));
       WALLS.add("SimpleGraniteGreen", makeSimpleGeomorph("SimpleGraniteGreen", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/granite-green-hr.jpg"));
       WALLS.add("SimpleFancyStone", makeSimpleGeomorph("SimpleFancyStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/FancyStone-01-hr.jpg"));
       WALLS.add("SimpleFancyStoneLt", makeSimpleGeomorph("SimpleFancyStoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/FancyStone-01-lt-hr.jpg"));
       
       // Sandstones
       WALLS.add("SimpleSandstoneLt", makeSimpleGeomorph("SimpleSandstoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/Sandstone-lt-hr.png"));
       WALLS.add("SimpleSandstoneNm", makeSimpleGeomorph("SimpleSandstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/Sandstone-hr.png"));
        WALLS.add("SimpleSandstoneRed", makeSimpleGeomorph("SimpleSandstoneRed", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/Sandstone-red-hr.jpg"));
       
       // Exotic Stones
       WALLS.add("SimpleBlueStone", makeSimpleGeomorph("SimpleBlueStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/blueball.png"));
       WALLS.add("SimpleLapisLazuli", makeSimpleGeomorph("SimpleLapisLazuli", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/BlueStone.png"));
       WALLS.add("SimpleRedBlack", makeSimpleGeomorph("SimpleRedBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/redball.png"));
       
       // Non-Stone
       WALLS.add("SimpleDirt", makeSimpleGeomorph("SimpleDirt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterials("Wall:Textures/dirt-01.png"));
       WALLS.add("SimpleOldWood", makeSimpleGeomorph("SimpleOldWood", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-wall", ".gltf", 1, 20)
            .setMaterialsNm("Wall:Textures/oldwood-01b-sm.png:Textures/oldwood-01b-sm-nml.png"));
              
        // Bricks
       WALLS.add("RedBrick", makeSimpleGeomorph("RedBrick", 
            "Models/geomorphs/walls/brick/Geomorph-brick-floor", 1, 20));
       WALLS.add("StoneBrick", makeSimpleGeomorph("StoneBrick", 
            "Models/geomorphs/walls/StoneBrick/Geomorph-StoneBrick-wall", ".glb", 1, 20));
       WALLS.add("SandstoneBrick", makeSimpleGeomorph("SandstoneBrick", 
            "Models/geomorphs/walls/StoneBrick/Geomorph-StoneBrick-wall", ".glb", 1, 20)
            .setMaterials("Wall:Textures/Sandstone-lt-hr.png"));
       WALLS.add("SandstoneBrickDk", makeSimpleGeomorph("SandstoneBrickDk", 
            "Models/geomorphs/walls/StoneBrick/Geomorph-StoneBrick-wall", ".glb", 1, 20)
            .setMaterials("Wall:Textures/Sandstone-hr.png"));
    }
    
    
    /**
     * Methods calls to add Ceiling sections.
     */
    private void addCielings() {
        // Basic and unworked stone
        CIELINGS.add("SimpleStone", new GeomorphModel("SimpleStone", 
            "Models/geomorphs/cielings/Geomorph-blank-ciel.j3o")
                .setMaterials("Floor:Textures/stone-hr.png"));
        CIELINGS.add("SimpleStoneDark", new GeomorphModel("SimpleStoneDark", 
            "Models/geomorphs/cielings/Geomorph-blank-ciel.j3o")
                .setMaterials("Floor:Textures/stone-dark-hr.png"));
        CIELINGS.add("SimpleDarkStone", new GeomorphModel("SimpleDarkStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/darkstone-hr.png:Textures/NoiseNormal.png"));
        CIELINGS.add("SimpleGneiss", new GeomorphModel("SimpleGneiss", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/gneiss-hr.jpg:Textures/NoiseNormal.png"));
        CIELINGS.add("SimpleMudstone", new GeomorphModel("SimpleMudstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/mudstone.png:Textures/NoiseNormal.png"));
                
        // Marble
        CIELINGS.add("SimpleMarble", new GeomorphModel("SimpleMarble", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/marble.png"));
        CIELINGS.add("SimpleMarbleBlack", new GeomorphModel("SimpleMarbleBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/marble-black.png"));
        
        //Granites
        CIELINGS.add("SimpleGraniteBlack", new GeomorphModel("SimpleGraniteBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/granite-black-hr.jpg:Textures/NoiseNormal.png"));
        CIELINGS.add("SimpleGraniteGreen", new GeomorphModel("SimpleGraniteGreen", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/granite-green-hr.jpg"));
        CIELINGS.add("SimpleFancyStone", new GeomorphModel("SimpleFancyStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/FancyStone-01-hr.jpg:Textures/NoiseNormal.png"));
        CIELINGS.add("SimpleFancyStoneLt", new GeomorphModel("SimpleFancyStoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/FancyStone-01-lt-hr.jpg"));
        
        //Sandstones
        CIELINGS.add("SimpleSandstoneLt", new GeomorphModel("SimpleSandstoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/Sandstone-lt-hr.png:Textures/NoiseNormal.png"));
        CIELINGS.add("SimpleSandstone", new GeomorphModel("SimpleSandstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/Sandstone-hr.png:Textures/NoiseNormal.png"));
        CIELINGS.add("SimpleSandstoneRed", new GeomorphModel("SimpleSandstoneRed", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/Sandstone-red-hr.jpg:Textures/NoiseNormal.png"));
        
        //Smooth Sandstones
        CIELINGS.add("SmoothSandstoneLt", new GeomorphModel("SimpleSandstoneLt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/Sandstone-lt-hr.png"));
        CIELINGS.add("SmoothSandstone", new GeomorphModel("SimpleSandstone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/Sandstone-hr.png"));
        CIELINGS.add("SmoothSandstoneRed", new GeomorphModel("SimpleSandstoneRed", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/Sandstone-red-hr.jpg"));
        
        //Exotic Stone
        CIELINGS.add("SimpleBlueStone", new GeomorphModel("SimpleBlueStone", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/blueball.png"));
        CIELINGS.add("SimpleLapisLazuli", new GeomorphModel("SimpleLapisLazuli", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/BlueStone.png"));
        CIELINGS.add("SimpleRedBlack", new GeomorphModel("SimpleRedBlack", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/redball.png:Textures/NoiseNormal.png"));
        
        // Concrete
        CIELINGS.add("Concrete01", new GeomorphModel("Concrete01", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/concrete-01.png"));
        CIELINGS.add("Concrete03", new GeomorphModel("Concrete03", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/concrete-03.png"));
        CIELINGS.add("Concrete04", new GeomorphModel("Concrete04", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/concrete-04.png"));
        
        // Non-Stone
        CIELINGS.add("SimpleDirt", new GeomorphModel("SimpleDirt", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterials("Floor:Textures/dirt-01.png"));
        CIELINGS.add("SimpleOldWood", new GeomorphModel("SimpleOldWood", 
            "Models/geomorphs/walls/simpleb/Geomorph-blank-ciel.gltf")
                .setMaterialsNm("Floor:Textures/oldwood-01b-sm.png:Textures/oldwood-01b-sm-nml.png"));
        
    }
    
    
    private void addGeomorphs() {
        // First, setup components; these must be in place!
        addFloors();
        addCielings();
        addWalls();
        addDecor();
        
        // Then, make the actual geomorphs!
        // First, pure stone...
        register(new Geomorph("SimpleStone", "SimpleStone", "SimpleStone", "SimpleStone"));
        register(new Geomorph("Marble", "SimpleMarble", "SimpleMarble", "SimpleMarble"));
        register(new Geomorph("DarkStone", "SimpleDarkStone", "SimpleDarkStone", "SimpleDarkStone"));
        register(new Geomorph("RoughGneiss", "SimpleGneiss", "SimpleGneiss", "SimpleGneiss"));
        register(new Geomorph("Mudstone", "SimpleMudstone", "SimpleMudstone", "SimpleMudstone"));
        register(new Geomorph("BlackGranite", "SimpleGraniteBlack", "SimpleGraniteBlack", "SimpleGraniteBlack"));
        register(new Geomorph("GreenGranite", "SimpleGraniteGreen", "SimpleGraniteGreen", "SimpleGraniteGreen"));
        register(new Geomorph("FancyBlackStone", "SimpleFancyStone", "SimpleFancyStone", "SimpleFancyStone"));
        register(new Geomorph("FancyPaleStone", "SimpleFancyStoneLt", "SimpleFancyStoneLt", "SimpleFancyStoneLt"));
        register(new Geomorph("RedSandstone", "SimpleSandstoneRed", "SimpleSandstoneRed", "SimpleSandstoneRed"));
        
        // Mixed/Fancy themes featuring stone textures
        register(new Geomorph("WhiteMarbleCarpet", "SimpleCarpet01", "SimpleMarble", "SimpleMarble"));
        register(new Geomorph("FancyBlue", "SimpleCarpet01", "SimpleBlueStone", "SimpleBlueStone"));
        register(new Geomorph("DirtNStone", "SimpleDirt", "SimpleStoneDark", "SimpleStoneDark"));
        register(new Geomorph("DarkStone", "SimpleDirt", "SimpleOldWood", "SimpleOldWood"));
        
        //Bricks
        register(new Geomorph("BrickNStone", "Concrete01", "RedBrick", "Concrete03"));
        register(new Geomorph("BrickNCarpet", "SimpleCarpet02", "RedBrick", "SimpleOldWood"));
        register(new Geomorph("StoneBrick02", "CobbleStone", "StoneBrick", "SimpleOldWood"));
        register(new Geomorph("SandstoneBrick01", "SmoothSandstoneLt", "SandstoneBrick", "SmoothSandstoneLt"));
        register(new Geomorph("SandstoneBrick02", "SmoothSandstone", "SandstoneBrickDk", "SmoothSandstone"));
    }
    
    
    public static Registry<GeomorphModel> getDecor() {
        return DECOR;
    }
}
