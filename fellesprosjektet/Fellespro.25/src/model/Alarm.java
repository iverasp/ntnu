package model;

import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Alarm {

	private String username;
	private int appointmentId;
	
	
	public Alarm(String username, int appointmentId) {
		this.username = username;
		this.appointmentId = appointmentId;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}
	
	
}
