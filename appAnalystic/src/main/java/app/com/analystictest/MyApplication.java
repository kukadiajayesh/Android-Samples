package app.com.analystictest;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Reena on 8/2/2017.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        sAnalytics = GoogleAnalytics.getInstance(this);
        sTracker = getDefaultTracker();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.app_tracker);
        }

        return sTracker;
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker tracker = getDefaultTracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().setNewSession().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    /***
     * Tracking Exception
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {

        if (e != null) {
            Tracker tracker = getDefaultTracker();

            tracker.send(new HitBuilders.ExceptionBuilder().setDescription(
                    new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Track Event
     * @param category event category
     * @param action action of event
     * @param label label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker tracker = getDefaultTracker();

        tracker.send(new HitBuilders.EventBuilder().setCategory(category)
                .setAction(action)
                .setLabel(label).build());
    }
}
