package controllers;

import models.Attendee;
import models.AttendeeIn;
import models.Event;
import models.Feedback;
import models.FeedbackIn;
import models.Question;
import models.RoomAttendance;
import models.RoomLocation;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.DataStaxUtil;

public class AttendanceController extends Controller {

	private static DataStaxUtil dataStaxUtil = new DataStaxUtil();
	
    public Result showAttendeeForm() {
    	return ok(views.html.attendanceform.render());
    }

    
    public Result postAttendee() {
		AttendeeIn attendeeIn = Json.fromJson(request().body().asJson(), AttendeeIn.class);
		String beaconID = attendeeIn.getBeaconID();
		String eventID = attendeeIn.getEventID();
		RoomLocation room = dataStaxUtil.getLocation(beaconID);
		Event event = dataStaxUtil.getEvent(eventID);
		
		if ( saveAttendeeByEvent(attendeeIn, event) && saveRoomAttendanceByEvent(attendeeIn, event.getName(), room)) {
			return ok(Json.newObject().put("response", "success"));	
		}
		else {
			return badRequest(Json.newObject().put("response", "error"));
		}
    }

    public Result getAttendeesByEvent(String eventID) {
		Logger.info("Get attendance list");
		return ok(dataStaxUtil.getAllAttendeeByEvent());		
    }

    public Result getQuestionsByEvent(String eventID) {
		Logger.info("Get question list");
		return ok(dataStaxUtil.getQuestionsByEvent(eventID));		
    }

    public Result postFeedback() {
		FeedbackIn feedbackIn = Json.fromJson(request().body().asJson(), FeedbackIn.class);
		
		if (saveFeedback(feedbackIn)) {
			return ok(Json.newObject().put("response", "success"));	
		}
		else {
			return badRequest(Json.newObject().put("response", "error"));
		}
    }

	public boolean saveFeedback(FeedbackIn feedbackIn) {

		String beaconID = feedbackIn.getBeaconID();
		String eventID = feedbackIn.getEventID();
		String questionID = feedbackIn.getQuestionID();
		
		RoomLocation room = dataStaxUtil.getLocation(beaconID);
		Event event = dataStaxUtil.getEvent(eventID);
		User user = dataStaxUtil.getUser(feedbackIn.getAttendeeID());
		Logger.info("feedbackIn.getAttendeeID()=" + feedbackIn.getAttendeeID());
		Question question = dataStaxUtil.getQuestion(eventID, questionID);

		Feedback feedback = new Feedback();
		feedback.setAttendeeID(feedbackIn.getAttendeeID());
		feedback.setEventID(feedbackIn.getEventID());
		feedback.setQuestionID(questionID);
		feedback.setAttendeeAddressL1(room.getAddressL1());
		feedback.setAttendeeAddressL2(room.getAddressL2());
		feedback.setAttendeeCity(room.getCity());
		feedback.setAttendeeState(room.getState());
		feedback.setAttendeeZip(room.getZip());
		feedback.setAttendeeCountry(room.getCountry());
		feedback.setAttendeeFirstName(user.getFirstName());
		feedback.setAttendeeLastName(user.getLastName());
		feedback.setEventName(event.getName());
		feedback.setQuestionText(question.getQuestionText());
		feedback.setRatingAmount(feedbackIn.getRating());
		feedback.setRatingComment(feedbackIn.getComment());
		feedback.setRatingDate(feedbackIn.getTimestamp());
				
		if (dataStaxUtil.saveFeedback(feedback)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean saveAttendeeByEvent(AttendeeIn attendeeIn, Event event) {
		
		Attendee attendee = new Attendee();		
		attendee.setEventID(attendeeIn.getEventID());
		attendee.setAttendeeID(attendeeIn.getAttendeeID());
		attendee.setEventName(event.getName());
		attendee.setEventAddressL1(event.getAddressL1());
		attendee.setEventAddressL2(event.getAddressL2());
		attendee.setEventCity(event.getCity());
		attendee.setEventState(event.getState());
		attendee.setEventZip(event.getZip());
		attendee.setEventCountry(event.getCountry());
		attendee.setTimestamp(attendeeIn.getTimestamp());
		
		attendee = dataStaxUtil.setUserData(attendee);
		
		
		if (dataStaxUtil.saveAttendeeByEvent(attendee)) {
			return true;
			//return ok(Json.newObject().put("response", "success"));					
		} else {
			return false;
			//return Controller.badRequest(Json.newObject().put("response", "error"));								
		}
	}

	
    /**
     * Sample post body:
     * {
	 *	  "beaconID": "6FBBEF7C-F92C-471E-8D5C-470E9B367FDB",
	 *	  "attendeeID": "D40373764",
	 *	  "firstName": "Gwowen",
	 *	  "lastName": "Fu",
	 *	  "eventID": "1",
	 *	  "eventName": "IT All Hands Meeting",
	 *	  "placeName": "DeVry Education Group - Home Office",
	 *	  "roomName": "Mobile Solutions Area",
	 *	  "deviceID": "test",
	 *	  "os": "Android",
	 *	  "timestamp": "2016-10-14 14:22:52 +0000"
	 *	}
	 *
     * @return
     */
	public boolean saveRoomAttendanceByEvent(final AttendeeIn attendeeIn, final String eventName, final RoomLocation room) {
		
		RoomAttendance roomAttendance = new RoomAttendance();		
		roomAttendance.setAttendeeID(attendeeIn.getAttendeeID());
		roomAttendance.setEventID(attendeeIn.getEventID());
		roomAttendance.setEventName(eventName);
		roomAttendance.setRoomName(room.getRoomName());
		roomAttendance.setRoomFloor(room.getRoomFloor());
		roomAttendance.setLocationName(room.getPlaceName());
		roomAttendance.setAddressL1(room.getAddressL1());
		roomAttendance.setAddressL2(room.getAddressL2());
		roomAttendance.setLocationCity(room.getCity());
		roomAttendance.setLocationState(room.getState());
		roomAttendance.setLocationZip(room.getZip());
		roomAttendance.setLocationCountry(room.getCountry());
		roomAttendance.setTimestamp(attendeeIn.getTimestamp());
		
		roomAttendance = dataStaxUtil.setUserName(roomAttendance);
				
		if (dataStaxUtil.saveRoomAttendance(roomAttendance)) {
			return true;
		} else {
			return false;
		}
	}

	public Result getRoomAttendanceByEvent(String eventID) {
		Logger.info("Get attendance list");
		return ok(dataStaxUtil.getAllRoomAttendanceByEvent());		
	}
    
}
