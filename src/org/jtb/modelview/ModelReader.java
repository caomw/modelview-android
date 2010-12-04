package org.jtb.modelview;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

abstract class ModelReader {
	protected static final int BUFFER_SIZE = 8192;
	
	protected Context context;
	
	ModelReader(Context context) {
		this.context = context;
	}
	
	abstract Mesh readMesh(InputStream is) throws ModelLoadException;
}
