package client_gui;

import interfaces.GroupListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.MaskFormatter;

import protocol.ServerFeedback;
import model.Appointment;
import model.Group;
import model.ParticipationStatus;
import model.Room;
import client.CalClient;

import com.toedter.calendar.JDateChooser;

public class MakeAppointment extends JPanel implements GroupListener  {
	
	ArrayList<String> participantsStatus = new ArrayList<String>();
	private boolean dateChosen = false;
	private boolean forceAcc = false;
	private static final long serialVersionUID = 1L;
	protected JDialog appointmentDialog;
	Date startDate;
	private String title;
	private String description;
	private String place;
	private String date;
	private String timeFrom;
	private String timeTo;
	private Room chosenRoom = null;
	private int id=0;
	
	private Appointment app = new Appointment();
	private JTextField titleTextField;
	private JTextField descriptionTextField;
	private JTextField placeTextField;
	private JTextField roomTextField;
    private JTextField emailText;
	private JLabel editLabel;
	private JLabel titleLabel;
	private JLabel timeLabel;
	private JLabel dateLabel;
	private JLabel roomLabel;
	private JLabel placeLabel;
	private JLabel descriptionLabel;
	
	private ArrayList<String> acceptedUsers;
	private ArrayList<Group> groups;
	private ArrayList<String> groupMembersArray;
	private ArrayList<String> invitedMembersArray;
	private JList<String> groupMembersJList;
	private JList<String> invitedMembersJList;
	private DefaultListModel<String> modelGroupMembers;
	private DefaultListModel<String> modelInvitedUsers;
	
	private JButton saveButton;
	private JButton findRoomButton;
    private JButton emailBox;
	private JButton addOneMemberButton;
	private JButton addAllMembersButton;
	private JButton removeOneMemberButton;
	private JButton removeAllMembersButton;
    private JButton approveInvitation;

	
	private JComboBox dropDownMenu;
	private JDateChooser jdc;	
	private Timestamp startStamp;
	private Timestamp stopStamp;
	private MaskFormatter formater;
	private JFormattedTextField formattedStartTime;
	private JFormattedTextField formattedEndTime;
	private FindRoom fr;
	private CalClient client;
	private JFrame parentFrame;

    private BasicScrollBarUI groupScrollBarUI, invScrollBarUI;
    private JScrollPane groupListScroller, invitedListScroller;
    private JScrollBar groupBar, invBar;

    private ArrayList<String> mails;
	
	public MakeAppointment(JFrame parentFrame, CalClient client) {
		this(parentFrame, client, null);
	}
	
