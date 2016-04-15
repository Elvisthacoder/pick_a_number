package view;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by taifa on 4/14/16.
 */
public class Coloring {

    private static final String LOG_TAG = Coloring.class.getSimpleName();
    private static final int BOUNDS = 1500;
    private static final int BRIGHTNESS_THRESHOLD = 180;
    private static final int FADE_DURATION = 200;

    private static final Object mInitializerLock;
    private static Coloring mInstance;

    static {
        mInitializerLock = new Object();
    }

    /**
     * Destroys everything related to coloring.<br>
     */
    public static synchronized void destroy() {
        mInstance = null;
    }

    /**
     * Returns the singleton factory object.
     *
     * @return The only available {@code Coloring}
     */


    public static Coloring get() {
        if (mInstance == null) {
            synchronized (mInitializerLock) {
                if (mInstance == null) {
                    mInstance = new Coloring();
                }
            }
        }
        return mInstance;
    }

    /* **********  Factory methods go below this line  ********** */

    /**
     * Converts a String hex color value to an Integer color value.<br>
     * <br>
     * <b>Supported formats:</b><br>
     * <ul>
     * <li>#aaRRggBb</li>
     * <li>0xaaRRggBb</li>
     * <li>0XaaRRggBb</li>
     * <li>#RRggBb</li>
     * <li>0xRRggBb</li>
     * <li>0XRRggBb</li>
     * </ul>
     *
     * @param colorString String value of the desired color
     * @return Integer value for the color, or gray if something goes wrong
     */


    public int decodeColor(String colorString) {
        if (colorString == null || colorString.trim().isEmpty())
            return Color.BLACK;

        if (colorString.startsWith("#"))
            colorString = colorString.replace("#", "");

        if (colorString.startsWith("0x"))
            colorString = colorString.replace("0x", "");

        if (colorString.startsWith("0X"))
            colorString = colorString.replace("0X", "");

        int alpha = -1, red = -1, green = -1, blue = -1;

        try {
            if (colorString.length() == 8) {
                alpha = Integer.parseInt(colorString.substring(0, 2), 16);
                red = Integer.parseInt(colorString.substring(2, 4), 16);
                green = Integer.parseInt(colorString.substring(4, 6), 16);
                blue = Integer.parseInt(colorString.substring(6, 8), 16);
            } else if (colorString.length() == 6) {
                alpha = 255;
                red = Integer.parseInt(colorString.substring(0, 2), 16);
                green = Integer.parseInt(colorString.substring(2, 4), 16);
                blue = Integer.parseInt(colorString.substring(4, 6), 16);
            }
            return Color.argb(alpha, red, green, blue);
        } catch (NumberFormatException e) {
            Log.w(LOG_TAG, "Error parsing color ", e);
            return Color.GRAY;
        }
    }

    /**
     * Blends given color with white background. This means that a full color<br>
     * with transparency (alpha) will be lightened to make it look like it is<br>
     * rendered over a white background. Resulting color will be non-transparent.
     *
     * @param color Color to use for blending
     * @return Lightened color to match a white underlay render
     */

    public int alphaBlendWithWhite(int color) {
        float alpha = Color.alpha(color) / 255f;
        int origR = Color.red(color);
        int origG = Color.green(color);
        int origB = Color.blue(color);
        int white = 255;

        // rule: outputRed = (foregroundRed * foregroundAlpha) + (backgroundRed * (1.0 - foregroundAlpha))
        int r = (int) ((origR * alpha) + (white * (1.0 - alpha)));
        if (r > 255)
            r = 255;
        int g = (int) ((origG * alpha) + (white * (1.0 - alpha)));
        if (g > 255)
            g = 255;
        int b = (int) ((origB * alpha) + (white * (1.0 - alpha)));
        if (b > 255)
            b = 255;

        return Color.argb(255, r, g, b);
    }

    public int darkenColor(int color) {
        int amount = 30;

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int a = Color.alpha(color);

        if (r - amount >= 0) {
            r -= amount;
        } else {
            r = 0;
        }

        if (g - amount >= 0) {
            g -= amount;
        } else {
            g = 0;
        }

        if (b - amount >= 0) {
            b -= amount;
        } else {
            b = 0;
        }

        return Color.argb(a, r, g, b);
    }





}
