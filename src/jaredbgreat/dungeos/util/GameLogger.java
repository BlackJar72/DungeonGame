//Copyright (c) Jared Blackburn 2017
package jaredbgreat.dungeos.util;

/**
 * A central logging system for the game.  This may wrap System.out / System.err 
 * or the Java logger, or perhaps even combine outputs and/or add file output. 
 * That is the point, a single place where such changes can be made without 
 * modifying code throughout the game.
 * 
 * @author Jared Blackburn
 */
public class GameLogger {
    
    /**
     * A a primary instance of logger for a main log file.  Specialized loggers 
     * may still be created.
     */
    public static final GameLogger mainLogger = new GameLogger();
    
    
    public GameLogger(){}
    
    
    /**
     * Print output directly to the system out, never any logging.
     * 
     * @param out 
     */
    public void printInfo(String out) {
        System.out.println(out);
    }
    
    
    /**
     * Output to the console.  Other functions may be added for more complete 
     * logging at some point.
     * 
     * @param out 
     */
    public void logInfo(String out) {
        System.out.println(out);
    }
    
    
    /**
     * Output error information.  Currently this wraps System.err, but other 
     * functionality may be added or the Java logger may be in the future.
     * 
     * @param out 
     */
    public void logError(String out) {
        System.err.println(out);
    }
    
    
    /**
     * Prints a stack trace to the console.
     * 
     * @param ex 
     */
    public void logException(Throwable ex) {
        logError(ex.getMessage());
        ex.printStackTrace();
    }
    
    
}
