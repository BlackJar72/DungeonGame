package jaredbgreat.cubicnightmare.mapping.decorator;

import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class RoomTheme {
    private final DecorList decorations;
    private final int minDim, minArea, rarity;
    private final boolean bUnique; // can only be used once per level
    
    private boolean used = false;
    
    public RoomTheme(int minDim, int minArea, int rarity, boolean unique) {
        decorations = new DecorList();
        this.minDim = minDim;
        this.minArea = minArea;
        this.rarity = 100 - rarity;
        this.bUnique = unique;
    }
    
    
    public boolean canUse(int width, int length, Random random) {
        boolean yes = !bUnique || !used;
        yes = yes && ((width >= minDim) && (length >= minDim));
        yes = yes && ((width * length) >= minArea);
        yes = yes && (random.nextInt(100) < rarity);
        return yes;
    }
    
    
    public void flagUsed() {
        used = true;
    }
    
    
    public void resetUsed() {
        used = false;
    }
    
    
    public DecorList getDecor() {
        return decorations;
    }
}
