package client_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;
//import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.JTableHeader;

import client.CalClient;
import interfaces.AppointmentListener;
import model.Appointment;

public class WeekView extends JPanel implements ActionListener, AppointmentListener {

	private static final long serialVersionUID = 1L;

	CalendarModel model = new CalendarModel();

	/* Tables */
	JTable table = new JTable(model);

	/* Dimensions */
	Dimension tableDim;
	Dimension topPanelDim;
	Dimension navPanelDim;
	Dimension centerPanelDim;
	Dimension bottomPanelDim;

	/* Labels */
	JLabel appTitleLabel;
	JLabel currentWeekLabel;
	JLabel dateViewLabel;

	/* Buttons */
	CalButton notificationButton;
	JButton addAppointmentButton;

	/* Panels */
	JPanel topPanel;
	JPanel logoPanel;
	JPanel actionPanel;
	NavPanel navPanel;
	JPanel centerPanel;
	BottomPanel bottomPanel;

	/* Scroll bars */
	JScrollPane tableScroller;
	JScrollBar bar;
	BasicScrollBarUI scrollBarUI;

	/* Icons */
	ImageIcon cross;

	/* Dialogs */
	JDialog appointmentDialog;
    JDialog viewAppDialog;

	Color bg;
	
	Calendar cal;

	CountDownLatch logoutSignal;

    JFrame parentFrame;

    WeekView weekView;

    ArrayList<Appointment> appointments = new ArrayList<Appointment>();

	private CalClient client;
	
