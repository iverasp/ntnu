package protocol;

import java.io.Serializable;

public class LoginProtocol implements Serializable {

	private String username;
	private String password;
	
	public LoginProtocol(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
