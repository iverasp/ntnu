package client_gui;

import interfaces.RoomListener;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Room;
import client.CalClient;

public class FindRoom extends JPanel implements RoomListener {
	private JList<Room> list;
	private ArrayList<Room> arrayListRooms;
	private DefaultListModel<Room> model;
	private static final long serialVersionUID = 1L;
	private int size;
	private JButton searchButton;
	private JButton reserveRoomButton;
	private JLabel findRoomLabel;
	private JLabel infoLabel;
	private JTextField sizeTextField;
	private GridBagConstraints gbc = new GridBagConstraints();
	final CalClient client;
	private MakeAppointment ma;
    private JButton removeReservation;
	public FindRoom(MakeAppointment ma, final CalClient client, Timestamp startInput, Timestamp endInput) {
		this.ma = ma;
		this.client = client;
		client.addRoomListener(this);
		
		final Timestamp start = startInput;
		final Timestamp end = endInput;
		model = new DefaultListModel<Room>();
		
		list = new JList<Room>();
		list.setPreferredSize(new Dimension(200, 400));
		
		
		setLayout(new GridBagLayout());
		gbc.fill=GridBagConstraints.HORIZONTAL;
		
		findRoomLabel = new JLabel("Finn et rom");
		gbc.gridx=0;
		gbc.gridy=0;
		add(findRoomLabel, gbc);
		
		infoLabel = new JLabel("Hvor mange skal det v�re plass til i rommet?");
		gbc.gridx=0;
		gbc.gridy=1;
		add(infoLabel, gbc);
		
		
		sizeTextField = new JTextField(5);
		gbc.gridx=0;
		gbc.gridy=2;
		add(sizeTextField, gbc);
		
		searchButton = new CalButton("Finn rom");
		gbc.gridx=0;
		gbc.gridy=3;
		add(searchButton, gbc);
		
		reserveRoomButton = new CalButton("Reserver valgt rom");
		gbc.gridx=0;
		gbc.gridy=5;
		gbc.anchor = GridBagConstraints.NORTH;
		add(reserveRoomButton, gbc);


        gbc.gridx=0;
        gbc.gridy=4;
        add(list, gbc);

        removeReservation = new CalButton("Fjern reservasjon");
        gbc.gridx=0;
        gbc.gridy=6;
        add(removeReservation, gbc);

        removeReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Room rom = new Room("None", 0);
                setChosenRoom(rom);
            }
        });

        ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					System.out.println("listener");
					size = Integer.parseInt(sizeTextField.getText());
					client.requestAvailableRooms(size, start, end);
//					list = dbc.getUnoccupiedMeetingRoomFromDB(size, start, end);
//					ArrayList<String> availibleRooms  = new ArrayList<String>();
//					for (int i = 0; i < list.size(); i++) {						
//							System.out.println(list.get(i));
//					}
				}
				catch (NumberFormatException e){
					System.out.println("Du har ikke oppgitt en gyldig input. Bare tall er tillatt.");
					
				}
			}
			
		};
		
		ActionListener saveRoom = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Room selected =  new Room();
				selected = list.getSelectedValue();
				setChosenRoom(selected);
				System.out.println(selected);
				
			}

		
		};
		reserveRoomButton.addActionListener(saveRoom);
		searchButton.addActionListener(listener);
	}

	private void setChosenRoom(Room room) {
		ma.setRoom(room);
		//ma.appointmentDialog.dispose();
		ma.appointmentDialog.setVisible(false); //Denne fungerer paa mac
	}
	
	
	
	public void onGetAvailiableRooms(ArrayList<Room> rooms) {
		emptyAll();
		this.arrayListRooms = rooms;
		addToModel(this.arrayListRooms);
	}

	private void addToModel(ArrayList<Room> rooms) {
		for(Room r: rooms) {
			this.model.addElement(r);			
		}
		setJList(model);
	}

	private void setJList(DefaultListModel<Room> model) {
		list.setModel(model);
		
	}	
	
	private void emptyAll() {
		this.model.clear();
		setJList(model);
		
	}
}

