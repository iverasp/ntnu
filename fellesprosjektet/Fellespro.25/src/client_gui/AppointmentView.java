package client_gui;

import client.CalClient;
import model.Appointment;
import model.ParticipationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppointmentView extends JPanel{
    CalClient client;
    Appointment appointment;
    JFrame parentFrame;
    JDialog editAppointmentDialog;
    WeekView weekView;
    ParticipationStatus type;
    JTextField alarm;

    public AppointmentView(Appointment appointment, CalClient client, JFrame parentFrame, WeekView weekView){
        this.client = client;
        this.appointment = appointment;
        this.parentFrame = parentFrame;
        this.weekView = weekView;
        this.type = appointment.getParticipantStatus(client.getCurrentUser());
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel(appointment.getTitle());
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        add(titleLabel, gbc(0,0));

        JLabel reservedRoomLabel = new JLabel("Rom: " + appointment.getReservedRoom());
        add(reservedRoomLabel, gbc(1, 0));

        JLabel locationLabel = new JLabel("Sted: " + appointment.getLocation());
        add(locationLabel, gbc(2,0));

        JLabel timeLabel = new JLabel("Tid: "+appointment.getStartTime().getHours() + " - " + appointment.getEndTime().getHours());
        add(timeLabel, gbc(3, 0));

        JLabel dateLabel = new JLabel("Dato: "+("" + appointment.getStartTime()).split(" ")[0]);
        add(dateLabel, gbc(4, 0));

        JLabel descLabel = new JLabel("Beskrivelse: " + appointment.getDescription());
        add(descLabel, gbc(5, 0));
        
        if (appointment.getResponsibleUser().equals(client.getCurrentUser()) || appointment.getParticipants().containsKey(client.getCurrentUser())) {
        	JLabel alarmLabel = new JLabel("Alarm: ");
        	add(alarmLabel, gbc(6,0));
        	alarm = new JTextField(15);
        	add(alarm, gbc(6,1));
        	JButton updateAlarm = new CalButton("Update");
        	add(updateAlarm, gbc(6,2));
        	updateAlarm.addMouseListener(new MouseAdapter() {
        		public void mouseClicked(MouseEvent e) {
        			updateAlarm();
        		}
        	});
        }

        if(type==ParticipationStatus.ACCEPTED){
            this.setBackground(Color.decode("#D9EAD3"));
        }else if(type==ParticipationStatus.DECLINED){
            this.setBackground(Color.decode("#E6B8AF"));
        }else{
            this.setBackground(Color.WHITE);
        }
        if(appointment.getResponsibleUser().equals(client.getCurrentUser())){
            this.setBackground(Color.decode("#C9DAF8"));
        }

        if(appointment.getResponsibleUser().equals(client.getCurrentUser())){
            JLabel invitedLabel = new JLabel("Inviterte: " + appointment.getNumberOfInvitedUsers());
            add(invitedLabel, gbc(7,0));
            JLabel acceptedLabel = new JLabel("Skal: " + appointment.getNumberOfAcceptedUsers());
            add(acceptedLabel, gbc(8,0));
            JLabel declinedLabel = new JLabel("Avslått: " + appointment.getNumberOfDeclinedUsers());
            add(declinedLabel, gbc(9,0));
            JLabel noResponseLabel = new JLabel("Ikke svart: " + appointment.getNumberOfUsersWhoHasNotResponded());
            add(noResponseLabel, gbc(10,0));

            JButton edit = new CalButton("Rediger");
            add(edit, gbc(11,1));
            edit.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    makeDialog();

                }
            });
            JButton delete = new CalButton("Slett");
            add(delete, gbc(11,2));
            delete.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    deleteAppointment();
                }
            });
        }else if (appointment.getParticipants().containsKey(client.getCurrentUser())){
            JButton accept = new CalButton("Godta");
            add(accept, gbc(11,0));
            accept.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    updateStatus(ParticipationStatus.ACCEPTED);

                }
            });

            JButton decline = new CalButton("Avslå");
            add(decline, gbc(11,1));
            decline.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    updateStatus(ParticipationStatus.DECLINED);
                }
            });
            JButton delete = new CalButton("Slett");
            add(delete, gbc(11,2));
            delete.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    removeParticipant();
                }
            });
        }
    }

    private void removeParticipant() {
        updateStatus(ParticipationStatus.HIDDEN);
        weekView.closeAppView();
    }

    private void deleteAppointment() {
        client.deleteAppointment(appointment.getId());
        weekView.closeAppView();
    }

    public GridBagConstraints gbc(int y, int x){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        if(x<=3 && y<=4){
            c.gridwidth=3;
        }else if(y==6){
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        if(y == 11){
            c.anchor = GridBagConstraints.SOUTH;
            c.weighty = 1.0;
        }
        else{
            c.anchor = GridBagConstraints.WEST;
        }
        c.insets = new Insets(5,5,5,5);
        return c;
    }
    public void updateStatus(ParticipationStatus status){
        client.updateParticipationStatus(appointment.getId(), status);
        weekView.closeAppView();
    }
    public void updateAlarm() {
    	client.updateAlarm(appointment.getId(), Integer.parseInt(alarm.getText()));
    }
    public void makeDialog(){
        weekView.closeAppView();
        editAppointmentDialog = new JDialog(parentFrame, true);
        editAppointmentDialog.setPreferredSize(new Dimension(850, 450));
        editAppointmentDialog.setResizable(true);
        editAppointmentDialog.setTitle("Rediger avtale");
        editAppointmentDialog.setContentPane(new MakeAppointment(parentFrame, client, appointment));
        editAppointmentDialog.pack();
        editAppointmentDialog.setLocationRelativeTo(parentFrame);
        editAppointmentDialog.setVisible(true);
        editAppointmentDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
