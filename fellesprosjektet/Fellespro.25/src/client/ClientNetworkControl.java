package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

import protocol.ClientRequest;
import protocol.LoginProtocol;
import protocol.ParticipationStatusProtocol;
import protocol.ServerFeedback;
import protocol.ServerResponse;


import model.Appointment;
import model.Group;
import model.Notification;
import model.ParticipationStatus;
import model.Room;
import model.User;


public class ClientNetworkControl {

	private Socket clientSocket;
	private ObjectOutputStream out;
	private Properties properties;
	private boolean running;
	
	private ClientControl control;
	
	public ClientNetworkControl(ClientControl control) {
		running = true;
		this.control = control;
		
		// Connect to server
		try {
			
			// Load properties
			FileInputStream inStream = new FileInputStream("network.properties");
			properties = new Properties();
			properties.load(inStream);
			
			String adress = properties.getProperty("HOST");
			int port = Integer.parseInt(properties.getProperty("PORT"));
			
			// TODO remove hardcoding of adress
			clientSocket = new Socket(adress, port);
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			// Start Response handler thread
			ResponseHandlerThread responseHandler = new ResponseHandlerThread();
			responseHandler.start();
			
		} catch (UnknownHostException e) {
			// TODO Add some more error handling here
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Add some more error handling here
			e.printStackTrace();
		}
	}
	
	
	
	public void sendLoginRequest(LoginProtocol login) {
		ClientRequest request = new ClientRequest("login", login);
		try {
			out.writeObject(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// Request sending
	public void sendClientRequest(ClientRequest request) {
		try {
			out.writeObject(request);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	// Response handling
	private class ResponseHandlerThread extends Thread {
		
		public void run() {
			while (running) {
				try {
					ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());			
					ServerResponse response = (ServerResponse)in.readObject();
					handleResponse(response);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void handleResponse(ServerResponse response) {
		System.out.println(response);
		
		//
		// Login
		//
		if (response.getResponse().equals("receiveLogin")) {
			String username = (String)response.getData();
			control.receiveLogin(username);
		
		//
		// Appointment
		//
		} else if (response.getResponse().equals("receiveAppointment")) {
			Appointment appointment = (Appointment)response.getData();
			control.receiveAppointment(appointment);
		} else if (response.getResponse().equals("receiveUserAppointments")) {
			ArrayList<Appointment> appointments = (ArrayList<Appointment>)response.getData();
			control.receiveUserAppointments(appointments);
		} else if (response.getResponse().equals("receiveDeleteAppointment")) {
			int appointmentId = (Integer)response.getData();
			control.receiveDeleteAppointment(appointmentId);
		
		} else if (response.getResponse().equals("receiveUserAppointments")) {
			ArrayList<Appointment> appointments = (ArrayList<Appointment>)response.getData();
			control.receiveUserAppointments(appointments);
		
		} else if (response.getResponse().equals("receiveParticipationStatus")) {
			ParticipationStatusProtocol participationStatusProtocol = (ParticipationStatusProtocol)response.getData();
			String username = participationStatusProtocol.getUsername();
			int appointmentId = participationStatusProtocol.getAppointmentId();
			ParticipationStatus status = participationStatusProtocol.getStatus();
			control.receiveParticipationStatus(username, appointmentId, status);
			
		//
		// User
		//
		} else if (response.getResponse().equals("receiveAllUsers")) {
			ArrayList<User> users = (ArrayList<User>)response.getData();
			control.receiveAllUsers(users);
		
		} else if (response.getResponse().equals("receiveGroupHierachy")) {
			ArrayList<Group> groups = (ArrayList<Group>)response.getData();
			control.receiveGroupHierachy(groups);
		
		
		//
		// Room
		//
		} else if (response.getResponse().equals("receiveAllRooms")) {
			ArrayList<Room> rooms = (ArrayList<Room>)response.getData();
			control.receiveAllRooms(rooms);
		
		} else if (response.getResponse().equals("receiveAvailiableRooms")) {
			ArrayList<String> rooms = (ArrayList<String>)response.getData();
			control.receiveAvailiableRooms(rooms);
		
		
		//
		// Notification
		//
		} else if (response.getResponse().equals("receiveNotification")) {
			Notification notification = (Notification)response.getData();
			control.receiveNotification(notification);

			
		//
		// ServerFeedback
		//
		} else if (response.getResponse().equals("receiveServerFeedback")) {
			ServerFeedback feedback = (ServerFeedback)response.getData();
			control.receiveServerFeedback(feedback);
		}
		
		
	}
	
}