	public MakeAppointment(JFrame parentFrame, CalClient client, Appointment inApp) {
		System.out.println("START");
		this.parentFrame = parentFrame;
		this.client = client;
        mails = new ArrayList<String>();
		client.addGroupListener(this);
		setLayout(new GridBagLayout());
		modelInvitedUsers = new DefaultListModel<String>();
		invitedMembersArray = new ArrayList<String>();
		acceptedUsers = new ArrayList<String>();
		
		editLabel = new JLabel("Rediger");
		add(editLabel, gbc(0,0));

		titleLabel = new JLabel("Tittel :");
		add(titleLabel, gbc(1,0));

		timeLabel = new JLabel("Fra / Til :");
		add(timeLabel, gbc(2,0));

		dateLabel = new JLabel("Dato :");
		add(dateLabel, gbc(3,0));

		roomLabel = new JLabel("Rom :");
		add(roomLabel, gbc(4,0));

		placeLabel = new JLabel("Sted :");
		add(placeLabel, gbc(5,0));

		descriptionLabel = new JLabel("Beskrivelse :");
		add(descriptionLabel, gbc(6,0));

		titleTextField = new JTextField(15);
		add(titleTextField, gbc(1,1));
		
		try {
			formater = new MaskFormatter("##:##");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formater.setPlaceholderCharacter('0');
		formattedStartTime  = new JFormattedTextField(formater);
		formattedStartTime.setColumns(7);

		try {
			formater = new MaskFormatter("##:##");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formater.setPlaceholderCharacter('0');
		formattedEndTime  = new JFormattedTextField(formater);
		formattedEndTime.setColumns(7);

		add(formattedStartTime, gbc(2,1));
		add(formattedEndTime, gbc(2,2));

		jdc = new JDateChooser();
		jdc.setDateFormatString("yyyy-MM-dd");
		add(jdc, gbc(3,1));

        roomTextField = new JTextField(5);
        roomTextField.setEditable(false);
        add(roomTextField, gbc(4,1));

        placeTextField = new JTextField(15);
        add(placeTextField, gbc(5,1));

        descriptionTextField = new JTextField(15);
        add(descriptionTextField, gbc(6,1));
        PropertyChangeListener ps = new PropertyChangeListener() {
      
        	//Listener for changes in the calendar
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                	dateChosen = true;
                	jdc.getDate();
                    java.util.Date dateFromDateChooser = (java.util.Date) jdc.getDate();
                    date = String.format("%1$td-%1$tm-%1$tY", dateFromDateChooser);
                }
            }
        };
        jdc.addPropertyChangeListener(ps);

		saveButton = new CalButton("Lagre avtale");
		add(saveButton, gbc(7, 1));

		findRoomButton = new CalButton("Legg til rom");
		add(findRoomButton, gbc(4, 2));
		
		dropDownMenu = new JComboBox();		
		add(dropDownMenu, gbc(0,4));

        emailText = new JTextField();
        emailText.setPreferredSize(new Dimension(150, 20));
        add(emailText, gbc(0, 6));

        emailBox = new CalButton("Send email");
        add(emailBox, gbc(0, 7));
		
		addOneMemberButton = new CalButton(">");
		add(addOneMemberButton,gbc(1,5));
		
		addAllMembersButton = new CalButton(">>");
		add(addAllMembersButton,gbc(2,5));
		
		removeOneMemberButton = new CalButton("<");
		add(removeOneMemberButton, gbc(5,5));
		
		removeAllMembersButton = new CalButton("<<");
		add(removeAllMembersButton, gbc(6,5));



        groupMembersJList = new JList<String>();
        groupListScroller = new JScrollPane(groupMembersJList);
        groupListScroller.setPreferredSize(new Dimension(200, 200));
        groupBar = groupListScroller.getVerticalScrollBar();
        groupBar.setPreferredSize(new Dimension(12, 0));
        groupScrollBarUI = new CalScrollBar();
        groupBar.setUI(groupScrollBarUI);
        add(groupListScroller, gbc(1, 4));

        invitedMembersJList = new JList<String>();
        invitedListScroller = new JScrollPane(invitedMembersJList);
        invitedListScroller.setPreferredSize(new Dimension(200, 200));
        invBar = invitedListScroller.getVerticalScrollBar();
        invBar.setPreferredSize(new Dimension(12, 0));
        invScrollBarUI = new CalScrollBar();
        invBar.setUI(invScrollBarUI);
        add(invitedListScroller, gbc(1, 6));

        approveInvitation = new CalButton("Godkjenn");
        add(approveInvitation, gbc(7, 6));
		
		
		
		//Listener for dropdown menu
		dropDownMenu.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				modelGroupMembers = new DefaultListModel<String>();
				groupMembersJList.setModel(modelGroupMembers);
				if(event.getStateChange() == ItemEvent.SELECTED) {
					getMembers(dropDownMenu.getSelectedItem().toString());
                }
				
			}
			
		});

        //Listener for approve
        approveInvitation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println("actionlistener");
                if(invitedMembersJList.getSelectedValue()!=null){
                	String user = divide(invitedMembersJList.getSelectedValue().toString());
                	acceptedUsers.add(user);
                	int index = invitedMembersJList.getSelectedIndex();
                	
    				modelInvitedUsers.removeElement(invitedMembersJList.getSelectedValue().toString());
    				modelInvitedUsers.add(index, user + "- ACCEPTED");
    				setInvitedMembersJList(modelInvitedUsers);
//                	System.out.println("STATUS: " + user);
//                	app.setParticipantStatus(user, ParticipationStatus.ACCEPTED);
//                	System.out.println("DETTE ER STATUS1: " + app.getParticipants().get("petterek"));                	
//                	forceAcc = true;
                	
                }
            }
        });
		
		//Listener for find room button
		ActionListener listenerRoom = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					openFindRoom();
				}
				catch(Exception e) {
					System.out.println("exception " + e);
					System.out.println("Du m� skrive inn en dato og en tid!");
				}

			}
		};

        ActionListener sendMail = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!emailText.equals("")){
                    mails.add(emailText.getText());
                    emailText.setText("");
                }
            }
        };

		ActionListener listenerSave = new ActionListener() {

			@Override
			//Listener for save appointment
			public void actionPerformed(ActionEvent event) {
					title = titleTextField.getText();
					if (title.equals("") || title.length() > 20) {
						System.out.println("tittelen din er for lang(Over 20 tegn) eller tom");
					}
					else if (dateChosen == false) {
						System.out.println("Du m� velge dato!");
					}
					else  {						
						place = placeTextField.getText();
						description = descriptionTextField.getText();
						getTimestamp();
						app = new Appointment();
						app.setTitle(title);
						app.setStartTime(startStamp);
						app.setEndTime(stopStamp);
						app.setDescription(description);
						app.setLocation(place);
						app.setId(id);
						System.out.println("Date: " + date);
						System.out.println("Title :"  + title);
						System.out.println("Place :" + place);
						System.out.println("Description :" + description);					
						System.out.println("Start: " + startStamp);
						System.out.println("Stopp: " + stopStamp);
						if (getChosenRoom() != null) {
							System.out.println("Room :" + chosenRoom.getRoomNumber());						
							app.setReservedRoom(chosenRoom.getRoomNumber());
						}
						else{
							app.setReservedRoom("None");
							System.out.println("Room: Not chosen");
						}
//						if (gotModel) {
//							invitedMembersArray = divide(participantsStatus);
//						}
	                	System.out.println("DETTE ER STATUSF�RFOR: " + app.getParticipants().get("petterek"));                	

						for (String i: invitedMembersArray) {
							app.addParticipant(i);
							System.out.println("Member : " + i);
						}
						for (String r: acceptedUsers) {
		                	app.setParticipantStatus(r, ParticipationStatus.ACCEPTED);
						}
				    	System.out.println("DETTE ER STATUS2: " + app.getParticipants().get("petterek"));
                        app.setEmails(mails);
						sendAppointment(app);
//						setModel(app);
//						emptyAll();


					}
			}
		};
		
		ActionListener addOrRemoveMemberserListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if(event.getSource().equals(addOneMemberButton)) {
					String s = groupMembersJList.getSelectedValue();
					System.out.println("S: " + s);
					addToInvitedMembersModel(s);
				}
				else if(event.getSource().equals(addAllMembersButton)) {
					addAllMembers();
				}
				else if(event.getSource().equals(removeOneMemberButton)) {
					removeOneMember();
				}
				else if(event.getSource().equals(removeAllMembersButton)) {
					removeAllMembers();
				}
				
			}

						
		};
		saveButton.addActionListener(listenerSave);
		findRoomButton.addActionListener(listenerRoom);
        emailBox.addActionListener(sendMail);
		addOneMemberButton.addActionListener(addOrRemoveMemberserListener);
		addAllMembersButton.addActionListener(addOrRemoveMemberserListener);
		removeOneMemberButton.addActionListener(addOrRemoveMemberserListener);
		removeAllMembersButton.addActionListener(addOrRemoveMemberserListener);
		
		if (inApp != null) {
			setModel(inApp);
		}
		onGetAllGroups(client.getGroups());
		
		String test = "Petter";
		String []pp = test.split("-", 2);
		System.out.println("TEST" + pp[0]);
	}

	public GridBagConstraints gbc(int y, int x){
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        if(x==1 && y!=2){
            c.gridwidth = 2;
        }
        if(x==1 && y==4){
            c.gridwidth = 1;
        }
        if(y==1 && (x==4 || x==6)){
            c.gridheight = 6;
            if(y==1 && x==6){
                c.gridwidth = 2;
            }
        }
        c.insets = new Insets(5,5,5,5);
        c.gridx = x;
        c.gridy = y;
        return c;
    }

	@SuppressWarnings("deprecation")
	private void getTimestamp() {
		timeFrom = formattedStartTime.getText();
		timeTo = formattedEndTime.getText();
		String[] partsDate = date.split("-", 3);
		int day = Integer.valueOf(partsDate[0]);
		int month = Integer.valueOf(partsDate[1]);
		int year = Integer.valueOf(partsDate[2]);

		String[] partsFromTime = timeFrom.split(":", 2);					
		int hoursStart = Integer.valueOf(partsFromTime[0]);
		int minutesStart = Integer.valueOf(partsFromTime[1]);
		int seconds = 00;
		int nano = 0;

		String[] partsToTime = timeTo.split(":", 2);					
		int hoursEnd = Integer.valueOf(partsToTime[0]);
		int minutesEnd = Integer.valueOf(partsToTime[1]);
		startStamp = new Timestamp(year - 1900, month - 1, day, hoursStart, minutesStart, seconds, nano);
		stopStamp = new Timestamp(year - 1900, month - 1, day, hoursEnd, minutesEnd, seconds, nano);
	}
	private void openFindRoom() {
		appointmentDialog = new JDialog(this.parentFrame, true);
		appointmentDialog.setPreferredSize(new Dimension(500, 600));
		appointmentDialog.setTitle("Lag avtale");
		getTimestamp();
		

		fr = new FindRoom(this, client, startStamp, stopStamp);
		appointmentDialog.setContentPane(fr);
		appointmentDialog.pack();
		appointmentDialog.setLocationRelativeTo(parentFrame);
		appointmentDialog.setVisible(true);
		
	}
		
	
	protected void setRoom(Room room) {
		this.chosenRoom = room;
		roomTextField.setText(this.chosenRoom.getRoomNumber());
		placeTextField.setText(this.chosenRoom.getRoomNumber());
	}

	public void getMembers(String group) {
		Group chosenGroup;
		for (Group g: groups) {
			if(g.getName().equals(group)) {
				chosenGroup = g;
				groupMembersArray = chosenGroup.getAllUsers();
				groupMembersArray.remove(client.getCurrentUser());
			}
		}		
		addToGroupMembersModel(groupMembersArray);
		
	}
	
	private void addToGroupMembersModel(ArrayList<String> array) {
		modelGroupMembers = new DefaultListModel<String>();
		for (String s: array) {
			System.out.println("addToGroupMembersModel");
			modelGroupMembers.addElement(s);
		}
		setGroupMembersJList(modelGroupMembers);
	}
	
	private void setGroupMembersJList(DefaultListModel<String> model) {
		this.groupMembersJList.setModel(model);
	}
	//Member = user-status. user = user
	private void addToInvitedMembersModel(String member) {
		String user = divide(member);
		if(modelInvitedUsers.contains(user)) {
			System.out.println("Can't have to equal members");
		}
		else {
			if (user != client.getCurrentUser()) {
				invitedMembersArray.add(user);
				modelInvitedUsers.addElement(member);
				setInvitedMembersJList(modelInvitedUsers);
	
			}
		}
		
	}
	
	private void addAllMembers() {
//		invitedMembersArray = groupMembersArray;
		for (String s: groupMembersArray) {
			addToInvitedMembersModel(s);
		}
	}
	
	private void setInvitedMembersJList(DefaultListModel<String> model) {
		invitedMembersJList.setModel(model);
	}
	
	public void onGetAllGroups(ArrayList<Group> groups) {
		this.groups = groups;
		String name;
		for(Group g: this.groups) {
			name = g.getName();
			dropDownMenu.addItem(name);
		}
	}
	
	private void removeOneMember() {
		//s = user-status
		String s = invitedMembersJList.getSelectedValue();
		String[] parts = s.split("-", 2);
		//P = user
		String p = parts[0];
		modelInvitedUsers.removeElement(s);
		invitedMembersArray.remove(p);
		setInvitedMembersJList(modelInvitedUsers);
		
	}
	//This function return the chosen room for this appointment
	public Room getChosenRoom() {
		return this.chosenRoom;
	}
	//This function removes all members from the model for invited users.
	private void removeAllMembers() {
		modelInvitedUsers.clear();
		invitedMembersArray.clear();
		setInvitedMembersJList(modelInvitedUsers);
	}
	//This function sends the appointment to the client
	private void sendAppointment(Appointment app) {
		System.out.println("sendapp");
    	System.out.println("DETTE ER STATUS3: " + app.getParticipants().get("petterek"));
		this.client.updateAppointment(app);
				
	}
	
	public void onServerFeedback(ServerFeedback feedback) {
		if (feedback.getEvent().equals("login")) {
			if (feedback.getType() == ServerFeedback.OK) {
				System.out.println("Appointment has been made!");
			} else if (feedback.getType() == ServerFeedback.ERROR) {
				System.out.println(feedback.getMessage());
			}
		}
	}
	//This function removes the status part that comes after the username in the list.
	public String divide(String string) {
		String[] parts = string.split("-", 2);
		return parts[0]; 
	}
	//This function is called when the constructor recieves an appointment as input
	public void setModel(Appointment app) {
//		gotModel = true;
		System.out.println("SETMODEL");
		String title = app.getTitle();
		String place = app.getLocation();
		String description = app.getDescription();
		if (place == "") {
			System.out.println("Fail");
		}
		titleTextField.setText(title);
//		System.out.println("LES: " + app.getTitle() + app.getLocation() + app.getDescription());
		placeTextField.setText(place);
		titleTextField.setText(title);
		descriptionTextField.setText(description);
		Timestamp inStart = app.getStartTime();
		Timestamp inStop = app.getEndTime();
		DecimalFormat formatterInt = new DecimalFormat("00");
		int startHours = inStart.getHours();
		int startMinutes = inStart.getMinutes();
		String a = formatterInt.format(startHours);
		String b = formatterInt.format(startMinutes);
		int endHours = inStop.getHours();
		int endMinutes = inStop.getMinutes();
		String c = formatterInt.format(endHours);
		String d = formatterInt.format(endMinutes);
		formattedStartTime.setText(a+":"+b);
		formattedEndTime.setText(c+":"+d);
		
		ArrayList<String> participants = app.getParticipantList();
		participants.remove(client.getCurrentUser());
		
		ArrayList<String> participantsStatus = new ArrayList<String>();
		HashMap<String, ParticipationStatus> invitedUser = app.getParticipants();
		String string;
		for(String s: participants) {
			string = s + "-		" + invitedUser.get(s);
			participantsStatus.add(string);
			System.out.println("DIZ" + string);
		}
		
		this.id = app.getId();
		for(String s: participantsStatus) {
			addToInvitedMembersModel(s);			
		}
		@SuppressWarnings("deprecation")
		Date test = new Date(inStart.getTime());
		try {
			jdc.setDate(test);
			dateChosen = true;
			System.out.println("try etter setDate");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
