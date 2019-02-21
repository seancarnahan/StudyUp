package edu.studyup.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.studyup.entity.Event;

/***
 * 
 * This class is a temporary class to be used in place of a database. The static
 * variable eventList holds all the event data.
 * 
 * @author Shivani
 * 
 */
public class DataStorage {
	private static final Map<Integer, Event> eventData = new HashMap<Integer, Event>();
	
	public static Event getEvent(int eventID) {
		return eventData.get(eventID);
	}
	
	public static void setEvent(int eventID, Event value) {
		eventData.put(eventID, value);
	}
	
	public static Set<Integer> keySet() {
		return eventData.keySet();
	}
	
	public static Event deleteEvent(int eventID) {
		return eventData.remove(eventID);
	}

	public static void clear() {
		eventData.clear();
		
	}
}
