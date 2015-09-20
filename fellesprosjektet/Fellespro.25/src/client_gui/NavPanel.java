package client_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class NavPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	JPanel navPanelLeft;
	JPanel navPanelRight;
	JPanel navPanelCenter;

	Dimension navPanelRightDim;
	Dimension navPanelLeftDim;
	Dimension navPanelCenterDim;

	JButton previousWeekButton;
	JButton nextWeekButton;

	ImageIcon arrowLeft ;
	ImageIcon arrowRight;

	/* Dialogs */
	JDialog appointmentDialog;

	JLabel dateViewLabel;

	Color bg;

	Calendar cal;
	int currentYear;
	int currentMonth;
	int currentWeek;

	String[] norMonths = {"Januar", "Februar", "Mars", "April", "Mai", "Juni <3", "Juli", "August", "September", "Oktober", "November", "Desember"};

    WeekView weekView;

	public NavPanel(Dimension navPanelDim, Dimension tableDim, JTable table, Calendar cal, WeekView weekView) {
		bg = Color.WHITE;

		this.cal = cal;
        this.weekView = weekView;
		
		navPanelLeft = new JPanel();
		navPanelRight = new JPanel();
		navPanelCenter = new JPanel();

		navPanelRightDim = new Dimension((int) tableDim.getWidth() / 3, (int) navPanelDim.getHeight());
		navPanelLeftDim = new Dimension((int) tableDim.getWidth() / 3, (int) navPanelDim.getHeight());
		navPanelCenterDim = new Dimension((int) tableDim.getWidth() / 3, (int) navPanelDim.getHeight());

		navPanelRight.setPreferredSize(navPanelRightDim);
		navPanelLeft.setPreferredSize(navPanelLeftDim);
		navPanelCenter.setPreferredSize(navPanelCenterDim);
		navPanelRight.setBackground(bg);
		navPanelLeft.setBackground(bg);
		navPanelRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
		navPanelLeft.setLayout(new FlowLayout(FlowLayout.LEFT));

		arrowLeft = new ImageIcon("img/arrow_left.png");
		arrowRight = new ImageIcon("img/arrow_right.png");

		previousWeekButton = new JButton("Forrige uke");
		previousWeekButton.setIcon(arrowLeft);
		previousWeekButton.setBorder(BorderFactory.createEmptyBorder());
		previousWeekButton.setBackground(bg);
		previousWeekButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		previousWeekButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		nextWeekButton = new JButton("Neste uke");
		nextWeekButton.setForeground(Color.decode("#999999"));
		previousWeekButton.setForeground(Color.decode("#999999"));
		
		nextWeekButton.setIcon(arrowRight);
		nextWeekButton.setBorder(BorderFactory.createEmptyBorder());
		nextWeekButton.setBackground(bg);
		nextWeekButton.setHorizontalTextPosition(SwingConstants.LEFT);
		nextWeekButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		previousWeekButton.addActionListener(this);
		nextWeekButton.addActionListener(this);

		currentYear = cal.get(Calendar.YEAR);
		currentMonth = cal.get(Calendar.MONTH);
		currentWeek = cal.get(Calendar.WEEK_OF_YEAR);

		dateViewLabel = new JLabel();
		dateViewLabel.setForeground(Color.decode("#999999"));
		updateDateViewLabel();

		navPanelRight.add(nextWeekButton);
		navPanelLeft.add(previousWeekButton);
		navPanelCenter.setBackground(bg);
		navPanelCenter.add(dateViewLabel);
		add(navPanelLeft, BorderLayout.WEST);
		add(navPanelCenter, BorderLayout.CENTER);
		add(navPanelRight, BorderLayout.EAST);
	}

	public void setCurrentYear() {
		currentYear = cal.get(Calendar.YEAR);
	}

	public void setCurrentMonth() {
		currentMonth = cal.get(Calendar.MONTH);
	}

	public void setCurrentWeek(int week) {
		cal.set(Calendar.WEEK_OF_YEAR, week);
		currentWeek = cal.get(Calendar.WEEK_OF_YEAR);

		setCurrentYear();
		setCurrentMonth();
		
		updateDateViewLabel();
	}

	private void updateDateViewLabel() {
		dateViewLabel.setText("<html><center><sup>" + norMonths[currentMonth] + " " + currentYear + "</sup><br><sub>Uke " + currentWeek + "</sub></center></html>");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();

		if (command.equals("Neste uke")) {
			setCurrentWeek(currentWeek + 1);
            weekView.onGetUserAppointments();
		}
		if (command.equals("Forrige uke")) {
			setCurrentWeek(currentWeek - 1);
            weekView.onGetUserAppointments();
		}

	}

}