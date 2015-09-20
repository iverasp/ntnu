package client_gui;

import interfaces.GroupListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import model.Group;
import client.CalClient;

public class ShowUsers extends JPanel implements GroupListener{
	
	private JList usersList;
	private DefaultListModel<String> model;
	private JButton chooseUserButton;
    private JButton removeUserButton;
	private CalClient client;
	private JComboBox dropDownMenu;
	private ArrayList<Group> groups;
	private ArrayList<String> groupMembersArray;
    private JScrollPane userListScroller;
    private JScrollBar userListBar;
    private BasicScrollBarUI userScrollBarUI;;
    private JLabel name;

    public ShowUsers(CalClient inputClient) {
		this.client = inputClient;
		client.addGroupListener(this);
		usersList = new JList();
        setLayout(new GridBagLayout());

        name = new JLabel("Legg til ukesvisning");
        add(name, gbc(0,0));

        userListScroller = new JScrollPane(usersList);
        userListScroller.setPreferredSize(new Dimension(150, 200));
        userListBar = userListScroller.getVerticalScrollBar();
        userListBar.setPreferredSize(new Dimension(12, 0));
        userScrollBarUI = new CalScrollBar();
        userListBar.setUI(userScrollBarUI);
        add(userListScroller, gbc(2, 0));

		
		chooseUserButton = new CalButton("Legg til");
		add(chooseUserButton, gbc(3,0));

        removeUserButton = new CalButton("Bare meg");
        add(removeUserButton, gbc(3, 1));
		
		dropDownMenu = new JComboBox();
        dropDownMenu.setPreferredSize(new Dimension(150, 20));
		add(dropDownMenu, gbc(1, 0));
		dropDownMenu.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
//				model = new DefaultListModel<String>();
//				usersList.setModel(model);
				if(event.getStateChange() == ItemEvent.SELECTED) {
					addToModel(dropDownMenu.getSelectedItem().toString());
                }
				
			}

		
			
		});
	
	    chooseUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(usersList.getSelectedValue()!=null){
                    client.showOtherUserAppointments(usersList.getSelectedValue().toString());
                }

            }
        });
        removeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.showOnlyMyAppointments();
            }
        });
        onGetAllGroups(client.getGroups());
	}

    public GridBagConstraints gbc(int y, int x){
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        if(y!=3){
            c.gridwidth = 2;
        }
        c.gridx = x;
        c.gridy = y;
        return c;
    }

	@Override
	public void onGetAllGroups(ArrayList<Group> groups) {
		String name;
		this.groups = groups;
		for(Group g: this.groups) {
			name = g.getName();
			dropDownMenu.addItem(name);
		}
	}	
	private void addToModel(String group) {
		Group chosenGroup;
		
		for (Group g: groups) {
			if(g.getName().equals(group)) {
				chosenGroup = g;
				groupMembersArray = chosenGroup.getAllUsers();
			}
		}		
		addToGroupMembersModel(groupMembersArray);
	}
	private void addToGroupMembersModel(ArrayList<String> group) {
		model = new DefaultListModel<String>();
		for (String s: group) {
//			System.out.println("addToGroupMembersModel");
			model.addElement(s);
		}
		setGroupMembersJList(model);
		
	}
	private void setGroupMembersJList(DefaultListModel<String> model) {
		this.usersList.setModel(model);
	}
}
