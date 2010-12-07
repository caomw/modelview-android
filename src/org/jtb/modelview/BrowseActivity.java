package org.jtb.modelview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseActivity extends ListActivity {
	@Override
	protected void onResume() {
		super.onResume();
		int version = Build.VERSION.SDK_INT;
		// use version 8+ method of killing our background processes
		if (version >= 8) {
			new Runnable() {
				public void run() {					
					ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					activityManager.killBackgroundProcesses(getApplication()
							.getPackageName());
				}
			}.run();
		}
	}

	private ArrayAdapter<BrowseElement> browseElementAdapter;
	private ListView listView;
	private BrowseElement browseElement;
	private OptionHandler optionHandler;

	@SuppressWarnings("serial")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);

		optionHandler = new OptionHandler(this);

		listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				BrowseElement be = browseElementAdapter.getItem(position);
				Intent i;
				if (!be.isDirectory()) {
					i = new Intent(BrowseActivity.this, ModelViewActivity.class);
				} else {
					i = new Intent(BrowseActivity.this, BrowseActivity.class);
				}
				i.putExtra("browseElement", be);
				BrowseActivity.this.startActivity(i);
			}
		});

		browseElement = savedInstanceState != null ? (BrowseElement) savedInstanceState
				.get("browseElement") : null;
		if (browseElement == null) {
			Bundle extras = getIntent().getExtras();
			browseElement = extras != null ? (BrowseElement) extras
					.get("browseElement") : null;
		}
		if (browseElement != null) {
			browseElement.setContext(this);
		}

		if (browseElement == null) {
			browseElement = new AggregateBrowseElement(this,
					new ArrayList<BrowseElement>() {
						{
							add(new AssetBrowseElement(BrowseActivity.this,
									"modelview-data"));
							add(new ExternalBrowseElement(BrowseActivity.this,
									Environment.getExternalStorageDirectory()
											.toString() + "/modelview-data"));
						}
					});
		}

		try {
			List<BrowseElement> bes = browseElement.getChildren();
			Collections.sort(bes);
			browseElementAdapter = new ArrayAdapter<BrowseElement>(this,
					R.layout.browse_item, bes);
			setListAdapter(browseElementAdapter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle(getResources().getString(R.string.app_name) + ": "
				+ browseElement.toPathString());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.modelview_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return optionHandler.handle(item.getItemId());
	}

}
