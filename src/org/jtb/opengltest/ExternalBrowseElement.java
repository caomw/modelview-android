package org.jtb.opengltest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

public class ExternalBrowseElement extends BrowseElement {
	private static final long serialVersionUID = 1L;

	ExternalBrowseElement(Context context, String path) {
		super(context, path);
		// TODO Auto-generated constructor stub
	}

	protected InputStream getInputStream() throws IOException {
		InputStream is = new FileInputStream(getPath());
		return is;
	}

	@Override
	List<BrowseElement> getChildren() throws IOException {
		File fp = new File(getPath());
		if (!fp.exists()) {
			return Collections.emptyList();
		}
		List<BrowseElement> bes = new ArrayList<BrowseElement>();
		String[] files = fp.list();
		for (String fn : files) {
			File f = new File(fp + "/" + fn);
			if (f.isDirectory() || isUnderstood(fn)) {
				BrowseElement be = new ExternalBrowseElement(getContext(),
						getPath() + "/" + fn);
				bes.add(be);
			}
		}
		return bes;
	}

	@Override
	boolean isDirectory() {
		return new File(getPath()).isDirectory();
	}
}
