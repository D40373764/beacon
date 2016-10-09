package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.ActorSystem;
import io.netty.util.internal.StringUtil;
import models.Attendee;
import play.Logger;
import play.data.FormFactory;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class BeaconController extends Controller {

	@Inject FormFactory formFactory;
	
	private Database db;
	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement prepStmt = null;
	private ResultSet rs = null;

	@Inject
	public BeaconController(ActorSystem system, Database db) {
		this.db = db;
	}

    public Result showAttendanceForm() {
    	return ok(views.html.beaconform.render());
    }

    public Result postAttendee() {
    	JsonNode json = request().body().asJson();
    	if (null == json) {
    		return badRequest("Expecting Json data");
    	}
    	
		String deviceID = json.findPath("deviceID").textValue();
		String beaconID = json.findPath("beaconID").textValue();
		String os = json.findPath("os").textValue();
		String timestamp = json.findPath("timestamp").textValue();
		String failure = "";
		
		connection = db.getConnection();
		try {
			prepStmt = connection.prepareStatement("INSERT INTO attendance VALUES (?,?,?,?);");
			prepStmt.setString(1, deviceID);
			prepStmt.setString(2, beaconID);
			prepStmt.setString(3, os);
			prepStmt.setString(4, timestamp);

			int code = prepStmt.executeUpdate();
			Logger.info("return code=" + code);
			

		} catch (SQLException e) {
			failure = e.getMessage();
		} finally {
			try {
				if (prepStmt != null) {
					prepStmt.close();
				}
				if (connection != null) {
					connection.close();					
				}
			} catch (SQLException e1) {
				Logger.error(e1.getMessage());
			}			
		}

		if (!StringUtil.isNullOrEmpty(failure)) {
			return notFound(Json.newObject().put("response", failure));			
		}
		
	    return ok(Json.newObject().put("response", "success"));
    }
    
	public Result postAttendance() {
    	Logger.info("Post attendance");
    	
    	Attendee attendance = formFactory.form(Attendee.class).bindFromRequest().get();
    	String failure = "";
    	
		connection = db.getConnection();
		try {
			prepStmt = connection.prepareStatement("INSERT INTO attendance VALUES (?,?,?,?);");
			prepStmt.setString(1, attendance.getDeviceID());
			prepStmt.setString(2, attendance.getBeaconID());
			prepStmt.setString(3, attendance.getOs());
			prepStmt.setString(4, attendance.getTimestamp());

			int code = prepStmt.executeUpdate();
			Logger.info("return code=" + code);
			

		} catch (SQLException e) {
			failure = e.getMessage();
		} finally {
			try {
				if (prepStmt != null) {
					prepStmt.close();
				}
				if (connection != null) {
					connection.close();					
				}
			} catch (SQLException e1) {
				Logger.error(e1.getMessage());
			}			
		}

		if (!StringUtil.isNullOrEmpty(failure)) {
			return notFound(failure);			
		}
		
    	return ok(Json.newObject().put("response", "success"));
	}

	public Result getAttendances() {
    	Logger.info("Get attendance list");
    	
		connection = db.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM attendance;");
			List<Attendee> attendances = new ArrayList<Attendee>();

			while (rs.next()) {
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				System.out.println(rs.getString(4));
				Attendee attendance = new Attendee();
				attendance.setDeviceID(rs.getString(1));
				attendance.setBeaconID(rs.getString(2));
				attendance.setOs(rs.getString(3));
				attendance.setTimestamp(rs.getString(4));
				attendances.add(attendance);
			}
			
			connection.close();
	    	return ok(Json.toJson(attendances));

		} catch (SQLException e) {
			try {
				connection.close();
			} catch (SQLException e1) {
				//
			}
			return notFound(Json.newObject().put("response", e.getMessage()));
		}
    	
	}
}
