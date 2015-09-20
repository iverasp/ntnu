package client_gui;

import javax.swing.table.AbstractTableModel;

public class CalendarModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	int count = 0;

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 7 + 1;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 24;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub

		if (col == 0) {
			return ("<html><sup>" + (row + 0) + "</sup><br><br><sub>" + (row + 1) + "</sub></html>");
		}


		return "";
	}

	public boolean getOddEven(int row) {
		//if (row % 8 == 0 || row % 8 == 2 || row % 8 == 3 || row % 8 == 1) return true;
		if (row % 2 == 0) return true;
		return false;
	}

}