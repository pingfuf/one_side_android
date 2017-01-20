package com.oneside.manager;

import android.app.Activity;

import com.oneside.ui.MainActivity;

import com.oneside.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.umd.cs.findbugs.annotations.NonNull;

public class CardActivityManager {
    /**
     * All the live activities.
     */
    @NonNull
    private static final ArrayList<WeakReference<Activity>> live_activities = new ArrayList<WeakReference<Activity>>();
    private static WeakReference<Activity> active_activity_ref;

    public static synchronized Activity getActiveActivity() {
        return active_activity_ref == null ? null : active_activity_ref.get();
    }

    public static synchronized boolean onCreated(Activity a) {

        live_activities.add(new WeakReference<Activity>(a));

        return false;
    }

    /**
     * Called when an activity is onPaused.
     *
     * @param a
     */
    public static synchronized void onResumed(@NonNull Activity a) {
        active_activity_ref = new WeakReference<Activity>(a);

    }

    /**
     * Called when an activity onDestroyed
     *
     * @param a
     */
    public static synchronized void onDestroyed(@NonNull Activity a) {
        for (int i = 0; i < live_activities.size(); i++) {

            WeakReference<Activity> wa = live_activities.get(i);
            if (wa.get() == a) {
                live_activities.remove(wa);
            }

        }

    }

    public static synchronized void onPaused(@NonNull Activity a) {
        if (getActiveActivity() == a)
            active_activity_ref = null;
    }

    /**
     * The size of live activities.
     *
     * @return
     */
    public static synchronized int liveActivityCount() {
        LogUtils.d("liveActivityCount %s", live_activities);
        return live_activities.size();
    }

    /**
     * Finish all the activities in the live activities list.
     */
    public static void finishAllActivities() {
        ArrayList<WeakReference<Activity>> cow = new ArrayList<WeakReference<Activity>>();
        cow.addAll(live_activities);
        int size = cow.size();
        for (int i = 0; i < size; i++) {

            WeakReference<Activity> a = cow.get(i);
            LogUtils.d("finishAllActivities  a %s", a);
            if (a != null && a.get() != null)
                try {
                    a.get().finish();
                } catch (Exception e) {
                    LogUtils.e(e, "Failed to finish activity: " + a);
                }
        }
    }



    public static void callBuySingleCardActivity() {
        ArrayList<WeakReference<Activity>> cow = new ArrayList<WeakReference<Activity>>();
        cow.addAll(live_activities);
        int size = cow.size();
        for (int i = 0; i < size; i++) {

            WeakReference<Activity> a = cow.get(i);
        }
    }


    public static void callBuyShowerActivity() {
        ArrayList<WeakReference<Activity>> cow = new ArrayList<WeakReference<Activity>>();
        cow.addAll(live_activities);
        int size = cow.size();
        for (int i = 0; i < size; i++) {

            WeakReference<Activity> a = cow.get(i);
        }
    }

    public static void callBuyCoachPlanActivity() {
        ArrayList<WeakReference<Activity>> cow = new ArrayList<WeakReference<Activity>>();
        cow.addAll(live_activities);
        int size = cow.size();
        for (int i = 0; i < size; i++) {
            WeakReference<Activity> a = cow.get(i);
        }
    }

    /**
     *
     */
    public static void finishOtherActivitiesExceptMain() {
        ArrayList<WeakReference<Activity>> cow = new ArrayList<WeakReference<Activity>>();
        cow.addAll(live_activities);
        int size = cow.size();
        for (int i = 0; i < size; i++) {

            WeakReference<Activity> a = cow.get(i);
            LogUtils.d("finishAllActivities  a %s", a);
            if (a != null && a.get() != null && !(a.get() instanceof MainActivity))
                try {
                    a.get().finish();
                } catch (Exception e) {
                    LogUtils.e(e, "Failed to finish activity: " + a);
                }
        }
    }
}
