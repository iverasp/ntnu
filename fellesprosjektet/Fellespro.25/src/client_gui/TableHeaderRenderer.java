package client_gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableHeaderRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	String[] norDays = {"", "MAN", "TIR", "ONS", "TOR", "FRE", "LØR", "SØN"};
	
	Date date;
	Calendar cal;
	int currentWeek;
	
	public TableHeaderRenderer(Calendar cal) {
		setHorizontalAlignment(CENTER);
		setHorizontalTextPosition(LEFT);
		setVerticalAlignment(BOTTOM);
		setOpaque(false);
		this.cal = cal;
		
		/* Because SWING */
        cal.add(Calendar.DATE, 2-cal.get(Calendar.DAY_OF_WEEK));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		/* Fix table stuff */
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        /* Get dates */
        Calendar cal = (Calendar)this.cal.clone();
        currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        date = cal.getTime();
        
        /* Make label */
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	    if (col != 0) {
	    	cal.add(Calendar.DAY_OF_YEAR, col - 1);
	    	l.setText("<html><center><sup>" + norDays[col] + "</sup><br><sub>" + (cal.get(Calendar.DATE)) + "</center></html>");
	    	l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
	    	cal.add(Calendar.DAY_OF_YEAR,  1 - col);
	    } else {
	    	l.setText("");
	    }
            
	    /* Red highlighting of weekend */
	    if (col == 6 || col == 7) {
	    	l.setForeground(Color.decode("#e06666"));
	    } else {
	    	/* weird bug where all text turns red. below line needed for fix */
	    	l.setForeground(Color.decode("#999999"));
	    }
	    
	    return l;
	}

}
