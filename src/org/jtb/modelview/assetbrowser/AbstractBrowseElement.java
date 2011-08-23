package org.jtb.modelview.assetbrowser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtb.modelview.d3.Mesh;
import org.jtb.modelview.d3.ModelLoadException;
import org.jtb.modelview.parsers.ObjReader;
import org.jtb.modelview.parsers.OffReader;

import android.content.Context;

public abstract class AbstractBrowseElement implements Serializable, Comparable<AbstractBrowseElement> {
	private static final long serialVersionUID = 1L;
	protected transient Context context;
	protected String path;
	
	@SuppressWarnings("serial")
	private static final Set<String> VALID_EXTENSIONS = new HashSet<String>() {
		{
			add(".off");
			add(".obj");
		}
	};

	AbstractBrowseElement(Context context, String path) {
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

	public Mesh getMesh() throws ModelLoadException {
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

	public abstract List<AbstractBrowseElement> getChildren() throws IOException;

	public abstract boolean isDirectory();

	public String toPathString() {
		int i = getPath().indexOf("modelview-data");
		return getPath().substring(i + "modelview-data".length(), getPath().length());
	}

	@Override
	public String toString() {
		return new File(path).getName();
	}
	
	public long getLastModoficationDate() {
		return new File(path).lastModified();
	}
	
	public String getFullPath() {
		return new File(path).getAbsolutePath();
	}
	
	public long getFileSize() {
		return new File(path).length();
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	public int compareTo(AbstractBrowseElement other) {
		return toString().compareTo(other.toString());
	}
}
