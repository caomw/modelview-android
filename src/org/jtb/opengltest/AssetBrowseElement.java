package org.jtb.opengltest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class AssetBrowseElement extends BrowseElement {
	private static final long serialVersionUID = 1L;

	AssetBrowseElement(Context context, String path) {
		super(context, path);
	}

	protected InputStream getInputStream() throws IOException {
		InputStream is = getContext().getResources().getAssets()
				.open(getPath());
		return is;
	}

	@Override
	List<BrowseElement> getChildren() throws IOException {
		String[] assets = getContext().getResources().getAssets()
				.list(getPath());
		List<BrowseElement> bes = new ArrayList<BrowseElement>();
		for (String asset : assets) {
			// try to list only directories or .off files
			// assets don't give a real way to distinguish between
			// files and dirs 
			if (!asset.contains(".") || isUnderstood(asset)) {
				BrowseElement be = new AssetBrowseElement(getContext(),
						getPath() + "/" + asset);
				bes.add(be);
			}
		}
		return bes;
	}

	@Override
	boolean isDirectory() {
		// can we do better?
		// we control this structure, so it's probably okay
		// to make such assumptions
		return !isUnderstood(getPath());
	}
}
