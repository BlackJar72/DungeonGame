package jaredbgreat.cubicnightmare.mapping.planner;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.cubicnightmare.componenent.GeomorphManager;
import jaredbgreat.cubicnightmare.componenent.geomorph.Geomorphs;
import jaredbgreat.cubicnightmare.mapping.tables.ECardinal;

/**
 *
 * @author jared
 */
/*
 I really need to decide, is this actually going to build the dungeon?  Or is a record keeping system 
 for use during generation, afterwhich the rooms will generate themselves?  The later would be work well 
 with a portal systems as well as ways to organize some other data during play.  *OR* I could use this 
 to build but attach what is built to the relevant rooms by id.S
*/
public class MapMatrix {
    public static final int EMPTY    =  0; // Marks area around rooms where doorways appear
    public static final int BOUNDARY = -1; // Marks area around rooms where doorways appear
    int[][] room;
    int[][] geomorph; // Do I need this, or should a let rooms build themselves, as nodes?
    int[][] type;     // What kind of "room"; is it a room, doorway, etc. (What table to reference.)
    int[][] passable; // Is this an area that can be normally entered? (Treated as numeric for special reasons.)
    int[][] dirs;     // Is this an area that can be normally entered? (Treated as numeric for special reasons.)
    boolean[][] pillars;  // pillars used to endcap walls
    
    
    public MapMatrix() {
        this(64);
    }
    
    
    public MapMatrix(int size) {
        room     = new int[size][size];
        geomorph = new int[size][size];
        type     = new int[size][size];
        passable = new int[size][size];
        dirs     = new int[size][size];
        pillars  = new boolean[size-1][size-1];
    }
    
    
    public void addRoom(int id, int geo, int type, int minx, int maxx, int minz, int maxz) {
        for(int i = minx; i <= maxx; i++) 
            for(int j = minz; j <= maxz; j++) {
                room[i][j] = id;
                geomorph[i][j] = geo;
                this.type[i][j] = type;
                passable[i][j] = 1;
            }
        int xboundl = Math.max(minx, 0);
        int zboundl = Math.max(minz - 1, 0);
        int xboundh = Math.min(maxx, room.length - 1);
        int zboundh = Math.min(maxz + 1, room.length - 1);
        for(int i = xboundl; i <= xboundh; i++) {
            if(room[i][zboundl] == 0) room[i][zboundl] = BOUNDARY;
            if(room[i][zboundh] == 0) room[i][zboundh] = BOUNDARY;
        }
        xboundl = Math.max(minx - 1, 0);
        zboundl = Math.max(minz, 0);
        xboundh = Math.min(maxx + 1, room.length - 1);
        zboundh = Math.min(maxz, room.length - 1);
        for(int i = zboundl; i <= zboundh; i++) {
            if(room[xboundl][i] == 0) room[xboundl][i] = BOUNDARY;
            if(room[xboundh][i] == 0) room[xboundh][i] = BOUNDARY;
        }
    }
    
    
    public Doorway addDoor(int x, int z, int y, ECardinal dir, Room from, Dungeon dungeon) {
        if((x > -1) && (x < (room.length - 1)) 
                    && (z > -1) && (z < (room[x].length - 1)) && (room[x][z] < 1)) {
            RoomList dl = dungeon.areas.getList(1);
            Doorway dw = new Doorway(x, z, y, dir, from);
            dl.add(dw);            
            room[x][z] = dl.realSize();
            dw.setID(room[x][z]);
            { 
                int geo;
                switch(dungeon.random.nextInt(3)) {
                    case 0: geo = dungeon.a; break;
                    case 1: geo = dungeon.b; break;
                    case 2:
                    default: geo = from.getBaseGeomorph(); break;
                }
                dw.setGeomorph(geo);
                geomorph[x][z] = geo;
            }
            type[x][z] = 1;
            passable[x][z] = 1;
            return dw;
        }
        return null;
    }
    
    
    public void removeDoor(Doorway dw, Dungeon dungeon) {
        room[dw.doorx][dw.doorz] = -1;
        passable[dw.doorx][dw.doorz] = type[dw.doorx][dw.doorz] = 0;
    }
    
    
    public void buildMap(Dungeon dungeon) {
        simpleRefineMap();
        findPillars();
        RoomList rooms = dungeon.areas.getList(0);
        for(int i = 0; i < geomorph.length; i++) {
            for(int j = 0; j < geomorph[i].length; j++) {
                if((room[i][j] > 0) || (type[i][j] > 0)) {
                    Room theRoom = dungeon.areas.getArea(type[i][j], room[i][j]);
                    Node tile;
                    if(type[i][j] == AreaType.TUNNEL.tid) {
                        tile = Geomorphs.REGISTRY.makeSpatialAt(geomorph[i][j] + 
                                dungeon.getThemeIDforLoc((i * 3) + 1.5f, theRoom.y1, (j * 3) + 1.5f), 
                                i * 3, theRoom.y1 * 3, j * 3);
                    } else {
                        tile = Geomorphs.REGISTRY.makeSpatialAt(geomorph[i][j] + theRoom.getBaseGeomorph(), 
                                i * 3, theRoom.y1 * 3, j * 3);
                    }
                    tile.setName("Dungeon: Room " + theRoom.id + " at " + i + ", " + j);
                    dungeon.geoman.attachSpatial(tile, theRoom.roomSpace);
                }
            }
        }
        for(int i = 0; i < pillars.length; i++) {
            for(int j = 0; j < pillars[i].length; j++) {
                if(pillars[i][j]) {
                    Room theRoom;
                    if(room[i][j] > 0) {
                        theRoom = dungeon.areas.getArea(type[i][j], room[i][j]);
                        if(type[i][j] == AreaType.TUNNEL.tid) {
                            Spatial pillar = dungeon.getTPillarforLoc((i * 3) + 1.5f, 
                                    theRoom.y1, (j * 3) + 1.5f).getSpatial();
                            GeomorphManager.manager.attachSpatial(pillar);
                        } else {
                            Spatial pillar = theRoom.getPillar().getSpatial();
                            pillar.setLocalTranslation(i * 3, theRoom.y1 * 3, j * 3);
                            GeomorphManager.manager.attachSpatial(pillar);
                        }
                    } else {
                        theRoom = dungeon.areas.getArea(type[i+1][j+1], room[i+1][j+1]);
                        if(type[i+1][j+1] == AreaType.TUNNEL.tid) {
                            Spatial pillar = dungeon.getTPillarforLoc((i * 3) + 1.5f, 
                                    theRoom.y1, (j * 3) + 1.5f).getSpatial();
                            GeomorphManager.manager.attachSpatial(pillar);
                        } else {
                            Spatial pillar = theRoom.getPillar().getSpatial();
                            pillar.setLocalTranslation(i * 3, theRoom.y1 * 3, j * 3);
                            GeomorphManager.manager.attachSpatial(pillar);
                    }
                    }
                }
            }
        }
        for(int i = 1; i < rooms.size(); i++) {
            dungeon.geoman.attachNode(rooms.get(i).roomSpace);
        }
        RoomList doorways = dungeon.areas.getList(AreaType.DOORWAY.tid);
        for(int i = 1; i < doorways.size(); i++) {
            dungeon.geoman.attachNode(doorways.get(i).roomSpace);
        }
        RoomList tunnels = dungeon.areas.getList(AreaType.TUNNEL.tid);
        for(int i = 1; i < tunnels.size(); i++) {
            dungeon.geoman.attachNode(tunnels.get(i).roomSpace);
        }
    }    
    
    
    
