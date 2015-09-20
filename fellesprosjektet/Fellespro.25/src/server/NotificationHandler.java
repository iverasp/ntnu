package server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.Alarm;
import model.Appointment;
import model.Notification;
import model.ParticipationStatus;
import database.DBControl;

public class NotificationHandler {
	DBControl dbc = new DBControl();
	//AppointmentHandler aph = new AppointmentHandler();

	private ArrayList<String> getAppointmentsWithin(Timestamp time, int timebeforeeAppointment){

		return dbc.getAppointmentsInTimeInterval(time,timebeforeeAppointment);
	}
	
	public ArrayList<Notification> getAllNotificationsForUser(String username){
		ArrayList<String> notiStrings = dbc.getAllNotificationForUserDB(username);
		ArrayList<Notification> notifFinished = new ArrayList<Notification>();
		System.out.println(notiStrings);
		//int appointmentId, String type, String message
		
		for (int j=0; j<=notiStrings.size() -1; j+=3) {
			Notification newNot = createNotificationModel(Integer.valueOf(notiStrings.get(j+2)),notiStrings.get(j),notiStrings.get(j+1));
			notifFinished.add(newNot);
		}
		return notifFinished;
	}
	
	private ArrayList<String> getUsersToSendReminderToo(int appointmentId,Timestamp appointmentStart,Timestamp currentTime){
		long minuttesUntilAppoitnmentStart =  (appointmentStart.getTime() - currentTime.getTime())/ (60 * 1000);
		//System.out.println("Current time: " + currentTime + ", Appointment start: " + appointmentStart + " Minutter til avalen starter: " + minuttesUntilAppoitnmentStart);
		
		return dbc.getParticipantsInTimeInterval(appointmentId,(int) minuttesUntilAppoitnmentStart);
	}
	
	
//Kjores ofte. Sjekker etter alarmer
	public void createAndSendReminderNotifications(Timestamp currentTime, int timebeforeeAppointment){
		
		ArrayList<String> appointments = getAppointmentsWithin(currentTime,timebeforeeAppointment);
		

		
		if(appointments == null) return;
		
		for (int j=0; j<=appointments.size() -1; j+=2) {
			
			int appointmentId = Integer.valueOf(appointments.get(j));
			ArrayList<String> usersToSendReminderToo = getUsersToSendReminderToo(appointmentId,Timestamp.valueOf(appointments.get(j+1)),currentTime);
			if(usersToSendReminderToo.size() == 0) return;
			
			int reminderID = getReminderNotification(appointmentId);
			if(reminderID < 0){
				String reminderMessage = createReminderString(appointmentId);
				reminderID = createReminderNotification(reminderMessage,appointmentId);
			}
			
			System.out.println("Brukere som skal varsles for avtale " + appointments.get(j) + " " + usersToSendReminderToo);
			createUserNotifactionsForUsers(usersToSendReminderToo,reminderID);
			//createNotifications(Integer.valueOf(appointments.get(j)), Timestamp.valueOf(appointments.get(j + 1)));
		}
		
	}
	
	public void createUserNotifactionsForUsers(ArrayList<String> users, int notifactionID ){
		for (int j=0; j<=users.size() -1; j+=1) {
			createUserNotification(notifactionID,users.get(j));
		}
	}
	
	public void createUserNotifactionsForUsers(HashMap<String, ParticipationStatus> users, int notifactionID ){
		for (String username : users.keySet()) {
			createUserNotification(notifactionID,username);
		}
	}
	
	public boolean deleteNotification(int notificationId,String username){
		return dbc.deleteNotification(notificationId,username);
	}
	
	public void deleteUserNotifications(String username) {
		dbc.deleteUserNotifications(username);
	}
	
	public String createReminderString(int appointmentId){
		//Appointment appointment = aph.getAppointmentFromDB(appointmentId);
		//String message = "Reminder for '" + appointment.getTitle() + "' at " + appointment.getStartTime().getHours() + ":" + appointment.getStartTime().getMinutes();
		//return message;
		return null;
	}
	
	public String createInvitationString(int appointmentId){
		//Appointment appointment = aph.getAppointmentFromDB(appointmentId);
		//String message = "Invitation to '" + appointment.getTitle() + "' at " + appointment.getStartTime();
		//return message;
		return null;
	}
	
	public void createAndSendInvitationNotifactionToUsers(HashMap<String, ParticipationStatus> list, int id){
		String reminderMessage = createInvitationString(id); 
		int notiID = createReminderNotification(reminderMessage,id);
		createUserNotifactionsForUsers(list,notiID);
	}
	
	
	// Denne funksjonen fungerer ikke
	public boolean userNotificationExist(int notifactionID, String username){
		return dbc.getNotificationFromUserNotificationFromDB(username, notifactionID) != null;
	}
	
	//Alle som returnere int returner IDen til en notification
	public int getReminderNotification(int appointmentId){
		return dbc.getNotificationFromDB("REMINDER", appointmentId);
	}
	
	public int getNotification(int appointmentId) {
		return dbc.getNotificationFromDB("NORMAL", appointmentId);
	}
	
	public int createReminderNotification(String description, int appointmentId){
		if(getReminderNotification(appointmentId) != -1) return -2;
		return dbc.createNotifactions(appointmentId, "REMINDER", description);
	}
	
	public int createNotification(String description, String type, int appointmentId){
		return dbc.createNotifactions(appointmentId, type, description);
	}

	public void createUserNotification(int notifactionID, String username){
		//System.out.println("usernotification exists: " + userNotificationExist(notifactionID,username));
		//if(userNotificationExist(notifactionID,username)) return;
		//System.out.println("user notif created");
		dbc.createUserNotifactions(notifactionID, username);
	}
	
	private Notification createNotificationModel(int appointmentId, String type, String message){
		return new Notification(appointmentId, type, message);
	}
	
	
	public ArrayList<Alarm> getTriggeredAlarms() {
		ArrayList<Alarm> triggeredAlarms = dbc.getTriggeredAlarms();
		// TODO set alarm to 0
		for (Alarm alarm : triggeredAlarms) {
			dbc.setAlarm(0, alarm.getUsername(), alarm.getAppointmentId());
		}
		return triggeredAlarms;
	}
	
	

	

}
