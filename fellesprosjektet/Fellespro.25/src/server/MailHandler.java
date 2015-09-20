package server;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.Appointment;

public class MailHandler {



//File Name SendEmail.java

	public boolean sendMailToAllAdresses(Appointment a){
		if(a.getEmails() == null) return false;
		if(a.getEmails().size() == 0) return false;
		
		String title = a.getTitle();
		
		String from = "no-reply@cooglegal.com";
		String subject = "Invitation to '" + title + "'";

		String startTime = a.getStartTime() + "";
		String endtime = a.getEndTime() + "";
		
		String text = "Invitation to "+ a.getResponsibleUser() + "'s event '" + title +
					"'\n" +
					"\n Description: " + a.getDescription() + 
					"\n	From: " + startTime + 
					"\n	To: " + endtime  +
					"\n	Location: " + a.getLocation() +
					"\n\n From the Coogle Gal team";
		
		for (int j=0; j<=a.getEmails().size() -1; j++) {
			System.out.println("Sender mail til" + a.getEmails().get(j));
			if(a.getEmails().get(j) == null) continue;
			sendMail(a.getEmails().get(j),from,subject,text);
			
			
		}
		
		return true;
	}
	
	public boolean sendMail(String to, String from, String subject, String text){
	   // Recipient's email ID needs to be mentioned.

	
	   // Sender's email ID needs to be mentioned
	   //String from = "Java";
	
	   // Assuming you are sending email from localhost
	
	
	   // Get system properties
	   Properties properties = System.getProperties();
	
	   // Setup mail server
	   //properties.setProperty("smtp.stud.ntnu.no", host);
	   properties.setProperty("mail.smtp.host", "smtp.stud.ntnu.no");
	   properties.setProperty("mail.smtp.port", "25");
	
	   // Get the default Session object.
	   Session session = Session.getDefaultInstance(properties);
	
	   try{
	      // Create a default MimeMessage object.
	      MimeMessage message = new MimeMessage(session);
	
	      // Set From: header field of the header.
	      message.setFrom(new InternetAddress(from));
	
	      // Set To: header field of the header.
	      message.addRecipient(Message.RecipientType.TO,
	                               new InternetAddress(to));
	
	      // Set Subject: header field
	      message.setSubject(subject);
	
	      // Now set the actual message
	      message.setText(text);
	
	      // Send message
	      Transport.send(message);
	      System.out.println("Sent message successfully....");
	      return true;
	   }catch (MessagingException mex) {
	      mex.printStackTrace();
	      return false;
	   }
	}
}
