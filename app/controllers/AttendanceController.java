package controllers;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.typesafe.config.ConfigFactory;

import actors.AttendanceActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.Attendance;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;


@Singleton
public class AttendanceController extends Controller {

//	@Inject FormFactory formFactory;
//	
//	ActorRef attendanceActor;
//	
//	private static String cassandraHost = ConfigFactory.load().getString("cassandra.url");
//
//	
//	@Inject
//	public AttendanceController(ActorSystem system) {
//		attendanceActor = system.actorOf(AttendanceActor.props);
//	}
//
//    public Result showAttendanceForm() {
//    	String cassandraURL = ConfigFactory.load().getString("cassandra.url");
//    	Logger.info("Cassandra host URL=" + cassandraURL);
//    	return ok(views.html.attendanceform.render());
//    }
//
//	public CompletionStage<Result> postAttendance() {
//    	Attendance attendance = formFactory.form(Attendance.class).bindFromRequest().get();
//    	Logger.info("attendance=" + attendance.toString());
//    	
//		Future<Object> future = ask(attendanceActor, attendance, 1000);
//		return FutureConverters.toJava(future)
//				.thenApply(response -> ok((String) response));
//	}
//
//	public CompletionStage<Result> getAttendances() {
//    	Logger.info("Get attendance list");
//    	
//		Future<Object> future = ask(attendanceActor, "GET_ATTENDANCE_LIST", 1000);
//		return FutureConverters.toJava(future)
//				.thenApply(response -> ok((ArrayNode) response));
//	}

    
}
