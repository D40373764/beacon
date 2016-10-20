package models;

import java.util.Date;

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
	private String eventID;
	private String attendeeID;
	private String firstName;
	private String lastName;
	private String division;
	private String department;
	private String eventName;
	private String eventAddressL1;
	private String eventAddressL2;
	private String eventCity;
	private String eventState;
	private String eventZip;
	private String eventCountry;
	private String timestamp;	
	
	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
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

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}


	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventAddressL1() {
		return eventAddressL1;
	}

	public void setEventAddressL1(String eventAddressL1) {
		this.eventAddressL1 = eventAddressL1;
	}

	public String getEventAddressL2() {
		return eventAddressL2;
	}

	public void setEventAddressL2(String eventAddressL2) {
		this.eventAddressL2 = eventAddressL2;
	}

	public String getEventCity() {
		return eventCity;
	}

	public void setEventCity(String eventCity) {
		this.eventCity = eventCity;
	}

	public String getEventState() {
		return eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public String getEventZip() {
		return eventZip;
	}

	public void setEventZip(String eventZip) {
		this.eventZip = eventZip;
	}

	public String getEventCountry() {
		return eventCountry;
	}

	public void setEventCountry(String eventCountry) {
		this.eventCountry = eventCountry;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String toString() {
		return new StringBuilder().
				append("{\"attendeeID\":").append(attendeeID). 
				append(", \"eventID\":").append(eventID).
				append(", \"firstName\":").append(firstName).
				append(", \"lastName\":").append(lastName).
				append(", \"eventName\":").append(eventName).
				append("}").toString();
	}
	
	public JsonNode toJson() {
		return Json.parse(this.toString());
	}
}
