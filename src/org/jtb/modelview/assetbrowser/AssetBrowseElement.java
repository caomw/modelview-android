package org.jtb.modelview.assetbrowser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

public class AssetBrowseElement extends AbstractBrowseElement {
	private static final long serialVersionUID = 1L;

	public AssetBrowseElement(Context context, String path) {
		super(context, path);
	}

	protected InputStream getInputStream() throws IOException {
		InputStream is = context.getResources().getAssets().open(getPath());
		return is;
	}

	@Override
	public List<AbstractBrowseElement> getChildren() throws IOException {
		String[] assets = context.getResources().getAssets().list(getPath());
		List<AbstractBrowseElement> bes = new ArrayList<AbstractBrowseElement>();
		for (String asset : assets) {
			// try to list only directories or .off files
			// assets don't give a real way to distinguish between
			// files and dirs
			if (!asset.contains(".") || isUnderstood(asset)) {
				AbstractBrowseElement be = new AssetBrowseElement(context, getPath() + "/" + asset);
				bes.add(be);
			}
		}
		return bes;
	}

	@Override
	public boolean isDirectory() {
		// can we do better?
		// we control this structure, so it's probably okay
		// to make such assumptions
		return !isUnderstood(getPath());
	}

	@Override
	public long getSize() throws IOException {
		AssetFileDescriptor afd = context.getAssets().openFd(getPath());
		return afd.getDeclaredLength();
	}

	@Override
	public long getLastModoficationDate() {
		return 0;
	}
}
