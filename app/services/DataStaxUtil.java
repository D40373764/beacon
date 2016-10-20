package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TupleType;
import com.datastax.driver.core.TupleValue;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import models.Attendee;
import models.Event;
import models.RoomAttendance;
import models.RoomLocation;
import play.Logger;
import play.libs.Json;

public class DataStaxUtil {
	
	private static final Config config = ConfigFactory.load();
	private static final String cassandraHost = config.getString("datastax.url"); 
	private static final int cassandraPost= config.getInt("datastax.port");
	private static final String keyspace = config.getString("datastax.keyspace");
	private static Cluster cluster;
	private static Session session;

	private static String INSERT_ATTENDEE = 
			"insert into room_attendance_by_event (event_id,room_name,room_floor,attendee_first_name,attendee_id,attendee_last_name,event_name,location_address_l1,location_address_l2,location_city,location_country,location_name,location_state,location_zip,registration_timestamp)" + 
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static String INSERT_ATTENDEE_BY_EVENT = 
			"insert into attendees_by_event (event_id,attendee_id,attendee_first_name,attendee_last_name,event_address_l1,event_address_l2,event_city,event_country,event_name,event_state,event_zip,registration_timestamp,attendee_organization_level)" + 
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static String SELECT_ALL_ROOM_ATTENDANCE_BY_EVENT = "SELECT * FROM attendees_by_event;";
	private static String SELECT_ALL_ATTENDEE_BY_EVENT = "SELECT * FROM attendees_by_event;";
	private static String SELECT_USER_BY_DSI = "select first_name, last_name, division, department_name from users where dsi = ?;";
	private static String SELECT_ALL_BEACON_LOCATION = "SELECT * FROM beacon_location;";
	private static String SELECT_ALL_EVENT = "SELECT * FROM events;";
	
	private static BoundStatement insertRoomAttendanceBS;
	private static BoundStatement insertAttendeeByEventBS;
	private static BoundStatement selectAllRoomAttendanceBS;
	private static BoundStatement selectAllAttendeeByEventBS;
	private static BoundStatement selectUserByDsiBS;
	private static BoundStatement selectBeaconLocationBS;
	private static BoundStatement selectAllEventBS;
	
	private static HashMap<String, RoomLocation> beaconLocationMap = new HashMap<>();
	private static HashMap<String, Event> eventMap = new HashMap<>();
	
	public DataStaxUtil() {
		Logger.info("Construct DataStaxUtil");
		
		if (null == cluster) {
			cluster = Cluster.builder()
					.addContactPoint(cassandraHost)
					.withPort(cassandraPost)
					.withCredentials(config.getString("datastax.username"), config.getString("datastax.password"))
					.build();			
		}
		if (null == session) {
			session = cluster.connect(keyspace);			
		}
		
		Logger.info("DataStaxUtil->DataStaxUtil session=" + session);
		
		insertRoomAttendanceBS = new BoundStatement(session.prepare(INSERT_ATTENDEE));
		insertAttendeeByEventBS = new BoundStatement(session.prepare(INSERT_ATTENDEE_BY_EVENT));
		selectAllRoomAttendanceBS = new BoundStatement(session.prepare(SELECT_ALL_ROOM_ATTENDANCE_BY_EVENT));
		selectAllAttendeeByEventBS = new BoundStatement(session.prepare(SELECT_ALL_ATTENDEE_BY_EVENT));
		selectUserByDsiBS = new BoundStatement(session.prepare(SELECT_USER_BY_DSI));
		selectBeaconLocationBS = new BoundStatement(session.prepare(SELECT_ALL_BEACON_LOCATION));
		selectAllEventBS = new BoundStatement(session.prepare(SELECT_ALL_EVENT));
		
		loadBeanLocations();
		loadEvents();
	}
	
	public Session getSession() {
		if (null == session) {
			
			if (null == cluster) {
				cluster = Cluster.builder()
						.addContactPoint(cassandraHost)
						.withPort(cassandraPost)
						.withCredentials(config.getString("datastax.username"), config.getString("datastax.password"))
						.build();				
			}
			session = cluster.connect(keyspace);
		}
		Logger.info("DataStaxUtil->getSession session=" + session);		
		return session;		
	}

	public RoomAttendance setUserName(RoomAttendance attendance) {
		
		selectUserByDsiBS.bind(attendance.getAttendeeID());
		
		ResultSet results = getSession().execute(selectUserByDsiBS);

		for (Row row : results) {
			attendance.setAttendeeFirstName(row.getString("first_name"));
			attendance.setAttendeeLastName(row.getString("last_name"));
		}
		
		return attendance;
	}

	public Attendee setUserData(Attendee attendee) {
		
		selectUserByDsiBS.bind(attendee.getAttendeeID());
		
		ResultSet results = getSession().execute(selectUserByDsiBS);

		for (Row row : results) {
			attendee.setFirstName(row.getString("first_name"));
			attendee.setLastName(row.getString("last_name"));
			attendee.setDivision(row.getString("division"));
			attendee.setDepartment(row.getString("department_name"));
		}
		
		return attendee;
	}
	
