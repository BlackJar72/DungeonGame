package jaredbgreat.dungeos.componenet.geomorph.advanced;

import jaredbgreat.dungeos.componenet.geomorph.GeomorphModel;

/**
 * This is a replacement for the original Geomorph classs to better
 * and more flexibly handle combinations of elements (notable floor 
 * wall combos, but maybe more later.
 * 
 * The plan goes like this (I think, so far):
 *      Byte 1: Floor type
 *      Byte 2: Wall type
 *      Byte 3: Wall possition/variants
 *      Byte 4: ??? (Reserved for future development
 * 
 * Hopefully that will do, I don't think I need more than 256 of 
 * each componenent.
 * 
 * @author Jared Blackburn
 */
public class AGeomorph {
    // Now, should this hold geomorphs models?  Or handles (ints) into a registry?
    private GeomorphModel floor;
    private GeomorphModel[] walls;
    
}
