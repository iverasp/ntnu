package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Group implements Serializable {
	private String name;
	private ArrayList<Group> children;
	private ArrayList<String> users;
	private String parentName;

	public Group(String name, ArrayList<String> users, String parentName) {
		super();
		this.name = name;
		this.users = users;
		this.parentName = parentName;
		children = new ArrayList<Group>();
	}

	public Group(String name, ArrayList<Group> children, ArrayList<String> users) {
		this.name = name;
		this.children = children;
		this.users = users;
		children = new ArrayList<Group>();
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Group> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Group> children) {
		this.children = children;
	}

	public void addChildren(Group group){
		children.add(group);
	}
	@Override
	public String toString() {
		return  "(" +name + "," + users
				+ " parentName=" + parentName + ") ";
	}

	public ArrayList<String> getUsersInChildren(){
		ArrayList<String> allUsers = users;

		for (int j=0; j<=children.size() -1; j++) {
			allUsers.addAll(children.get(j).getUsersInChildren());
		}

		return allUsers;
	}
	
	public ArrayList<String> getAllUsers() {
		 HashSet<String> set = new HashSet<String>();
		 set.addAll(this.getUsersInChildren());
		 set.addAll(this.getUsers());
		 ArrayList<String> list = new ArrayList<String>();
		 for (String s: set) {
			 list.add(s);
		 }
		 return list;
	}
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public ArrayList<String> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}


}