package models;

/**
 * Instances of this class contain information about {@link EmergencyContact}s.
 */
public class EmergencyContact {

    private String name;
    private String email;
    private String phone;

    /**
     * Constructor creating an instance of {@link EmergencyContact}
     * @param name
     * @param email
     * @param phone 
     */
    public EmergencyContact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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


}
