package edu.wm.cs.cs301.IgnatMiagkov.gui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class MazePanel extends View implements P5PanelF21 {
    private Paint paint = new Paint();
    private Canvas myCanvas;
    private Path newPath;

    private Bitmap bitmap = Bitmap.createBitmap(Resources.getSystem().getDisplayMetrics().heightPixels ,Resources.getSystem().getDisplayMetrics().widthPixels, Bitmap.Config.ARGB_8888);

    private static final Typeface font = Typeface.SANS_SERIF;

    static final int greenWM = Integer.decode("#115740");
    static final int goldWM = Integer.decode("#916f41");
    static final int yellowWM = Integer.decode("#FFFF99");
    static final int colorWhite = Integer.decode("#FFFFFF");
    static final int colorYellow = Integer.decode("#FFFF00");
    static final int colorGray = Integer.decode("#808080");
    static final int colorRed = Integer.decode("#FF0000");
    static final int colorLightGray = Integer.decode("#C0C0C0");

    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        myCanvas = new Canvas(bitmap);
        myTestImage();
    }

    @Override
    public void onDraw(Canvas canvas){
//        canvas.drawRect(100, 100, 200, 200, paint);
        canvas.drawBitmap(bitmap, 0, 0, paint);
//        paint.setColor(Color.BLACK);
//        this.addBackground(10,Resources.getSystem().getDisplayMetrics().heightPixels ,Resources.getSystem().getDisplayMetrics().widthPixels);

    }

    private void myTestImage(){
//        this.addBackground(10,Resources.getSystem().getDisplayMetrics().heightPixels ,Resources.getSystem().getDisplayMetrics().widthPixels);
        this.setColor(Color.parseColor("#ff0000"));
        this.addFilledOval(200, 200, 200, 200);
        this.setColor(Color.parseColor("#00ff00"));
        this.addFilledOval(600, 200, 200, 200);
        this.setColor(Color.parseColor("#ffff33"));
        this.addFilledRectangle(200, 1050, 200, 200);
    }


    @Override
    public void commit() {

    }

    @Override
    public boolean isOperational() {
        if (this.myCanvas != null)
            return true;
        return false;
    }

    @Override
    public void setColor(int rgb) {
        this.paint.setColor(rgb - 16777216);
    }

    @Override
    public int getColor() {
        return paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit, int viewHeight, int viewWidth) {
        paint.setStyle(Paint.Style.FILL);
        this.setColor(getBackgroundColor(10, true));
        myCanvas.drawRect(0, 0, viewWidth, viewHeight/2, paint);
        this.setColor(getBackgroundColor(10, false));
        myCanvas.drawRect(0, viewHeight/2, viewWidth,viewHeight, paint);
    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        myCanvas.drawRect(x, y, x + width, y + height, paint);
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        paint.setStyle(Paint.Style.FILL);
        newPath.reset();
        newPath.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++){
            newPath.lineTo(xPoints[i], yPoints[i]);
        }
        newPath.lineTo(xPoints[0], yPoints[0]);
        myCanvas.drawPath(newPath, paint);
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        paint.setStyle(Paint.Style.STROKE);
        newPath.reset();
        newPath.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++){
            newPath.lineTo(xPoints[i], yPoints[i]);
        }
        newPath.lineTo(xPoints[0], yPoints[0]);
        myCanvas.drawPath(newPath, paint);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        myCanvas.drawLine(startX, startY, endX, endY, paint);
    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        myCanvas.drawOval(x, y, x + width, y + height, paint);
    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        paint.setStyle(Paint.Style.STROKE);
        myCanvas.drawArc(x, y, x + width, y + height, startAngle, arcAngle, false, paint);
    }

    @Override
    public void addMarker(float x, float y, String str) {
        myCanvas.drawText(str, x, y, paint);
    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {

    }

    /**
     * Determine the background color for the top and bottom
     * rectangle as a blend between starting color settings
     * of yellowWM and lightGray towards goldWM and greenWM as final
     * color settings close to the exit
     * @param percentToExit describes how far it is to the exit as a percentage value
     * @param top is true for the top rectangle, false for the bottom
     * @return the color to use for the background rectangle
     */
    private int getBackgroundColor(float percentToExit, boolean top) {
        return top? blend(yellowWM, goldWM, percentToExit) :
                blend(colorLightGray, greenWM, percentToExit);
    }

    /**
     * Calculates the weighted average of the two given colors.
     * The weight for the first color is expected to be between
     * 0 and 1. The weight for the other color is then 1-weight0.
     * The result is the weighted average of the red, green, and
     * blue components of the colors. The resulting alpha value
     * for transparency is the max of the alpha values of both colors.
     * @param fstColor is the first color
     * @param sndColor is the second color
     * @param weightFstColor is the weight of fstColor, {@code 0.0 <= weightFstColor <= 1.0}
     * @return blend of both colors as weighted average of their rgb values
     */
    private int blend(int fstColor, int sndColor, double weightFstColor) {
        if (weightFstColor < 0.1)
            return sndColor;
        if (weightFstColor > 0.95)
            return fstColor;
        //here, bit operations are used to determine rgb value of the specific colors then merge them together
        double r = (weightFstColor * ((fstColor >> 16) & 0xFF)) + ((1-weightFstColor) * ((sndColor >> 16) & 0xFF)); //red in first two bits
        double g = (weightFstColor * ((fstColor >> 8) & 0xFF)) + ((1-weightFstColor) * ((sndColor >> 8) & 0xFF)); //green in middle two bits
        double b = (weightFstColor * ((fstColor) & 0xFF)) + ((1-weightFstColor) * ((sndColor) & 0xFF)); //blue in last two bits
        double a = Math.max(fstColor >> 24 & 0xFF, sndColor >> 24 & 0xFF);

//	    return new Color((int) r, (int) g, (int) b, (int) a);
        return ((int)r + (int)g + (int)b + (int)a);
    }

}
