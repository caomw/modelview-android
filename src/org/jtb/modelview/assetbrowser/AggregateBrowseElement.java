package org.jtb.modelview.assetbrowser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class AggregateBrowseElement extends AbstractBrowseElement {
	private static final long serialVersionUID = 1L;
	private final List<AbstractBrowseElement> aggregates;
	
	public AggregateBrowseElement(Context context, List<AbstractBrowseElement> aggregates) {
		super(context, "/");
		this.aggregates = aggregates;
	}

	protected InputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException("cannnot open aggregate browse element");
	}

	@Override
	public List<AbstractBrowseElement> getChildren() throws IOException {
		List<AbstractBrowseElement> bes = new ArrayList<AbstractBrowseElement>();
		for (AbstractBrowseElement be: aggregates) {
			bes.addAll(be.getChildren());
		}
		return bes;
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

	@Override
	public String toPathString() {
		return "/";
	}

	@Override
	long getSize() throws IOException {
		throw new UnsupportedOperationException("cannnot get size of aggregate browse element");
	}

	@Override
	public long getLastModoficationDate() {
		long lastModified = 0;
		for (AbstractBrowseElement be : aggregates) {
			if (be.getLastModoficationDate() > lastModified) {
				lastModified = be.getLastModoficationDate();
			}
		}
		
		return lastModified;
	}

	
}
