package models;

import java.util.Date;
import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * Instances of this class contain reports from {@link Sheep}.
 */
public class Report {
    private Sheep sheep;
    private GeoPosition sheepLocation;
    private int heartRate;
    private Date time;
    
    /**
     * Constructor used when getting a {@link Report} from the database.
     * @param sheep
     * @param sheepLocation
     * @param heartRate
     * @param time 
     */
    public Report(Sheep sheep, GeoPosition sheepLocation, int heartRate, 
            Date time){
        this.sheep = sheep;
        this.sheepLocation = sheepLocation;
        this.heartRate = heartRate;
        this.time = time;
    }
    
    /**
     * Constructor used when adding a {@link Report} to the database.
     * @param sheep
     * @param sheepLocation
     * @param heartRate 
     */
    public Report(Sheep sheep, GeoPosition sheepLocation, int heartRate){
        this.sheep = sheep;
        this.sheepLocation = sheepLocation;
        this.heartRate = heartRate;
        this.time = time;
    }

    /**
     * @return the {@link Sheep}
     */
    public Sheep getSheep() {
        return sheep;
    }

    /**
     * @param sheep the {@link Sheep} to set
     */
    public void setSheep(Sheep sheep) {
        this.sheep = sheep;
    }

    /**
     * @return the sheepLocation
     */
    public GeoPosition getSheepLocation() {
        return sheepLocation;
    }

    /**
     * @param sheepLocation the sheepLocation to set
     */
    public void setSheepLocation(GeoPosition sheepLocation) {
        this.sheepLocation = sheepLocation;
    }

    /**
     * @return the heartRate
     */
    public int getHeartRate() {
        return heartRate;
    }

    /**
     * @param heartRate the heartRate to set
     */
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }
}
