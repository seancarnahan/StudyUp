package edu.studyup.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create student2
		Student studentTwo = new Student();
		studentTwo.setFirstName("Marky");
		studentTwo.setLastName("Mark");
		studentTwo.setEmail("mWallberg@email.com");
		studentTwo.setId(2);
		
		//Create Event1: one student added
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Create Event2: two students added
		Event eventTwo = new Event();
		eventTwo.setEventID(2);
		eventTwo.setDate(new Date());
		eventTwo.setName("Event 2");
		Location locationTwo = new Location(-123, 36);
		eventTwo.setLocation(locationTwo);
		List<Student> eventStudentsTwo = new ArrayList<>();
		eventStudentsTwo.add(student);
		eventStudentsTwo.add(studentTwo);
		eventTwo.setStudents(eventStudentsTwo);
		
		DataStorage.eventData.put(eventTwo.getEventID(), eventTwo);
		
		//Create Event3: no students added
		Event eventThree = new Event();
		eventThree.setEventID(3);
		Date dateInPast = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
		eventThree.setDate(dateInPast);
		eventThree.setName("Event 3");
		Location locationThree = new Location(-124, 35);
		eventThree.setLocation(locationThree);
		
		
		DataStorage.eventData.put(eventThree.getEventID(), eventThree);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	
	//tests for updateEventName()
	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEventName_20charlength() throws StudyUpException {
		//exposes bug -> 20 char length should not throw exception
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Thisstringistwentych");
		assertEquals("Thisstringistwentych", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEventName_GreaterThan20() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "This string is greater than 20");	
		 });
	}
	
	@Test
	void testUpdateEventName_emptyString() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "");
		assertEquals("", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 4;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		});
	}
		
	
	//tests for addStudentToEvent()
	@Test
	void testAddStudentToEvent_oneTotalStudent() throws StudyUpException {
		int eventId = 3;
		Student studentThree = new Student();
		studentThree.setFirstName("Edgar");
		studentThree.setLastName("Allen");
		studentThree.setEmail("Poe@email.com");
		studentThree.setId(3);
		eventServiceImpl.addStudentToEvent(studentThree, eventId);
		
		Event event = DataStorage.eventData.get(eventId);
		List<Student> presentStudents = event.getStudents();
		assertEquals("Edgar", presentStudents.get(0).getFirstName());	
	}
	
	@Test
	void testAddStudentToEvent_twoTotalStudents() throws StudyUpException {
		int eventId = 1;
		Student studentThree = new Student();
		studentThree.setFirstName("Edgar");
		studentThree.setLastName("Allen");
		studentThree.setEmail("Poe@email.com");
		studentThree.setId(3);
		eventServiceImpl.addStudentToEvent(studentThree, eventId);
		
		Event event = DataStorage.eventData.get(eventId);
		List<Student> presentStudents = event.getStudents();
		assertEquals("Edgar", presentStudents.get(1).getFirstName());
	}
	
	@Test
	void testAddStudentToEvent_ThreeTotalStudents() {
		//exposes bug -> There can only be at most two students
		int eventId = 2;
		Student studentThree = new Student();
		studentThree.setFirstName("Edgar");
		studentThree.setLastName("Allen");
		studentThree.setEmail("Poe@email.com");
		studentThree.setId(3);

		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(studentThree,eventId);
		});	
	}
	
	@Test
	void testAddStudentToEvent_nonExistingId() {
		int eventId = 5;
		Student studentThree = new Student();
		studentThree.setFirstName("Edgar");
		studentThree.setLastName("Allen");
		studentThree.setEmail("Poe@email.com");
		studentThree.setId(3);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(studentThree,eventId);
		});
	}
	
	
	//tests for getActiveEvents()
	@Test
	void testGetActiveEvents_usingDateInPast() {
		//exposes bug -> past dates should throw an error event3 is in the past
		List<Event> activeEvents = new ArrayList<>();
		activeEvents = eventServiceImpl.getActiveEvents();
		
		for(Event e : activeEvents) {
			if(e.getEventID() == 3) {
				fail("failed because eventId 3 is an old date and shouldnt be passed to active elements");
			}
		}	
	}
	
	@Test
	void testGetActiveEvents_goodCase() {
		List<Event> activeEvents = new ArrayList<>();
		activeEvents = eventServiceImpl.getActiveEvents();
		List<Integer> events = new ArrayList<>();
		
		for(Event e : activeEvents) {
			if(e.getEventID() == 3) {
				continue;
			} else {
				events.add(e.getEventID());
			}
		}	
		assertEquals(events.size(), 2);
	}
	
	
	//tests for getPastEvents
	@Test
	void testGetPastEvents_goodCase() {
		List<Event> pastEvents = new ArrayList<>();
		pastEvents = eventServiceImpl.getPastEvents();
		assertEquals(pastEvents.get(0).getEventID(), 3);
	}
	
		
	//tests for deleteEvent()
	@Test
	void testDeleteEvent_goodCase() throws StudyUpException {
		int eventId = 1;
		
		assertEquals(eventId, eventServiceImpl.deleteEvent(eventId).getEventID());	
	}
}
