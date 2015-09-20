package server;

import java.sql.Timestamp;
import java.util.ArrayList;

import model.Room;
import database.DBControl;

public class RoomHandler {
	DBControl dbc = new DBControl();

	public ArrayList<String> getAvailiableRooms(int capacity, Timestamp start, Timestamp end){
		return dbc.getUnoccupiedMeetingRoomFromDB(capacity,start,end);
		//ArrayList<String> roomList = 
		//return createRoomList(roomList);
	}
	
	private ArrayList<Room> createRoomList(ArrayList<String> rooms){
		ArrayList<Room> roomList = new ArrayList<Room>();
		for (int j=0; j<=rooms.size() -1; j+=2) {
			Room room = createRoomObject(rooms.get(j),rooms.get(j+1));
			roomList.add(room);
		}
		
		return roomList;
	}
	
	private Room createRoomObject(String roomnumber, String capacity){
		return new Room(roomnumber,Integer.parseInt(capacity));
	}
	
	public ArrayList<Room> getAllRooms(){
		ArrayList<String> roomList = dbc.getAllMeetingRooms();
		return createRoomList(roomList); 
	}
	
	public boolean isReserved(String roomnumber, Timestamp startTime, Timestamp endTime){
		System.out.println(dbc.isMeetingRoomReserved(roomnumber,startTime,endTime));
		if(roomnumber.equals("None")) return false;
		return dbc.isMeetingRoomReserved(roomnumber,startTime,endTime) == null;
	}
	

}
