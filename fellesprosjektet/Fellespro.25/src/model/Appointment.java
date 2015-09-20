package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;



public class Appointment implements Serializable {

	private int id;
	private String title;
	private Timestamp startTime;
	private Timestamp endTime;


	private String location;
	private String description;

	private String responsibleUser;

	private String reservedRoom;

	private int alarm;
	
	private ArrayList<String> emails;
	


	private transient PropertyChangeSupport pcs;

	private HashMap<String, ParticipationStatus> participants;


	// Constructor
	public Appointment() {



		participants = new HashMap<String, ParticipationStatus>();

	}

	public void createPropertyChangeSupport() {
		pcs = new PropertyChangeSupport(this);		
	}

	

	

	public Appointment(String title, Timestamp startTime, Timestamp endTime, String location,
			String description, String responsibleUser, String reservedRoom) {
		
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.description = description;
		this.responsibleUser = responsibleUser;
		this.reservedRoom = reservedRoom;
		
		setRoomToNoneIfNull();
		
		participants = new HashMap<String, ParticipationStatus>();

	}

	public Appointment(ArrayList<String> list) {
		this.id = Integer.valueOf(list.get(0));
		this.startTime = java.sql.Timestamp.valueOf(list.get(1));
		this.endTime = java.sql.Timestamp.valueOf(list.get(2));
		this.location = list.get(4);
		this.description = list.get(3);
		this.responsibleUser = list.get(5);
		this.reservedRoom = list.get(6);
		this.title = list.get(7);
		
		setRoomToNoneIfNull();
		
		participants = new HashMap<String, ParticipationStatus>();
	}
	
	public Appointment(List<String> list) {
		this.id = Integer.valueOf(list.get(0));
		this.startTime = java.sql.Timestamp.valueOf(list.get(1));
		this.endTime = java.sql.Timestamp.valueOf(list.get(2));
		this.location = list.get(4);
		this.description = list.get(3);
		this.responsibleUser = list.get(5);
		this.reservedRoom = list.get(6);
		this.title = list.get(7);
		
		setRoomToNoneIfNull();
		
		participants = new HashMap<String, ParticipationStatus>();
	}
	
	public Appointment(ArrayList<String> list, HashMap<String, ParticipationStatus> participants) {
		this.id = Integer.valueOf(list.get(0));
		this.startTime = java.sql.Timestamp.valueOf(list.get(1));
		this.endTime = java.sql.Timestamp.valueOf(list.get(2));
		this.description = list.get(4);
		this.location = list.get(3);
		this.responsibleUser = list.get(5);
		this.reservedRoom = list.get(6);
		this.title = list.get(7);
		this.participants = participants;
		
		setRoomToNoneIfNull();

		pcs = new PropertyChangeSupport(this);
	}



