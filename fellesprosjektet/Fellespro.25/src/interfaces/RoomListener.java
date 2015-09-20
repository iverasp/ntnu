package interfaces;

import java.util.ArrayList;

import model.Room;

public interface RoomListener {

	public void onGetAvailiableRooms(ArrayList<Room> rooms);
	
}
