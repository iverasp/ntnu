package client_gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import client.CalClient;
import interfaces.AppointmentListener;
import model.Appointment;
import model.ParticipationStatus;

public class StatusColumnCellRenderer extends DefaultTableCellRenderer{
	private static final long serialVersionUID = 1L;

	CalClient client;
	Calendar cal;
    WeekView weekView;
    int counter;

    /* Making the Appointment Borders*/
    Border redApBorder = BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#85200C"));
    Border blueApBorder = BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#1155CC"));
    Border greenApBorder = BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#6AA84F"));
    Border invBorder = BorderFactory.createDashedBorder(Color.GRAY, 1, 5, 3, true);
    Border duplicateBorder = BorderFactory.createDashedBorder(Color.decode("#f1c232"), 1, 5, 3, true);

    /* Making the Appointment Borders - selected */
    Border redApBorderSelected = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.decode("#85200C"));
    Border blueApBorderSelected = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.decode("#1155CC"));
    Border greenApBorderSelected = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.decode("#6AA84F"));
    Border duplicateBorderSelected = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#f1c232"));



    Color redBackground = Color.decode("#E6B8AF");
    Color blueBackground = Color.decode("#C9DAF8");
    Color greenBackground = Color.decode("#D9EAD3");
    Color invBackground = Color.decode("#EEEEEE");
    Color invBackgroundSelected = Color.decode("#cfcfcf");
    Color dupblicateBackgroud = Color.decode("#ffe599");

	public StatusColumnCellRenderer(CalClient client, Calendar cal, WeekView weekView) {
		this.client = client;
        this.cal = cal;
        this.weekView = weekView;
	}

	@Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        /* Initializing cells */
	    CalendarModel tableModel = (CalendarModel) table.getModel();
	    if (tableModel.getOddEven(row)) {
	      l.setBackground(new Color(248, 248, 248));
	    } else {
	      l.setBackground(Color.WHITE);
	    }

	    if (col == 0) {
	    	l.setHorizontalAlignment(RIGHT);
	    }

        counter = 0;
        /* Setting appointment cells */
        for(Appointment a: weekView.getAppointments()){
            if(col == weekView.getAppCol(a) && row == weekView.getAppRow(a)){
                counter++;
                if(a.getResponsibleUser().equals(client.getCurrentUser())){
                    if(hasFocus){
                        l.setBackground(blueBackground);
                        l.setBorder(blueApBorderSelected);
                    }
                    else{
                        l.setBackground(blueBackground);
                        l.setBorder(blueApBorder);
                    }
                    l.setText("  "+a.getTitle());
                }else{
                    ParticipationStatus type = a.getParticipantStatus(client.getCurrentUser());
                    if(type==ParticipationStatus.ACCEPTED){
                        if(hasFocus){
                            l.setBackground(greenBackground);
                            l.setBorder(greenApBorderSelected);
                        }
                        else{
                            l.setBackground(greenBackground);
                            l.setBorder(greenApBorder);
                        }
                    }else if (type==ParticipationStatus.DECLINED){
                        if(hasFocus){
                            l.setBackground(redBackground);
                            l.setBorder(redApBorderSelected);
                        }
                        else{
                            l.setBackground(redBackground);
                            l.setBorder(redApBorder);
                        }
                    }else if (type==ParticipationStatus.INVITED){
                        if(hasFocus){
                            l.setBackground(invBackgroundSelected);
                            l.setBorder(invBorder);
                        }
                        else{
                            l.setBackground(invBackground);
                            l.setBorder(invBorder);

                        }
                    }
                    l.setText("  "+a.getTitle());
                }
            }
        }
        if(counter>1){
            l.setText("Flere avtaler");
            if(hasFocus){
                l.setBorder(duplicateBorderSelected);
                l.setBackground(dupblicateBackgroud);
            }else{
                l.setBorder(duplicateBorder);
                l.setBackground(dupblicateBackgroud);
            }
        }

        l.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		System.out.println("clicked");
        	}
        });
        
	  return l;

	}
}
