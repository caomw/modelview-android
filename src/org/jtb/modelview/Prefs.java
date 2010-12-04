package org.jtb.modelview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class Prefs {
	static final String BACKFACES_KEY = "backFaces";

	private SharedPreferences sharedPrefs = null;
	
	Prefs(Context context) {
		sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(context);
	}

	private boolean getBoolean(String key, boolean def) {
		boolean b = sharedPrefs.getBoolean(key, def);
		return b;
	}
	
	boolean isBackFaces() {
		return getBoolean("backFaces", false);
	}
}