	public WeekView(JFrame parentFrame, CountDownLatch logoutSignal, CalClient client) {
		super();
		
		this.client = client;
        client.addAppointmentListener(this);
        this.parentFrame = parentFrame;
        weekView = this;

		cal = Calendar.getInstance();
		//cal.setFirstDayOfWeek(3);
		
		this.logoutSignal = logoutSignal;

		bg = Color.WHITE;
		setBackground(bg);

		topPanel = new JPanel();
		centerPanel = new JPanel();
		bottomPanel = new BottomPanel(bottomPanelDim, logoutSignal, client);
		logoPanel = new JPanel();
		actionPanel = new JPanel();
		logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		topPanelDim = new Dimension(1024, 100);
		navPanelDim = new Dimension(1024, 50);
		tableDim = new Dimension(900, 450);
		centerPanelDim = new Dimension(1024, 520);
		bottomPanelDim = new Dimension(1024, 100);
		navPanel = new NavPanel(navPanelDim, tableDim, table, cal, weekView);

		centerPanel.setBackground(bg);
		topPanel.setBackground(bg);
		bottomPanel.setBackground(bg);
		navPanel.setBackground(bg);

		topPanel.setPreferredSize(topPanelDim);
		navPanel.setPreferredSize(navPanelDim);

		appTitleLabel = new JLabel("CoogleGal");
		appTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));

		/* Add appointment button */
		cross = new ImageIcon("img/cross_24x24.png");
		addAppointmentButton = new JButton("Lag avtale");
		addAppointmentButton.setIcon(cross);
		addAppointmentButton.setBackground(bg);
		addAppointmentButton.setBorder(BorderFactory.createEmptyBorder());
		addAppointmentButton.setHorizontalTextPosition(SwingConstants.LEFT);
		addAppointmentButton.addActionListener(this);
		addAppointmentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		addAppointmentButton.setForeground(Color.decode("#999999"));

		logoPanel.add(appTitleLabel);
		logoPanel.setBackground(bg);
		actionPanel.add(addAppointmentButton);
		actionPanel.setBackground(bg);

		logoPanel.setPreferredSize(new Dimension((int) tableDim.getWidth() / 2, (int) topPanelDim.getHeight()));
		actionPanel.setPreferredSize(new Dimension((int) tableDim.getWidth() / 2, (int) topPanelDim.getHeight()));

		topPanel.add(new NotificationsPanel(client, parentFrame), BorderLayout.NORTH);
		topPanel.add(logoPanel, BorderLayout.WEST);
		topPanel.add(actionPanel, BorderLayout.EAST);

		table.setRowHeight(60);
		table.setShowHorizontalLines(false);
		
		tableScroller = new JScrollPane(table);
		tableScroller.setPreferredSize(tableDim);
		tableScroller.setBorder(BorderFactory.createEmptyBorder());
		tableScroller.setBackground(bg);
		tableScroller.setHorizontalScrollBar(null);
		bar = tableScroller.getVerticalScrollBar();
		bar.setPreferredSize(new Dimension(12, 0));
		scrollBarUI = new CalScrollBar();
		
		bar.setUI(scrollBarUI);

		setTableHeader();

		centerPanel.setPreferredSize(centerPanelDim);
		centerPanel.add(navPanel, BorderLayout.NORTH);
		centerPanel.add(tableScroller, BorderLayout.SOUTH);

		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		setCellAppearance();
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable)e.getSource();
					Object cellTarget = (Object)model.getValueAt(target.getSelectedRow(), target.getSelectedColumn());
                    for(Appointment a: appointments){
                        if(target.getSelectedColumn() == getAppCol(a) && target.getSelectedRow() == getAppRow(a)){
                            Appointment targetAppointment = a;
                            createAppointmentView(targetAppointment);
                        }
                    }
				}
			}
		});

	}

    private void createAppointmentView(Appointment appointment){
        viewAppDialog = new JDialog(parentFrame, true);
        viewAppDialog.setPreferredSize(new Dimension(300, 400));
        viewAppDialog.setResizable(true);
        viewAppDialog.setTitle("Appointment");
        viewAppDialog.setContentPane(new AppointmentView(appointment, client, parentFrame, this));
        viewAppDialog.pack();
        viewAppDialog.setLocationRelativeTo(parentFrame);
        viewAppDialog.setVisible(true);
    }
    public void closeAppView(){
        viewAppDialog.setVisible(false);
    }

	private void setCellAppearance() {
		table.getColumnModel().getColumn(0).setMaxWidth(40);

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new StatusColumnCellRenderer(client, cal, weekView));
			if (i != 0) table.getColumnModel().getColumn(i).setMinWidth(121);
			if (i != 0) table.getColumnModel().getColumn(i).setMaxWidth(121);
		}
	}

	private void addAppointment() {
        appointmentDialog = new JDialog(parentFrame, true);
        appointmentDialog.setPreferredSize(new Dimension(850, 450));
        appointmentDialog.setTitle("Lag avtale");
        appointmentDialog.setResizable(true);
        appointmentDialog.setContentPane(new MakeAppointment(parentFrame, client));
        appointmentDialog.pack();
        appointmentDialog.setLocationRelativeTo(parentFrame);
        appointmentDialog.setVisible(true);
        appointmentDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

    public int getAppRow(Appointment a) {
        return a.getStartTime().getHours();
    }

    public int getAppCol(Appointment a) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(a.getStartTime().getTime());
        int dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK)-1);
        if (dayOfWeek == 0) {
        	dayOfWeek = 7;
        }
        return dayOfWeek;
    }

	private void setTableHeader() {
		/* Set table header (days of the week) size and color */
		table.setTableHeader(new JTableHeader(table.getColumnModel()) {

			private static final long serialVersionUID = 1L;

			@Override public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 60;
				d.width = table.getWidth();
				return d;
			}
			@Override public Color getBackground() {
				return bg;
			}

		});

		table.getTableHeader().setDefaultRenderer(new TableHeaderRenderer(cal));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();

		if (command.equals("Lag avtale")) {
			addAppointment();
		}
	}

    public ArrayList<Appointment> getAppointments(){
        return appointments;
    }
    @Override
    public void onGetUserAppointments() {
        appointments = client.getAppointments(cal);
    }

    @Override
    public void onAddAppointment(Appointment appointment) {
        appointments = client.getAppointments(cal);
    }

    @Override
    public void onDeleteAppointment(int appointmentId) {
        appointments = client.getAppointments(cal);
    }
}