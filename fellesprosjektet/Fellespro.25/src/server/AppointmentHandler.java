
package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.Appointment;
import model.ParticipationStatus;
import database.DBControl;

public class AppointmentHandler {
	DBControl dbc = new DBControl();
	UserHandler userHandler = new UserHandler();
	//NotificationHandler notificationHandler = new NotificationHandler();
	
	
	public Appointment getAppointmentFromDB(int id){
		ArrayList<String> list = dbc.getAppointmentFromDB(id);
		HashMap<String, ParticipationStatus> users = getParticipantsInAppointment(id);
		//System.out.println(list);
		if(list == null) return null;
		return new Appointment(list,users);
	}
	
	public int addAppointmenInDB(Appointment appointment){
		//String username = user.getUsername();
		//if(getUserFromDB(username) != null) return "Username taken";
		if(! userHandler.userExsist(appointment.getResponsibleUser())) return -1;

		int id = dbc.addAppointmentDBC(appointment);
		
		//notificationHandler.createAndSendInvitationNotifactionToUsers(appointment.getParticipants(),id);
		addParticpant(appointment.getParticipants(),id);
		
		// Add responsible user as participant
		addParticipant(appointment.getResponsibleUser(), id, ParticipationStatus.ACCEPTED.toString(), 0, 0);
		
		return id;
	}
	
	public void updateAppointmenInDB(Appointment appointment){
		//String username = user.getUsername();
		//if(getUserFromDB(username) != null) return "Username taken";
		
		int id = appointment.getId();

		ArrayList<String> usersToDelete = getAppointmentFromDB(id).getParticipantList();
		usersToDelete.removeAll(appointment.getParticipantList());
		
		deleteParticipants(usersToDelete,id);
		
		dbc.updateAppointmentDBC(appointment);
		addParticpant(appointment.getParticipants(),id);

	}
	
	public boolean appointmentExsist(int id){
		return getAppointmentFromDB(id) != null;
	}
	
	public void deleteParticipants(ArrayList<String> usersToDelete , int id){
		for (int j=0; j<=usersToDelete.size() -1; j++) {
			dbc.deleteParticipation(id,usersToDelete.get(j));
			System.out.println("User " + usersToDelete.get(j) + " has been removed from appointment");
		}
	}
	
	
	public HashMap<String, ParticipationStatus> getParticipantsInAppointment(int id){
		ArrayList<String> list = dbc.getParticipantsFromDB(id);
		HashMap<String, ParticipationStatus> returnList = new HashMap<String, ParticipationStatus>();

		for (int j=0; j<=list.size() -1; j+=2) {
			//System.out.println("User: " + list.get(j) + " has " + list.get(j + 1));
			returnList.put(list.get(j), ParticipationStatus.valueOf(list.get(j + 1)));
		}
		return returnList;
	}
	

	public boolean addParticpant( HashMap<String, ParticipationStatus> list, int appointmentID){
		
		for(Entry<String, ParticipationStatus> entry : list.entrySet()) {
		    String userName = entry.getKey();
		    String status = entry.getValue().toString();
		    String response = addParticipant(userName,appointmentID,status,0,0);
		    System.out.println(response + " " + userName);
		}
		return true;
	}
	
	public boolean particpantExist(String username, int appointmentID){
		return dbc.getParticipantFromDB(username,appointmentID) != null;
	}
	
	public boolean deleteAppointment(int id){
		return dbc.deleteAppointment(id);
	}
	
	
	public  ArrayList<Appointment> getUserAppointments(String username){
		ArrayList<String> appoinmentIds = dbc.getUsersAppointmentsFromDB(username);
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		System.out.println("Getting " + appoinmentIds.size()/8 + " appointments");

		for (int j=0; j<=appoinmentIds.size() -1; j+=8) {
			//appointments.add(getAppointmentFromDB(Integer.valueOf(appoinmentIds.get(j))));
			Appointment ap = new Appointment(appoinmentIds.subList(j, j+8));
			HashMap<String, ParticipationStatus> users = getParticipantsInAppointment(ap.getId());
			ap.setParticipants(users);
			appointments.add(ap);
		}
		return appointments;
	}
	
	public String addParticipant(String username, int appointmentID, String status,int hidden, int alarm){
		if(! userHandler.userExsist(username)) return "User does not exsist";
		if(! appointmentExsist(appointmentID)) return "Appointment does not exsist";
		if(particpantExist(username,appointmentID)){
			dbc.updateParticipation(username,appointmentID,status,hidden,alarm);
			return "Participant updated";
		}else{
			if (dbc.addParticipantDBC(username,appointmentID,status,hidden,alarm)) return "Participant created";
			return "There was an error creating a participant";
		}

	}
	
	public void updateAlarm(String username, int appointmentId, int alarm) {
		dbc.setAlarm(alarm, username, appointmentId);
	}
}

