package view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;

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

    /**
     * Makes the given color a little bit darker.
     *
     * @param color Original color that needs to be darker
     * @return Darkened original color
     */

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

    /**
     * Makes the given color a little bit lighter.
     *
     * @param color Original color that needs to be lighter
     * @return Lightened original color
     */

    public int lightenColor(int color) {
        int amount = 60;

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int a = Color.alpha(color);

        if (r + amount <= 255) {
            r += amount;
        } else {
            r = 255;
        }

        if (g + amount <= 255) {
            g += amount;
        } else {
            g = 255;
        }

        if (b + amount <= 255) {
            b += amount;
        } else {
            b = 255;
        }

        return Color.argb(a, r, g, b);
    }

    /**
     * Creates a new drawable (implementation of the Drawable object may vary depending on OS version).<br>
     * Drawable will be colored with given color, and clipped to match given boundaries.
     *
     * @param color Integer color used to color the output drawable
     * @param bounds Four-dimensional vector bounds
     * @return Colored and clipped drawable object
     */
    @SuppressWarnings("UnusedDeclaration")

    public Drawable createDrawable(int color, Rect bounds) {
        // init normal state drawable
        Drawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {
                color, color
        }).mutate();
        if (color == Color.TRANSPARENT) {
            drawable.setAlpha(0);
        }
        drawable.setBounds(bounds);
        return drawable;
    }

    /**
     * Colors the given drawable to a specified color. Uses mode SRC_ATOP.
     *
     * @param context Which context to use
     * @param drawable Which drawable to color
     * @param color Which color to use
     * @return A colored drawable ready for use
     */

    public Drawable colorDrawable(Context context, @Nullable Drawable drawable, int color) {
        if (!(drawable instanceof BitmapDrawable)) {
            Log.w(LOG_TAG, "Original drawable is not a bitmap! Trying with constant state cloning.");
            return colorUnknownDrawable(drawable, color);
        }

        Bitmap original = ((BitmapDrawable) drawable).getBitmap();
        Bitmap copy = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        Paint paint = new Paint();
        Canvas c = new Canvas(copy);
        paint.setColorFilter(new PorterDuffColorFilter(color, SRC_ATOP));
        c.drawBitmap(original, 0, 0, paint);

        return new BitmapDrawable(context.getResources(), copy);
    }

    /**
     * Colors the given drawable to a specified color set using the drawable wrapping technique.
     *
     * @param drawable Which drawable to color
     * @param colorStates Which color set to use
     * @return A colored drawable ready to use
     */

    public Drawable colorDrawableWrap(Drawable drawable, ColorStateList colorStates) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawable, colorStates);
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            drawable = DrawableCompat.unwrap(drawable);
            return drawable;
        }
        return null;
    }

    /**
     * Colors the given drawable to a specified color using the drawable wrapping technique.
     *
     * @param drawable Which drawable to color
     * @param color Which color to use
     * @return A colored drawable ready to use
     */

    public Drawable colorDrawableWrap(Drawable drawable, int color) {
        if (drawable != null) {
            Drawable wrapped = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrapped, color);
            DrawableCompat.setTintMode(wrapped, PorterDuff.Mode.SRC_ATOP);
            return DrawableCompat.unwrap(wrapped);
        }
        return null;
    }




}
