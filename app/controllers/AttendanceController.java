package controllers;

import models.Attendee;
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
	public Result postAttendee() {
		Attendee attendee = Json.fromJson(request().body().asJson(), Attendee.class);
		if (dataStaxUtil.saveAttendee(attendee)) {
			return ok(Json.newObject().put("response", "success"));					
		} else {
			return Controller.badRequest(Json.newObject().put("response", "error"));								
		}
	}

	public Result getAttendees() {
		Logger.info("Get attendance list");
		return ok(dataStaxUtil.getAttendees());		
	}
    
}
