package models;

public class Attendance {
	
	public String deviceID;
	public String beaconID;
	public String os;
	public String timestamp;
	
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getBeaconID() {
		return beaconID;
	}
	public void setBeaconID(String beaconID) {
		this.beaconID = beaconID;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
