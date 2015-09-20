package model;

import java.io.Serializable;

public class Notification implements Serializable {
	
	private int appointmentId;
	private String type;
	private String message;
	
	
	public Notification(int appointmentId, String type, String message) {
		this.appointmentId = appointmentId;
		this.type = type;
		this.message = message;
	}


	
	@Override
	public String toString() {
		return  message + " [Appointment ID=" + appointmentId + ", type: " + type + " notification] ";
	}



	public void setAppointment(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public int getAppointment() {
		return appointmentId;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
