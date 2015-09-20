package sheepwatchui;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;

/**
 * A custom {@link Waypoint} for painting different types of waypoints with icons.
 */
public class SheepWaypoint extends Waypoint {

    private int type;

    /**
     * Creates a new customized {@link SheepWaypoint}
     * @param pos the {@link GeoPosition} of the {@link Waypoint}
     * @param type 0 for sheep image, 1 for barn image, 
     * 2 for sheep attack image, 3 for sheep death image
     */
    public SheepWaypoint(GeoPosition pos, final int type) {
        super(pos);
        this.type = type;
    }

    /**
     * @return Return the waypoint type: type 0 is sheep and 1 is barn
     */
    public final int getType() {
        return type;
    }

    /**
     * @param type The waypoint type. 0 for sheep and 1 for barn
     */
    public final void setType(final int type) {
        this.type = type;
    }
}
