package model;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String phone;

	public User(String username, String firstName, String lastName, String password, String email, String phone) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		//setPassword(password);
		this.email = email;
		this.phone = phone;

	}

	public User(String username, String firstName, String lastName) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;

	}

	public User(ArrayList<String> list){
		this.username = list.get(0);
		this.password = list.get(1);
		this.firstName = list.get(2);
		this.lastName = list.get(3);
		this.email = list.get(4);
		this.phone = list.get(5);

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String toString(){
		return "Name: " + getFullName() + " Username: " + getUsername() + " Phone: " + getPhone() + " mail: " + getEmail();
	}

}