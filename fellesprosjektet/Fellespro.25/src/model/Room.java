package model;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Date;

public class Room implements Serializable {
	private String roomNumber;
	private int capacity;
	

	public Room() {
		
	}
	public Room(String roomNumber, int capacity) {
		this.roomNumber = roomNumber;
		this.capacity = capacity;
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	
	public boolean isReserved(Date startTime, Date endTime) {
		// TODO Check server if room is reserved between startTime and endTime
		return false;
	}
	
	public void reserve(Date startTime, Date endTime) {
		// TODO Reserve room between startTime and endTime on the server
	}
	
	public void unreserve(Date startTime, Date endTime) {
		// TODO Remove the reservation of the room between startTime and endTime on the server
	}
	
	@Override
	public String toString() {
		return  roomNumber + " [" + capacity
				+ " personer]";
	}
}
