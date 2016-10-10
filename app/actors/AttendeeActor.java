package actors;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Attendee;
import play.Logger;
import play.libs.Json;
import services.DataStaxUtil;

public class AttendeeActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public static final String GET_ATTENDEE_LIST = "GET_ATTENDEE_LIST";
	
	private Session session = DataStaxUtil.getSession();
	PreparedStatement insertStatment = 
			session.prepare("INSERT INTO attendee (beacon_id, device_id, date, time, os) values (?,?,?,?,?);");
	PreparedStatement selectStatment = 
			session.prepare("SELECT * FROM attendee;");
	BoundStatement insertbs = new BoundStatement(insertStatment);
	BoundStatement selectbs = new BoundStatement(selectStatment);
	

	public static Props props = Props.create(AttendeeActor.class);

	@Override
	public void onReceive(Object msg) throws Throwable {
		
		if (msg instanceof Attendee) {
			Logger.info("Received: Attendee");
			Attendee attendee = (Attendee) msg;
			
			insertbs.bind(
				attendee.getDeviceID(), 
				attendee.getBeaconID(), 
				attendee.getTimestamp().substring(0, 10),
				attendee.getTimestamp(),
				attendee.getOs()
			);
			
			session.execute(insertbs);
			sender().tell(Json.newObject().put("response", "success"), self());
		}
		else if (msg instanceof String) {
			
			switch ((String) msg) {
				case GET_ATTENDEE_LIST:
					ResultSet results = session.execute(selectbs);
					List<Attendee> attendees = new ArrayList<Attendee>();
					
					for (Row row : results) {
						Attendee attendee = new Attendee();
						attendee.setBeaconID(row.getString("beacon_id"));
						attendee.setDeviceID(row.getString("device_id"));
						attendee.setDate(row.getString("date"));
						attendee.setTimestamp(row.getString("time"));
						attendee.setOs(row.getString("os"));
						attendees.add(attendee);
					}
					
					sender().tell(Json.toJson(attendees), self());
					
					break;
				default:
			}
			
		}
		else {
			unhandled(msg);
		}
		
	}

}
