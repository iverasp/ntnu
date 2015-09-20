package client_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalScrollBarUI;

public class CalScrollBar extends MetalScrollBarUI{
	@Override
	protected JButton createDecreaseButton(int orientation) {
	    return createZeroButton();
	}
	@Override
	protected JButton createIncreaseButton(int orientation) {
	    return createZeroButton();
	}
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.drawLine(0, 0, 0, trackBounds.height - 1);
        g.drawLine(trackBounds.width - 2, 0, trackBounds.width - 2,
          trackBounds.height - 1);
        g.drawLine(1, trackBounds.height - 1, trackBounds.width - 1,
          trackBounds.height - 1);
        g.drawLine(1, 0, trackBounds.width - 1, 0);
        g.setColor(Color.decode("#EEEEEE"));
        g.drawLine(trackBounds.width - 1, 0, trackBounds.width - 1,
          trackBounds.height - 1);
	}
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		int w = thumbBounds.width;
        int h = thumbBounds.height;
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(Color.decode("#EEEEEE"));
        g2d.drawLine(0, 0, 0, h - 2); // Left
        g2d.drawLine(0, 0, w - 2, 0); // Top
        g.setColor(Color.decode("#EEEEEE"));
        g2d.drawLine(w - 1, 0, w - 1, h - 1); // Right
        g2d.drawLine(0, h - 1, w - 1, h - 1); // Bottom
//        g2d.setColor(Color.decode("#EEEEEE"));
        g2d.drawLine(w - 2, 1, w - 2, h - 2); // Right
        g2d.drawLine(1, h - 2, w - 2, h - 2); // Bottom

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRoundRect(1, 1, w - 3, h - 3, 20, 20);
        g2d.translate(-thumbBounds.x, -thumbBounds.y);
	}
	
	protected JButton createZeroButton() {
	    JButton button = new JButton("zero button");
	    Dimension zeroDim = new Dimension(0,0);
	    button.setPreferredSize(zeroDim);
	    button.setMinimumSize(zeroDim);
	    button.setMaximumSize(zeroDim);
	    return button;
	}
    
}
