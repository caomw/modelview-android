package org.jtb.opengltest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Mesh {

	private FloatBuffer verticesBuffer = null;
	private ShortBuffer indicesBuffer = null;
	private int numOfIndices = -1;
	private FloatBuffer colorBuffer = null;
	private FloatBuffer normalsBuffer;

	private int frontFace = GL10.GL_CCW;
	
	// Rotate params.
	float rx = 0f;
	float ry = 0f;
	float dx = 0f;
	float dy = 0f;
	float dxSpeed = 0f;
	float dySpeed = 0f;
	
	Vertex max, min, mid;
	Float radius;
	
	public void draw(GL10 gl) {
		gl.glFrontFace(frontFace);

		gl.glEnable(GL10.GL_CULL_FACE);

		//gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_DST_ALPHA);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

		if (normalsBuffer != null) {
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuffer);
		}

		if (colorBuffer != null) {
			// Enable the color array buffer to be used during rendering.
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		}

		gl.glCullFace(GL10.GL_FRONT);
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
				GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		gl.glCullFace(GL10.GL_BACK);
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
				GL10.GL_UNSIGNED_SHORT, indicesBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_BLEND);
	}

	protected void setTriangles(List<Triangle> tris) {
		setTriangles(tris.toArray(new Triangle[0]));
	}

	protected void setTriangles(Triangle[] tris) {
		float[] floats = Triangle.toFloatArray(tris);
		setVertices(floats);

		float[] normals = Triangle.toNormals(tris);
		setNormals(normals);

		int[] indices = Triangle.toIndices(tris);
		setIndices(indices);

		float[] colors = Triangle.toColors(tris);
		setColors(colors);
	}

	private void setVertices(float[] vertices) {
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * Float.SIZE / 8);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
	}

	private void setNormals(float[] normals) {
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(normals.length * Float.SIZE / 8);
		vbb.order(ByteOrder.nativeOrder());
		normalsBuffer = vbb.asFloatBuffer();
		normalsBuffer.put(normals);
		normalsBuffer.position(0);
	}

	private void setIndices(int[] indices) {
		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * Short.SIZE / 8);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer = ibb.asShortBuffer();
		
		short[] shorts = new short[indices.length];
		for (int i = 0; i < shorts.length; i++) {
			int x = indices[i];
			if (x > 32767) {
				x -= 65536;
			}
			shorts[i] = (short) x;
		}
		indicesBuffer.put(shorts);
		indicesBuffer.position(0);
		numOfIndices = indices.length;
	}

	private void setColors(float[] colors) {
		// float has 4 bytes.
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	void setFrontFace(int frontFace) {
		this.frontFace = frontFace;
	}
	
	boolean isMoving() {
		if (dxSpeed != 0.0f) {
			return true;
		}
		if (dySpeed != 0.0f) {
			return true;
		}
		return false;
	}
	
	void dampenSpeed(long deltaMillis) {
		if (dxSpeed != 0.0f) {
			dxSpeed *= (1.0f - 0.001f * deltaMillis);
			if (Math.abs(dxSpeed) < 0.001f) dxSpeed = 0.0f;
		}
		
		if (dySpeed != 0.0f) {
			dySpeed *= (1.0f - 0.001f * deltaMillis);
			if (Math.abs(dySpeed) < 0.001f) dySpeed = 0.0f;
		}
	}
	
	
}
