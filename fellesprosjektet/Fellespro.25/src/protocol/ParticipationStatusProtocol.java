package protocol;

import java.io.Serializable;

import model.ParticipationStatus;

public class ParticipationStatusProtocol implements Serializable {

	String username;
	int appointmentId;
	ParticipationStatus status;
	
	public ParticipationStatusProtocol(String username, int appointmentId, ParticipationStatus status) {
		this.username = username;
		this.appointmentId = appointmentId;
		this.status = status;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}
	
	public ParticipationStatus getStatus() {
		return status;
	}
	
}
