package jaredbgreat.dungeos.mapping.dld;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		


/**
 * An enumeration of dungeon size categories.
 * 
 * @author Jared Blackburn
 *
 */
// FIXME: Sized should be devided by 3 to fit new scale. (or left big for testing?)
public enum Sizes implements Autoselectable {
	TINY 	(27,	13,		42,	5,	2,	2),
	SMALL 	(33,	16,		50,	6,	3,	2),
	MEDIUM	(41,	20,		58,	7,	4,	2),
	LARGE	(49,	24,		82,	8,	5,	3),
	HUGE	(61,	30,             112,	9,	6,	4);
	
	public final int width;		// Distance across the dungeon zone
	public final int radius;	// Square "radius" around center block (pre-calculated)
	public final int maxRooms;
	public final int maxRoomSize;
	public final int maxNodes;
	public final int minNodes;
	
	Sizes(int d, int r, int mr, int mrs, int maxn, int minn) {
		width = d;
		radius = r;
		maxRooms = mr;
		maxRoomSize = mrs;
		maxNodes = maxn;
		minNodes = minn;
	}
}
