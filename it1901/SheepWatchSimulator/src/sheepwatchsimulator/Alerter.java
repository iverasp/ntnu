package sheepwatchsimulator;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import models.*;

/**
 * This class is used to send email, sms and phone alerts to a {@link Farmer}
 * and an {@link EmergencyContact} when a {@link Sheep} is under attack.
 */
public class Alerter {
    /**
     * Sends an alert mail to the {@link Farmer} and the {@link EmergencyContact} that
     * a {@link Sheep} is under attack.
     * @param farmer The {@link Farmer} and his {@link EmergencyContact} who will receive
     * the alert message.
     * @param sheep The {@link Sheep} who is under attack.
     * @return true if the email is sendt, false if not.
     */
    public static boolean sendAlertMail(Farmer farmer, Sheep sheep) {
        // Creating a map to be displayed in the email, showing the location
        // of the sheep under attack.
        String map = "https://maps.google.com/maps?q=" + sheep.getLocation().getLatitude() + ","
                + sheep.getLocation().getLongitude() + "+(Sau+" + sheep.getId() + ")&z=14&ll="
                + sheep.getLocation().getLatitude() + "," + sheep.getLocation().getLongitude();

        // Specifying the email properties
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");

        // Setting up the FROM email
        Session s = Session.getDefaultInstance(p,
            new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication
                        getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            "it1901.gruppe1@gmail.com", "informatikk"
                    );
                }
            });

        try {
            // Setting up the message 
            Message message = new MimeMessage(s);
            message.setFrom(new InternetAddress("it1901.gruppe1@gmail.com"));

            message.setSubject("Sau " + sheep.getId() + " er under angrep!");
            message.setText("Posisjonen til sauen er: " + sheep.getLocation().getLatitude() + ", "
                    + sheep.getLocation().getLongitude() + "\n" + map
                    + "\n\n-- \nHilsen\nSheepWatch");
            
            // Sends the message to the farmer
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(farmer.getEmail()));
            Transport.send(message);
            // Sends the message to the emergencycontact
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(farmer.getEmergencyContact().getEmail()));
            Transport.send(message);
            
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
