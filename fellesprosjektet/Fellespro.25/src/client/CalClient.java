package client;

import interfaces.AppointmentListener;
import interfaces.GroupListener;
import interfaces.NotificationListener;
import interfaces.RoomListener;
import interfaces.ServerFeedbackListener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import protocol.LoginProtocol;
import protocol.ServerFeedback;
import client_gui.MainFrame;
import database.MD5Hash;
import model.Appointment;
import model.Group;
import model.Notification;
import model.ParticipationStatus;
import model.Room;
import model.User;


public class CalClient {

	// The user that is logged in
	private String username;
	
	// Client control
	private ClientControl control;
	
	
	
	
	// TODO Need to cache some stuff here
	
	// Caches
	private HashMap<Integer, Appointment> appointmentCache;
	private HashMap<String, User> userCache;
	private HashMap<String, Group> groupCache;
	private HashMap<String, Room> roomCache;
	// TODO make group cache
	
	
	
	// Listeners
	private ArrayList<AppointmentListener> appointmentListeners;
	private ArrayList<RoomListener> roomListeners;
	private ArrayList<NotificationListener> notificationListeners;
	private ArrayList<ServerFeedbackListener> serverFeedbackListeners;
	private ArrayList<GroupListener> groupListeners;
	
	
	// The main JFrame of the system
	private MainFrame mainFrame;
	
	
	public CalClient() {
		
		// Create listener lists
		appointmentListeners = new ArrayList<AppointmentListener>();
		roomListeners = new ArrayList<RoomListener>();
		notificationListeners = new ArrayList<NotificationListener>();
		serverFeedbackListeners = new ArrayList<ServerFeedbackListener>();
		groupListeners = new ArrayList<GroupListener>();
		
		// Create caches
		appointmentCache = new HashMap<Integer, Appointment>();
		userCache = new HashMap<String, User>();
		roomCache = new HashMap<String, Room>();
		groupCache = new HashMap<String, Group>();
		
		// Create control
		control = new ClientControl(this);
		
		// Create mainFrame
		mainFrame = new MainFrame(this);
	}
	
	
	
	//
	// Listeners
	//
	public void addAppointmentListener(AppointmentListener listener) {
		appointmentListeners.add(listener);
	}
	public void removeAppointmentListener(AppointmentListener listener) {
		appointmentListeners.remove(listener);
	}
	public void addRoomListener(RoomListener listener) {
		roomListeners.add(listener);
	}
	public void removeRoomListener(RoomListener listener) {
		roomListeners.remove(listener);
	}
	public void addNotificationListener(NotificationListener listener) {
		notificationListeners.add(listener);
		// Send all the notifications again since swing is a bit slow
		
	}
	public void removeNotificaitonListener(NotificationListener listener) {
		notificationListeners.remove(listener);
	}
	public void addServerFeedbackListener(ServerFeedbackListener listener) {
		serverFeedbackListeners.add(listener);
	}
	public void removeServerFeedbackListener(ServerFeedbackListener listener) {
		serverFeedbackListeners.remove(listener);
	}
	public void addGroupListener(GroupListener listener) {
		groupListeners.add(listener);
	}
	public void removeGroupListener(GroupListener listener) {
		groupListeners.remove(listener);
	}
	
	
	//
	// Cache users and rooms
	//
	public void cacheAppointments(ArrayList<Appointment> appointments) {
		for (Appointment appointment : appointments) {
			appointmentCache.put(appointment.getId(), appointment);
		}
		for (AppointmentListener listener : appointmentListeners) {
			listener.onGetUserAppointments();
		}
	}
	public void cacheUsers(ArrayList<User> users) {
		for (User user : users) {
			userCache.put(user.getUsername(), user);
		}
	}
	public void cacheRooms(ArrayList<Room> rooms) {
		for (Room room : rooms) {
			roomCache.put(room.getRoomNumber(), room);
		}
	}
	public void cacheGroups(ArrayList<Group> groups) {
		for (Group group : groups) {
			groupCache.put(group.getName(), group);
		}
		for (GroupListener listener : groupListeners) {
			listener.onGetAllGroups(groups);
		}
	}
	
	
	//
	// Appointment
	//
	public ArrayList<Appointment> getAppointments(Calendar cal) {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Calendar start = (Calendar)cal.clone();
		start.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end = (Calendar)start.clone();
		end.add(Calendar.DATE, 7);
		
		for (Appointment appointment : appointmentCache.values()) {
			if (appointment.getStartTime().getTime() >= start.getTime().getTime() && appointment.getEndTime().getTime() < end.getTime().getTime()) {
				appointments.add(appointment);
			}
		}
		return appointments;
	}
	
