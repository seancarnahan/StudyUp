package edu.studyup.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.studyup.entity.Event;
import edu.studyup.entity.Student;
import edu.studyup.service.EventService;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

public class EventServiceImpl implements EventService {

	@Override
	public Event updateEventName(int eventID, String name) throws StudyUpException {
		Event event = DataStorage.getEvent(eventID);
		if(event == null) {
			throw new StudyUpException("No event found.");
		}

		if(name.length() >= 20) {
			throw new StudyUpException("Length too long. Maximun is 20");
		}
		event.setName(name);
		return event;
	}
	
	@Override
	public List<Event> getActiveEvents() {
		List<Event> activeEvents = new ArrayList<>();
		
		for (Integer key : DataStorage.keySet()) {
			Event ithEvent= DataStorage.getEvent(key);
			activeEvents.add(ithEvent);
		}
		return activeEvents;
	}

	@Override
	public List<Event> getPastEvents() {
		List<Event> pastEvents = new ArrayList<>();
		
		for (Integer key : DataStorage.keySet()) {
			Event ithEvent= DataStorage.getEvent(key);
			// Checks if an event date is before today, if yes, then add to the past event list.
			if(ithEvent.getDate().before(new Date())) {
				pastEvents.add(ithEvent);
			}
		}
		return pastEvents;
	}

	@Override
	public Event addStudentToEvent(Student student, int eventID) throws StudyUpException {
		Event event = DataStorage.getEvent(eventID);
		if(event == null) {
			throw new StudyUpException("No event found.");
		}
		List<Student> presentStudents = event.getStudents();
		if(presentStudents == null) {
			presentStudents = new ArrayList<>();
		}
		presentStudents.add(student);
		event.setStudents(presentStudents);		
		return event;
	}

	@Override
	public Event deleteEvent(int eventID) {		
		return DataStorage.deleteEvent(eventID);
	}

}
