package models;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

/**
 * Sample beacon IDs:
 * 6FBBEF7C-F92C-471E-8D5C-470E9B367FDB
 * 48CAFFE0-C786-4AB9-85F3-6585ACE3BAEE
 * 
 * Sample time stamp:
 * 2016-10-14 14:22:52 +0000
 * 
 * @author gwowen
 *
 */
public class Attendee {
	public String beaconID;
	public String attendeeID;
	public String firstName;
	public String lastName;
	public String eventID;
	public String eventName;
	public String placeName;
	public String roomName;
	public String deviceID;
	public String date;
	public String timestamp;
	public String os;
	
	public String getBeaconID() {
		return beaconID;
	}

	public void setBeaconID(String beaconID) {
		this.beaconID = beaconID;
	}

	public String getAttendeeID() {
		return attendeeID;
	}

	public void setAttendeeID(String attendeeID) {
		this.attendeeID = attendeeID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String toString() {
		return new StringBuilder().
				append("{\"attendeeID\":").append(attendeeID). 
				append(", \"deviceID\":").append(deviceID).
				append(", \"date\":").append(date).
				append(", \"timestamp\":").append(timestamp).
				append(", \"os\":").append(os).
				append("}").toString();
	}
	
	public JsonNode toJson() {
		return Json.parse(this.toString());
	}
}