	public ArrayNode getAllRoomAttendanceByEvent() {
		ResultSet results = getSession().execute(selectAllRoomAttendanceBS);
		List<RoomAttendance> roomAttendanceList = new ArrayList<>();
		
		for (Row row : results) {
			RoomAttendance roomAttendance = new RoomAttendance();
			roomAttendance.setAttendeeID(row.getString("attendee_id"));
			roomAttendance.setAttendeeFirstName(row.getString("first_name"));
			roomAttendance.setAttendeeLastName(row.getString("last_name"));
			roomAttendance.setEventID(row.getString("event_id"));
			roomAttendance.setEventName(row.getString("event_name"));
			roomAttendance.setLocationName(row.getString("place_name"));
			roomAttendance.setRoomName(row.getString("room_name"));			
			roomAttendance.setTimestamp(row.getString("timestamp"));
			roomAttendanceList.add(roomAttendance);
		}
		return (ArrayNode) Json.toJson(roomAttendanceList);
	}

	public ArrayNode getAllAttendeeByEvent() {
		ResultSet results = getSession().execute(selectAllAttendeeByEventBS);
		List<Attendee> attendees = new ArrayList<Attendee>();
		
		for (Row row : results) {
			TupleValue tupleValue = row.getTupleValue("attendee_organization_level");
			
			Attendee attendee = new Attendee();
			attendee.setEventID(row.getString("event_id"));
			attendee.setEventName(row.getString("event_name"));
			attendee.setAttendeeID(row.getString("attendee_id"));
			attendee.setFirstName(row.getString("attendee_first_name"));
			attendee.setLastName(row.getString("attendee_last_name"));
			attendee.setEventAddressL1(row.getString("event_address_l1"));
			attendee.setEventAddressL2(row.getString("event_address_l2"));
			attendee.setEventCity(row.getString("event_city"));
			attendee.setEventState(row.getString("event_state"));
			attendee.setEventZip(row.getString("event_zip"));
			attendee.setEventCountry(row.getString("event_country"));
			attendee.setTimestamp(row.getString("registration_timestamp"));
			if (null != tupleValue) {
				attendee.setDivision(tupleValue.getString(0));
				attendee.setDepartment(tupleValue.getString(1));				
			}

			attendees.add(attendee);
		}
		return (ArrayNode) Json.toJson(attendees);
	}

	public boolean saveRoomAttendance(RoomAttendance attendee) {
				
		insertRoomAttendanceBS.bind()
			.setString(0, attendee.getEventID())
			.setString(1, attendee.getRoomName())
			.setString(2, attendee.getRoomFloor())
			.setString(3, attendee.getAttendeeID())
			.setString(4, attendee.getAttendeeFirstName())
			.setString(5, attendee.getAttendeeLastName())
			.setString(6, attendee.getEventName())
			.setString(7, attendee.getAddressL1())
			.setString(8, attendee.getAddressL2())
			.setString(9, attendee.getLocationCity())
			.setString(10, attendee.getLocationCountry())
			.setString(11, attendee.getLocationName())
			.setString(12, attendee.getLocationState())
			.setString(13, attendee.getLocationZip())
			.setString(14, attendee.getTimestamp());
					
		ResultSet results = getSession().execute(insertRoomAttendanceBS);
		Logger.info("results=" + results.toString());
		return true;
	}
	
	public boolean saveAttendeeByEvent(Attendee attendee) {
		
	    TupleType organizationType = cluster.getMetadata().newTupleType(DataType.text(), DataType.text());  
		TupleValue tupleValue = organizationType.newValue(attendee.getDivision(), attendee.getDepartment());
		
		insertAttendeeByEventBS.bind()
			.setString(0, attendee.getEventID())
			.setString(1, attendee.getAttendeeID())
			.setString(2, attendee.getFirstName())
			.setString(3, attendee.getLastName())
			.setString(4, attendee.getEventAddressL1())
			.setString(5, attendee.getEventAddressL2())
			.setString(6, attendee.getEventCity())
			.setString(7, attendee.getEventCountry())
			.setString(8, attendee.getEventName())
			.setString(9, attendee.getEventState())
			.setString(10, attendee.getEventZip())
			.setString(11, attendee.getTimestamp())
			.setTupleValue(12, tupleValue);
					
		ResultSet results = getSession().execute(insertAttendeeByEventBS);
		Logger.info("results=" + results.toString());
		return true;
	}

	public RoomLocation getLocation(String beaconID) {
		return beaconLocationMap.get(beaconID);
	}

	public Event getEvent(String eventID) {
		return eventMap.get(eventID);
	}

	private void loadBeanLocations() {
		
		ResultSet results = session.execute(selectBeaconLocationBS);
		
		for (Row row : results) {
			RoomLocation room = new RoomLocation();
			room.setRoomName(row.getString("room_name"));
			room.setRoomFloor(row.getString("room_floor"));
			room.setPlaceName(row.getString("place_name"));
			room.setAddressL1(row.getString("address_l1"));
			room.setAddressL2(row.getString("address_l2"));
			room.setCity(row.getString("city"));
			room.setState(row.getString("state"));
			room.setZip(row.getString("zip"));
			room.setCountry(row.getString("country"));
			
			Logger.info("room="+ room.toString());
			beaconLocationMap.put(row.getString("beacon_id"), room);
		}	
	}
	
	private void loadEvents() {
		
		ResultSet results = session.execute(selectAllEventBS);
		
		for (Row row : results) {
			Event event = new Event();
			event.setId(row.getString("event_id"));
			event.setName(row.getString("event_name"));
			event.setDate(row.getDate("event_date"));
			event.setAddressL1(row.getString("event_address_l1"));
			event.setAddressL2(row.getString("event_address_l2"));
			event.setCity(row.getString("event_city"));
			event.setState(row.getString("event_state"));
			event.setZip(row.getString("event_zip"));
			event.setCountry(row.getString("event_country"));
			
			Logger.info("event="+ event.toString());
			eventMap.put(row.getString("event_id"), event);
		}	
	}
	
	
}
