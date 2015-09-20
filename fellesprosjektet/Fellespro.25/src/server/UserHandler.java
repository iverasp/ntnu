package server;

import java.util.ArrayList;

import model.User;
import model.ParticipationStatus;
import database.DBControl;

public class UserHandler {
	DBControl dbc = new DBControl();
	
	
	public User getUserFromDB(String username){
		ArrayList<String> list = dbc.getUser(username);
		//System.out.println(list);
		if(list == null) return null;
		return new User(list);
	}
	
	public ArrayList<User> getAllUsers() {
		System.out.println("Getting all users. This can take a while. Sorry");
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<String> u = dbc.getAllUsers();

		//User(String username, String firstName, String lastName, String password, String email, String phone) 
		//

		
		for (int j=0; j<=u.size() -1; j+=6) {

			users.add(new User(u.get(j),u.get(j+1),u.get(j+2),u.get(j+3),u.get(j+4),u.get(j+5)));
		}
		return users;
	}
	
	public String insertUserInDB(User user){
		String username = user.getUsername();
		if(getUserFromDB(username) != null) return "Username taken";
		if( dbc.addUserDBC(user)) return "User " + username + " created";
		return "Error";
	}
	
	public String updateUserInDB(User user){
		String username = user.getUsername();
		if(getUserFromDB(username) == null) return "User not found";
		if( dbc.updateUserDBC(user)) return "Not supported";
		return "Error";
	}
	
	public boolean userExsist(String username){
		return getUserFromDB(username) != null;
	}
	
	public boolean updateParticipation(String username, int appointmentID, ParticipationStatus status,int hidden, int alarm){
		return dbc.updateParticipation(username,appointmentID,status.toString(),hidden,alarm);
	}
	
	public boolean updateParticipationStatus(String username, int appointmentID, ParticipationStatus status){
		//return dbc.updateParticipation(username,appointmentID,status.toString());
		return false;// ikke i bruk
	}
	
	public boolean updateParticipationAlarm(String username, int appointmentID, int alarm){
		//return dbc.updateParticipation(username,appointmentID,alarm);
		return false; // ikke i bruk
	}
	
	public ArrayList<User> convertToUsers(ArrayList<String> usernames){
		 ArrayList<User> users = new  ArrayList<User>();
		for (int j=0; j<=usernames.size() -1; j+=1) {
			users.add(getUserFromDB(usernames.get(j)));
		}
		
		return users;
	}

}
