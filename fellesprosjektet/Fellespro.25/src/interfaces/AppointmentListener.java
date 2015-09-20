package interfaces;

import model.Appointment;

public interface AppointmentListener {

	public void onGetUserAppointments(); // Need to get appointments from the client manually
	
	public void onAddAppointment(Appointment appointment);
	
	public void onDeleteAppointment(int appointmentId);
	
	
}
