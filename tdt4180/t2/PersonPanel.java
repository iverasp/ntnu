package t2;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PersonPanel extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;

	private JPanel jpanel;
	private JTextField name, email, birth;
	@SuppressWarnings("rawtypes")
	private JComboBox gender;
	private JSlider height;
	private Person person;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PersonPanel() {

		person = null;
		
		jpanel = new JPanel();
		add(jpanel);
		jpanel.setLayout(new GridLayout(0, 2));

		JLabel nameLbl = new JLabel("Name:");
		jpanel.add(nameLbl);
		name = new JTextField();
		name.setColumns(20);
		name.setName("NamePropertyComponent");
		name.addActionListener(this);
		jpanel.add(name);

		JLabel emailLbl = new JLabel("Email:");
		jpanel.add(emailLbl);
		email = new JTextField();
		email.setColumns(20);
		email.setName("EmailPropertyComponent");
		email.addActionListener(this);
		jpanel.add(email);

		JLabel birthLbl = new JLabel("Birthday:");
		jpanel.add(birthLbl);
		birth = new JTextField();
		birth.setColumns(20);
		birth.setName("DateOfBirthPropertyComponent");
		birth.addActionListener(this);
		jpanel.add(birth);

		JLabel genderLbl = new JLabel("Gender:");
		jpanel.add(genderLbl);
		gender = new JComboBox(Gender.values());
		gender.setName("GenderPropertyComponent");
		gender.addActionListener(this);
		jpanel.add(gender);

		JLabel heightLbl = new JLabel("Height:");
		jpanel.add(heightLbl);
		height = new JSlider(JSlider.HORIZONTAL, 120, 220, 175);
		height.setPaintLabels(true);
		height.setLabelTable(height.createStandardLabels(5));
		height.setName("HeightPropertyComponent");
		height.addChangeListener(this);
		jpanel.add(height);

	}

	public void setModel(Person person) {
		this.person = person;
		name.setText(person.getName());
		email.setText(person.getEmail());
		birth.setText(person.getDateOfBirth());
		gender.setSelectedItem(person.getGender());
		height.setValue(person.getHeight());
	}

	public Person getModel() {
		return person;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (person != null) {
			person.setHeight(height.getValue());
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object s = arg0.getSource();

		if (person != null) {
			if (s == name) {
				person.setName(name.getText());
				System.out.println(person.getName());
			} else if (s == email) {
				person.setEmail(email.getText());
			} else if (s == birth) {
				person.setDateOfBirth(birth.getText());
			} else if (s == gender) {
				person.setGender((Gender)gender.getSelectedItem());
			}
		}
		System.out.println(s);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new PersonPanel());
		frame.pack();
		frame.setVisible(true);
	}

}


