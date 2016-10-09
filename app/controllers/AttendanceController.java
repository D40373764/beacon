package controllers;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;

import actors.AttendeeActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.Attendee;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

public class AttendanceController extends Controller {

	@Inject FormFactory formFactory;
	
	ActorRef attendeeActor;
	
	private static String cassandraHost = ConfigFactory.load().getString("cassandra.url");

	
	@Inject
	public AttendanceController(ActorSystem system) {
		attendeeActor = system.actorOf(AttendeeActor.props);
	}

    public Result showAttendanceForm() {
    	String cassandraURL = ConfigFactory.load().getString(cassandraHost);
    	Logger.info("Cassandra host URL=" + cassandraURL);
    	return ok(views.html.attendanceform.render());
    }

	public CompletionStage<Result> postAttendance() {
    	Attendee attendance = formFactory.form(Attendee.class).bindFromRequest().get();
    	Logger.info("attendance=" + attendance.toString());
    	
		Future<Object> future = ask(attendeeActor, attendance, 1000);
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
