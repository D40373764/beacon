package actors;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.typesafe.config.ConfigFactory;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Attendance;
import play.Logger;
import play.libs.Json;

public class AttendanceActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public static String GET_ATTENDANCE_LIST = "GET_ATTENDANCE_LIST";

	@Override
	public void onReceive(Object arg0) throws Throwable {
	}
	
//	private final String cassandraHost = ConfigFactory.load().getString("cassandra.url");
//	private final Cluster cluster = Cluster.builder().addContactPoint(cassandraHost).build();
//	private final Session session = cluster.connect("my_keyspace");
//	private final PreparedStatement insertStatment = 
//			session.prepare("INSERT INTO attendance (deviceid, uuid, os, date, time) values (?,?,?,?,?);");
//	private final PreparedStatement selectStatment = 
//			session.prepare("SELECT * FROM attendance;");
//	private final BoundStatement insertbs = new BoundStatement(insertStatment);
//	private final BoundStatement selectbs = new BoundStatement(selectStatment);

//	public static Props props = Props.create(AttendanceActor.class);

//	@Override
//	public void onReceive(Object msg) throws Throwable {
//
//		if (msg instanceof Attendance) {
//			Logger.info("Received: Attendance");
//			Attendance attendance = (Attendance) msg;
//			
//			insertbs.bind(attendance.getDeviceID(), 
//					attendance.getBeaconID(), 
//					attendance.getOs(), 
//					attendance.getTimestamp(),
//					attendance.getTimestamp());
//			
//			ResultSet results = session.execute(insertbs);
//			sender().tell("uuid: " + ((Attendance) msg).getDeviceID() + " attendance saved", self());
//		}
//		else if (msg instanceof String) {
//			
//			switch ((String) msg) {
//				case "GET_ATTENDANCE_LIST":
//					ResultSet results = session.execute(selectbs);
//					List<Attendance> attendances = new ArrayList<Attendance>();
//					
//					for (Row row : results) {
//						Attendance attendance = new Attendance();
//						attendance.setDeviceID(row.getString("deviceid"));
//						attendance.setBeaconID(row.getString("uuid"));
//						attendance.setOs(row.getString("os"));
//						attendance.setTimestamp(row.getString("time"));
//						attendances.add(attendance);
//					}
//					
//					sender().tell(Json.toJson(attendances), self());
//					
//					break;
//				default:
//			}
//			
//		}
//		else {
//			unhandled(msg);
//		}
//		
//	}

}
