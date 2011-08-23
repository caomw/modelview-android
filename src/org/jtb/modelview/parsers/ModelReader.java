package org.jtb.modelview.parsers;

import java.io.InputStream;

import org.jtb.modelview.d3.Mesh;
import org.jtb.modelview.d3.ModelLoadException;

import android.content.Context;

public abstract class ModelReader {
	public static final int BUFFER_SIZE = 8192;
	
	protected Context context;
	
	public ModelReader(Context context) {
		this.context = context;
	}
	
	public abstract Mesh readMesh(InputStream is) throws ModelLoadException;
}
