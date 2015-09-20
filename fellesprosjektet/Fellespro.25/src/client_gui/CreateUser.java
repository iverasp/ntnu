package client_gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.transform.Source;

import database.DBControl;
import database.MD5Hash;

public class CreateUser extends JPanel{
	DBControl dbc = new DBControl();
	MD5Hash md5 = new MD5Hash();
	//Here we create the variables for the class
	private GridBagConstraints gbc = new GridBagConstraints();
	private JButton createUserButton;
	
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField emailTextField;
	private JTextField phoneTextField;
	
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel emailLabel;
	private JLabel phoneLabel;
	
	public CreateUser() {
		setLayout(new GridBagLayout());
		
		usernameLabel = new JLabel("Brukernavn: ");
		gbc.gridx=0;
		gbc.gridy=0;
		add(usernameLabel, gbc);
		
		passwordLabel = new JLabel("Passord: ");
		gbc.gridx=0;
		gbc.gridy=1;
		add(passwordLabel, gbc);
		
		firstNameLabel = new JLabel("Fornavn: ");
		gbc.gridx=0;
		gbc.gridy=2;
		add(firstNameLabel, gbc);
		
		lastNameLabel = new JLabel("Etternavn: ");
		gbc.gridx=0;
		gbc.gridy=3;
		add(lastNameLabel, gbc);
		
		emailLabel = new JLabel("epost: ");
		gbc.gridx=0;
		gbc.gridy=4;
		add(emailLabel, gbc);
		
		phoneLabel = new JLabel("Telefon: ");
		gbc.gridx=0;
		gbc.gridy=5;
		add(phoneLabel, gbc);
		
		
		usernameTextField = new JTextField(15);
		gbc.gridx=1;
		gbc.gridy=0;
		add(usernameTextField, gbc);
		
		passwordTextField = new JPasswordField(15);
		gbc.gridx=1;
		gbc.gridy=1;
		add(passwordTextField, gbc);
		
		firstNameTextField = new JTextField(15);
		gbc.gridx=1;
		gbc.gridy=2;
		add(firstNameTextField, gbc);
		
		lastNameTextField = new JTextField(15);
		gbc.gridx=1;
		gbc.gridy=3;
		add(lastNameTextField, gbc);
		
		emailTextField = new JTextField(15);
		gbc.gridx=1;
		gbc.gridy=4;
		add(emailTextField, gbc);
		
		phoneTextField = new JTextField(15);
		gbc.gridx=1;
		gbc.gridy=5;
		add(phoneTextField, gbc);
		
		createUserButton = new JButton("Lag bruker");
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() == createUserButton) {
					String usernameInput = usernameTextField.getText();
					String passwordInput = pwToString(passwordTextField.getPassword());
					String fnInput = firstNameTextField.getText();
					String lnInput = lastNameTextField.getText();
					String emailInput = emailTextField.getText();
					String phoneInput = phoneTextField.getText();
					
					//Encrypt the password and send data
					try {
						String cryptPW = md5.MD5(passwordInput);
						addUser(usernameInput, cryptPW,fnInput,lnInput,emailInput,phoneInput);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}			
		};
		
		createUserButton.addActionListener(listener);
		gbc.gridx=1;
		gbc.gridy=6;
		add(createUserButton, gbc);
	}
	//The login function uses the loginDBC from DBControl and checks if the username and password matches / exists
	private void addUser(String usernameInput, String passwordInput, String fnInput, String lnInput, String emailInput, String phoneInput) {
		if (dbc.addUserDBC(usernameInput, passwordInput,fnInput,lnInput,emailInput,phoneInput)) {
			System.out.println("User created!");
		}
	}
	
	//Converts the list of chars to a string
	public String pwToString(char[] list) {
		String pw = "";
		for (int i = 0; i < list.length; i++) {
			pw += list[i];
		}
		return pw;
	}
	
//	public static void main (String [] args) {
//		CreateUser createUser = new CreateUser();		
//		JFrame frame = new JFrame("Create User");
//		frame.setContentPane(createUser);
//		frame.pack();
//		frame.setVisible(true);	
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	}
}
