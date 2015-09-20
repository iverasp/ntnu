package client_gui;

import java.awt.Dimension;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;

import client.CalClient;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	CountDownLatch logoutSignal;
	CountDownLatch loginSignal;

	private CalClient client;
	public MainFrame(CalClient client) {
		
		this.client = client;
		setSize(new Dimension(1024, 768));
		setTitle("CoogleGal");
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		while (true) {
			showLogin();
			showWeekView();
		}
	}

	public void showLogin() {
		loginSignal = new CountDownLatch(1);
		LoginPanel loginPanel = new LoginPanel(this, loginSignal, client );
		add(loginPanel);
		revalidate();
		try {
			loginSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		remove(loginPanel);
	}
	
	public void showWeekView() {
		logoutSignal = new CountDownLatch(1);	
		WeekView weekView = new WeekView(this, logoutSignal, client);
		add(weekView);
		revalidate();
		try {
			logoutSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		remove(weekView);
	}
	

	/*
	public static void main(String[] args) throws InterruptedException {
		JFrame mainframe = new JFrame("Kalendersystem");
		mainframe.setSize(new Dimension(1024, 768));
		mainframe.setResizable(true);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CountDownLatch loginSignal = new CountDownLatch(1);
		

		LogIn login = new LogIn(mainframe, loginSignal);

		mainframe.add(login);

		mainframe.setVisible(true);

		loginSignal.await();

		mainframe.remove(login);
		System.out.println("success");
		WeekView week_view = new WeekView(mainframe, logoutSignal);
		mainframe.add(week_view);
		mainframe.revalidate();

		logoutSignal.await();

		mainframe.dispose();
	
	}
	*/
}