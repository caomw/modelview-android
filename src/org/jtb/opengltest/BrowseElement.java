package org.jtb.opengltest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.os.Handler;

abstract class BrowseElement implements Serializable, Comparable<BrowseElement> {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	private static final Set<String> VALID_EXTENSIONS = new HashSet<String>() {
		{
			add(".off");
			add(".obj");
		}
	};

	protected transient Context context;
	protected String path;

	BrowseElement(Context context, String path) {
		this.context = context;
		this.path = path;
	}

	protected static boolean isUnderstood(String path) {
		for (String ext : VALID_EXTENSIONS) {
			if (path.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	String getPath() {
		return path;
	}

	protected abstract InputStream getInputStream() throws IOException;

	Mesh getMesh() throws ModelLoadException {
		try {
			if (getPath().endsWith(".off")) {
				return new OffReader(context).readMesh(getInputStream());
			}
			if (getPath().endsWith(".obj")) {
				return new ObjReader(context).readMesh(getInputStream());
			}
			throw new ModelLoadException();
		} catch (IOException ioe) {
			ModelLoadException mle = new ModelLoadException(ioe);
			mle.setPath(toPathString());
			throw mle;
		} catch (ModelLoadException mle) {
			mle.setPath(toPathString());
			throw mle;
		}
	}

	abstract long getSize() throws IOException;

	abstract List<BrowseElement> getChildren() throws IOException;

	abstract boolean isDirectory();

	String toPathString() {
		int i = getPath().indexOf("modelview-data");
		return getPath().substring(i + "modelview-data".length(),
				getPath().length());
	}

	@Override
	public String toString() {
		return new File(path).getName();
	}

	void setContext(Context context) {
		this.context = context;
	}

	public int compareTo(BrowseElement other) {
		return toString().compareTo(other.toString());
	}
}
