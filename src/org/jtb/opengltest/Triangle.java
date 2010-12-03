package org.jtb.opengltest;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Triangle {
	Vertex v1, v2, v3;

	Triangle(Color color, Vertex v1, Vertex v2, Vertex v3) {
		this(v1, v2, v3);
		this.v1.color = color;
		this.v2.color = color;
		this.v3.color = color;
	}

	Triangle(Vertex v1, Vertex v2, Vertex v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	Triangle reverse() {
		return new Triangle(v1, v3, v2);
	}

	public float[] toFloatArray() {
		float[] floats = new float[3 * 3];
		System.arraycopy(v1.toFloatArray(), 0, floats, 0 * 3, 3);
		System.arraycopy(v2.toFloatArray(), 0, floats, 1 * 3, 3);
		System.arraycopy(v3.toFloatArray(), 0, floats, 2 * 3, 3);

		return floats;
	}

	public float[] colorArray() {
		float[] floats = new float[3 * 4];
		System.arraycopy(v1.color.toFloatArray(), 0, floats, 0 * 4, 4);
		System.arraycopy(v2.color.toFloatArray(), 0, floats, 1 * 4, 4);
		System.arraycopy(v3.color.toFloatArray(), 0, floats, 2 * 4, 4);

		return floats;
	}

	@SuppressWarnings("serial")
	List<Vertex> verticesAsList() {
		return new ArrayList<Vertex>() {
			{
				add(v1);
				add(v2);
				add(v3);
			}
		};
	}

	public static float[] toFloatArray(Triangle[] tris) {
		float[] floats = new float[tris.length * 3 * 3];
		for (int i = 0; i < tris.length; i++) {
			System.arraycopy(tris[i].toFloatArray(), 0, floats, i * 3 * 3,
					3 * 3);
		}
		return floats;
	}

	public Vertex normal() {
		Vertex u = v2.sub(v1);
		Vertex v = v3.sub(v1);
		Vertex n = u.cross(v);

		// normalize
		double factor = Math.sqrt(Math.pow(n.x, 2) + Math.pow(n.y, 2)
				+ Math.pow(n.z, 2));
		n.x /= factor;
		n.y /= factor;
		n.z /= factor;

		return n;
	}

	public float[] normalArray() {
		Vertex n = normal();
		float[] a = n.toFloatArray();
		float[] na = new float[9];
		for (int i = 0; i < 3; i++) {
			System.arraycopy(a, 0, na, i * 3, 3);
		}

		return na;
	}

	public static float[] toNormals(Triangle[] tris) {
		float[] floats = new float[tris.length * 3 * 3];
		for (int i = 0; i < tris.length; i++) {
			float[] na = tris[i].normalArray();
			System.arraycopy(na, 0, floats, i * 9, 9);
		}
		return floats;
	}

	public static int[] toIndices(Triangle[] tris) {
		int[] indices = new int[tris.length * 3];
		int i = 0;
		for (i = 0; i < indices.length; i++) {
			indices[i] = i;
		}
		return indices;
	}

	public static float[] toColors(Triangle[] tris) {
		float[] floats = new float[tris.length * 3 * 4];
		for (int i = 0; i < tris.length; i++) {
			float[] ca = tris[i].colorArray();
			System.arraycopy(ca, 0, floats, i * 3 * 4, 3 * 4);
		}
		return floats;
	}

	static void center(List<Triangle> tris, float minX, float maxX, float minY,
			float maxY) {
		float midX = (minX + maxX) / 2;
		float midY = (minY + maxY) / 2;

		for (Triangle t : tris) {
			for (Vertex v : t.verticesAsList()) {
				v.x -= midX;
				v.y -= midY;
			}
		}
	}

	static float boundingRadius(List<Triangle> tris, Vertex min, Vertex max) {
		Vertex c = new Vertex((min.x + max.x) / 2, (min.y + max.y) / 2,
				(min.z + max.z) / 2);
		float radius = 0f;
		for (Triangle t : tris) {
			for (Vertex v : t.verticesAsList()) {
				float dist = c.distance(v);
				if (dist > radius) {
					radius = dist;
				}
			}
		}
		return radius;
	}
}
