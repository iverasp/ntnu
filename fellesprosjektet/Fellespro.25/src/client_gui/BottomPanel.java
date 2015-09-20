package client_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.CalClient;

public class BottomPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	ImageIcon infoInvitation;
	ImageIcon infoApproved;
	ImageIcon infoDenied;
	ImageIcon infoCreate;
	
	JLabel infoInvitationLabel;
	JLabel infoApprovedLabel;
	JLabel infoDeniedLabel;
	JLabel infoCreateLabel;
	
	CalButton logoutButton;
	
	CalClient client;
	CountDownLatch logoutSignal;
	
	public BottomPanel(Dimension bottomPanelDim, CountDownLatch logoutSignal, CalClient client) {
		
		this.client = client;
		this.logoutSignal = logoutSignal;
		
		setLayout(new FlowLayout());
		setPreferredSize(bottomPanelDim);
		
		infoInvitation = new ImageIcon("img/dashed.png");
		infoApproved = new ImageIcon("img/green.png");
		infoDenied = new ImageIcon("img/red.png");
		infoCreate = new ImageIcon("img/blue.png");
		infoInvitationLabel = new JLabel("Invitasjon");
		infoApprovedLabel = new JLabel("Godtatt avtale");
		infoDeniedLabel = new JLabel("Avslaatt avtale");
		infoCreateLabel = new JLabel("Opprett avtale");
		infoInvitationLabel.setIcon(infoInvitation);
		infoApprovedLabel.setIcon(infoApproved);
		infoDeniedLabel.setIcon(infoDenied);
		infoCreateLabel.setIcon(infoCreate);
		
		infoInvitationLabel.setForeground(Color.decode("#999999"));
		infoApprovedLabel.setForeground(Color.decode("#999999"));
		infoDeniedLabel.setForeground(Color.decode("#999999"));
		infoCreateLabel.setForeground(Color.decode("#999999"));

		add(infoInvitationLabel);
		add(infoApprovedLabel);
		add(infoDeniedLabel);
		add(infoCreateLabel);
		
		/* Logout button */
		logoutButton = new CalButton("Logg ut");
		logoutButton.addActionListener(listener);
		logoutButton.setBounds(0,0,30,25);
		add(logoutButton);
	}
	
	ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			client.requestLogout();
			logoutSignal.countDown();
		}
		
	};

}
