package client;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import protocol.AlarmProtocol;
import protocol.ClientRequest;
import protocol.LoginProtocol;
import protocol.ParticipationStatusProtocol;
import protocol.ServerFeedback;
import protocol.RoomReservationProtocol;
import protocol.UserAppointmentsProtocol;


import model.Appointment;
import model.Group;
import model.Notification;
import model.ParticipationStatus;
import model.Room;
import model.User;

public class ClientControl {

	private CalClient client;
	private ClientNetworkControl network;
	
	public ClientControl(CalClient client) {
		this.client = client;
		
		network = new ClientNetworkControl(this);
	}
	
	
	//
	// Login
	//
	public void requestLogin(String username, String password) {
		LoginProtocol loginProtocol = new LoginProtocol(username, password);
		network.sendLoginRequest(loginProtocol);
	}
	
	public void requestLogout() {
		ClientRequest request = new ClientRequest("requestLogout");
		network.sendClientRequest(request);
	}
	
	public void receiveLogin(String username) {
		client.receiveLogin(username);
	}
	
	//
	// Appointment
	//
	public void createAppointment(Appointment appointment) {
		ClientRequest request = new ClientRequest("createAppointment", appointment);
		network.sendClientRequest(request);
	}
	
	public void updateAppointment(Appointment appointment) {
		ClientRequest request = new ClientRequest("updateAppointment", appointment);
		network.sendClientRequest(request);
	}
	
	public void receiveAppointment(Appointment appointment) {
		client.receiveAppointment(appointment);
	}
	
	public void deleteAppointment(int appointmentId) {
		ClientRequest request = new ClientRequest("deleteAppointment", appointmentId);
		network.sendClientRequest(request);
		
	}
	
	public void receiveDeleteAppointment(int appointmentId) {
		client.receiveDeleteAppointment(appointmentId);
	}
	
	public void requestUserAppointments(String username) {
		ClientRequest request = new ClientRequest("requestUserAppointments", username);
		network.sendClientRequest(request);
	}
	
	public void receiveUserAppointments(ArrayList<Appointment> appointments) {
		client.cacheAppointments(appointments);
	}
	
	public void updateParticipationStatus(String username, int appointmentId, ParticipationStatus status) {
		ParticipationStatusProtocol participationStatusProtocol = new ParticipationStatusProtocol(username, appointmentId, status);
		ClientRequest request = new ClientRequest("updateParticipationStatus", participationStatusProtocol);
		network.sendClientRequest(request);
	}
	
	public void receiveParticipationStatus(String username, int appointmentId, ParticipationStatus status) {
		// TODO Not used for now, consider removing
	}
	
	public void requestUpdateAlarm(int appointmentId, int alarm) {
		AlarmProtocol alarmProtocol = new AlarmProtocol(appointmentId, alarm);
		ClientRequest request = new ClientRequest("requestUpdateAlarm", alarmProtocol);
		network.sendClientRequest(request);
	}
	
	
	//
	// User
	//
	public void createUser(User user) {
		ClientRequest request = new ClientRequest("createUser", user);
		network.sendClientRequest(request);
	}
	
	public void requestAllUsers() {
		ClientRequest request = new ClientRequest("requestAllUsers");
		network.sendClientRequest(request);
	}
	
	public void receiveAllUsers(ArrayList<User> users) {
		// TODO
	}
	
	public void requestGroupHierachy() {
		ClientRequest request = new ClientRequest("requestGroupHierachy");
		network.sendClientRequest(request);
	}
	
	public void receiveGroupHierachy(ArrayList<Group> groups) {
		client.cacheGroups(groups);
	}
	
	//
	// Room
	//
	public void requestAllRooms() {
		ClientRequest request = new ClientRequest("requestAllRooms");
		network.sendClientRequest(request);
	}
	
	public void receiveAllRooms(ArrayList<Room> rooms) {
		client.cacheRooms(rooms);
	}
	
	public void requestAvailiableRooms(int capacity, Timestamp startTime, Timestamp endTime) {
		RoomReservationProtocol roomReservationProtocol = new RoomReservationProtocol(capacity, startTime, endTime);
		ClientRequest request = new ClientRequest("requestAvailiableRooms", roomReservationProtocol);
		network.sendClientRequest(request);
	}
	
	public void receiveAvailiableRooms(ArrayList<String> rooms) {
		client.receiveAvailableRooms(rooms);
	}
	
	//
	// Notification
	//
	public void requestUserNotifications() {
		ClientRequest request = new ClientRequest("requestUserNotifications");
		network.sendClientRequest(request);
	}
	  
	public void requestDeleteNotification(int notificationId) {
		ClientRequest request = new ClientRequest("requestDeleteNotification", notificationId);
		network.sendClientRequest(request);
	}
	
	public void receiveNotification(Notification notification) {
		client.receiveNotification(notification);
	}
	
	
	//
	// ServerFeedback
	//
	public void receiveServerFeedback(ServerFeedback feedback) {
		client.receiveServerFeedback(feedback);
	}


	
	
}
