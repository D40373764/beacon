package actors;

import java.sql.*;

import javax.inject.Inject;

import akka.actor.UntypedActor;
import models.Attendance;
import play.Logger;
import play.db.Database;
import play.libs.Json;

public class BeaconActor extends UntypedActor {

	private Database db;
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	
	@Inject
	public BeaconActor(Database db) {
		this.db = db;
	}
	
	@Override
	public void onReceive(Object msg) throws Throwable {

		if (msg instanceof Attendance) {
			Logger.info("Received: Attendance");
//			Attendance attendance = (Attendance) msg;
			sender().tell("Hello!", self());
		}
		else if (msg instanceof String) {

			try {
//				connection = db.getConnection();
//				statement = connection.createStatement();
//				rs = statement.executeQuery("SELECT VERSION()");
//				
//				if (rs.next()) {
//					System.out.println(rs.getString(1));
//				}
				
				sender().tell("World!", self());
				
			}
			catch (Exception ex) {
				
			}
			
		}
		else {
			unhandled(msg);
		}
		
		connection.close();
	}

}
