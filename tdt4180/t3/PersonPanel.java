package t3;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PersonPanel extends JPanel implements ActionListener, ChangeListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	protected static JPanel jpanel = new JPanel();
	protected JTextField name, email, birth, genderTxt, heightTxt;
	@SuppressWarnings("rawtypes")
	protected JComboBox gender;
	protected JSlider height;
	protected JLabel nameLbl, emailLbl, birthLbl, genderLbl, heightLbl;
	private Person person;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PersonPanel() {

		person = null;
		
		add(jpanel);
		jpanel.setLayout(new GridLayout(0, 2));

		nameLbl = new JLabel("Name:");
		jpanel.add(nameLbl);
		name = new JTextField();
		name.setColumns(20);
		name.setName("NamePropertyComponent");
		name.addActionListener(this);
		jpanel.add(name);

		emailLbl = new JLabel("Email:");
		jpanel.add(emailLbl);
		email = new JTextField();
		email.setColumns(20);
		email.setName("EmailPropertyComponent");
		email.addActionListener(this);
		jpanel.add(email);

		birthLbl = new JLabel("Birthday:");
		jpanel.add(birthLbl);
		birth = new JTextField();
		birth.setColumns(20);
		birth.setName("DateOfBirthPropertyComponent");
		birth.addActionListener(this);
		jpanel.add(birth);

		genderLbl = new JLabel("Gender:");
		jpanel.add(genderLbl);
		gender = new JComboBox(Gender.values());
		gender.setName("GenderPropertyComponent");
		gender.addActionListener(this);
		jpanel.add(gender);

		heightLbl = new JLabel("Height:");
		jpanel.add(heightLbl);
		height = new JSlider(JSlider.HORIZONTAL, 120, 220, 175);
		height.setPaintLabels(true);
		height.setLabelTable(height.createStandardLabels(5));
		height.setName("HeightPropertyComponent");
		height.addChangeListener(this);
		jpanel.add(height);
		
		genderTxt = new JTextField(20);
		heightTxt = new JTextField(20);
	}

	public void setModel(Person person) {
		this.person = person;
		person.addPropChLs(this);
		name.setText(person.getName());
		email.setText(person.getEmail());
		birth.setText(person.getDateOfBirth());
		gender.setSelectedItem(person.getGender());
		height.setValue(person.getHeight());
		genderTxt.setText(person.getGender().toString());
		heightTxt.setText(Integer.toString(person.getHeight()));
		
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
			} else if (s == email) {
				person.setEmail(email.getText());
			} else if (s == birth) {
				person.setDateOfBirth(birth.getText());
			} else if (s == gender) {
				person.setGender((Gender)gender.getSelectedItem());
			}
		}
	}

	public static void main(String[] args) {
		
		Person t1 = new Person("Rune Molden");
		t1.setEmail("runemol@idi.ntnu.no");
		t1.setDateOfBirth("27.mai.1975");
		t1.setGender(Gender.male);
		t1.setHeight(175);
		
		PersonPanel panel1 = new PersonPanel();
		panel1.setModel(t1);
		PassivePersonPanel panel2 = new PassivePersonPanel(jpanel);
		panel2.setModel(t1);
		
		JFrame frame = new JFrame("PersonPanel");
		frame.getContentPane().add(panel1);
		frame.pack();
		frame.setVisible(true);
		
		JFrame frame1 = new JFrame("PassivePersonPanel");
		frame1.getContentPane().add(panel2);
		frame1.pack();
		frame1.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		Object s = arg0.getPropertyName();
		
		if (s.equals("name")) {
			name.setText(person.getName());
		} else if (s.equals("email")) {
			email.setText(person.getEmail());
		} else if (s.equals("birth")) {
			birth.setText(person.getDateOfBirth());
		} else if (s.equals("gender")) {
			gender.setSelectedItem((Gender)person.getGender());
			genderTxt.setText(person.getGender().toString());
		} else if (s.equals("height")) {
			height.setValue(person.getHeight());
			heightTxt.setText(Integer.toString(person.getHeight()));
		}
		
		System.out.println("prop change trig: " + s);
	}

}


