package protocol;

import java.io.Serializable;

public class UserAppointmentsProtocol implements Serializable {

	private String username;
	private int weekNumber;
	
	public UserAppointmentsProtocol(String username, int weekNumber) {
		this.username = username;
		this.weekNumber = weekNumber;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getWeekNumber() {
		return weekNumber;
	}
}
