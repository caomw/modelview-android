package org.jtb.opengltest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class AggregateBrowseElement extends BrowseElement {
	private static final long serialVersionUID = 1L;
	private List<BrowseElement> aggregates;
	
	AggregateBrowseElement(Context context, List<BrowseElement> aggregates) {
		super(context, "/");
		this.aggregates = aggregates;
	}

	protected InputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException("cannnot open aggregate browse element");
	}

	@Override
	List<BrowseElement> getChildren() throws IOException {
		List<BrowseElement> bes = new ArrayList<BrowseElement>();
		for (BrowseElement be: aggregates) {
			bes.addAll(be.getChildren());
		}
		return bes;
	}

	@Override
	boolean isDirectory() {
		return true;
	}

	@Override
	String toPathString() {
		return "/";
	}

}
