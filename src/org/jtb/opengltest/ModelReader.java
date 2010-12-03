package org.jtb.opengltest;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

abstract class ModelReader {
	protected Context context;
	
	ModelReader(Context context) {
		this.context = context;
	}
	
	abstract Mesh readMesh(InputStream is) throws ModelLoadException;
}
