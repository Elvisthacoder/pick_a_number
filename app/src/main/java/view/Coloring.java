package view;

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




}
