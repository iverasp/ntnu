package sheepwatchui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

/**
 * A custom {@link WaypointRenderer} for painting with custom icons.
 */
public class SheepWaypointRenderer implements WaypointRenderer{
    // Image for barn
    private BufferedImage imgBarn;
    // Image for sheep
    private BufferedImage imgSheep;
    // Image for sheep attack
    private BufferedImage imgSheepAttack;
    // Image for sheep death
    private BufferedImage imgSheepDeath;

    /**
     * Creates a new instance for painin
     */
    public SheepWaypointRenderer() {
        try {
            imgBarn = ImageIO.read(getClass().getResource(
                    "/resources/barn.png"));
            imgSheep = ImageIO.read(getClass().getResource(
                    "/resources/sheep.png"));
            imgSheepAttack = ImageIO.read(getClass().getResource(
                    "/resources/SheepAttack.png"));
            imgSheepDeath = ImageIO.read(getClass().getResource(
                    "/resources/SheepDeath.png"));
        } catch (Exception ex) {
            System.out.println("Error while reading image(s).");
        }
    }

    /**
     * {@inheritDoc}
     * @param g
     * @param map
     * @param waypoint
     * @return false if the painting fails
     */
    @Override
    public final boolean paintWaypoint(Graphics2D g, JXMapViewer map,
    Waypoint waypoint) {
        // Casting the @Waypoint to a {@link Sheep}Waypoint
        SheepWaypoint sheepWaypoint = (SheepWaypoint) waypoint;
        
        if (imgSheep != null && imgBarn != null 
                && imgSheepAttack != null && imgSheepDeath != null) {
            if (sheepWaypoint.getType() == 0) {
                // Draws the sheep icon
                g.drawImage(imgSheep, -imgSheep.getWidth() / 2,
                        -imgSheep.getHeight(), null);
            } else if(sheepWaypoint.getType() == 1) {
                // Draws the barn icon
                g.drawImage(imgBarn, -imgBarn.getWidth() / 2,
                        -imgBarn.getHeight(), null);
            } else if(sheepWaypoint.getType() == 2) {
                // Draws the sheep attack icon
                g.drawImage(imgSheepAttack, -imgSheepAttack.getWidth() / 2,
                        -imgSheepAttack.getHeight(), null);
            } else if(sheepWaypoint.getType() == 3) {
                // Draws the sheep attack icon
                g.drawImage(imgSheepDeath, -imgSheepDeath.getWidth() / 2,
                        -imgSheepDeath.getHeight(), null);
            }
        } else {
            // Draws a normal icon if the image is not found
            g.setStroke(new BasicStroke(3f));
            g.setColor(Color.BLUE);
            g.drawOval(-10, -10, 20, 20);
            g.setStroke(new BasicStroke(1f));
            g.drawLine(-10, 0, 10, 0);
            g.drawLine(0, -10, 0, 10);
        }
        return false;
    }
}
