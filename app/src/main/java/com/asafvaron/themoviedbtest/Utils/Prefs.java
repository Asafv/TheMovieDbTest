package com.asafvaron.themoviedbtest.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class Prefs {
    public static final String TAG = Prefs.class.getSimpleName();
    public static Prefs sInstance;

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "the_movie_db_test";

    @SuppressLint("CommitPrefEdits")
    private Prefs(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * must be initialized called on Application class
     *
     * @param context
     */
    public static void init(Context context) {
        sInstance = new Prefs(context);
    }

    public static Prefs getInstance() {
        return sInstance;
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public Long getLong(String key) {
        return pref.getLong(key, 0);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public Set<String> getStringSet(String key) {
        return new HashSet<>(pref.getStringSet(key, new HashSet<String>()));
    }

    public void putStringSet(String key, Set<String> set) {
        editor.putStringSet(key, set).apply();
    }

    public boolean doesPrefExist(String key) {
        return pref.contains(key);
    }

    public void removeFromPref(String key) {
        editor.remove(key).apply();
    }

    public void clearAllData() {
        editor.clear().apply();
    }
}

