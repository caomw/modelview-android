package org.jtb.modelview.assetbrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

public class ExternalBrowseElement extends AbstractBrowseElement {
	private static final long serialVersionUID = 1L;
	private File filePath;

	public ExternalBrowseElement(Context context, String path) {
		super(context, path);
		filePath = new File(path);
	}

	protected InputStream getInputStream() throws IOException {
		InputStream is = new FileInputStream(getPath());
		return is;
	}

	@Override
	public List<AbstractBrowseElement> getChildren() throws IOException {
		if (!filePath.exists()) {
			return Collections.emptyList();
		}
		List<AbstractBrowseElement> bes = new ArrayList<AbstractBrowseElement>();
		String[] files = filePath.list();
		AbstractBrowseElement be;
		for (String fn : files) {
			File f = new File(filePath.toString() + "/" + fn);
			if (f.isDirectory() || isUnderstood(fn)) {
				be = new ExternalBrowseElement(context, getPath() + "/" + fn);
				bes.add(be);
			}
		}
		return bes;
	}

	@Override
	public boolean isDirectory() {
		return new File(getPath()).isDirectory();
	}

	@Override
	public long getSize() {
		return filePath.length();
	}
}
