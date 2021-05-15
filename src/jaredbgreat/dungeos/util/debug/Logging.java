package jaredbgreat.dungeos.util.debug;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


/**
 * A mostly unused wrapper for the Java logger, to make getting a logger easier.
 * 
 * @author Jared Blackburn
 */

public class Logging {
        private static final String LOG_NAME = "LOGGED PROFILE";
	private static Logging log;
	
	private Logging() {}
	
	
	public static Logging getInstance() {
		if(log == null) {
			log = new Logging();
		}
		return log;
	}
	
	
	public static void logError(String error) {
		if(log == null) {
			log = new Logging();
		}
		System.err.println(error);
	}
	
	
	public static void logStacktrace(String error) {
		if(log == null) {
			log = new Logging();
		}
		System.err.println(error);
		new Exception().printStackTrace();
	}
	
	
	public static void logInfo(String info) {
		if(log == null) {
			log = new Logging();
		}
	}

}
