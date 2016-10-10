package controllers;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import actors.AttendeeActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.Attendee;
import play.Logger;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

public class AttendanceController extends Controller {

	@Inject FormFactory formFactory;
	
	ActorRef attendeeActor;
	
	@Inject
	public AttendanceController(ActorSystem system) {
		attendeeActor = system.actorOf(AttendeeActor.props);
	}

    public Result showAttendanceForm() {
    	return ok(views.html.attendanceform.render());
    }

	public CompletionStage<Result> postAttendance() {
    	Attendee attendee = Json.fromJson(request().body().asJson(), Attendee.class);
    	Logger.info("attendee=" + attendee.toString());
    	 	
		Future<Object> future = ask(attendeeActor, attendee, 1000);
		return FutureConverters.toJava(future)
				.thenApply(response -> ok((ObjectNode) response));
	}

	public CompletionStage<Result> getAttendances() {
    	Logger.info("Get attendance list");
    	
		Future<Object> future = ask(attendeeActor, AttendeeActor.GET_ATTENDEE_LIST, 1000);
		return FutureConverters.toJava(future)
				.thenApply(response -> ok((ArrayNode) response));
	}

    
}
