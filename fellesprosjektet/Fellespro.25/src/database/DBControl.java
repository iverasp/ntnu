package database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.Alarm;
import model.Appointment;
import model.User;
public class DBControl {
	//String url = "jdbc:mysql://dev.iegget.com/cal";
	String url = "jdbc:mysql://localhost/cal";
	String driver = "com.mysql.jdbc.Driver";
	//String user = "cal";
	//String pw = "lolcats2";
	String user = "CalServer";
	String pw = "securepassword";
		
	
	DBConnection dbc=new DBConnection(driver, url, user, pw);
	
	
	//getUser takes a username as parameter, and returns an arraylist containing all avalible information about this user
	public ArrayList<String> getUserFromDB(String usernameInput) {
		ArrayList<String> data = new ArrayList<String>();
		String sql="select * from user where username = ?";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql, usernameInput);
			if (rs.next()) {	
				for (int j=1; j<=6; j++) {
					data.add(rs.getString(j));
				}
				rs.close();
				dbc.close();
				return data;
			}
			else  {
				data.add("-1");
				rs.close();
				dbc.close();
				return data;
			}
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	//getNotificationFromUserNotificationFromDB
	public ArrayList<String> getNotificationFromUserNotificationFromDB(String usernameInput, int notifactionID) {
		ArrayList<String> data = new ArrayList<String>();
		String sql="select n.type, n.description, n.appointmentId from notification as n, user_notification as un where  un.notificationId = ? and un.username = ? and un.notificationId = n.id;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.getNotifications(sql, notifactionID,usernameInput);
			if (rs.next()) {	
				for (int j=1; j<=3; j++) {
					data.add(rs.getString(j));
				}
				rs.close();
				dbc.close();
				return data;
			}
			else  {
				data.add("-1");
				rs.close();
				dbc.close();
				return data;
			}
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	public int getNotificationFromDB(String type, int appointmentId) {
		String sql="select n.id from notification as n where n.appointmentId = ? and n.type = ?;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.getNotifications(sql, appointmentId,type);
			if (rs.next()) {
				int notificationId = rs.getInt(1);
				rs.close();
				dbc.close();
				return notificationId;
				}
				
			
			else  {
				rs.close();
				dbc.close();
				return -1;
			}
		}
		catch(SQLException se) {
			se.printStackTrace();
			return -1;

		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		
	}
	
	public ArrayList<String> getParticipantFromDB(String username, int participantId) {
		String sql="select * from participant as p where p.id = ? and p.username = ?;";	
		ArrayList<String> data = new ArrayList<String>();
		try {
			dbc.initialize();
			ResultSet rs=dbc.getNotifications(sql, participantId,username);
			if (rs.next()) {
				for (int j=1; j<=3; j++) {
					data.add(rs.getString(j));
				}
				rs.close();
				dbc.close();
				return data;
				}
				
			
			else  {
				rs.close();
				dbc.close();
				return null;
			}
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public ArrayList<String> getAllNotificationForUserDB(String username) {
		ArrayList<String> data = new ArrayList<String>();
		String sql="select n.type, n.description, n.appointmentId from notification as n, user_notification as un where n.id = un.notificationId and un.username = ?;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql, username);
			while (rs.next()) {	
				for (int j=1; j<=3; j++) {
					data.add(rs.getString(j));
				}

			}
			rs.close();
			dbc.close();
			return data;

		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public ArrayList<String> getAppointmentsInTimeInterval(Timestamp time, int interval){
		ArrayList<String> data = new ArrayList<String>();
		String sql="select id,starttime from appointment where starttime >= ? and starttime <= ?";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.checkTimeInvterval(sql, time,interval);
			while (rs.next()) {	
				for (int j=1; j<=2; j++) {
					data.add(rs.getString(j));
				}

			}
			rs.close();
			dbc.close();
			return data;

		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int createNotifactions(int appointmentId, String type, String description){

		String sql="INSERT INTO notification VALUES (default,?,?,?)";	
		try {
			dbc.initialize();
			int tall=dbc.createNotifactions(sql, appointmentId,type,description);
			dbc.close();
			return tall;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return -1;

		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		
		
		
	}
	
	public void createUserNotifactions(int notificationId, String username){

		String sql="INSERT INTO user_notification VALUES (?,?)";	
		try {
			dbc.initialize();
			dbc.makeSingleQuery(sql, notificationId,username);
			dbc.close();
		}
		catch(SQLException se) {
			se.printStackTrace();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public boolean deleteParticipation(int appointmentId, String username){

		String sql="DELETE FROM participant WHERE id=? and username=?;";	
		try {
			dbc.initialize();
			dbc.makeSingleQuery(sql, appointmentId,username);
			dbc.close();
			return true;
			
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}
	
	public boolean deleteNotification(int notificationId, String username){

		String sql="DELETE FROM user_notification WHERE notificationId=? and username=?;";	
		try {
			dbc.initialize();
			dbc.makeSingleQuery(sql, notificationId,username);
			dbc.close();
			return true;
			
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}
	
	public boolean deleteAppointment(int id){

		String sql="DELETE FROM appointment WHERE id=?;";	
		try {
			dbc.initialize();
			dbc.deleteQuery(sql, id);
			dbc.close();
			return true;
			
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}
	
	public ArrayList<String> getParticipantsInTimeInterval(int appointmentId, int timeLeft){
		ArrayList<String> data = new ArrayList<String>();
		String sql="select username from participant where id = ? and alarm >= ? ;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql, appointmentId,timeLeft);
			while (rs.next()) {	
				for (int j=1; j<=1; j++) {
					data.add(rs.getString(j));
				}

			}
			rs.close();
			dbc.close();
			return data;

		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getGroups(){
		ArrayList<String> data = new ArrayList<String>();
		String sql="select * from group_table;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeQuery(sql);
			while (rs.next()) {	
				for (int j=1; j<=2; j++) {
					data.add(rs.getString(j));
				}

			}
			rs.close();
			dbc.close();
			return data;

		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getAllUsersInGroup(String groupName){
		ArrayList<String> data = new ArrayList<String>();
		String sql="select username from member where groupname = ?;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql,groupName);
			while (rs.next()) {	
				for (int j=1; j<=1; j++) {
					data.add(rs.getString(j));
				}

			}
			rs.close();
			dbc.close();
			return data;

		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getAllMembers(){
		ArrayList<String> data = new ArrayList<String>();
		String sql="select * from member;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeQuery(sql);
			while (rs.next()) {	
				for (int j=1; j<=2; j++) {
					data.add(rs.getString(j));
				}

			}
			rs.close();
			dbc.close();
			return data;

		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getAppointmentFromDB(int id) {
		ArrayList<String> data = new ArrayList<String>();
		String sql="select * from appointment where id = ?";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql, id);
			if (rs.next()) {	
				for (int j=1; j<=8; j++) {
					data.add(rs.getString(j));
				}
				//System.out.println(data);
				rs.close();
				dbc.close();
				return data;
			}
			else  {
				data.add("-1");
				rs.close();
				dbc.close();
				return data;
			}
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public ArrayList<String> getParticipantsFromDB(int id) {
		ArrayList<String> data = new ArrayList<String>();
		String sql="select username,status from participant where id = ?";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql, id);
			while (rs.next()) {	
				for (int j=1; j<=2; j++) {
					data.add(rs.getString(j));
				}
				//return data;
			}
			rs.close();
			dbc.close();
			return data;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	
	public ArrayList<String> getUsersAppointmentsFromDB(String username) {
		ArrayList<String> data = new ArrayList<String>();
		String sql="select * "
				+ "from appointment as a, cal.participant as p "
				+ "where a.id = p.id and p.username = ?;";		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeSingleQuery(sql, username);
			while (rs.next()) {	
				for (int j=1; j<=8; j++) {
					data.add(rs.getString(j));
				}
				//return data;
			}
			rs.close();
			dbc.close();
			return data;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	public void updateAppointmentDBC(Appointment a){
		updateAppointmentDBC(a.getId(),a.getTitle(),a.getStartTime(),a.getEndTime(),a.getDescription(),a.getLocation(),a.getResponsibleUser(),a.getReservedRoom());
	
	}
	
	public void updateAppointmentDBC(int id, String tittel, Timestamp startDate, Timestamp endDate, String description, String location, String respUser, String room){
		String sql="UPDATE appointment SET starttime=?, endtime=?, description=?, location=?, responsible_username=?, roomnumber =?, title = ? WHERE  id=?";
		try {
			dbc.initialize();
			dbc.updateAppointment(sql, id, tittel, startDate,endDate,description,location,respUser,room);
			dbc.close();
		}
		catch(SQLException se) {
			se.printStackTrace();


		}
		catch(Exception e) {
			e.printStackTrace();

		}
	}
	
	public ArrayList<String> getAllMeetingRooms(){
		String sql= "select * from cal.meetingroom;";
		ArrayList<String> data = new ArrayList<String>();
		
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeQuery(sql);
			while (rs.next()) {	
				for (int j=1; j<=2; j++) {
					data.add(rs.getString(j));
				}
				//return data;
			}
			rs.close();
			dbc.close();
			return data;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getUnoccupiedMeetingRoomFromDB(int capacity, Timestamp start, Timestamp end) {
		ArrayList<String> data = new ArrayList<String>();
		//String sql="select m.roomnumber from appointment as a,meetingroom as m WHERE "
				//+ "((a.starttime >= ? and a.starttime >= ?) or (a.endtime <= ? and a.endtime <= ?) and m.roomnumber = a.roomnumber)  "
				//+ " or m.roomnumber not in (SELECT a.roomnumber FROM cal.appointment as a) "
				//+ " GROUP BY roomnumber;";		
		String sql="select m.roomnumber, m.capacity from cal.appointment as a, meetingroom as m WHERE a.roomnumber = m.roomnumber and  m.capacity >= ? and a.roomnumber not in (SELECT a2.roomnumber FROM cal.appointment as a2 WHERE (a2.starttime <= ? and  a2.endtime >= ? ) "
				+ "or (a2.starttime <= ? and a2.endtime >= ?) ) "
				+ "or m.roomnumber not in (SELECT a.roomnumber FROM cal.appointment as a) and m.capacity >= ? GROUP BY roomnumber;";
		
		//String sql= "SELECT a2.roomnumber FROM cal.appointment as a2 WHERE (a2.starttime <= ? and  a2.endtime >= ? ) or (a2.starttime <= ? and a2.endtime >= ?) ;";
		try {
			dbc.initialize();
			ResultSet rs=dbc.getUnoccupiedMeetingRoom(sql,capacity, start, end);
			while (rs.next()) {	
				for (int j=1; j<=1; j++) {
					data.add(rs.getString(j));
				}
				//return data;
			}
			rs.close();
			dbc.close();
			return data;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public ArrayList<String> isMeetingRoomReserved(String  meetingRoom, Timestamp start, Timestamp end) {
		ArrayList<String> data = new ArrayList<String>();
		//String sql="select m.roomnumber from appointment as a,meetingroom as m WHERE "
				//+ "((a.starttime >= ? and a.starttime >= ?) or (a.endtime <= ? and a.endtime <= ?) and m.roomnumber = a.roomnumber)  "
				//+ " or m.roomnumber not in (SELECT a.roomnumber FROM cal.appointment as a) "
				//+ " GROUP BY roomnumber;";		
		String sql="select m.roomnumber, m.capacity from cal.appointment as a, meetingroom as m WHERE a.roomnumber = m.roomnumber and  m.roomnumber = ? and a.roomnumber not in (SELECT a2.roomnumber FROM cal.appointment as a2 WHERE (a2.starttime <= ? and  a2.endtime >= ? ) "
				+ "or (a2.starttime <= ? and a2.endtime >= ?) ) "
				+ "or m.roomnumber not in (SELECT a.roomnumber FROM cal.appointment as a) and m.roomnumber = ? GROUP BY roomnumber;";
		
		//String sql= "SELECT a2.roomnumber FROM cal.appointment as a2 WHERE (a2.starttime <= ? and  a2.endtime >= ? ) or (a2.starttime <= ? and a2.endtime >= ?) ;";
		try {
			dbc.initialize();
			ResultSet rs=dbc.getUnoccupiedMeetingRoom(sql,meetingRoom, start, end);
			if(rs.next()) {	
				for (int j=1; j<=1; j++) {
					data.add(rs.getString(j));
				}
				return data;
			}
			rs.close();
			dbc.close();
			return null;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public boolean addUserDBC(User user){
		
		return addUserDBC(user.getUsername(),user.getPassword(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getPhone());
	}
	
	public boolean addUserDBC(String username, String password, String fname, String lname, String mail, String phone){
		String sql="INSERT INTO user VALUES (?,?,?,?,?,?)";
		try {
			dbc.initialize();
			int tall = dbc.makeUser(sql, username,password,fname,lname,mail,phone);
			dbc.close();
			return true;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	public boolean updateUserDBC(User user){
		
		return updateUserDBC(user.getUsername(),user.getPassword(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getPhone());
	}
	
	public boolean updateUserDBC(String username, String password, String fname, String lname, String mail, String phone){
		String sql="UPDATE user SET ";
		System.out.println("Not supported yet. Try again later");
		//UPDATE participant SET status=?, hsidden=?, alarm=? WHERE username=? AND id=?
		try {
			dbc.initialize();
			int tall = dbc.makeUser(sql, username,password,fname,lname,mail,phone);
			dbc.close();
			return true;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int addAppointmentDBC(Appointment a){
		return addAppointmentDBC(a.getTitle(), a.getStartTime(),a.getEndTime(),a.getDescription(),a.getLocation(),a.getResponsibleUser(),a.getReservedRoom());
	
	}
	

	
	
	public int addAppointmentDBC(String tittel, Timestamp startDate, Timestamp endDate, String description, String location, String respUser, String room){
		String sql="INSERT INTO appointment VALUES (default,?,?,?,?,?,?,?)";
		try {
			dbc.initialize();
			int tall = dbc.makeAppointment(sql, tittel, startDate,endDate,description,location,respUser,room);
			//System.out.println(tall);
			dbc.close();
			return tall;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return -1;

		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	

	
	public boolean addParticipantDBC(String username, int appointmentID, String status,int hidden, int alarm){
		String sql="INSERT INTO participant VALUES (?,?,?,?,?)";
		try {
			dbc.initialize();
			dbc.makeParticipant(sql, username,appointmentID,status,hidden,alarm);
			dbc.close();
			return true;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateParticipation(String username, int appointmentID, String status,int hidden, int alarm){
		//String sql="UPDATE participant SET status=?, hsidden=?, alarm=? WHERE username=? AND id=?";
		String sql="UPDATE participant SET status=?, hsidden=? WHERE username=? AND id=?";
		try {
			dbc.initialize();
			dbc.updateParticipant(sql, username,appointmentID,status,hidden,alarm);
			dbc.close();
			return true;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;

		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public ArrayList<String> getAllUsers() {
		ArrayList<String> data = new ArrayList<String>();
		String sql = "select * from user;";
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeQuery(sql);
			while (rs.next()) {	
				for (int j=1; j<=6; j++) {
					data.add(rs.getString(j));
				}
				
			}
			rs.close();
			dbc.close();
			return data;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		

	}
	
	public boolean loginDBC(String username, String passwordInput) {
		ArrayList<String> list = new ArrayList<String>();
		list = getUser(username);
		if (list == null) {
			System.out.println("User doesn't exist");
			return false;
		}
		else {
			String passwordDatabase = list.get(1);
			if (passwordDatabase.equals(passwordInput)) {
				System.out.println("Success!");
				return true;
			}
			else {
				System.out.println("Wrong password for this user!");
				return false;
			}
		}
	}
	
	
	
	public ArrayList<String> getUser(String username) {
		ArrayList<String> list = new ArrayList<String>();
		list = getUserFromDB(username);
		if (list.get(0) == "-1") {
			//System.out.println("User doesn't exist");
			return null;
		}
		else {
			//System.out.println("User found");
			return list;
		}

	}
	
	public void deleteUserNotifications(String username) {
		String sql = "delete from user_notification where username=?;";
		try {
			dbc.initialize();
			dbc.deleteQuery(sql, username);
			dbc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Alarm> getTriggeredAlarms() {
		ArrayList<Alarm> data = new ArrayList<Alarm>();
//		String sql = "select p.username, a.id" +
//				"from appointment as a inner join participant as p on a.id=p.id " +
//				"where a.starttime > now() " +
//				"and timestampdiff(MINUTE, now(), a.starttime) < p.alarm " +
//				"and p.alarm != 0;";
		String sql = "select p.username, a.id from appointment as a inner join participant as p on a.id=p.id where a.starttime > now() and timestampdiff(MINUTE, now(), a.starttime) < p.alarm and p.alarm != 0;";
		try {
			dbc.initialize();
			ResultSet rs=dbc.makeQuery(sql);
			while (rs.next()) {	
				Alarm alarm = new Alarm(rs.getString(1), rs.getInt(2));
				data.add(alarm);
			}
			rs.close();
			dbc.close();
			return data;
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setAlarm(int alarm, String username, int appointmentId) {
		String sql = "update participant set alarm=? where username=? and id=?;";
		try {
			dbc.initialize();
			dbc.updateAlarm(sql, alarm, appointmentId, username);
			dbc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void insertData() {
//		Scanner reader = new Scanner(System.in);
//		System.out.println("Enter the first number");
//		//get user input for a
//		String a=reader.nextLine();
//		String sql = "INSERT INTO user (username, password, first_name, last_name, mail, phone)" + "VALUES('WEFwef', 'hue', 'Torstein', 'Granskogen', 'this@mail.be', 12345678) ";
//		System.out.println("Trying to connect to databse...");
//		try {
//			dbc.initialize();
//			System.out.println("Success!");
//			dbc.makeSingleQueryUpdate(a);
//			System.out.println("insertion complete");
//		}
//		catch(SQLException se) {
//			se.printStackTrace();
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public void readData() throws ClassNotFoundException, SQLException 	{
//		
//		String sql="select username, first_name,last_name from user";		
//		dbc.initialize();
//		ResultSet rs=dbc.makeSingleQuery(sql);
//		ResultSetMetaData meta=rs.getMetaData();
//		rs.beforeFirst();
//		System.out.print("Col1:"+meta.getColumnName(1));
//		System.out.print(" Col2:"+meta.getColumnName(2));
//		System.out.print(" Col3:"+meta.getColumnName(3));
//		System.out.println();
//		while(rs.next())
//		{
//			String username=rs.getString(1);
//			String first_name=rs.getString(2);
//			String last_name=rs.getString(3);
//			System.out.println(String.format("%s %s %s\n",username,first_name,last_name));
//		}
//		rs.close();
//		dbc.close();
//		
//	}
	public static void main(String [] args) {
		DBControl t = new DBControl();
		ArrayList<String> list = new ArrayList<String>();
//		list = t.getUser("petterek");
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}

	}

}
