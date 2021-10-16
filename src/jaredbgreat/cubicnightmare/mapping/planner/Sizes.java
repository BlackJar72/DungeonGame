package jaredbgreat.cubicnightmare.mapping.planner;

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
public enum Sizes {
	TINY 	(24,	12,		24,	3,	2),
	SMALL 	(32,	16,		64,	5,	3),
	MEDIUM	(48,	24,		96,	6,	3),
	LARGE	(64,	32,		128,	6,	4),
	HUGE	(128,	64,             256,	6,	6);
	
	public final int width;		// Distance across the dungeon zone
	public final int radius;	// Square "radius" around center block (pre-calculated)
	public final int maxRooms;
	public final int maxNodes;
	public final int minNodes;
	
	Sizes(int d, int r, int mr, int maxn, int minn) {
		width = d;
		radius = r;
		maxRooms = mr;
		maxNodes = maxn;
		minNodes = minn;
	}
}
