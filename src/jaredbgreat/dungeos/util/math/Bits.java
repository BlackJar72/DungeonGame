package jaredbgreat.dungeos.util.math;

/**
 *
 * @author Jared Blackburn
 */
public class Bits {
    
    public static int setBit(int in, int bit) {
        return in | (1 << (bit - 1));
    }
    
    public static long setBit(long in, int bit) {
        return in | (1 << (bit - 1));
    }
    
    public static int unsetBit(int in, int bit) {
        return in & ~ (1 << (bit - 1));
    }
    
    public static long unsetBit(long in, int bit) {
        return in & ~ (1 << (bit - 1));
    }
    
    
    public static boolean checkBit(int in, int bit) {
        return ((in >> (bit - 1)) & 1) == 1;
    }
    
    
    public static boolean checkBit(long in, int bit) {
        return ((in >> (bit - 1)) & 1) == 1;
    }
    
    
    
    
}
