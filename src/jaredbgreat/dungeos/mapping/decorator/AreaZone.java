package jaredbgreat.dungeos.mapping.decorator;

import com.jme3.math.Vector3f;
import jaredbgreat.dungeos.mapping.dld.Dungeon;
import jaredbgreat.dungeos.mapping.dld.Room;

/**
 *
 * @author Blackburn
 */
public class AreaZone {
    final Dungeon dungeon;
    final float x, z;
    final int   value;
    final double strength;
    Room hub;
    Vector3f center;

    public AreaZone(Room hub, int value, double strength, Dungeon dungeon) {
        this.dungeon = dungeon;
        center = hub.getCenterAsVec();
        this.hub = hub;
        this.x = center.x;
        this.z = center.z;
        this.value = value;
        this.strength = strength;
        hub.setBaseGeomorph(value);
    }
    
    
    public double getWeaknessAt(float atx, float aty) {
        double xdisplace = ((double)(x - atx));
        double ydisplace = ((double)(z - aty));
        return (xdisplace * xdisplace) + (ydisplace * ydisplace);
    }
    
    
    public static void summateEffect(AreaZone[] n, Room r) {
        Vector3f t = r.getCenterAsVec();
        double effect = 0.0;
        int index = 0;
        double power;
        for(int i = 0; i < n.length; i++)  {
                power = n[i].strength / n[i].getWeaknessAt(t.x, t.z);
                if(effect < power) {
                    effect = power;
                    index = i;
            }
        }
        r.setBaseGeomorph(n[index].value);
    }
    
}
