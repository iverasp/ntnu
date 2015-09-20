
package sheepwatchui;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Sheep;

/**
 * Class containing methods for validating different data.
 */
public class ValidationHelper {
    /**
     * Checks if a string has a valid email format.
     * @param email
     * @return true if the email is valid. 
     */
    public static boolean checkEmail(String email){
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }
    /**
     * Checks if a string is an 8 digit number.
     * @param phone
     * @return true if the number is valid.
     */
    public static boolean checkPhoneNumber(String phone){
        boolean isValid = true;
        try { 
            Integer.parseInt(phone); 
        } catch(NumberFormatException e) { 
            isValid = false; 
        }
        if(phone.length() != 8){
            isValid = false;
        }
        return isValid;
    }
    /**
     * Checks if a string is a 4 digit number.
     * @param postnr
     * @return true if the postnr is valid.
     */
    public static boolean checkPostnr(String postnr){
        boolean isValid = true;
        try { 
            Integer.parseInt(postnr); 
        } catch(NumberFormatException e) { 
            isValid = false; 
        }
        if(postnr.length() != 4){
            isValid = false;
        }
        return isValid;
    }
    
    /**
     * Checks if the date is before the current time.
     * Makes sure that the {@link Sheep} cannot be born in the future.
     * @param d
     * @return true if the {@link Date} is valid. false if invalid.
     */
    public static boolean isValidDate(Date d){
        if(d.before(Calendar.getInstance().getTime())){
            return true;
        }else{
            return false;
        }
    }
}
