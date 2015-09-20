package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import protocol.AlarmProtocol;
import protocol.ClientRequest;
import protocol.LoginProtocol;
import protocol.ParticipationStatusProtocol;
import protocol.ServerFeedback;
import protocol.ServerResponse;
import protocol.RoomReservationProtocol;
import protocol.UserAppointmentsProtocol;

import model.Appointment;
import model.ParticipationStatus;
import model.User;

public class ServerNetworkController {

	
	CalServer server;
	
	ServerSocket serverSocket;
	
	AcceptLoopThread acceptLoop;
	
	private HashMap<String, Socket> connectedClients;
	private ArrayList<String> userList;
	
	public ServerNetworkController(CalServer server) {
		this.server = server;
		
		connectedClients = new HashMap<String, Socket>();
		userList = new ArrayList<String>();
		
		// Create server socket
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not listen on port 4444");
			e.printStackTrace();
		}
		
		// Start accept loop
		acceptLoop = new AcceptLoopThread();
		acceptLoop.start();
	}
	
	
	private void addClient(String username, Socket clientSocket) {
		connectedClients.put(username, clientSocket);
		userList.add(username);
	}
	
	private void removeClient(String username) {
		connectedClients.remove(username);
		userList.remove(username);
	}
	
	public ArrayList<String> getUserList() {
		return userList;
	}
	
	public void sendServerFeedback(String client, int type, String event, String message) {
		ServerFeedback feedback = new ServerFeedback(type, event, message);
		sendServerResponse(client, "receiveServerFeedback", feedback);
	}
	
	public void sendServerFeedback(Socket client, int type, String event, String message) {
		ServerFeedback feedback = new ServerFeedback(type, event, message);
		sendServerResponse(client, "receiveServerFeedback", feedback);
	}
	
	public void sendServerResponse(String client, String response, Serializable serializable) {
		ServerResponse serverResponse = new ServerResponse(response, serializable);
		sendServerResponse(client, serverResponse);
	}
	
	public void sendServerResponse(Socket client, String response, Serializable serializable) {
		ServerResponse serverResponse = new ServerResponse(response, serializable);
		sendServerResponse(client, serverResponse);
	}
	
	
	public void sendServerResponse(Socket clientSocket, ServerResponse response) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.writeObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendServerResponse(String username, ServerResponse response) {
		Socket clientSocket = connectedClients.get(username);
		sendServerResponse(clientSocket, response);
	}
	
	
	private class AcceptLoopThread extends Thread {
		public void run() {
			while (true) {
				try {
					System.out.println("Waiting for new connection.");
					
					// Accept new socket
					Socket clientSocket = serverSocket.accept();
					
					// Create new handler thread and start it
					ClientHandlerThread handler = new ClientHandlerThread();
					handler.setClientSocket(clientSocket);
					handler.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ClientHandlerThread extends Thread {
		
		private Socket clientSocket;
		private boolean loggedIn = false;
		private String username;
		
		public void setClientSocket(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		
		public void run() {
			ObjectInputStream objectInputStream = null;
			try {
				objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			while (true) {
				try {
					ClientRequest request = (ClientRequest)objectInputStream.readObject();
					handleClientRequest(request);
				} catch (SocketException e) {
					// Connection reset or similar, break thread
					System.out.println(e.getMessage());
					// TODO Remove this client from the connectedClients
					if (loggedIn) {
						removeClient(username);
						username = "";
						loggedIn = false;
					}
					break;
				} catch (IOException e) {
					e.printStackTrace();
					break;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// Handling client requests
		private void handleClientRequest(ClientRequest request) {
			System.out.println(username + ": " + request);
			
			if (loggedIn) {
				
				
				//
				// Logout
				//
				if (request.getRequest().equals("requestLogout")) {
					removeClient(username);
					username = "";
					loggedIn = false;
			
				
				//
				// Appointment
				//
				} else if (request.getRequest().equals("createAppointment")) {
					Appointment appointment = (Appointment)request.getData();
					server.createAppointment(this.username, appointment);
					
				} else if (request.getRequest().equals("updateAppointment")) {
					Appointment appointment = (Appointment)request.getData();
					server.updateAppointment(this.username, appointment);
					
				} else if (request.getRequest().equals("deleteAppointment")) {
					int appointmentId = (Integer)request.getData();
					server.deleteAppointment(this.username, appointmentId);
				
				} else if (request.getRequest().equals("requestUserAppointments")) {
					String username = (String)request.getData();
					server.sendUserAppointments(this.username, username);
				
				} else if (request.getRequest().equals("updateParticipationStatus")) {
					ParticipationStatusProtocol participationStatusProtocol = (ParticipationStatusProtocol)request.getData();
					String username  = participationStatusProtocol.getUsername();
					int appointmentId = participationStatusProtocol.getAppointmentId();
					ParticipationStatus status = participationStatusProtocol.getStatus();
					server.updateParticipationStatus(this.username, username, appointmentId, status);
					
				} else if (request.getRequest().equals("requestUpdateAlarm")) {
					AlarmProtocol alarmProtocol = (AlarmProtocol)request.getData();
					int appointmentId = alarmProtocol.getAppointmentId();
					int alarm = alarmProtocol.getAlarm();
					server.updateAlarm(this.username, appointmentId, alarm);
					
				//
				// User
				//
				} else if (request.getRequest().equals("createUser")) {
					User user = (User)request.getData();
					server.createUser(this.username, user);
				
				} else if (request.getRequest().equals("requestAllUsers")) {
					server.sendAllUsers(this.username);
				
				} else if (request.getRequest().equals("requestGroupHierachy")) {
					server.sendGroupHierachy(this.username);
				
				//
				// Room
				//
				} else if (request.getRequest().equals("requestAllRooms")) {
					server.sendAllRooms(this.username);
				
				} else if (request.getRequest().equals("requestAvailiableRooms")) {
					RoomReservationProtocol roomReservationProtocol = (RoomReservationProtocol)request.getData();
					int capacity = roomReservationProtocol.getCapacity();
					Timestamp startTime = roomReservationProtocol.getStartTime();
					Timestamp endTime = roomReservationProtocol.getEndTime();
					server.sendAvailiableRooms(this.username, capacity, startTime, endTime);

				//
				// Notification
				//
				} else if (request.getRequest().equals("requestUserNotifications")) {
					server.sendUserNotifications(this.username);
				
				} else if (request.getRequest().equals("requestDeleteNotification")) {
					int notificationId = (Integer)request.getData();
					server.deleteNotification(this.username, notificationId);
				}
					
			} else {
				//
				// Login
				//
				if (request.getRequest().equals("login")) {
					LoginProtocol login = (LoginProtocol)request.getData();
					String username = login.getUsername();
					String password = login.getPassword();
					
					int loginMessage = server.checkLogin(username, password);
					
					if (loginMessage == CalServer.LOGIN_OK) {
						loggedIn = true;
						this.username = username;
						addClient(username, clientSocket);
						server.sendLoginResponse(username);
					} else if (loginMessage == CalServer.LOGIN_ALREADY_LOGGED_IN) {
						sendServerFeedback(clientSocket, ServerFeedback.ERROR, "login", "User is already logged in.");
					} else if (loginMessage == CalServer.LOGIN_WRONG_USERNAME_OR_PASSWORD) {
						sendServerFeedback(clientSocket, ServerFeedback.ERROR, "login", "Wrong username or password");
					}

				}
				
				// TODO Register user
			}
			
		}
	}
	
	
}
