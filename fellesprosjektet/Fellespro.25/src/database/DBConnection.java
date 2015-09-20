
package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DBConnection {
	private String JDBC_DRIVER;
	private String DB_URL;
	private String USER;
	private String PW;
	private Connection conn;
	
	public DBConnection(String driver, String url, String user, String pw) {
		this.JDBC_DRIVER = driver;
		this.DB_URL = url;
		this.USER = user;
		this.PW = pw;
	}

	public void initialize() throws ClassNotFoundException, SQLException {		
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL,USER,PW);
	}
//	public void makeSingleQueryUpdate(String sql) throws SQLException {
//		Statement st = conn.createStatement();
//		st.executeUpdate(sql);
//	}
//	public ResultSet makeSingleQuery(String sql) throws SQLException {
//		Statement st = conn.createStatement();
//		System.out.println("RS");
//		return st.executeQuery(sql);
//	}
	public ResultSet makeSingleQuery(String sql, String var) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, var);
		return st.executeQuery();
	}
	
	public ResultSet makeQuery(String sql) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		return st.executeQuery();
	}
	
	public ResultSet makeSingleQuery(String sql, int var) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, var);
		return st.executeQuery();
	}
	
	public ResultSet makeSingleQuery(String sql,int var1, int var2) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, var1);
		st.setInt(2, var2);

		return st.executeQuery();
	}
	
	public int makeSingleQuery(String sql,int var2, String var1) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, var2);
		st.setString(2, var1);
		
		return st.executeUpdate();
	}
	
	public int updateAlarm(String sql,int alarm, int appointmentId, String username) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, alarm);
		st.setString(2, username);
		st.setInt(3, appointmentId);
		return st.executeUpdate();
	}
	
	public int deleteQuery(String sql,int var) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, var);
		return st.executeUpdate();
	}
	public void deleteQuery(String sql, String var) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, var);
		st.executeUpdate();
	}
	
	public ResultSet checkTimeInvterval(String sql, Timestamp time, int timebefore) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		Timestamp interval1 = time;
		

		st.setString(1, time.toString());
		interval1.setHours(interval1.getHours() +1);
		st.setString(2, interval1.toString());
		interval1.setHours(interval1.getHours() -1);
		//System.out.println(st);
		return st.executeQuery();
	}
	
	public ResultSet getNotifications(String sql,int id, String username) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, id);
		st.setString(2, username);
		return st.executeQuery();
	}
	
	public ResultSet getUnoccupiedMeetingRoom(String sql,int capacity, Timestamp start, Timestamp end) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, capacity);
		st.setString(2, start.toString());
		st.setString(3, start.toString());
		st.setString(4, end.toString());
		st.setString(5, end.toString());
		st.setInt(6, capacity);
		return st.executeQuery();
	}
	
	public ResultSet getUnoccupiedMeetingRoom(String sql,String meetingRoom, Timestamp start, Timestamp end) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, meetingRoom);
		st.setString(2, start.toString());
		st.setString(3, start.toString());
		st.setString(4, end.toString());
		st.setString(5, end.toString());
		st.setString(6, meetingRoom);
		return st.executeQuery();
	}
	
	public int createNotifactions(String sql,int appointmentId, String type, String description) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		st.setString(1, type);
		st.setString(2, description);
		st.setInt(3, appointmentId);

		st.executeUpdate();
		ResultSet rs = st.getGeneratedKeys();
	    rs.next();
	    int tall = rs.getInt(1);
		
		return tall;
	}
	
	
	public int makeUser(String sql, String username, String password, String fname, String lname, String mail, String phone ) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, username);
		st.setString(2, password);
		st.setString(3, fname);
		st.setString(4, lname);
		st.setString(5, mail);
		st.setString(6, phone);
		return st.executeUpdate();
	}
	
	public int makeParticipant(String sql, String username, int appointmentID, String status, int hidden, int alarm) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, username);
		st.setInt(2, appointmentID);
		st.setString(3, status);
		st.setInt(4, hidden);
		st.setInt(5, alarm);
		return st.executeUpdate();
	}
	
	public int updateParticipant(String sql, String username, int appointmentID, String status, int hidden, int alarm) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, status);
		st.setInt(2, hidden);
		//st.setInt(3, alarm);
		//st.setString(4, username);
		st.setString(3, username);
		//st.setInt(5, appointmentID);
		st.setInt(4, appointmentID);
		return st.executeUpdate();
	}
	
	public int updateAppointment(String sql, int id, String title, Timestamp startDate, Timestamp endDate, String description, String location, String respUser, String room) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setTimestamp(1, startDate);
		st.setTimestamp(2, endDate);
		st.setString(3, description);
		st.setString(4, location);
		st.setString(5, respUser);
		st.setString(6, room);
		st.setString(7, title);
		st.setInt(8, id);
		return st.executeUpdate();
	}
	

	
	public int makeAppointment(String sql, String title, Timestamp startDate, Timestamp endDate, String description, String location, String respUser, String room) throws SQLException {
		PreparedStatement st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		st.setTimestamp(1, startDate);
		st.setTimestamp(2, endDate);
		st.setString(3, description);
		st.setString(4, location);
		st.setString(5, respUser);
		st.setString(6, room);
		st.setString(7, title);
		
		st.executeUpdate();
		ResultSet rs = st.getGeneratedKeys();
	    rs.next();
	    int tall = rs.getInt(1);
		
		return tall;
	}
	
	
	public void close() throws SQLException {
		conn.close();
	}


}