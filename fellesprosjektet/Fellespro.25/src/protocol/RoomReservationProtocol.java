package protocol;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class RoomReservationProtocol implements Serializable {

	private int capacity;
	private Timestamp startTime;
	private Timestamp endTime;
	
	public RoomReservationProtocol(int capacity, Timestamp startTime, Timestamp endTime) {
		this.capacity = capacity;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public Timestamp getStartTime() {
		return startTime;
	}
	
	public Timestamp getEndTime() {
		return endTime;
	}
	
}
