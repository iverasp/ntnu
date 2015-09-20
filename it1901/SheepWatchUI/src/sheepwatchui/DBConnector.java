package sheepwatchui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.*;
import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 *  Class handling all communication with the database.
 */
public class DBConnector {
    private static String url = "jdbc:mysql://mysql.stud.ntnu.no/kristoau_sheepwatch";
    private static String user = "kristoau_sheep";
    private static String password = "gruppe1";
    private static Connection connection;
    
    /**
     * A method taking a query for retreiving data from the database.
     * @param query
     * @return The ResultSet for the query. 
     * null on @SQLException or @CLassNotFoundException
     */
    public static ResultSet insertQuery(final String query){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            return connection.createStatement().executeQuery(query);
        } catch (ClassNotFoundException|SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * A method taking a statement for inserting, updating and deleting of data
     * from the database.
     * @param statement
     * @return The number of rows affected. -1 on @SQLException.
     */
    public static int insertStatement(final String statement){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection  = DriverManager.getConnection(url, user, password);
            return connection.createStatement().executeUpdate(statement);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Adding the given {@link Farmer} in the farmer table in the database.
     * Also adds the {@link Farmer}'s {@link EmergencyContact} in the emergencycontact table.
     * @param f The farmer to be added in the database.
     */
    public static void addFarmerAndEC(Farmer f){
        // Creating a string statement for adding a farmer in the database
        String createFarmer = 
                "INSERT INTO farmer (f_email, f_tlf, f_name, f_address, "
                + "f_postnr, f_city, f_lat, f_lng, f_pw, ec_email) VALUES ('"
                + f.getEmail() + "', '"
                + f.getPhone() + "', '"
                + f.getName() + "', '"
                + f.getAddress() + "', '"
                + f.getPostnr() + "', '"
                + f.getCity() + "', "
                + f.getFarmLocation().getLatitude()+", "
                + f.getFarmLocation().getLongitude()+", '"
                + f.getPassword() + "', '"
                + f.getEmergencyContact().getEmail()
                + "');";
        
        
        // Checking if the emergencycontact already exists in the database
        boolean ecExists;
        String checkEc = "SELECT * FROM emergencycontact WHERE "
                + "ec_email='" + f.getEmergencyContact().getEmail()+"';";
        ResultSet rs = insertQuery(checkEc);
        try{
            ecExists = rs.next();
        }catch(SQLException e){
            ecExists = false;
        }
        
        if(!ecExists){
            // Creating a string statement for adding the farmer's emergency contact
            // if the emergencycontact does not exist in the table.
            EmergencyContact ec = f.getEmergencyContact();
            String createEc = 
                    "INSERT INTO emergencycontact (ec_email, ec_tlf, ec_name) "
                    + "VALUES ('"
                    + ec.getEmail() + "', '"
                    + ec.getPhone() + "', '"
                    + ec.getName()
                    + "');";
            insertStatement(createEc);
        }
        insertStatement(createFarmer);
    }
    /**
     * Edits the fields of the given {@link Farmer} along with his 
     * {@link EmergencyContact}.
     * @param f The {@link Farmer} to be edited in the database.
     */
    public static void editFarmerAndEC(Farmer f, Farmer oldFarmer){
        // Creating a string statement for editing a farmer in the database.
        String editFarmer =
                "UPDATE farmer SET f_email='"+f.getEmail()+
                "', f_tlf='"+f.getPhone()+
                "', f_name='"+f.getName()+
                "', f_address='"+f.getAddress()+
                "', f_postnr='"+f.getPostnr()+
                "', f_city='"+f.getCity()+
                "', f_lat="+f.getFarmLocation().getLatitude()+
                ", f_lng="+f.getFarmLocation().getLongitude()+
                ", f_pw='"+f.getPassword()+
                "' WHERE f_email='"+oldFarmer.getEmail()+"';";
        
        boolean ecExists = false;
        if(!oldFarmer.getEmergencyContact().getEmail().equals(
                f.getEmergencyContact().getEmail())){
            // Checking if the emergencycontact already exists in the database
            // only if the email is new.
            String checkEc = "SELECT * FROM emergencycontact WHERE "
                    + "ec_email='" + f.getEmergencyContact().getEmail()+"';";
            ResultSet rs = insertQuery(checkEc);
            try{
                rs.next();
                ecExists = true;
            }catch(SQLException e){
                // Continue...
            }
        }
        if(!ecExists){
            // Creating a string statement for editing an emergencycontact 
            // in the database if the new email does not exist in the table
            EmergencyContact ec = f.getEmergencyContact();
            String editEc = 
                    "UPDATE emergencycontact SET ec_email='"+ec.getEmail()+
                    "', ec_tlf='"+ec.getPhone()+
                    "', ec_name='"+ec.getName()+
                    "' WHERE ec_email='"+oldFarmer.getEmergencyContact().getEmail()+"';";
            insertStatement(editEc);
        }else{
            // If the new email is already in the database, 
            // use just switch the farmer's foreign key "farmer.ec_email"
            String updateFarmerEcEmail = "UPDATE farmer SET "
                    + "ec_email='"+f.getEmergencyContact().getEmail()+"' "
                    + "WHERE f_email='" + f.getEmail()+"';";
            insertStatement(updateFarmerEcEmail);
        }
        insertStatement(editFarmer);
    }
    
    /**
     * Adding the given {@link Sheep} to the sheep table in the database.
     * @param s The {@link Sheep} to be added to the database.
     */
    public static void addSheep(Sheep s){
        
        // Creating a string statement for adding a sheep in the database.
        String createSheep = 
                "INSERT INTO sheep (s_weight, s_name, s_bday, s_gender, "
                + "s_health, f_email) VALUES ('"
                + s.getWeight() + "', '"
                + s.getName()+ "', '"
                + s.getDateOfBirth()+ "', '"
                + s.getGender() + "', '"
                + s.getHealth() +"', '"
                + s.getFarmer().getEmail()
                + "');";
        insertStatement(createSheep);
    }
   
    /**
     * Edits the fields of the given {@link Sheep}.
     * @param s  The {@link Sheep} to be edited in the database.
     */
    public static void editSheep(Sheep s){
        // Creating a string statement for editing a sheep in the database.
        String dDay;
        if(s.getDateOfDeath() == null){
            dDay = "NULL";
        }else{
            dDay = "'"+s.getDateOfDeath()+"'";
        }
        String editSheep;
        if(s.getLocation() != null){
            editSheep = 
                    "UPDATE sheep SET s_weight="+s.getWeight()+
                    ", s_name='"+s.getName()+
                    "', s_bday='"+s.getDateOfBirth()+
                    "', s_dday="+dDay+
                    ", s_gender='"+s.getGender()+
                    "', s_health='"+s.getHealth()+
                    "', s_lat="+s.getLocation().getLatitude()+
                    ", s_lng="+s.getLocation().getLongitude()+
                    " WHERE s_id="+s.getId()+";";
        }else{
            editSheep = 
                    "UPDATE sheep SET s_weight="+s.getWeight()+
                    ", s_name='"+s.getName()+
                    "', s_bday='"+s.getDateOfBirth()+
                    "', s_dday="+dDay+
                    ", s_gender='"+s.getGender()+
                    "', s_health='"+s.getHealth()+
                    "' WHERE s_id="+s.getId()+";";
        }
        insertStatement(editSheep);
    }
    /**
     * Deletes the given {@link Sheep} from the database.
     * @param s the {@link Sheep} to be deleted
     */
    public static void deleteSheep(Sheep s){
        String deleteSheep = "DELETE FROM sheep WHERE s_id="+s.getId()+";";
        insertStatement(deleteSheep);
    }
    
    /**
     * Getting all the {@link Sheep} owned by a given {@link Farmer}.
     * @param f The {@link Farmer} owning the {@link Sheep}.
     * @return @ArrayList<Sheep> with {@link Sheep} objects.
     */
    public static ArrayList<Sheep> getSheepList(Farmer f){
        ArrayList<Sheep> alSheep = new ArrayList<Sheep>();
        String sheepQuery = "SELECT * FROM sheep WHERE f_email='"+f.getEmail()+"';";
        ResultSet rs = insertQuery(sheepQuery);
        
        try{
            while(rs.next()){
                GeoPosition loc = new GeoPosition(rs.getFloat("s_lat"), rs.getFloat("s_lng"));
                String dday = rs.getString("s_dday");
                if(rs.wasNull()){
                    dday = null;
                }else{
                    dday = rs.getString("s_dday");
                }
                
                Sheep sheep = new Sheep(rs.getInt("s_id"), 
                        f, 
                        rs.getString("s_name"), 
                        rs.getString("s_bday"), 
                        loc, 
                        dday, 
                        rs.getString("s_gender").charAt(0), 
                        rs.getString("s_health"), 
                        rs.getDouble("s_weight"));
                alSheep.add(sheep);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return alSheep;
    }
    
    /**
     * Getting all the living {@link Sheep} in the sheep table.
     * @return @ArrayList<Sheep> with {@link Sheep} objects.
     */
    public static ArrayList<Sheep> getAllLivingSheepList(){
        ArrayList<Sheep> alSheep = new ArrayList<Sheep>();
        String sheepQuery = "SELECT * FROM sheep, farmer, emergencycontact WHERE s_dday IS NULL"
                + " AND farmer.f_email = sheep.f_email"
                + " AND farmer.ec_email = emergencycontact.ec_email;";
        ResultSet rs = insertQuery(sheepQuery);
        
        try{
            while(rs.next()){
                EmergencyContact ec = new EmergencyContact(rs.getString("ec_name"), 
                        rs.getString("ec_email"), rs.getString("ec_tlf"));
                
                // Coordinates of the farmers farm
                GeoPosition farmPos = new GeoPosition(rs.getFloat("f_lat"), rs.getFloat("f_lng"));
                
                Farmer f = new Farmer(rs.getString("f_name"),
                        rs.getString("f_email"), rs.getString("f_tlf"), 
                        ec, rs.getString("f_address"), 
                        rs.getString("f_postnr"), rs.getString("f_city"), farmPos);
                
                GeoPosition loc = new GeoPosition(rs.getFloat("s_lat"), rs.getFloat("s_lng"));
                String dday = rs.getString("s_dday");
                if(rs.wasNull()){
                    dday = null;
                }else{
                    dday = rs.getString("s_dday");
                }
                
                Sheep sheep = new Sheep(rs.getInt("s_id"), 
                        f, 
                        rs.getString("s_name"), 
                        rs.getString("s_bday"), 
                        loc, 
                        dday, 
                        rs.getString("s_gender").charAt(0), 
                        rs.getString("s_health"), 
                        rs.getDouble("s_weight"));
                alSheep.add(sheep);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return alSheep;
    }
    /**
     * Checks if the email and password matches a registered {@link Farmer}.
     * If it does, the {@link Farmer} object is returned.
     * If it does not, @null is returned.
     * @param mail
     * @param password
     * @return {@link Farmer} if login is correct, @null if it is wrong.
     */
    public static Farmer checkUser(String mail, String password) {
        ResultSet rs = insertQuery("SELECT f_email, f_pw, f_name, f_tlf,"
                + "f_address, f_postnr, f_city, f_lat, f_lng, ec.ec_email, ec.ec_tlf, ec.ec_name "
                + "FROM farmer AS f, emergencycontact AS ec WHERE "
                + "f.ec_email=ec.ec_email AND "
                + "f_email='" + mail + "' AND f_pw='" + password + "';");
        try {
            rs.next();
            // Getting EmergencyContact info from resultset.
            EmergencyContact ec = new EmergencyContact(rs.getString("ec.ec_name"), 
                    rs.getString("ec.ec_email"), rs.getString("ec.ec_tlf"));
            // Coordinates of the farmers farm
            GeoPosition farmPos = new GeoPosition(rs.getFloat("f_lat"), rs.getFloat("f_lng"));
            // getting Farmer info from the resultset.
            Farmer farmer = new Farmer(rs.getString("f_name"), rs.getString("f_email")
                    , rs.getString("f_tlf"), ec, rs.getString("f_address"), 
                    rs.getString("f_postnr"), rs.getString("f_city"), farmPos, rs.getString("f_pw"));
            
            return farmer;
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * Adding a {@link Sheep} 's {@link Report} to the database.
     * @param r the {@link Report} to be added
     */
    public static void addReport(Report r){
        // Creating a string statement for adding a sheep in the database.
        String addReport = 
                "INSERT INTO report (r_lat, r_lng, r_puls, s_id "
                + ") VALUES ("
                + r.getSheepLocation().getLatitude() + ", "
                + r.getSheepLocation().getLongitude()+ ", "
                + r.getHeartRate() + ", "
                + r.getSheep().getId()
                + ");";
        insertStatement(addReport);
    }
    
    /**
     * Getting the list of {@link Report} from the {@link Sheep} belonging to the given {@link Farmer} 
     * The list is ordered from newest to oldest reports.
     * @param f the {@link Farmer} owning the sheep reporting
     * @return list of all reports from the {@link Farmer} {@link Sheep}
     */
    public static ArrayList<Report> getReportList(Farmer f, Sheep sheep){
        ArrayList<Report> alReport = new ArrayList<Report>();
        // Creating a string statement for getting the report list of the farmer
        String getReportList = "";
        
        // If no sheep is selected, get all the farmer's sheep.
        if(sheep == null){
            getReportList = "SELECT r_id, r_lat, r_lng, r_puls, r_time, "
                    + "sheep.s_id, s_name, s_weight, s_bday, s_dday, s_gender, "
                    + "s_lat, s_lng, s_health "
                    + "FROM report, sheep, farmer WHERE "
                    + "farmer.f_email = sheep.f_email AND "
                    + "sheep.s_id = report.s_id AND "
                    + "farmer.f_email = '" + f.getEmail() 
                    + "' ORDER BY r_id DESC;";
        }
        // If a sheep is selected, only get this sheep's reports.
        else{
            getReportList = "SELECT r_id, r_lat, r_lng, r_puls, r_time, "
                    + "sheep.s_id, s_name, s_weight, s_bday, s_dday, s_gender, "
                    + "s_lat, s_lng, s_health "
                    + "FROM report, sheep, farmer WHERE "
                    + "farmer.f_email = sheep.f_email AND "
                    + "sheep.s_id = report.s_id AND "
                    + "farmer.f_email = '" + f.getEmail() 
                    + "' AND sheep.s_id=" + sheep.getId()
                    + " ORDER BY r_id DESC;";
        }
        ResultSet rs = insertQuery(getReportList);
        try{
            while(rs.next()){
                GeoPosition loc = new GeoPosition(rs.getFloat("r_lat"), rs.getFloat("r_lng"));
                String dday = rs.getString("s_dday");
                if(rs.wasNull()){
                    dday = null;
                }else{
                    dday = rs.getString("s_dday");
                }

                Sheep s = new Sheep(rs.getInt("s_id"), 
                        f, 
                        rs.getString("s_name"), 
                        rs.getString("s_bday"), 
                        loc, 
                        dday, 
                        rs.getString("s_gender").charAt(0), 
                        rs.getString("s_health"), 
                        rs.getDouble("s_weight"));
                    
                Report r = new Report(s, loc, rs.getInt("r_puls"), rs.getTimestamp("r_time"));
                alReport.add(r);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return alReport;
    }
    
    /**
     * Getting only the newest report from each distinct {@link Sheep}.
     * @param f the {@link Farmer} owning the {@link Sheep}
     * @param sheep the {@link Sheep} to filtrate on, null if all are to be shown.
     * @return A list of the newest {@link Report}s.
     */
    public static ArrayList<Report> getNewestReports(Farmer f, Sheep sheep){
        ArrayList<Report> alReport = new ArrayList<Report>();
        // Creating a string statement for getting the report list of the farmer
        String getNewestReports = "";
        
        // If no sheep is selected, get all the farmer's sheep.
        if(sheep == null){
            getNewestReports = "SELECT r_id, r_lat, r_lng, r_puls, r_time, "
                    + "sheep.s_id, s_name, s_weight, s_bday, s_dday, s_gender, "
                    + "s_lat, s_lng, s_health "
                    + "FROM report, sheep, farmer WHERE "
                    + "farmer.f_email = sheep.f_email AND "
                    + "sheep.s_id = report.s_id AND "
                    + "farmer.f_email = '" + f.getEmail() 
                    + "' ORDER BY r_id DESC";
        }
        // If a sheep is selected, only get this sheep's reports.
        else{
            getNewestReports = "SELECT r_id, r_lat, r_lng, r_puls, r_time, "
                    + "sheep.s_id, s_name, s_weight, s_bday, s_dday, s_gender, "
                    + "s_lat, s_lng, s_health "
                    + "FROM report, sheep, farmer WHERE "
                    + "farmer.f_email = sheep.f_email AND "
                    + "sheep.s_id = report.s_id AND "
                    + "farmer.f_email = '" + f.getEmail() 
                    + "' AND sheep.s_id=" + sheep.getId()
                    + " ORDER BY r_id DESC";
        }
        String finalString = "SELECT * FROM ("+getNewestReports+")x GROUP BY s_id;";
        ResultSet rs = insertQuery(finalString);
        try{
            while(rs.next()){
                GeoPosition loc = new GeoPosition(rs.getFloat("r_lat"), rs.getFloat("r_lng"));
                String dday = rs.getString("s_dday");
                if(rs.wasNull()){
                    dday = null;
                }else{
                    dday = rs.getString("s_dday");
                }

                Sheep s = new Sheep(rs.getInt("s_id"), 
                        f, 
                        rs.getString("s_name"), 
                        rs.getString("s_bday"), 
                        loc, 
                        dday, 
                        rs.getString("s_gender").charAt(0), 
                        rs.getString("s_health"), 
                        rs.getDouble("s_weight"));
                    
                Report r = new Report(s, loc, rs.getInt("r_puls"), rs.getTimestamp("r_time"));
                alReport.add(r);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return alReport;
    }
    /**
     * Formats a java.util.Date to java.sql.Date
     * @param date
     * @return java.sql.Date
     */
    public static java.sql.Date formatDateToSql(java.util.Date date){
        java.sql.Date newDate = new java.sql.Date(date.getYear(), 
                date.getMonth(), date.getDay());
        return newDate;
    }
    /**
     * Creating the initial tables for the database.
     */
    public static void initDatabase(){
        // Creating string statement for sheep table creation.
        String createSheepTable =
                "CREATE TABLE sheep (" +
                "s_id INT AUTO_INCREMENT," +
                "s_name VARCHAR(255)," +
                "s_weight DOUBLE PRECISION," +
                "s_bday VARCHAR(15)," +
                "s_dday VARCHAR(15)," +
                "s_gender CHAR(1)," +
                "s_lat DECIMAL(17,7)," +
                "s_lng DECIMAL(17,7)," +
                "s_health VARCHAR(500)," +
                "f_email VARCHAR(255)," +
                "PRIMARY KEY(s_id)," + 
                "FOREIGN KEY(f_email) " +
                    "REFERENCES farmer(f_email)" +
                    "ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=INNODB;";
        
        // Creating string statement for farmer table creation.
        String createFarmerTable =
                "CREATE TABLE farmer (" +
                "f_email VARCHAR(255)," +
                "f_tlf VARCHAR(255)," +
                "f_name VARCHAR(255)," +
                "f_address VARCHAR(255)," +
                "f_postnr VARCHAR(4)," +
                "f_city VARCHAR(255)," +
                "f_lat DECIMAL(17,7)," +
                "f_lng DECIMAL(17,7)," +
                "f_pw VARCHAR(255)," +
                "ec_email VARCHAR(255)," +
                "PRIMARY KEY(f_email)," + 
                "FOREIGN KEY(ec_email)" +
                    "REFERENCES emergencycontact(ec_email) " +
                    "ON UPDATE CASCADE " +
                ")ENGINE=INNODB;";
        
        // Creating string statement for emergencycontact table creation.
        String createEmergencyContactTable =
                "CREATE TABLE emergencycontact (" +
                "ec_email VARCHAR(255)," +
                "ec_tlf VARCHAR(255)," +
                "ec_name VARCHAR(255)," +
                "PRIMARY KEY(ec_email)" + 
                ")ENGINE=INNODB;";
        
        // Creating string statement for sheep report table creation.
        String createReportTable =
                "CREATE TABLE report (" +
                "r_id INT AUTO_INCREMENT," +
                "r_lat DECIMAL(17,7)," +
                "r_lng DECIMAL(17,7)," +
                "r_puls VARCHAR(3)," +
                "r_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "s_id INT," +
                "PRIMARY KEY(r_id)," + 
                "FOREIGN KEY(s_id)" +
                    "REFERENCES sheep(s_id) " +
                    "ON DELETE CASCADE " +
                ")ENGINE=INNODB;";
        
        // Creating the tables.
        insertStatement(createEmergencyContactTable);
        insertStatement(createFarmerTable);
        insertStatement(createSheepTable);
        insertStatement(createReportTable);
    }
}

