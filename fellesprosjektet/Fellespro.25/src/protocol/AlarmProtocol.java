package protocol;

import java.io.Serializable;

public class AlarmProtocol implements Serializable {

	private int appointmentId;
	private int alarm;
	
	public AlarmProtocol(int appointmentId, int alarm) {
		this.appointmentId = appointmentId;
		this.alarm = alarm;
	}
	
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public int getAppointmentId() {
		return appointmentId; 
	}
	
	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}
	
	public int getAlarm() {
		return alarm;
	}
	
	public String toString() {
		return appointmentId + " : " + alarm;
	}
	
}
