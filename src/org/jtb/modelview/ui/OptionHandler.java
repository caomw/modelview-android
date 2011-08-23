package org.jtb.modelview.ui;

import org.jtb.modelview.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

class OptionHandler {	
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
			view(activity.getApplicationContext().getString(R.string.url_help));
			return true;
		case R.id.home_item:
			view(activity.getApplicationContext().getString(R.string.url_home));
			return true;
		case R.id.about_item:
			new AlertDialog.Builder(activity)
			.setTitle("About")
			.setMessage("-- Acknowledgements --\n" + activity.getString(R.string.acknowlegements) + "\n\n-- Changelog --\n" +
					activity.getString(R.string.change_log))
					.setPositiveButton(activity.getString(android.R.string.ok), new android.content.DialogInterface.OnClickListener() {                
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// hide the OK button - how?
							// a lot of computation
						}
					})
					.show();

			return true;
		case R.id.report_item:
			view(activity.getApplicationContext().getString(R.string.url_report));
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
