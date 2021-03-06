package com.coldstoragecoins.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This class is used for storing data
 *
 * @author Virendra
 * @version 1.0
 * @since 2017-05-06
 */
public class SharedPreferenceUtility {

    private static SharedPreferences mPref;
    private static SharedPreferenceUtility mRef;
    private Editor mEditor;

    private SharedPreferenceUtility() {
    }

    /**
     * Singleton method return the instance
     *
     * @param context
     * @return
     */
    public static SharedPreferenceUtility getInstance(Context context) {
        if (mRef == null) {
            mRef = new SharedPreferenceUtility();
            mPref = context.getApplicationContext().getSharedPreferences(
                    "MyPref", 0);
            return mRef;
        }
        return mRef;
    }

    /**
     * Put long value into sharedpreference
     *
     * @param key
     * @param value
     */
    public void putLong(String key, long value) {
        try {
            mEditor = mPref.edit();
            mEditor.putLong(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get long value from sharedpreference
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        try {
            long lvalue;
            lvalue = mPref.getLong(key, 0);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put int value into sharedpreference
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        try {
            mEditor = mPref.edit();
            mEditor.putInt(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get int value from sharedpreference
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        try {
            int lvalue;
            lvalue = mPref.getInt(key, 0);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put String value into sharedpreference
     *
     * @param key
     * @param value
     * @return
     */
    public String putString(String key, String value) {
        try {
            mEditor = mPref.edit();
            mEditor.putString(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * Get String value from sharedpreference
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        try {
            String lvalue;
            lvalue = mPref.getString(key, "");
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Put String value into sharedpreference
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean putBoolean(String key, Boolean value) {
        try {
            mEditor = mPref.edit();
            mEditor.putBoolean(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Get String value from sharedpreference
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        try {
            Boolean lvalue;
            lvalue = mPref.getBoolean(key, false);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Clear the prefs
     */
    public void clearPref() {

        mPref.edit().clear().commit();
    }

}
