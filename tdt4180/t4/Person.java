package t4;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Person {

	private String name, dateOfBirth, email;
	private int height;
	private Gender gender;
	private PropertyChangeSupport propChSp;
	
	public Person(String name) {
		this.name = name;
		propChSp = new PropertyChangeSupport(this);
	}

	public void addPropChLs(PropertyChangeListener listener) {
		propChSp.addPropertyChangeListener(listener);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		String old = this.name;
		this.name = name;
		propChSp.firePropertyChange("name", old, name);
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		String old = this.dateOfBirth;
		this.dateOfBirth = dateOfBirth;
		propChSp.firePropertyChange("birth", old, dateOfBirth);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		String old = this.email;
		this.email = email;
		propChSp.firePropertyChange("email", old, email);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		int old = this.height;
		this.height = height;
		propChSp.firePropertyChange("height", old, height);
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		Gender old = this.gender;
		this.gender = gender;
		propChSp.firePropertyChange("gender", old, gender);
	}
}
