package org.jtb.modelview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

class OptionHandler {
	private static final String HELP_URL = "http://code.google.com/p/modelview-android/wiki/Help";
	private static final String HOME_URL = "http://code.google.com/p/modelview-android";
	private static final String REPORT_URL = "http://code.google.com/p/modelview-android/issues/entry";
	
	static final int PREFERENCES_REQUEST = 0;

	private Activity activity;
	
	OptionHandler(Activity activity) {
		this.activity = activity;
	}
	
	boolean handle(int id) {
		Intent intent;
		
		switch (id) {
		case R.id.preferences_item:
			intent = new Intent(activity, PrefsActivity.class);
			activity.startActivityForResult(intent, PREFERENCES_REQUEST);
			return true;
		case R.id.help_item:
			view(HELP_URL);
			return true;
		case R.id.home_item:
			view(HOME_URL);
			return true;
		case R.id.report_item:
			view(REPORT_URL);
			return true;
		}		
		
		return false;
	}
	
	private void view(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		activity.startActivity(intent);		
	}
}
