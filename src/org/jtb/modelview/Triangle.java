package org.jtb.modelview;

import static org.jtb.modelview.Vertex.X;
import static org.jtb.modelview.Vertex.Y;
import static org.jtb.modelview.Vertex.Z;

import java.util.ArrayList;
import java.util.List;

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
		float[] floats = new float[3 * 4];
		System.arraycopy(vertices[V1].color.rgba, 0, floats, 0 * 4, 4);
		System.arraycopy(vertices[V2].color.rgba, 0, floats, 1 * 4, 4);
		System.arraycopy(vertices[V3].color.rgba, 0, floats, 2 * 4, 4);

		return floats;
	}

	@SuppressWarnings("serial")
	List<Vertex> verticesAsList() {
		return new ArrayList<Vertex>() {
			{
				add(vertices[V1]);
				add(vertices[V2]);
				add(vertices[V3]);
			}
		};
	}

	public static float[] toFloatArray(Triangle[] tris) {
		float[] floats = new float[tris.length * 3 * 3];
		for (int i = 0; i < tris.length; i++) {
			// 3 * 3 = 9 = vertices x 3 points
			System.arraycopy(tris[i].toFloatArray(), 0, floats, i * 9, 9);
		}
		return floats;
	}

	public Vertex normal() {
		if (normal == null) {
			Vertex u = vertices[V2].sub(vertices[V1]);
			Vertex v = vertices[V3].sub(vertices[V1]);
			normal = u.cross(v);

			// normalize
			double factor = Math.sqrt(Math.pow(normal.vertex[X], 2)
					+ Math.pow(normal.vertex[Y], 2)
					+ Math.pow(normal.vertex[Z], 2));
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
				v.vertex[X] -= midX;
				v.vertex[Y] -= midY;
			}
		}
	}

	static float boundingRadius(List<Triangle> tris, Vertex min, Vertex max) {
		Vertex c = new Vertex((min.vertex[X] + max.vertex[X]) / 2,
				(min.vertex[Y] + max.vertex[Y]) / 2,
				(min.vertex[Z] + max.vertex[Z]) / 2);
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
