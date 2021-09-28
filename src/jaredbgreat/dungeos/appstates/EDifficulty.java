package jaredbgreat.dungeos.appstates;

/**
 *
 * @author Jared Blackburn
 */
public enum EDifficulty {
    
    LOOKING (0.3f,   0f,     10f, 30f, 5.5f, 1000L),
    EASY    (0.05f,  0.1f,   10f, 30f, 5.75f, 900L),
    NORMAL  (0.03f,  0.2f,   10f, 30f, 6.0f,  800L),
    HARD    (0.01f,  0.2f ,  10f, 30f, 6.25f, 700L),
    HORROR  (0f,     0.175f,  6f, 10f, 6.5f,  500L);
    
    
    public final float alight;
    public final float tbright;
    public final float tmax1;
    public final float tmax2;
    public final float mobSpeed;
    public final long  mobCooldown;
    public final float genericFactor;
    

    static final EDifficulty DEFAULT = NORMAL;
    static EDifficulty current = DEFAULT;
    
    
    EDifficulty(float alight, float tbright, float tmax1, float tmax2, float mobSpeed, long mobCooldown) {
        this.alight      = alight;
        this.tbright     = tbright;
        this.tmax1       = tmax1;
        this.tmax2       = tmax2;
        this.mobSpeed    = mobSpeed;
        this.mobCooldown = mobCooldown;
        genericFactor    = (float)(((double)mobCooldown) / 1000d);
    }
    
    
    public static void setCurrent(EDifficulty in) {
        current = in;
    }
    
    
    public static EDifficulty setCurrent(int num) {
        switch(num) {
            case 0: 
                current = LOOKING;
                break;
            case 1:
                current = EASY;
                break;
            case 2:
                current = NORMAL;
                break;
            case 3:
                current = HARD;
                break;
            case 4:
                current = HORROR;
                break;
            default:
                current = NORMAL;
        }
        return current;
    }
    
    
    public static EDifficulty getCurrent() {
        return current;
    }
    
}