	// PropertyChangeSupport
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}


	// Returns the id that the appointment has in the database
	public int getId() {
		return id;
	}


	// Title
	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		if (pcs != null) pcs.firePropertyChange("title", oldTitle, title);
	}

	public String getTitle() {
		return title;
	}

	// StartTime
	public void setStartTime(Timestamp startTime) {
		Timestamp oldStartTime = this.startTime;
		this.startTime = startTime;
		if (pcs != null) pcs.firePropertyChange("startTime", oldStartTime, startTime);
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	// EndTime
	public void setEndTime(Timestamp endTime) {
		Timestamp oldEndTime = this.endTime;
		this.endTime = endTime;
		if (pcs != null) pcs.firePropertyChange("endTime", oldEndTime, endTime);
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	// Calculates the duration of the appointment in minutes
	public int getDuration() {
		// TODO calculate duration in minutes
		return 0;
	}

	// Location
	public void setLocation(String location) {
		String oldLocation = this.location;
		this.location = location;
		if (pcs != null) pcs.firePropertyChange("location", oldLocation, location);
	}

	public String getLocation() {
		return location;
	}

	// Responsible user
	public void setResponsibleUser(String responsibleUser) {
		String oldResponsibleUser = this.responsibleUser;
		this.responsibleUser = responsibleUser;
		if (pcs != null) pcs.firePropertyChange("responsibleUser", oldResponsibleUser, responsibleUser);
	}

	public String getResponsibleUser() {
		return responsibleUser;
	}
	
	public void setEmails(ArrayList<String> emails){
		this.emails = emails;
	}
	
	public ArrayList<String> getEmails(){
		return emails;
	}

	// Description
	public void setDescription(String description) {
		String oldDescription = this.description;
		this.description = description;
		if (pcs != null) pcs.firePropertyChange("description", oldDescription, description);
	}

	public String getDescription() {
		return description;
	}

	
	public void setId(int id){
		this.id = id;
	}
	
	public void setRoomToNoneIfNull(){
		if(reservedRoom != null) return;
		this.reservedRoom = "None";
		System.out.println("RoomNumber er satt til None");
	}
	
	// Set reserved room
	public void setReservedRoom(String reservedRoom) {
		String oldReservedRoom = this.reservedRoom;
		setRoomToNoneIfNull();
		this.reservedRoom = reservedRoom;
		//if (pcs != null) pcs.firePropertyChange("reservedRoom", oldReservedRoom, reservedRoom);
	}

	public String getReservedRoom() {
		return reservedRoom;
	}
	

	// Alarm
	public int getAlarm() {
		return alarm;
	}


	// Participants
	public void addParticipant(String participant) {
		if(participants.containsKey(participant)){
			System.out.println(participant + " has acceptetd and status will not be changed to invited");
			return;
		}
		participants.put(participant, ParticipationStatus.INVITED);
	}

	public void removeParticipant(User participant) {
		participants.remove(participant);
		// TODO PropertyChangeSupport
	}

	public HashMap<String, ParticipationStatus> getParticipants() {
		
		return participants;
	}
	
	public  void setParticipants(HashMap<String, ParticipationStatus> part) {
		participants = part;
		//System.out.println(participants.size());
	}

	public void setParticipantStatus(String participant, ParticipationStatus status) {
		participants.put(participant, status);
		// TOOD PropertyChangeSupport
	}

	public ParticipationStatus getParticipantStatus(String participant) {
		return participants.get(participant);
	}
	
	public int getNumberOfInvitedUsers(){
		return participants.size();
	}
	
	public int getNumberOfUserWithStatus(ParticipationStatus status){
		int numberOf = 0;
		
		for(Entry<String, ParticipationStatus> entry : participants.entrySet()) {
		    if( entry.getValue().equals(status)) numberOf++;
		}
		
		return numberOf;
	}
	
	public ArrayList<String> getParticipantList() {
		return new ArrayList<String>(participants.keySet());
	}
	
	public int getNumberOfAcceptedUsers(){
		return getNumberOfUserWithStatus(ParticipationStatus.ACCEPTED);
	}
	
	public int getNumberOfDeclinedUsers(){
		return getNumberOfUserWithStatus(ParticipationStatus.DECLINED);
	}
	
	public int getNumberOfUsersWhoHasNotResponded(){
		return getNumberOfUserWithStatus(ParticipationStatus.INVITED);
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", title=" + title + ", startTime="
				+ startTime + ", endTime=" + endTime + ", location=" + location
				+ ", description=" + description + ", responsibleUser="
				+ responsibleUser + ", reservedRoom=" + reservedRoom
				+ ", alarm=" + alarm + "Participants=" + participants.size() + "]";
	}
	
	public String getResponsibleUserWithStorebokstav(){
		return responsibleUser.substring(0, 1).toUpperCase() + responsibleUser.substring(1);
	}
	
	public String getEventString(){
		return getResponsibleUserWithStorebokstav() + " sin avtale '" + title + "'";
	}




	


}