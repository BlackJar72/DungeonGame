package jaredbgreat.dungeos.mapping.dld;

import static jaredbgreat.dungeos.mapping.dld.Direction.*;


/**
 *
 * @author Jared Blackburn
 */
public class Room {
    public static final Room NULL = new Room();
    
    public final int id;
    public final int x, z;
    public final int geomorph;
    int sizex, sizez;
    int startx;
    int endx;
    int startz;
    int endz;
    
    
    /**
     * A constructor specifically for the null room 
     * (areas not in a room).
     */
    private Room() {
        id = 0;
        // Other fields are irrelevant as this "room" is not actually 
        // built, being a null room representing unused space.
        x = z = 0;        
        geomorph = 0;
    }
    
    
    public Room(int id, int startx, int endx, int startz, int endz, int geomorph, Dungeon dungeon) {
        this.id = id;
        dungeon.rooms.add(this);
        this.geomorph = geomorph;
        System.out.println(this.geomorph);
        this.x  = startz + sizex / 2;
        this.z  = startz + sizez / 2; 
        makePrebounded(startx, endx, startz, endz, dungeon);
    }
    
    
    private void makePrebounded(int startx, int endx, int startz, int endz, Dungeon dungeon) {
        this.sizex = endx - startx;
        this.sizez = endz - startz;
        // Find the bounds of the room; yes its biased positive
        this.startx = Math.max(1, x - sizex / 2);
        this.endx   = Math.min(dungeon.size.width - 2, x + sizex / 2 + sizex % 2);
        this.startz = Math.max(1, z - sizez / 2);
        this.endz   = Math.min(dungeon.size.width - 2, z + sizez / 2 + sizez % 2);
    }
    
    
    public Room build(int[][] rooms, int[][] geos) {
        for(int i = startx; i <= endx; i++) 
            for(int j = startz; j <= endz; j++) {
                rooms[i][j] = id;
                geos[i][j] = geomorph + (96 << 16);
                //System.out.println(geomorph + " = " + geos[i][j]);
            }
        // TODO: Find doors
        for(int i = startz; i <= endz; i++) {
            geos[startx][i] = W.addWall(geos[startx][i]);
            geos[endx][i]   = E.addWall(geos[endx][i]);
        }
        for(int i = startx; i <= endx; i++) {
            geos[i][startz] = N.addWall(geos[i][startz]);
            geos[i][endz]   = S.addWall(geos[i][endz]);
        }
//        for(int i = startx; i <= endx; i++) 
//            for(int j = startz; j <= endz; j++) {
//                System.out.println(geomorph + " = " + (geos[i][j] & 0xffff));
//            }
        return this;
    }
    
    
    public void addDoorway(Dungeon dungeon, int x, int z, Direction dir) {
        int ox = x + dir.incx;
        int oz = z + dir.incz;
        if((ox < 0) || (ox > dungeon.size.width) || (oz < 0) || (oz > dungeon.size.width)) {
            return;
        }
        dungeon.geomodel[x][z] = dir.removeWall(dungeon.geomodel[x][z]);
        dungeon.geomodel[ox][oz] 
                = dir.opposite().removeWall(dungeon.geomodel[ox][oz]);
    }
    
    
	
	
    /**
     * Adds a room new room branching from this one that is part of a sequence of 
     * rooms between two dungeon nods.
     * 
     * @param dungeon
     * @param dir
     * @param xdim
     * @param zdim
     * @param height
     * @param source
     * @return
     */
    public Room connector(Dungeon dungeon, Direction dir, int xdim, int zdim, int height, Route source) {
            int xExtend = 0;
            int zExtend = 0;
            int xSeedDir = dir.incx;
            int zSeedDir = dir.incz;
            int ox, oz, oppX, oppZ;
            switch (dir) {
                    case E:
                            ox = startx;
                            oppX = endx;
                            oz = z;
                            oppZ = oz;
                            zExtend = dungeon.random.nextInt(3) - 1;
                            break;
                    case N:
                            oz = startz;
                            oppZ = endz;
                            ox = x;
                            oppX = ox;
                            xExtend = dungeon.random.nextInt(3) - 1;
                            break;
                    case W:
                            ox = endx;
                            oppX = startx;
                            oz = z;
                            oppZ = oz;
                            zExtend = dungeon.random.nextInt(3) - 1;
                            break;
                    case S:
                            oz = endz;
                            oppZ = startz;
                            ox = x;
                            oppX = ox;
                            xExtend = dungeon.random.nextInt(3) - 1;
                            break;
                    default: // Removes the "...may not be initialized" warning.
                            ox = startx;
                            oppX = endx;
                            oz = z;
                            oppZ = oz;
                            oz += startz;
                            xSeedDir = -1;
                            zSeedDir =  0;
                            zExtend = dungeon.random.nextInt(3) - 1;
                            break;
            }
            addDoorway(dungeon, ox + xExtend, oz + zExtend, dir);
            if(((ox + xSeedDir) >= dungeon.size.width) ||
                            ((ox + xSeedDir) < 0) ||
                            ((oz + zSeedDir) >= dungeon.size.width) ||
                            ((oz + zSeedDir) < 0)) return null;
            if(dungeon.room[ox + xSeedDir][oz + zSeedDir] != 0) {
                            return dungeon.rooms.get(dungeon.room[ox + xSeedDir][oz + zSeedDir]);
                    }
            else if(dir.onX()) return new RoomSeed(ox + xSeedDir, 0, oz + zSeedDir)
                            .growRoomZ(xdim, zdim, height, dungeon, null, this);
            else return new RoomSeed(ox + xSeedDir, 0, oz + zSeedDir)
                            .growRoomX(xdim, zdim, height, dungeon, null, this);
    }
    
}
