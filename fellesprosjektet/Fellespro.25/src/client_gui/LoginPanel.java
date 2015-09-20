package client_gui;

import interfaces.ServerFeedbackListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import protocol.ServerFeedback;
import client.CalClient;
import database.MD5Hash;
import database.DBControl;

public class LoginPanel extends JPanel implements ServerFeedbackListener {

	private static final long serialVersionUID = 1L;
	
//	private MD5Hash md5 = new MD5Hash();
	
	//Here we create the variables for the class
	private CalButton loginButton;
	private CalButton registerButton;
	private String usernameInput;
	private String passwordInput;
	private JTextField usernameTextField;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel errorMessage;
	private JPasswordField passfield;
	
	private JDialog registerDialog;
	
	CountDownLatch loginSignal;
	CalClient client;
	
//	ImageIcon logo = new ImageIcon("img/logo.jpg");
//	JLabel logoLabel = new JLabel();
	
	public LoginPanel(JFrame parentFrame, CountDownLatch loginSignal, CalClient client) {
		this.loginSignal = loginSignal;
		this.client = client;
		
//		logoLabel.setIcon(logo);
//		add(logoLabel);
		
		// Register ServerFeedbackListener
		client.addServerFeedbackListener(this);
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() == loginButton) {
					loginUser();
				}
				else if(event.getSource()==registerButton) {
					addRegisterDialog();
				}
			}	
		};
		
		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					loginUser();
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		};
		
		setLayout(new GridBagLayout());
		setBackground(Color.white);
		
		usernameLabel = new JLabel("Brukernavn: ");
		add(usernameLabel, gbc(0,0));
		
		passwordLabel = new JLabel("Passord: ");
		add(passwordLabel, gbc(1,0));
		
		usernameTextField = new JTextField(15);
		add(usernameTextField, gbc(0,1));
		usernameTextField.addKeyListener(keyListener);
		
		passfield = new JPasswordField(15);
		add(passfield, gbc(1,1));
		passfield.addKeyListener(keyListener);
		
		loginButton = new CalButton("Logg inn");
        loginButton.setPreferredSize(new Dimension(170, 30));
		//registerButton = new CalButton("Registrer ny bruker");
		
		loginButton.addActionListener(listener);
		add(loginButton, gbc(2,0));
		
		//registerButton.addActionListener(listener);
		//add(registerButton, gbc(2,1));
		
		errorMessage = new JLabel(" ");
		errorMessage.setForeground(Color.RED);
		add(errorMessage, gbc(3,0));
		
		registerDialog = new JDialog(parentFrame, true);
		registerDialog.setPreferredSize(new Dimension(400, 400));
		registerDialog.setTitle("Lag bruker");
		registerDialog.pack();
		registerDialog.setLocationRelativeTo(parentFrame);		
		setVisible(true);
	}
	private void addRegisterDialog(){
		CreateUser createUser = new CreateUser();
		registerDialog.setContentPane(createUser);
		registerDialog.setVisible(true);
	}
	
	public GridBagConstraints gbc(int y, int x){
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		//Extra padding to the left
		if(x == 0 && (y == 2 || y == 3)){
			// ErrorMessage label
			if(x==0 && y==2){
                c.gridwidth = 3;
            }
            if(x == 0 && y == 3){
				c.gridwidth = 2;
			}
			c.insets = new Insets(5,85,5,5);
		}else{
			c.insets = new Insets(5,5,5,5);			
		}
		return c;
	}
	
	private void loginUser() {
		usernameInput = usernameTextField.getText();
		passwordInput = pwToString(passfield.getPassword());
		client.requestLogin(usernameInput, passwordInput);
	//	loginSignal.countDown();
//		try {
//			String cryptPW = MD5Hash.MD5(passwordInput);
//			System.out.println(cryptPW);
//			login(usernameInput, cryptPW);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			errorMessage.setText("Unsupported encoding");
//		} catch (NullPointerException e) {
//			errorMessage.setText("Please enter your username and password");
//		}
	}	
	
	//The login function uses the loginDBC from DBControl and checks if the username and password matches / exists
//	private void login(String usernameInput, String passwordInput) {
//		if (dbc.loginDBC(usernameInput, passwordInput)) {
//			System.out.println("login success!");
//			loginSignal.countDown();
//		}
//	}	
	//Converts the list of chars to a string
	public String pwToString(char[] list) {
		String pw = "";
		for (int i = 0; i < list.length; i++) {
			pw += list[i];
		}
		return pw;
	}
//	public static void main (String [] args) {
//		LogIn logIn = new LogIn();		
//		JFrame frame = new JFrame("Logg inn");
//		frame.setContentPane(logIn);
//		frame.pack();
//		frame.setVisible(true);	
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	}
	@Override
	public void onServerFeedback(ServerFeedback feedback) {
		if (feedback.getEvent().equals("login")) {
			if (feedback.getType() == ServerFeedback.OK) {
				loginSignal.countDown();
			} else if (feedback.getType() == ServerFeedback.ERROR) {
				errorMessage.setText(feedback.getMessage());
			}
		}
		
	}
}