    private void simpleRefineMap() {
        for(int i = 0; i < room.length; i++) 
            for(int j = 0; j < room[i].length; j++) {
                geomorph[i][j] += (findRotationFromBorders(i, j) << 16);
                if(room[i][j] < 0) {
                    room[i][j] = 0;
                }
            }
    }
    
    
    private int findRotationFromBorders(int x, int z) {
        int out = 96;
        if(((x - 1) < 0) || (room[x - 1][z] < 1)) out+= 1;
        if(((z + 1) == room[x].length) || (room[x][z + 1] < 1)) out+= 8;
        if(((x + 1) == room.length) || (room[x + 1][z] < 1)) out+= 4;
        if(((z - 1) < 0) || (room[x][z - 1] < 1)) out+= 2;
        return out;
    }
    
    
    public void populateDirs() {
        for(int i = 0; i < passable.length; i++) 
            for(int j = 0; j < passable[i].length; j++) {
                addDirsToCell(i, j);
            }
    }
    
    
    private void addDirsToCell(int x, int z) {
        int result = getCanEnterCell(x - 1, z);
        result    |= getCanEnterCell(x,     z + 1) << 2;
        result    |= getCanEnterCell(x + 1, z)     << 4;
        result    |= getCanEnterCell(x,     z - 1) << 6;
        
        result |= getCanEnterCell(x - 1, z + 1) << 1;
        result |= getCanEnterCell(x + 1, z + 1) << 3;
        result |= getCanEnterCell(x + 1, z - 1) << 5;
        result |= getCanEnterCell(x - 1, z + 1) << 7;
        
        result &= (result >> 1) & (result << 1);
        result &= ((result & 1) << 1);
        
        dirs[x][z] = result;
    }
    
    
    private int getCanEnterCell(int x, int z) {
        if((x < 0) || (x >= passable.length) || (z < 0) || (z >= passable[x].length)) return 0;
        return passable[x][z];
    }
    
    
    public void printPassmap() {
        for(int i = 0; i < passable.length; i++) {
            System.out.println();
            for(int j = 0; j < passable[i].length; j++) 
                System.out.print(passable[i][j]);
        }
        System.out.println();
    }
    

    void addTunnelStep(int x, int z, Tunnel tunnel, ECardinal heading) {
        room[x][z] = tunnel.id;
        type[x][z] = tunnel.getType();
        geomorph[x][z] = tunnel.getBaseGeomorph();
        passable[x][z] = 1;
    }
    
    
    /**
     * This should detect wall corners by finding tile corners with walls along 
     * the tile boundaries adjacent to the corner.
     * 
     * @param x
     * @param z
     * @return true is there is a corner, false otherwise
     */
    private boolean shouldHavePillar(int x, int z) {
        boolean nw = room[x][z]      >  0;
        boolean ne = room[x+1][z]    >  0; 
        boolean se = room[x+1][z+1]  >  0; 
        boolean sw = room[x][z+1]    >  0;
        boolean out = (!nw &&  ne  &&  se &&  sw)
                   ||  (nw && !ne  &&  se &&  sw)
                   ||  (nw &&  ne  && !se &&  sw)
                   ||  (nw &&  ne  &&  se && !sw);
        return out;
    }
    
    
    private void findPillars() {
        for(int i = 0; i < pillars.length; i++)
            for(int j = 0; j < pillars[i].length; j++) {
                if(shouldHavePillar(i, j)) {
                    pillars[i][j] = true;
                }
            }
    }
}