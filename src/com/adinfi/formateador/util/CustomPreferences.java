package com.adinfi.formateador.util;

import java.util.prefs.Preferences;

/**
 *
 * @author 
 */
public class CustomPreferences {
    private static CustomPreferences pcp;
    private Preferences preferences;

    private CustomPreferences() {
        preferences = Preferences.userNodeForPackage(CustomPreferences.class);
    }

    public static synchronized CustomPreferences getInstance() {
        if (pcp == null) {
            pcp = new CustomPreferences();
        }
        return pcp;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
