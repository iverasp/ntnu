package server;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import protocol.LoginProtocol;
import protocol.ServerFeedback;
import protocol.ServerResponse;
import model.Alarm;
import model.Appointment;
import model.Group;
import model.Notification;
import model.ParticipationStatus;
import model.Room;
import model.User;
import database.DBControl;

public class CalServer {


	public static final int LOGIN_OK = 1;
	public static final int LOGIN_ALREADY_LOGGED_IN = 2;
	public static final int LOGIN_WRONG_USERNAME_OR_PASSWORD = 3;

	private ServerNetworkController network;
	private DBControl dbControl;



	// Handlers
	private UserHandler userHandler;
	private AppointmentHandler appointmentHandler;
	private MailHandler mailHandler;
	private ParticipationHandler participationHandler;
	private RoomHandler roomHandler;
	private NotificationHandler notificationHandler;
	private GroupHandler groupHandler;

	//


	public CalServer() {

		network = new ServerNetworkController(this);
		dbControl = new DBControl();


		// Initialize 
		userHandler = new UserHandler();
		appointmentHandler = new AppointmentHandler();
		mailHandler = new MailHandler();
		participationHandler = new ParticipationHandler();
		roomHandler = new RoomHandler();
		notificationHandler = new NotificationHandler();
		groupHandler = new GroupHandler();
		
		AlarmThread alarmThread = new AlarmThread();
		alarmThread.start();
	}