	public ArrayList<Appointment> getAppointments(int weekNumber) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekNumber);
		return getAppointments(cal);
		//return new ArrayList<Appointment>(appointmentCache.values());
	}
	
	public void createAppointment(Appointment appointment) {
		appointment.setResponsibleUser(this.username);
		control.createAppointment(appointment);
	}
	
	public void updateAppointment(Appointment appointment) {
		appointment.setResponsibleUser(this.username);
		if (appointment.getId() == 0) {
			// create new appointment
			control.createAppointment(appointment);
		} else {
			// Update existing appointment
			control.updateAppointment(appointment);
		}
	}
	
	public void deleteAppointment(int appointmentId) {
		control.deleteAppointment(appointmentId);
	}
	
	public void receiveAppointment(Appointment appointment) {
		//if (!appointmentCache.containsKey(appointment.getId())) {
		//	appointmentCache.put(appointment.getId(), appointment);
		//}
		appointmentCache.put(appointment.getId(), appointment);
		
		for (AppointmentListener listener : appointmentListeners) {
			listener.onAddAppointment(appointment);
		}
			
	}
	
	public void receiveDeleteAppointment(int appointmentId) {
		if (appointmentCache.containsKey(appointmentId)) {
			appointmentCache.remove(appointmentId);
		}
		
		for (AppointmentListener listener : appointmentListeners) {
			listener.onDeleteAppointment(appointmentId);
		}
		System.out.println("On delete appointment");
	}
	
	
	public void showOtherUserAppointments(String username) {
		control.requestUserAppointments(username);
	}
	
	public void showOnlyMyAppointments() {
		ArrayList<Appointment> appointmentsToRemove = new ArrayList<Appointment>();
		
		for (Appointment appointment : appointmentCache.values()) {
			if (!appointment.getResponsibleUser().equals(this.username) && !appointment.getParticipants().containsKey(this.username)) {
				appointmentsToRemove.add(appointment);
			}
		}
		for (Appointment appointment : appointmentsToRemove) {
			appointmentCache.remove(appointment.getId());
		}
		// Fire listeners
		for (AppointmentListener listener : appointmentListeners) {
			listener.onGetUserAppointments();
		}
	}
	
	
	
	public void updateAlarm(int appointmentId, int alarm) {
		control.requestUpdateAlarm(appointmentId, alarm);
		
	}
	
	//
	// User
	//
	public User getUser(String username) {
		return userCache.get(username);
	}
	
	public String getCurrentUser() {
		return username;
	}
	
	public void updateParticipationStatus(int appointmentId, ParticipationStatus status) {
		updateParticipationStatus(this.username, appointmentId, status);
	}
	
	public void updateParticipationStatus(String username, int appointmentId, ParticipationStatus status) {
		control.updateParticipationStatus(username, appointmentId, status);
	}
	
	
	//
	// Room
	// 
	public void requestAvailableRooms(int capacity, Timestamp startTime, Timestamp endTime) {
		control.requestAvailiableRooms(capacity, startTime, endTime);
	}
	
	public void receiveAvailableRooms(ArrayList<String> roomNumbers) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		for (String roomNumber : roomNumbers) {
			rooms.add(roomCache.get(roomNumber));
		}
		for (RoomListener listener : roomListeners) {
			listener.onGetAvailiableRooms(rooms);
		}
	}
	
	
	//
	// Login
	//
	public void requestLogin(String username, String password) {
		try {
			String cryptPW = MD5Hash.MD5(password);
			control.requestLogin(username, cryptPW);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
//			errorMessage.setText("Unsupported encoding");
		} catch (NullPointerException e) {
//			errorMessage.setText("Please enter your username and password");
		}
	}
	
	public void requestLogout() {
		control.requestLogout();
		// clear caches
		appointmentCache.clear();
		userCache.clear();
		groupCache.clear();
		roomCache.clear();
	}
	
	public void receiveLogin(String username) {
		this.username = username;
		
		// Login received, start to cache users, rooms and appointments
		control.requestAllRooms();
		control.requestAllUsers();
		control.requestUserAppointments(username);
		control.requestGroupHierachy();
		control.requestUserNotifications();
		
		
	}
	
	
	public boolean isLoggedIn() {
		return (username != null);
	}
	
	
	//
	// Group
	//
	public ArrayList<Group> getGroups() {
		return new ArrayList<Group>(groupCache.values());
	}
	
	
	
	//
	// Notification
	//
	public void receiveNotification(Notification notification) {
		for (NotificationListener listener : notificationListeners) {
			listener.onAddNotification(notification);
		}
		System.out.println(notification.getMessage());
	}
	
	public void deleteNotification(int notificationId) {
		control.requestDeleteNotification(notificationId);
	}
	
	
	//
	// ServerFeedback
	//
	public void receiveServerFeedback(ServerFeedback feedback) {
		for (ServerFeedbackListener listener : serverFeedbackListeners) {
			listener.onServerFeedback(feedback);
		}
	}	
	
	
	
	//
	// Main
	//
	public static void main(String[] args) {
		CalClient cal = new CalClient();
	}



}
