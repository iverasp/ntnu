
package server;



import java.sql.Timestamp;
import java.util.ArrayList;

import model.Appointment;


public class ServerTesting {
	

	public static void main(String[] args) {
		/*
		
		User user = new User("sims1","123","123","465","123@123.com","123345");
		System.out.println("Adding this user to DB: " + user);
		System.out.println("Response: " + userHandler.insertUserInDB(user));
		
		System.out.println("Getting user from DB: " + userHandler.getUserFromDB("sims1"));
		*/
		UserHandler userHandler = new UserHandler();
		RoomHandler roomHandler = new RoomHandler();
		NotificationHandler alarmHandler = new NotificationHandler();
		GroupHandler grouphandler = new GroupHandler();

		
		AppointmentHandler appointmentHandler = new AppointmentHandler();
		
		//3914-07-12 13:14:00
		Timestamp start = new Timestamp(2014, 06 , 12, 16,45, 00, 0);
		Timestamp end = new Timestamp(2014, 06, 12, 18, 00, 00, 0);
		
		ArrayList<String> oppdatertList = new ArrayList<String>();
		oppdatertList.add("Simen");
		//oppdatertList.add("Slett meg 1");
		oppdatertList.add("Per");
		//oppdatertList.add("Slett meg 2");
		oppdatertList.add("Ikke slett meg");
		
//		ArrayList<String> gammeltList = new ArrayList<String>();
//		gammeltList.add("Simen");
//		gammeltList.add("Slett meg 1");
//		gammeltList.add("Per");
//		gammeltList.add("Slett meg 2");
//		gammeltList.add("Ikke slett meg");
//		
//		gammeltList.removeAll(oppdatertList);
//		//System.out.println(gammeltList);
//		
//		
//		
//		ArrayList<String> mailliste = new ArrayList<String>();
//		mailliste.add("simense@stud.ntnu.no");
//		
//		appointment.setEmails(mailliste);
//		
//		MailHandler mail = new MailHandler();
//		mail.sendMailToAllAdresses(appointment);
		
		System.out.println(roomHandler.isReserved("1c",start,end));
		
		//Appointment appointment = new Appointment("Mail testing",start,end,"Mail Testing","R-bygget","hacker","1c");
		//int id = appointmentHandler.addAppointmenInDB(appointment);
		
		//System.out.println(roomHandler.isReserved("1c",start,end));
		
		//getParticipantList()
		
		//System.out.println(roomHandler.getAllRooms());
		//System.out.println(appointmentHandler.getAppointmentFromDB(10));
		
		//System.out.println(alarmHandler.getAppointmentsWithin(start,1));
		
		//System.out.println(alarmHandler.createNotification(15,start));
		//System.out.println(alarmHandler.createReminderNotification("Alarm for mote ditt",15));

		//alarmHandler.createAndSendReminderNotifications(start,90);
		
		//System.out.println(alarmHandler.getAllNotificationsForUser("sims1"));
		
		//System.out.println(appointmentHandler.getUserAppointments("sims"));
		//alarmHandler.deleteNotification(20,"sims1");
		

		//System.out.println(userHandler.getAllUsers());
		
		//System.out.println(grouphandler.getAllGroups());
		
		//System.out.println(appointmentHandler.particpantExist("finnen",15));
		//appointmentHandler.deleteAppointment(45);

		//
		//int id = appointmentHandler.addAppointmenInDB(appointment);
		//System.out.println(appointmentHandler.getAppointmentFromDB(id));
		//appointment.setId(id);
		//appointment.setTitle("Velkommen no sammen");
		
		//appointmentHandler.updateAppointmenInDB(appointment);
		
		//System.out.println(roomHandler.getFreeRoomBetween(4,start,end));
		//System.out.println("Getting avtale from DB: " + appointmentHandler.getAppointmentFromDB(1));
		/*

		appointment.addParticipant("sims");
		appointment.addParticipant("finnen");
		appointment.addParticipant("iver");
		
		int id = appointmentHandler.addAppointmenInDB(appointment);
		userHandler.updateParticipation("sims", id, ParticipationStatus.DECLINED, 0, 10);
		userHandler.updateParticipation("iver", id, ParticipationStatus.ACCEPTED, 0, 10);
		
		
		
		*/

	}

}
