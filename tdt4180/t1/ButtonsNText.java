package t1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class ButtonsNText extends JPanel {

	private static final long serialVersionUID = 1L;
	
	JPanel jpanel;
	ButtonGroup btngrp;
	JCheckBox contbtn;
	JTextField txtfield;
	JToggleButton uCsBtn, dCsBtn;

	public ButtonsNText() {
		jpanel = new JPanel();
		add(jpanel);
		
		txtfield = new JTextField();
		txtfield.setColumns(20);
		txtfield.setName("TextLine");
		txtfield.addKeyListener(new TxtAction());
		jpanel.add(txtfield);
		
		btngrp = new ButtonGroup();
		uCsBtn = new JToggleButton("Upper case");
		dCsBtn = new JToggleButton("Lower case");
		uCsBtn.setName("UpperCaseButton");
		dCsBtn.setName("LowerCaseButton");
		
		uCsBtn.addActionListener(new UCBtnAction());
		dCsBtn.addActionListener(new DCBtnAction());
		
		btngrp.add(uCsBtn);
		btngrp.add(dCsBtn);
		
		jpanel.add(uCsBtn);
		jpanel.add(dCsBtn);
		
		contbtn = new JCheckBox("Continous?");
		contbtn.setName("ContinuousButton");
		jpanel.add(contbtn);
	}
	
	class UCBtnAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String text = txtfield.getText();
			txtfield.setText(text.toUpperCase());
			
		}
		
	}
	
	class DCBtnAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String text = txtfield.getText();
			txtfield.setText(text.toLowerCase());
		}
	}
	
	class TxtAction implements KeyListener {
		@Override
		public void keyPressed(KeyEvent arg0) {
			int c = txtfield.getCaretPosition();
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				keyMani();
			}
			txtfield.setCaretPosition(c);
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			int c = txtfield.getCaretPosition();
			if (contbtn.isSelected()) {
				keyMani();
			}
			txtfield.setCaretPosition(c);
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
		}	
	}
	
	public void keyMani() {
		String text = txtfield.getText();
		if (uCsBtn.isSelected()) {
			txtfield.setText(text.toUpperCase());
		} else {
			txtfield.setText(text.toLowerCase());
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ButtonsNText());
		frame.pack();
		frame.setVisible(true);
	}

}

/* Lexical: StringTokenizer, getKeyCode
 * Syntactical: KeyListener, ActionListener
 * Semantical: none?
 * 
 * Syntactical events are sequences of lexical events. Makes things easier.
 * 
 * 
 */
