package models;

public class FeedbackIn {

	private String attendeeID;
	private String beaconID;
	private String eventID;
	private String questionID;
	private int rating;
	private String comment;
	private String timestamp;
	
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
	public String getQuestionID() {
		return questionID;
	}
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
}
