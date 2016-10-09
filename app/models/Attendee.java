package models;

public class Attendee {
	
	public String beaconID;
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
	
}
