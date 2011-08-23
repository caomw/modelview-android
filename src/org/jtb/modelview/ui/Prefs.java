package org.jtb.modelview.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {
	static final String BACKFACES_KEY = "backFaces";
	private final SharedPreferences sharedPrefs;
	
	public Prefs(final Context context) {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	private boolean getBoolean(final String key, final boolean def) {
		return sharedPrefs.getBoolean(key, def);
	}
	
	public boolean isBackFaces() {
		return getBoolean("backFaces", false);
	}
}
