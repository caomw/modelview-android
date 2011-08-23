package org.jtb.modelview.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jtb.modelview.R;
import org.jtb.modelview.assetbrowser.AbstractBrowseElement;
import org.jtb.modelview.assetbrowser.AggregateBrowseElement;
import org.jtb.modelview.assetbrowser.AssetBrowseElement;
import org.jtb.modelview.assetbrowser.ExternalBrowseElement;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BrowseActivity extends ListActivity {
	private final String TAG =  this.getClass().getName();
	
	@Override
	protected void onResume() {
		super.onResume();
		int version = Build.VERSION.SDK_INT;
		// use version 8+ method of killing our background processes
		if (version >= 8) {
			new Runnable() {
				public void run() {					
					ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					activityManager.killBackgroundProcesses(getApplication().getPackageName());
				}
			}.run();
		}
	}

	private FileListAdapter browseElementAdapter;
	private ListView listView;
	private AbstractBrowseElement browseElement;
	private OptionHandler optionHandler;

	@SuppressWarnings("serial")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_activity);

		optionHandler = new OptionHandler(this);

		listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				AbstractBrowseElement be = browseElementAdapter.getItem(position);
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

		browseElement = savedInstanceState != null ? (AbstractBrowseElement) savedInstanceState
				.get("browseElement") : null;
				if (browseElement == null) {
					Bundle extras = getIntent().getExtras();
					browseElement = extras != null ? (AbstractBrowseElement) extras.get("browseElement") : null;
				}
				if (browseElement != null) {
					browseElement.setContext(this);
				}

				if (browseElement == null) {
					browseElement = new AggregateBrowseElement(this,
							new ArrayList<AbstractBrowseElement>() {
						{
							add(new AssetBrowseElement(BrowseActivity.this, "modelview-data"));
							add(new ExternalBrowseElement(BrowseActivity.this, Environment.getExternalStorageDirectory().toString() + "/modelview-data"));
							add(new ExternalBrowseElement(BrowseActivity.this, Environment.getExternalStorageDirectory().toString() + "/Download"));
						}
					});
				}

				try {
					List<AbstractBrowseElement> bes = browseElement.getChildren();
					BrowseElementComparator bec = new BrowseElementComparator();
					Collections.sort(bes, bec);					
					browseElementAdapter = new FileListAdapter(this, R.layout.list_row, bes);
					setListAdapter(browseElementAdapter);
				} catch (IOException e) {
					Log.e(TAG, "^ Error assigning FileListAdaper,", e);
					e.printStackTrace();
				}

				setTitle(getResources().getString(R.string.app_name) + ": " + browseElement.toPathString());
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
	
	
	public class BrowseElementComparator implements Comparator<AbstractBrowseElement>{

	    @Override
	    public int compare(AbstractBrowseElement element1, AbstractBrowseElement element2) {
	    	if(element1.isDirectory() && !element2.isDirectory()){
	    		return 11;
	    	}else if(!element1.isDirectory() && element2.isDirectory()){
	    		return +1;
	    	}else{
	    		String name1 = element1.toString();        
	            String name2 = element2.toString();
	    		return name1.compareTo(name2);
	    	}
	    }
	}

}
