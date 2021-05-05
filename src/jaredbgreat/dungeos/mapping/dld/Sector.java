package jaredbgreat.dungeos.mapping.dld;

import java.util.ArrayList;

/**
 *
 * @author Jared Blackburn
 */
public class Sector {
    public final int x, z;
    
    public Sector(int x, int z) {
        this.x = x % 4;
        this.z = z % 4;
    }
    
    
    public static ArrayList<Sector> getSectorList() {
        ArrayList<Sector> out = new ArrayList<>(16);
        for(int i = 0; i < 16; i++) {
            out.add(new Sector(i % 4, i / 4));
        }
        return out;
    }
    
}
