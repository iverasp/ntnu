package sheepwatchsimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import sheepwatchui.DBConnector;
import models.Farmer;
import models.Sheep;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.Report;
import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * A simulator creating reports from the {@link Sheep} to the {@link Farmer}.
 */
public class SheepWatchSimulator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SheepWatchSimulator();
    }
    /**
     * Constructor starting the simulation
     */
    public SheepWatchSimulator(){
        JFrame frame = new JFrame("Simulator");
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        JLabel labMsg = new JLabel("Simulatoren kjører...");
        JButton btnSimulate = new JButton("Simuler rapporter");
        btnSimulate.setSize(10, 20);
        btnSimulate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                createSheepReport();
            }
        });
        panel.add(labMsg);
        panel.add(btnSimulate);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 200);
        frame.setVisible(true);
        
        int lastMinutes = -1;
        while(true){
            // Get time
           Date currentDateTime = Calendar.getInstance().getTime();
           int minutes = currentDateTime.getMinutes();
           int seconds = currentDateTime.getSeconds();

           // Creates sheep report every 30 minutes
           if((minutes%30 == 0) && (seconds == 0) && lastMinutes != minutes){
               lastMinutes = minutes;
               createSheepReport();
           }
        }
    }
    
    
    /**
     * Creates a random sheep report with attack, location and time info
     */
    private void createSheepReport(){
        // Getting sheep list of living sheep from the database
        ArrayList<Sheep> alSheepList = DBConnector.getAllLivingSheepList();
        GeoPosition randomPos;
        for (int i = 0; i < alSheepList.size(); i++) {
            Sheep s = alSheepList.get(i);
            Farmer f = s.getFarmer();
            
            int plusOrMinusOne = Math.random()<0.5 ? -1 : 1;
            // Generate random location
            double lat = f.getFarmLocation().getLatitude() + 
                    plusOrMinusOne*(Math.random()/50);
            double lng = f.getFarmLocation().getLongitude()+ 
                    plusOrMinusOne*(Math.random()/50);
            randomPos = new GeoPosition(lat, lng);
            System.out.println("Sau: " + s.getName() + " - Pos: " + randomPos.getLatitude() + " - " +randomPos.getLongitude());
            // Get time
            Date currentDateTime = Calendar.getInstance().getTime();
            int date = currentDateTime.getDate();
            int month = currentDateTime.getMonth()+1;
            int year = currentDateTime.getYear()+1900;
            
            // Generate random heart rate to see if the sheep is being attacked
            // or if it is dead
            int heartRate = (int)(Math.random()*100);
            
            // Checks if the sheep is under attack
            // or if it's dead
            if(heartRate > 85){
                // Sends an alert email to the farmer and the emergency contact
                Alerter.sendAlertMail(f, s);
            }else if(heartRate < 5){
                // Marks the sheep as dead
                String deathDay = date+"."+month+"."+year;
                s.setDateOfDeath(deathDay);
                s.setHealth("Død");
            }
            // Updating latitude and longitude for the sheep
            s.setLocation(randomPos);
            // Updating the sheep in the database
            DBConnector.editSheep(s);
            // Create report object
            Report report = new Report(s, randomPos, heartRate);
            
            // Add report to the database
            DBConnector.addReport(report);
        }
    }
}