	private class AlarmThread extends Thread {
		public void run() {
			while (true) {
				//System.out.println("Alarmthread check");
				ArrayList<Alarm> triggeredAlarms = notificationHandler.getTriggeredAlarms();
				for (Alarm alarm : triggeredAlarms) {
					Appointment appointment = appointmentHandler.getAppointmentFromDB(alarm.getAppointmentId());
					Notification notification = new Notification(appointment.getId(), "REMINDER", "P&minnelse til "+ appointment.getEventString());
					sendNotification(alarm.getUsername(), notification);
				}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
			
	
	
	public ArrayList<String> getUserList() {
		return network.getUserList();
	}

	public boolean getUserConnected(String username) {
		return getUserList().contains(username);
	}


	//
	// Login
	//
	public int checkLogin(String username, String password) {
		// Check if user is already logged in
		if (getUserConnected(username)) {
			return LOGIN_ALREADY_LOGGED_IN;
		}
		if (dbControl.loginDBC(username, password)) {
			return LOGIN_OK;
		} else {
			return LOGIN_WRONG_USERNAME_OR_PASSWORD;
		}
	}

	public void sendLoginResponse(String client) {
		//User user = userHandler.getUserFromDB(client);
		network.sendServerResponse(client, "receiveLogin", client);

		// Send server feedback
		network.sendServerFeedback(client, ServerFeedback.OK, "login", "");
	}




	//
	// Appointment
	//
	public void createAppointment(String client, Appointment appointment) {
		
		// TODO check if room is already reserved
		if (!appointment.getReservedRoom().equals("None")) {
			if (roomHandler.isReserved(appointment.getReservedRoom(), appointment.getStartTime(), appointment.getEndTime())) {
				network.sendServerFeedback(client, ServerFeedback.ERROR, "makeAppointment", "This room is already reserved for this time. Please choose another.");
				Notification notification = new Notification(appointment.getId(), "NORMAL", "Moterom "+ appointment.getReservedRoom() + " er opptatt på '" + appointment.getTitle() + "' sin nye tid. Velg et annet rom.");
				sendNotification(client, notification);
				return;
			}
		}
		
		
		int appointmentId = appointmentHandler.addAppointmenInDB(appointment);
		Appointment newAppointment = appointmentHandler.getAppointmentFromDB(appointmentId);
		for (String username : newAppointment.getParticipants().keySet()) {
			if (!username.equals(client)) {
				Notification notification = new Notification(appointmentId, "NORMAL", "Du har blitt invitert til "+newAppointment.getEventString());
				sendNotification(username, notification);
			}
			if (getUserConnected(username)) {
				network.sendServerResponse(username, "receiveAppointment", newAppointment);
			}
		}
		network.sendServerFeedback(client, ServerFeedback.OK,  "makeAppointment", "");
		
		MailHandler mail = new MailHandler();
		mail.sendMailToAllAdresses(appointment);

	}

	public void updateAppointment(String client, Appointment appointment) {
		Appointment oldAppointment = appointmentHandler.getAppointmentFromDB(appointment.getId());
		
		MailHandler mail = new MailHandler();
		mail.sendMailToAllAdresses(appointment);
		

		// Check if some users has already accepted or declined
		for (String username : appointment.getParticipantList()) {
			if (oldAppointment.getParticipants().containsKey(username)) {
				if (appointment.getParticipantStatus(username) == ParticipationStatus.INVITED) {
					if (oldAppointment.getParticipantStatus(username) == ParticipationStatus.ACCEPTED) {
						appointment.setParticipantStatus(username, ParticipationStatus.ACCEPTED);
					} else if (oldAppointment.getParticipantStatus(username) == ParticipationStatus.DECLINED) {
						appointment.setParticipantStatus(username, ParticipationStatus.DECLINED);
					}
				}
			} else {
				// New user added, send invite notification
				Notification notification = new Notification(appointment.getId(), "NORMAL", "Du har blitt invitert til "+ appointment.getEventString());
				sendNotification(username, notification);
			}
		}
		
		
		// Set responsible user as accepted participant
		appointment.setParticipantStatus(client, ParticipationStatus.ACCEPTED);
		
		// check if time has changed
		if (!appointment.getStartTime().equals(oldAppointment.getStartTime()) || !appointment.getEndTime().equals(oldAppointment.getEndTime())) {
			// Loop through all participants that have accepted and set them as invited
			for (String username : appointment.getParticipantList()) {
				if (appointment.getParticipantStatus(username) == ParticipationStatus.ACCEPTED && !username.equals(appointment.getResponsibleUser())) {
					appointment.setParticipantStatus(username, ParticipationStatus.INVITED);
				}
			}

			String oldRoom = appointment.getReservedRoom();
			
			
			// Set room to None
			appointment.setReservedRoom("None");
			
			// update appointment in db
			appointmentHandler.updateAppointmenInDB(appointment);
			
			// Try check if old room is reserved
			if (roomHandler.isReserved(oldRoom, appointment.getStartTime(), appointment.getEndTime())) {
				// Send error back
				Notification notification;
				network.sendServerFeedback(client, ServerFeedback.ERROR, "makeAppointment", "This room is reserved for the new time. Reserve another room.");
				if(oldRoom.equals("None"))  notification = new Notification(appointment.getId(), "NORMAL", "Valgte moterom er opptatt p& '" + appointment.getTitle() + "' sin nye tid. Velg et annet rom.");
				else  notification = new Notification(appointment.getId(), "NORMAL", "Moterom '"+ appointment.getReservedRoom() + "' er opptatt p& '" + appointment.getTitle() + "' sin nye tid. Velg et annet rom.");
				
				sendNotification(client, notification);
			} else {
				// Reserve room again
				appointment.setReservedRoom(oldRoom);
			}
			
		}
		
		// Update appointment in database
		appointmentHandler.updateAppointmenInDB(appointment);
		
		
		// Send info back
		for (String username : appointment.getParticipantList()) {
			if (appointment.getParticipantStatus(username) == ParticipationStatus.HIDDEN) {
				continue;
			}
			
			if (!username.equals(client)) {
				Notification notification = new Notification(appointment.getId(), "NORMAL", appointment.getEventString() + " har blitt oppdatert.");
				sendNotification(username, notification);
			
			}
			if (getUserConnected(username)) {
				network.sendServerResponse(username, "receiveAppointment", appointment);
			}
		}
		
		// Delete appointment from the users that has been deleted
		for (String username : oldAppointment.getParticipantList()) {
			if (username.equals(client)) {
				continue;
			}
			if (!appointment.getParticipants().containsKey(username)) {
				Notification notification = new Notification(appointment.getId(), "NORMAL", "Du har blitt fjernet fra "+ appointment.getEventString());
				sendNotification(username, notification);
				if (getUserConnected(username)) {
					network.sendServerResponse(username, "receiveDeleteAppointment", appointment.getId());
				}
			}
		}
	}

	public void deleteAppointment(String client, int appointmentId) {
		Appointment appointment = appointmentHandler.getAppointmentFromDB(appointmentId);
		appointmentHandler.deleteAppointment(appointmentId);
		for (String username : appointment.getParticipants().keySet()) {
			if (appointment.getParticipantStatus(username) == ParticipationStatus.HIDDEN) {
				continue;
			}
			
			if (!username.equals(client)) {
				Notification notification = new Notification(appointmentId, "NORMAL",  appointment.getEventString() + " har blitt avlyst.");
				sendNotification(username, notification);
			}
			if (getUserConnected(username)) {
				network.sendServerResponse(username, "receiveDeleteAppointment", appointmentId);
			}
		}
		
	}

	public void sendAppointment(String client, Appointment appointment) {
		network.sendServerResponse(client, "receiveAppointment", appointment);
	}

	public void sendUserAppointments(String client, String username) {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		ArrayList<Appointment> allAppointments = appointmentHandler.getUserAppointments(username);
		// fix hidden appointments
		for (Appointment appointment : allAppointments) {
			if (appointment.getResponsibleUser().equals(client) || appointment.getParticipants().get(client) != ParticipationStatus.HIDDEN){
				appointments.add(appointment);
			}
		}
		network.sendServerResponse(client, "receiveUserAppointments", appointments);
	}

	public void updateParticipationStatus(String client, String username, int appointmentId, ParticipationStatus status) {
		
		appointmentHandler.addParticipant(username, appointmentId, status.toString(), 0, 0);
		// TODO send notification if DECLINED
		
		
		// Get appointment and send it back
		Appointment appointment = appointmentHandler.getAppointmentFromDB(appointmentId);
		for (String user : appointment.getParticipantList()) {
			if (!user.equals(client)) {
				if (status == ParticipationStatus.DECLINED || status == ParticipationStatus.HIDDEN) {
					Notification notification = new Notification(appointment.getId(), "NORMAL", client + " har avsl&tt invitiasjonen til '" + appointment.getTitle() + "'");
					sendNotification(user, notification);
				}
			}
			if (getUserConnected(user)) {
				network.sendServerResponse(user, "receiveAppointment", appointment);
			}
		}
		
		if (status == ParticipationStatus.HIDDEN) {
			network.sendServerResponse(client, "receiveDeleteAppointment", appointment.getId());
		}
	}


	public void updateAlarm(String client, int appointmentId, int alarm) {
		System.out.println("here");
		appointmentHandler.updateAlarm(client, appointmentId, alarm);
	}

	//
	// User
	//
	public void createUser(String client, User user) {
		// TODO
	}


	public void sendAllUsers(String client) {
		ArrayList<User> users = userHandler.getAllUsers();
		network.sendServerResponse(client, "receiveAllUsers", users);
	}


	public void sendGroupHierachy(String client) {
		ArrayList<Group> groups = groupHandler.getAllGroups();
		network.sendServerResponse(client, "receiveGroupHierachy", groups);
	}


	//
	// Room
	//
	public void sendAllRooms(String client) {
		ArrayList<Room> rooms = roomHandler.getAllRooms();
		network.sendServerResponse(client, "receiveAllRooms", rooms);
	}

	public void sendAvailiableRooms(String client, int capacity, Timestamp startTime, Timestamp endTime) {
		ArrayList<String> rooms = roomHandler.getAvailiableRooms(capacity, startTime, endTime);
		network.sendServerResponse(client, "receiveAvailiableRooms", rooms);
	}


	//
	// Notification
	//
	public void sendUserNotifications(String client) {
		ArrayList<Notification> notifications = notificationHandler.getAllNotificationsForUser(client);
		// TODO delete the notifications
		notificationHandler.deleteUserNotifications(client);
		for (Notification notification : notifications) {
			sendNotification(client, notification);
		}
		
	}

	public void deleteNotification(String client, int notificationId) {
		// TODO Maybe remove this, not really needed
	}

	public void sendNotification(String client, Notification notification) {
		// TODO Save notification to DB if user is not connected
		if (getUserConnected(client)) {
			network.sendServerResponse(client, "receiveNotification", notification);			
		} else {
			int notificationId = notificationHandler.createNotification(notification.getMessage(), "NORMAL", 1);
			notificationHandler.createUserNotification(notificationId, client);
		}
	}




	public static void main(String[] args) {
		CalServer server = new CalServer();
	}


}
