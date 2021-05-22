package jaredbgreat.dungeos.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Registry<T> extends ArrayList<T> {
    private final HashMap<String, Integer> intMap = new HashMap<>();
    
    
    @Override
    @Deprecated
    /**
     * This should not be used, as a name is needed for the registry.  Use 
     * add(String name, T t) instead.
     * 
     * Calling this will log an error and always return false; the object will 
     * not be added.
     */
    public boolean add(T t) {
        GameLogger.mainLogger.logError("ERROR!  Trying to add " 
                + t.getClass().getSimpleName() + " to registry " 
                + "without a name!  Use add(String name, " 
                + t.getClass().getSimpleName() + " "
                + t.getClass().getSimpleName().toLowerCase() 
                + " instead.");
        GameLogger.mainLogger.logException(new Exception());
        return false;
    }
    
    
    /**
     * Adds an object to the registry, to be registered under the String name.
     * @param name
     * @param t 
     */
    public void add(String name, T t) {
        super.add(t);
        intMap.put(name, indexOf(t));
    }
    
    
    /**
     * Adds a Graphic to the registry, to be registered under the String name. 
     * This will also mainLogger the addition to for debugging purposes
     * @param name
     * @param t 
     */
    public void logAdd(String name, T t) {
        super.add(t);
        int id = this.indexOf(t);
        intMap.put(name, id);
        GameLogger.mainLogger.logInfo("Added " + t.getClass().getSimpleName() 
                + " " + name + " to registry with ID " + id);
    }
    
    
    /**
     * Retrieves an object from the registry based on its name.  Generally this 
     * should not be used, as its better to call getID(String name) during 
     * initialization, cache the ID, and the use get(Integer id) later, as this 
     * is more efficient.  Use of this method only makes sense to get a single  
     * graphic for one time use, which is likely to be unusual.  It is included 
     * for that purpose.
     * 
     * @param name
     * @return 
     */
    public T getFromName(String name) {
        return get(intMap.get(name));
    }
    
    
    /**
     * Retrieves the integer ID of a stored object from the registry.  This 
     * should usually be cached to allow fast retrieval of the graphic for 
     * future use. 
     * 
     * Technically, the ID is equal to the object's array index in storage, 
     * making future references to the Graphic through the get(int index) 
     * method suitably fast while also avoiding multiple references to the 
     * Graphic object.
     * 
     * @param name
     * @return 
     */
    public int getID(String name) {
        return intMap.get(name);
    }
    
    
    /**
     * Returns a random ID.
     * 
     * @param random
     * @return 
     */
    public int getRandomID(Random random) {
        return random.nextInt(size());
    }
}
