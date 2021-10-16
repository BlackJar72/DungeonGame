package jaredbgreat.cubicnightmare.mapping.planner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class Sector {
    public final int x, z;
    
    
    private static class Coord {
        public int val;
        public Coord(int n) {
            val = n;
        }    
        public static ArrayList<Coord> getCoordList(Random random) {
            ArrayList<Coord> out = new ArrayList<>(4);
            for(int i = 0; i < 4; i++) {
                out.add(new Coord(i));
            }
            Collections.shuffle(out, random);
            return out;
        }
    }
    
    
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
    
    
    public static ArrayList<Sector> getShuffledList(Random random) {
        ArrayList<Sector> tmp = new ArrayList<>(16);
        ArrayList<Sector> out = new ArrayList<>(16);
        for(int i = 0; i < 16; i++) {
            tmp.add(new Sector(i % 4, i / 4));
        }
        List<Coord> clist1 = Coord.getCoordList(random);
        List<Coord> clist2 = Coord.getCoordList(random);
        for(int i = 0; i < 4; i++) {
            out.add(new Sector(clist1.get(i).val, clist2.get(i).val));
        }
        Collections.shuffle(tmp, random);
        for(int i = 0; i < 16; i++) {
            Sector s = tmp.get(i);
            if(!out.contains(s)) {
                out.add(s);
            }
        }
        return out;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Sector)) return false;
        Sector other = (Sector)o;
        return (x == other.x) && (z == other.z);
    }
    
}
