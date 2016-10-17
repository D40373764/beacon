package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import models.Attendee;
import play.Logger;
import play.libs.Json;

public class DataStaxUtil {
	
	private static final Config config = ConfigFactory.load();
	private static final String cassandraHost = config.getString("datastax.url"); 
	private static final int cassandraPost= config.getInt("datastax.port");
	private static final String keyspace = config.getString("datastax.keyspace");
	private static Cluster cluster;
	private static Session session;

	private static PreparedStatement insertStatment;
	private static PreparedStatement selectStatment; 
	private static BoundStatement insertbs;
	private static BoundStatement selectbs;
	private static BoundStatement selectBeaconLocations;
	
	private static HashMap<String, List<Object>> beaconLocationMap = new HashMap<>();
	
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
		
		insertStatment = session.prepare("INSERT INTO attendee_play (attendee_id, first_name, last_name, event_id, event_name, place_name, room_name, device_id, date, timestamp, os) values (?,?,?,?,?,?,?,?,?,?,?);");
		selectStatment = session.prepare("SELECT * FROM attendee_play;");
		insertbs = new BoundStatement(insertStatment);
		selectbs = new BoundStatement(selectStatment);
		initBeanLocations();		
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
	
	public ArrayNode getAttendees() {
		ResultSet results = getSession().execute(selectbs);
		List<Attendee> attendees = new ArrayList<Attendee>();
		
		for (Row row : results) {
			Attendee attendee = new Attendee();
			attendee.setAttendeeID(row.getString("attendee_id"));
			attendee.setFirstName(row.getString("first_name"));
			attendee.setLastName(row.getString("last_name"));
			attendee.setEventID(row.getString("event_id"));
			attendee.setEventName(row.getString("event_name"));
			attendee.setPlaceName(row.getString("place_name"));
			attendee.setRoomName(row.getString("room_name"));			
			attendee.setDeviceID(row.getString("device_id"));
			attendee.setDate(row.getString("date"));
			attendee.setTimestamp(row.getString("timestamp"));
			attendee.setOs(row.getString("os"));
			attendees.add(attendee);
		}
		return (ArrayNode) Json.toJson(attendees);
	}

	public boolean saveAttendee(Attendee attendee) {
		String beaconID = attendee.getBeaconID();
		List<Object> location = beaconLocationMap.get(beaconID);
				
		if (null == location) {
			location = Arrays.asList("NA", "NA");
		}
		
		Logger.info("location=" + location.toString());
		insertbs.bind()
			.setString("attendee_id", attendee.getAttendeeID())
			.setString("first_name", attendee.getFirstName())
			.setString("last_name", attendee.getLastName())
			.setString("event_id", attendee.getEventID())
			.setString("event_name", attendee.getEventName())
			.setString("place_name", location.get(0).toString())
			.setString("room_name", location.get(1).toString())
			.setString("device_id", attendee.getDeviceID())
			.setString("date", attendee.getTimestamp().substring(0, 10))
			.setString("timestamp", attendee.getTimestamp())
			.setString("os", attendee.getOs());
					
		ResultSet results = getSession().execute(insertbs);
		Logger.info("results=" + results.toString());
		return true;
	}
	
	private void initBeanLocations() {
		
		selectBeaconLocations = new BoundStatement(session.prepare("SELECT * FROM beacon_location;"));
		
		ResultSet results = session.execute(selectBeaconLocations);
		
		for (Row row : results) {
			String beacon_id = row.getString("beacon_id");
			Object address = row.getObject("address");
			String place_name = row.getString("place_name");
			String room_name = row.getString("room_name");
			
			Logger.info("beacon_id =" + beacon_id);
			Logger.info("address =" + address.toString());
			Logger.info("place_name =" + place_name);
			Logger.info("room_name =" + room_name);
			
			
			beaconLocationMap.put(beacon_id, Arrays.asList(place_name, room_name, address));
		}	
	}
}
