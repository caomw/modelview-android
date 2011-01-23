package org.jtb.modelview;

import static org.jtb.modelview.Vertex.X;
import static org.jtb.modelview.Vertex.Y;
import static org.jtb.modelview.Vertex.Z;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;

public class Triangle {
	static final int V1 = 0;
	static final int V2 = 1;
	static final int V3 = 2;

	Vertex[] vertices = new Vertex[3];
	private Vertex normal = null;

	Triangle(Color color, Vertex v1, Vertex v2, Vertex v3, Vertex normal) {
		this(color, v1, v2, v3);
		this.normal = normal;
	}

	Triangle(Color color, Vertex v1, Vertex v2, Vertex v3) {
		this(v1, v2, v3);
		this.vertices[V1].color = color;
		this.vertices[V2].color = color;
		this.vertices[V3].color = color;
	}

	Triangle(Vertex v1, Vertex v2, Vertex v3) {
		this.vertices[V1] = v1;
		this.vertices[V2] = v2;
		this.vertices[V3] = v3;
	}

	Triangle(Vertex v1, Vertex v2, Vertex v3, Vertex normal) {
		this.vertices[V1] = v1;
		this.vertices[V2] = v2;
		this.vertices[V3] = v3;
		this.normal = normal;
	}

	Triangle reverse() {
		return new Triangle(vertices[V1], vertices[V3], vertices[V2]);
	}

	public float[] toFloatArray() {
		float[] floats = new float[3 * 3];
		System.arraycopy(vertices[V1].vertex, 0, floats, 0 * 3, 3);
		System.arraycopy(vertices[V2].vertex, 0, floats, 1 * 3, 3);
		System.arraycopy(vertices[V3].vertex, 0, floats, 2 * 3, 3);

		return floats;
	}

	public float[] colorArray() {
		float[] floats = new float[12]; // 12 = 3 * 4
		System.arraycopy(vertices[V1].color.rgba, 0, floats, 0 * 4, 4);
		System.arraycopy(vertices[V2].color.rgba, 0, floats, 1 * 4, 4);
		System.arraycopy(vertices[V3].color.rgba, 0, floats, 2 * 4, 4);

		return floats;
	}

	public static float[] toFloatArray(List<Triangle> tris) {
		int size = tris.size();
		float[] floats = new float[size * 9]; // 9 = 3 * 3
		for (int i = 0; i < size; i++) {
			// 3 * 3 = 9 = vertices x 3 points
			System.arraycopy(tris.get(i).toFloatArray(), 0, floats, i * 9, 9);
		}
		return floats;
	}

	public Vertex normal() {
		if (normal == null) {
			Vertex u = vertices[V2].sub(vertices[V1]);
			Vertex v = vertices[V3].sub(vertices[V1]);
			normal = u.cross(v);

			// normalize
			float factor = FloatMath.sqrt(normal.vertex[X]*normal.vertex[X]
					+ normal.vertex[Y]*normal.vertex[Y]
					+ normal.vertex[Z]*normal.vertex[Z]);
			normal.vertex[X] /= factor;
			normal.vertex[Y] /= factor;
			normal.vertex[Z] /= factor;
		}

		return normal;
	}

	public float[] normalArray() {
		Vertex n = normal();
		float[] na = new float[9];
		for (int i = 0; i < 3; i++) {
			System.arraycopy(n.vertex, 0, na, i * 3, 3);
		}

		return na;
	}

	public static float[] toNormals(List<Triangle> tris) {
		int size = tris.size();
		float[] floats = new float[size * 9]; // 9 = 3 * 3
		float[] na;
		for (int i = 0; i < size; i++) {
			na = tris.get(i).normalArray();
			System.arraycopy(na, 0, floats, i * 9, 9);
		}
		return floats;
	}

	public static int[] toIndices(List<Triangle> tris) {
		int size = tris.size();
		int[] indices = new int[size * 3];
		int i = 0;
		for (i = 0; i < indices.length; i++) {
			indices[i] = i;
		}
		return indices;
	}

	public static float[] toColors(List<Triangle> tris) {
		int size = tris.size();
		float[] floats = new float[size * 12]; // 12 = 3 * 4
		float[] ca;
		for (int i = 0; i < size; i++) {
			ca = tris.get(i).colorArray();
			System.arraycopy(ca, 0, floats, i * 12, 12); // 12 = 3*4
		}
		return floats;
	}

	static float boundingRadius(List<Triangle> tris, Vertex mid) {
		float radius = 0f;
		float dist;
		for (Triangle t : tris) {
			for (Vertex v : t.vertices) {
				dist = mid.distance(v);
				if (dist > radius) {
					radius = dist;
				}
			}
		}
		return radius;
	}
}
