package server;

import java.util.ArrayList;

import database.DBControl;
import model.User;
import model.Group;

public class GroupHandler {
	DBControl dbc = new DBControl();
	UserHandler userHandler = new UserHandler();
	
	public ArrayList<String> getAllUsersInGroup(String groupName){
		return dbc.getAllUsersInGroup(groupName);
	}
	
	public ArrayList<String> getAllUsersInGroup(String groupName,ArrayList<String> users){
		ArrayList<String> usersInGroup = new ArrayList<String>();
		for (int j=0; j<=users.size() -1; j+=2) {
			
			if(users.get(j).equals(groupName)){
				usersInGroup.add(users.get(j+1));

			}
		}
		
		return usersInGroup;
	}
	
	public Group createGroup(String groupName, String parentName){
		ArrayList<String> users = getAllUsersInGroup(groupName);
		return new Group(groupName,users,parentName);
	}
	
	public Group createGroup(String groupName, String parentName,ArrayList<String> users){
		ArrayList<String> users2 = getAllUsersInGroup(groupName,users);
		return new Group(groupName,users2,parentName);
	}
	
	public ArrayList<Group> getAllGroups(){
		System.out.println("Getting all groups. This can take a while. Sorry");
		ArrayList<String> groupStrings = dbc.getGroups();
		ArrayList<Group> groups = new ArrayList<Group>();
		System.out.println("	-Getting from DB done");
		
		ArrayList<String> members = dbc.getAllMembers();
		
		for (int j=0; j<=groupStrings.size() -1; j+=2) {
			groups.add(createGroup(groupStrings.get(j),groupStrings.get(j+1),members));
			System.out.println("		-Group " + groupStrings.get(j));
		}
		System.out.println("	-Make group objects done");
		
		/*
		for (int j=0; j<=groups.size() -1; j++) {
			Group child = groups.get(j);
			String parentName = child.getParentName();
			if(parentName == null) continue;
			
			for (int k=0; k<=groups.size() -1; k++) {
				if(groups.get(k).getName().equals(parentName)){
					groups.get(k).addChildren(child);
					break;
				}
			}
				
		}
		*/
		System.out.println("	-Done adding children");
		
		
		
		return groups;
	}

}
