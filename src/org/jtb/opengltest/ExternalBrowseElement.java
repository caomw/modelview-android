package org.jtb.opengltest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;

public class ExternalBrowseElement extends BrowseElement {
	private static final long serialVersionUID = 1L;
	private File filePath;

	ExternalBrowseElement(Context context, String path) {
		super(context, path);
		filePath = new File(path);
	}

	protected InputStream getInputStream() throws IOException {
		InputStream is = new FileInputStream(getPath());
		return is;
	}

	@Override
	List<BrowseElement> getChildren() throws IOException {
		if (!filePath.exists()) {
			return Collections.emptyList();
		}
		List<BrowseElement> bes = new ArrayList<BrowseElement>();
		String[] files = filePath.list();
		for (String fn : files) {
			File f = new File(filePath.toString() + "/" + fn);
			if (f.isDirectory() || isUnderstood(fn)) {
				BrowseElement be = new ExternalBrowseElement(context, getPath()
						+ "/" + fn);
				bes.add(be);
			}
		}
		return bes;
	}

	@Override
	boolean isDirectory() {
		return new File(getPath()).isDirectory();
	}

	@Override
	long getSize() {
		return filePath.length();
	}
}
