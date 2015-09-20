package models;

import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * Instances of this class contain information about {@link Farmer}s.
 */

public class Farmer {
    private String name;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String postnr;
    private String city;
    private GeoPosition farmLocation;
    private EmergencyContact emergencyContact;

    /**
     * Constructor with password
     * @param name
     * @param email
     * @param phone
     * @param emergencyContact
     * @param address
     * @param postnr
     * @param city
     * @param password 
     */
    public Farmer(String name, String email, String phone, 
            EmergencyContact emergencyContact, String address, String postnr,
            String city, GeoPosition farmLocation, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.address = address;
        this.postnr = postnr;
        this.city = city;
        this.farmLocation = farmLocation;
        this.password = password;
    }
    /**
     * Constructor without password
     * @param name
     * @param email
     * @param phone
     * @param emergencyContact
     * @param address
     * @param postnr
     * @param city 
     */
    public Farmer(String name, String email, String phone, 
            EmergencyContact emergencyContact, String address, String postnr,
            String city, GeoPosition farmLocation) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.address = address;
        this.postnr = postnr;
        this.city = city;
        this.farmLocation = farmLocation;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postnr
     */
    public String getPostnr() {
        return postnr;
    }

    /**
     * @param postnr the postnr to set
     */
    public void setPostnr(String postnr) {
        this.postnr = postnr;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the {@link EmergencyContact}
     */
    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    /**
     * @param emergencyContact the {@link EmergencyContact} to set
     */
    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    /**
     * @return the farmLocation
     */
    public GeoPosition getFarmLocation(){
        return farmLocation;
    }
    
    /**
     * @param farmLocation the farmLocation to set
     */
    public void setFarmLocation(GeoPosition farmLocation){
        this.farmLocation = farmLocation;
    }
}
