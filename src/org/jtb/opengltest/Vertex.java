package org.jtb.opengltest;

public class Vertex {
	float x, y, z;
	Color color = Color.WHITE;

	public Vertex() {	
	}
	
	public Vertex(Color color, float x, float y, float z) {
		this(x, y, z);
		this.color = color;
	}

	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vertex sub(Vertex other) {
		Vertex r = new Vertex(this.x - other.x, this.y - other.y, this.z
				- other.z);
		return r;
	}

	public Vertex cross(Vertex other) {
		Vertex r = new Vertex(this.y * other.z - this.z * other.y, -(this.x
				* other.z - this.z * other.x), this.x * other.y - this.y
				* other.x);
		return r;
	}

	public float[] toFloatArray() {
		return new float[] { x, y, z };
	}

	float distance(Vertex other) {
		float xd = this.x - other.x;
		float yd = this.y - other.y;
		float zd = this.z - other.z;
		float d = (float) Math.sqrt(xd * xd + yd * yd + zd * zd);
		return d;
	}
}
