package client_gui;

import interfaces.NotificationListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Notification;
import client.CalClient;

public class NotificationsPanel extends JPanel implements NotificationListener {

	private static final long serialVersionUID = 1L;
	
	protected JDialog appointmentDialog;
	CalButton manyCalButton;
	JLabel notificationLabel;
	JPanel descriptionPanel;
	Dimension notificationPanelDim;
	Dimension notificationAlertDim;
	String notificationText;
	JLabel notificationAlert;
	JPanel userPanel;
	JLabel userLabel;
	
	CalClient client;
	List<Notification> notifications;
	
	ImageIcon notificationIcon;
    JFrame parentFrame;

	public NotificationsPanel(CalClient client, JFrame parentFrame) {
		this.client = client;
        this.parentFrame = parentFrame;
		client.addNotificationListener(this);
		notifications = new ArrayList<Notification>();
		notificationText = "";
		notificationAlert = new JLabel();
		notificationAlertDim = new Dimension(25 ,25);
		notificationAlert.setBorder(BorderFactory.createEmptyBorder());
		notificationAlert.setHorizontalAlignment(JLabel.RIGHT);
		notificationAlert.setPreferredSize(notificationAlertDim);
		notificationAlert.setForeground(Color.RED);
		
		manyCalButton = new CalButton("Vis flere");
		manyCalButton.setBounds(0,0,30,25);
		manyCalButton.addActionListener(listener);
		manyCalButton.setHorizontalAlignment(JLabel.RIGHT);
		
		notificationPanelDim = new Dimension(900, 50);
		descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.setBackground(Color.WHITE);
		descriptionPanel.setPreferredSize(new Dimension((int) notificationPanelDim.getWidth() - 360, (int) notificationPanelDim.getHeight()));
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setPreferredSize(notificationPanelDim);
		
		userPanel = new JPanel();
		userPanel.setPreferredSize(new Dimension(200, (int) descriptionPanel.getPreferredSize().getHeight()));
		userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userPanel.setBackground(Color.WHITE);
		userLabel = new JLabel("Hei, " + client.getCurrentUser());
		userPanel.add(userLabel);
		
		manyCalButton = new CalButton("Vis brukere");
		manyCalButton.setBounds(0,0,30,25);
		manyCalButton.addActionListener(listener);
		manyCalButton.setHorizontalAlignment(JLabel.RIGHT);
		add(userPanel);
		add(descriptionPanel);
		add(notificationAlert);
		add(manyCalButton);
		
		notificationLabel = new JLabel();
		notificationLabel.setHorizontalAlignment(JLabel.LEFT);
		notificationLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!notifications.isEmpty()) {
					notifications.remove(notifications.size() - 1);
					updateNotifications();
					updateAlertNotifications(notifications.size());
				}
			}
		});
		
		notificationIcon = new ImageIcon("img/cross_tilted_12x12.png");
		
		descriptionPanel.add(notificationLabel);
	}
	
	private ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ShowUsers su = new ShowUsers(client);
			JDialog frame = new JDialog(parentFrame, true);
            frame.setPreferredSize(new Dimension(220, 350));
            frame.setContentPane(su);
            frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(parentFrame);
            frame.setResizable(false);
		}
	};
	
	private void updateNotifications() {
		if (!notifications.isEmpty()) {
			notificationLabel.setText(notifications.get(notifications.size() - 1).getMessage());
			notificationLabel.setIcon(notificationIcon);
		} else {
			notificationLabel.setText("");
			notificationLabel.setIcon(null);
		}
	}
    
    private void updateAlertNotifications(int size) {
    	if (size > 0) {
    		notificationAlert.setText(Integer.toString(size));
    	} else {
    		notificationAlert.setText("");
    	}
    }
    
	@Override
	public void onAddNotification(Notification notification) {
		notifications.add(notification);
		updateAlertNotifications(notifications.size());
		updateNotifications();
		
	}

}
