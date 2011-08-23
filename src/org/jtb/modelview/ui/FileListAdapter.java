package org.jtb.modelview.ui;

import java.util.List;

import org.jtb.modelview.R;
import org.jtb.modelview.assetbrowser.AbstractBrowseElement;
import org.jtb.modelview.util.Helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileListAdapter extends ArrayAdapter<AbstractBrowseElement> {
	private final List<AbstractBrowseElement> items;
	private final Context context;
	private final int[] colors;
	private final static String ASSETS_FOLDER = "/modelview-data/";
	
	public FileListAdapter(Context context, int textViewResourceId, List<AbstractBrowseElement> bes) {
		super(context, textViewResourceId, bes);
		
		colors = new int[] { 
				context.getResources().getColor(R.color.row_alt),
				context.getResources().getColor(R.color.row_main)};
		
		this.items = bes;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_row, null);
		}

		int colorPos = position % colors.length;
		v.setBackgroundColor(colors[colorPos]);
		
		AbstractBrowseElement o = items.get(position);
		if (o != null) {			
			//Log.d(TAG, "^ Adding item: " + o.toString() + " " + o.isDirectory());
			TextView tlt = (TextView) v.findViewById(R.id.topLeftText);
			TextView blt = (TextView) v.findViewById(R.id.bottomLeftText);
			TextView trt = (TextView) v.findViewById(R.id.topRightText);
			TextView brt = (TextView) v.findViewById(R.id.bottomRightText);
			
			ImageView iv = (ImageView)v.findViewById(R.id.icon);

			
			if(isAsset(o)){
				((LinearLayout) v.findViewById(R.id.llright)).setVisibility(View.GONE);
			}
			
			if (tlt != null) {
				tlt.setText(o.toString());                            
			}

			if (brt != null) {
				brt.setText(Helper.createDatestring(o.getLastModoficationDate()));
			}
			
			if (iv != null) {
				if(o.isDirectory()){
					iv.setImageResource(R.drawable.folder);
				} else {
					iv.setImageResource(getFileIconResource(o.toString()));
				
					if(blt != null){
						String path = o.getFullPath();
						if(path.endsWith(o.toString())){
							path = path.substring(0, path.length()-o.toString().length());
						}
						
						blt.setText(path);
					}
					
					if(trt != null){
						trt.setText(Helper.humanReadableByteCount(o.getFileSize(), true));;
					}
				}
			}

		}
		return v;
	}
	
	private boolean isAsset(AbstractBrowseElement o){
		if(o.getFullPath().startsWith(ASSETS_FOLDER)){
			Log.d("TAG", "^ true!");
			return true;
		}
		return false;
	}
	
	private int getFileIconResource(String filename){
		int res = R.drawable.unknown;
		
		if(filename.length()>=5){
			if(filename.toLowerCase().endsWith(".obj")){
				res = R.drawable.obj; 
			}
			else if(filename.toLowerCase().endsWith(".off")){
				res = R.drawable.off; 
			}
		}
		return res;
	}
	
	
}