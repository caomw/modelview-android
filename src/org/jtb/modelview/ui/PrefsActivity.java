package org.jtb.modelview.ui;

import org.jtb.modelview.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private CheckBoxPreference backFacesPref;
	private Prefs mPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.prefs);

		mPrefs = new Prefs(this);
		backFacesPref = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Prefs.BACKFACES_KEY);

		setResult(ModelViewActivity.RESULT_NONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(Prefs.BACKFACES_KEY)) {
			setResult(ModelViewActivity.RESULT_INIT);
		}
	}
}
