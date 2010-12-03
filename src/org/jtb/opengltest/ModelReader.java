package org.jtb.opengltest;

abstract class ModelReader {
	private Mesh mesh;

	ModelReader() {
		mesh = new Mesh();
	}
	
	Mesh getMesh() {
		return mesh;
	}	
}
