package models;

public class AttendeeIn {
	private String attendeeID;
	private String beaconID;
	private String eventID;
	private String eventName;
	private String timestamp;
	private String os;
	
	public String getAttendeeID() {
		return attendeeID;
	}
	public void setAttendeeID(String attendeeID) {
		this.attendeeID = attendeeID;
	}
	public String getBeaconID() {
		return beaconID;
	}
	public void setBeaconID(String beaconID) {
		this.beaconID = beaconID;
	}
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}		
}
