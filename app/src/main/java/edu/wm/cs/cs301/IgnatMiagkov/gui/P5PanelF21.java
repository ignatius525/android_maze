package edu.wm.cs.cs301.IgnatMiagkov.gui;

/**
 * Provides an adapter for a graphics object for the first person view
 * and the map view to draw on.
 * Any implementing class encapsulates and hides its dependency on AWT.
 * To its clients it offers the notion of an editor:
 * one can clear it by adding the background,
 * then put graphical entities to draw into it,
 * and finally commit the complete drawing to the UI.
 * The naming is technical as it is for Project 5
 * and should serve as a specification for the MazePanel class.
 *
 * Note that methods for drawX in awt have a corresponding
 * method addX with same parameters here. The naming emphasizes
 * that the P5Panel accumulates bits and pieces for an
 * overall drawing that is then shown on the screen
 * when the editor's content is complete and committed
 * for drawing.
 *
 * The documentation includes guidance which AWT method is encapsulated
 * or can be substituted by which interface method.
 *
 * @author Peter Kemper
 *
 */
public interface P5PanelF21 {
    // TODO FOR P5:
    // Add code to the following static method that will help you with Wall.java
    //
    /**
     * Determines the color for a wall.
     * Supports color determination for the Wall.initColor method.
     * See also https://www.geeksforgeeks.org/static-method-in-interface-in-java/
     * @param distance is the distance to the exit
     * @param cc is an obscure parameter used in Wall for color determination, just passed in here
     * @param extensionX is the wall's length and direction (sign), horizontal dimension
     * @return the rgb value for the color of the wall
     */

    static final int RGB_DEF = 20;
    static final int RGB_DEF_GREEN = 60;

    public static int getWallColor(int distance, int cc, int extensionX) {
        final int d = distance / 4;
        final int rgbValue = calculateRGBValue(d, extensionX);
        switch (((d >> 3) ^ cc) % 6) {
            case 0:
//             return(new Color(rgbValue, RGB_DEF, RGB_DEF));
                return ((rgbValue << 16) + (RGB_DEF << 8) + RGB_DEF);
            case 1:
//         	return(new Color(RGB_DEF, RGB_DEF_GREEN, RGB_DEF));
                return ((RGB_DEF << 16) + (RGB_DEF_GREEN << 8) + RGB_DEF);
            case 2:
//         	return(new Color(RGB_DEF, RGB_DEF, rgbValue));
                return ((RGB_DEF << 16) + (RGB_DEF << 8) + rgbValue);
            case 3:
//         	return(new Color(rgbValue, RGB_DEF_GREEN, RGB_DEF));
                return ((rgbValue << 16) + (RGB_DEF_GREEN << 8) + RGB_DEF);
            case 4:
//         	return(new Color(RGB_DEF, RGB_DEF_GREEN, rgbValue));
                return ((RGB_DEF << 16) + (RGB_DEF_GREEN << 8) + rgbValue);
            case 5:
//         	return(new Color(rgbValue, RGB_DEF, rgbValue));
                return ((rgbValue << 16) + (RGB_DEF << 8) + rgbValue);
            default:
//         	return(new Color(RGB_DEF, RGB_DEF, RGB_DEF));
                return ((RGB_DEF << 16) + (RGB_DEF << 8) + RGB_DEF);
            // YOUR CODE IS MISSING HERE
        } // THIS IS JUST TO MAKE THE COMPILER HAPPY YOU NEED TO FIX THIS
    };

    public static int calculateRGBValue(final int distance, final int extensionX) {
        // compute rgb value, depends on distance and x direction
        // 7 in binary is 0...0111
        // use AND to get last 3 digits of distance
        final int part1 = distance & 7;
        final int add = (extensionX != 0) ? 1 : 0;
        return ((part1 + 2 + add) * 70) / 8 + 80;
    };


    /**
     * Commits all accumulated drawings to the UI.
     * Substitute for MazePanel.update method.
     */
    public void commit();

    /**
     * Tells if instance is able to draw. This ability depends on the
     * context, for instance, in a testing environment, drawing
     * may be not possible and not desired.
     * Substitute for code that checks if graphics object for drawing is not null.
     * @return true if drawing is possible, false if not.
     */
    public boolean isOperational();

    /**
     * Sets the color for future drawing requests. The color setting
     * will remain in effect till this method is called again and
     * with a different color.
     * Substitute for Graphics.setColor method.
     * @param rgb gives the red green and blue encoded value of the color
     */
    public void setColor(int rgb);

    /**
     * Returns the RGB value for the current color setting.
     * @return integer RGB value
     */
    public int getColor();


    /**
     * Draws two solid rectangles to provide a background.
     * Note that this also erases any previous drawings.
     * The color setting adjusts to the distance to the exit to
     * provide an additional clue for the user.
     * Colors transition from black to gold and from grey to green.
     * Add additional height and width window parameters to incorporate background draw in one method for maze panel.
     * Substitute for FirstPersonView.drawBackground method.
     * @param percentToExit gives the distance to exit
     */
    public void addBackground(float percentToExit, int viewHeight, int viewWidth);

    /**
     * Adds a filled rectangle.
     * The rectangle is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis.
     * Substitute for Graphics.fillRect() method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    public void addFilledRectangle(int x, int y, int width, int height);

    /**
     * Adds a filled polygon.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.fillPolygon() method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints);

    /**
     * Adds a polygon.
     * The polygon is not filled.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.drawPolygon method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints);

    /**
     * Adds a line.
     * A line is described by {@code (x,y)} coordinates for its
     * starting point and its end point.
     * Substitute for Graphics.drawLine method
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    public void addLine(int startX, int startY, int endX, int endY);

    /**
     * Adds a filled oval.
     * The oval is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis. An oval is
     * described like a rectangle.
     * Substitute for Graphics.fillOval method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    public void addFilledOval(int x, int y, int width, int height);
    /**
     * Adds the outline of a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at startAngle and extends for arcAngle degrees,
     * using the current color. Angles are interpreted such that 0 degrees
     * is at the 3 o'clock position. A positive value indicates a counter-clockwise
     * rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is
     * (x, y) and whose size is specified by the width and height arguments.
     * The resulting arc covers an area width + 1 pixels wide
     * by height + 1 pixels tall.
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the start
     * and end of the arc segment will be skewed farther along the longer
     * axis of the bounds.
     * Substitute for Graphics.drawArc method
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) ;
    /**
     * Adds a string at the given position.
     * Substitute for CompassRose.drawMarker method
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    public void addMarker(float x, float y, String str) ;
    /**
     * An enumerated type to match 1-1 the awt.RenderingHints used
     * in CompassRose and MazePanel.
     */
    enum P5RenderingHints { KEY_RENDERING, VALUE_RENDER_QUALITY, KEY_ANTIALIASING, VALUE_ANTIALIAS_ON, KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR } ;
    /**
     * Sets the value of a single preference for the rendering algorithms.
     * It internally maps given parameter values into corresponding java.awt.RenderingHints
     * and assigns that to the internal graphics object.
     * Hint categories include controls for rendering quality
     * and overall time/quality trade-off in the rendering process.
     * Refer to the awt RenderingHints class for definitions of some common keys and values.
     * @param hintKey the key of the hint to be set.
     * @param hintValue the value indicating preferences for the specified hint category.
     */
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue);
}


