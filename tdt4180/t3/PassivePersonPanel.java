package t3;

import javax.swing.JPanel;

public class PassivePersonPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;
	
	public PassivePersonPanel(JPanel jpanel) {
		super();
		
		name.setEditable(false);
		email.setEditable(false);
		birth.setEditable(false);
		genderTxt.setEditable(false);
		heightTxt.setEditable(false);
		
		jpanel.remove(gender);
		jpanel.remove(heightLbl);
		jpanel.remove(height);
		jpanel.add(genderTxt);
		jpanel.add(heightLbl);
		jpanel.add(heightTxt);
		
	}

}
