package com.example.myapp.Volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentManager;

import java.util.Set;

public class PreferenceProvider {

    private static int attempt = 1;

    private static Context context = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static FragmentManager supportFragmentManager;

    public static Context getContext() {
        return context;
    }

    public static FragmentManager getfragmentManager(){
        return supportFragmentManager;
    }

    public static void initialize(Context con) {
        if (context == null) {
            context = con;
        }
        if (null == preferences) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (null == editor) {
            editor = preferences.edit();
        }
    }

    public static void save(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static void save(String key, Integer value) {
        save(key, String.valueOf(value));
    }

    public static void save(String key, Set set) {
        editor.putStringSet(key, set);
        editor.commit();
    }


    public static Set getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    public static void save(String key, Long value) {
        save(key, String.valueOf(value));
    }

    public static String get(String key) {
        return preferences.getString(key, null);
    }

    public static Boolean contains(String key) {
        return preferences.contains(key);
    }

    public static void removeKey(String key) {
        editor.remove(key);
        editor.commit();
    }

    public static void setFragmentManger(FragmentManager fragmentManger) {
        supportFragmentManager = fragmentManger;
    }
}
