package client_gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class CalButton extends JButton implements MouseListener{
	
	private boolean mousePressed = false;
	
	CalButton(String text){
		super(text);
		setBackground(Color.DARK_GRAY);
		setForeground(Color.WHITE);
		setFocusable(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));	
		addMouseListener(this);
	}
	
	 public void paint(Graphics g) {
		   this.setContentAreaFilled(false);
		   this.setBorderPainted(false);

		   Graphics2D g2d = (Graphics2D)g;
		      
		   g2d.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		   g2d.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		    

		   if(mousePressed){
			   g2d.setColor(Color.WHITE);
			   g2d.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
			   g2d.setColor(Color.DARK_GRAY);
			   g2d.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
		   }
		   else{
			   g2d.setColor(Color.DARK_GRAY);
			   g2d.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
			   g2d.setColor(Color.WHITE);
		   }

		   FontRenderContext frc = new FontRenderContext(null, false, false);
		   Rectangle2D r = getFont().getStringBounds(getText(), frc);

		   float xMargin = (float)(getWidth()-r.getWidth())/2;
		   float yMargin = (float)(getHeight()-getFont().getSize())/2;

		   g2d.drawString(getText(),xMargin,
		     (float)getFont().getSize()+yMargin);
		 }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		mousePressed = true;
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		mousePressed = false;
		repaint();
	}
}
	

