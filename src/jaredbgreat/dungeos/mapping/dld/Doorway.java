package jaredbgreat.dungeos.mapping.dld;

import jaredbgreat.dungeos.mapping.tables.ECardinal;
import jaredbgreat.dungeos.mapping.tables.Tables;

/**
 *
 * @author jared
 */
// FIXME? Should this extend room?  Or should this and room extend an AbstractRoom class?
public class Doorway extends Room {
    final ECardinal heading;
    final Room[] connects;
    int doorx, doorz;
    
    
    public Doorway(int x, int z, int y, ECardinal dir, Room from) {
        super(x, x + 1, z, z + 1, y, y + 1);
        heading = dir;
        connects = new Room[2];
        connects[0] = from;
        doorx = x;
        doorz = z;
    }
    
    
    @Override
    public int getType() {
        return 1;
    }
    
    
    public Room makeOtherRoom(Dungeon dungeon) {
        RoomSeed rseed = new RoomSeed(doorx + heading.incx, y1, doorz + heading.incz);
        int[] dims = Tables.getRoomSize(dungeon.random);
        connects[1] = rseed.growRoom(dims[0], dims[1], 0, dungeon, null, connects[0]);
        if(connects[1] != null) {
            RoomList rl = dungeon.areas.getList(0);
            rl.add(connects[1]);
            connects[1].setID(rl.realSize());
            // FIXME/TODO: This is not how the geomorph will be set!
            switch(dungeon.random.nextInt(2)) {
                case 0:
                    connects[1].setGeomorph(dungeon.b);
                    break;
                case 1:
                default:
                    connects[1].setGeomorph(dungeon.a);
                    break;
            }
            connects[1].buildIn(dungeon.map);
            connects[1].addDoors(dungeon, false);
        }
        return connects[1];
    }
    
}
