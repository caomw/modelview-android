package org.jtb.modelview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseActivity extends ListActivity {
	private ArrayAdapter<BrowseElement> browseElementAdapter;
	private ListView listView;
	private BrowseElement browseElement;

	@SuppressWarnings("serial")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);

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

		// dirText = (TextView) findViewById(R.id.browse_dir_text);
		// dirText.setText(dir);

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
}
