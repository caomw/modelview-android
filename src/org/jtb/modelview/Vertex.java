package org.jtb.modelview;

import android.util.FloatMath;

public class Vertex {
	static final int X = 0;
	static final int Y = 1;
	static final int Z = 2;

	float[] vertex = new float[3];

	Color color = Color.WHITE;

	Vertex() {
	}

	Vertex(Color color, float x, float y, float z) {
		this(x, y, z);
		this.color = color;
	}

	Vertex(float x, float y, float z) {
		vertex[X] = x;
		vertex[Y] = y;
		vertex[Z] = z;
	}

	Vertex sub(Vertex other) {
		Vertex r = new Vertex(vertex[X] - other.vertex[X], this.vertex[Y]
				- other.vertex[Y], this.vertex[Z] - other.vertex[Z]);
		return r;
	}

	Vertex cross(Vertex other) {
		Vertex r = new Vertex(this.vertex[Y] * other.vertex[Z] - this.vertex[Z]
				* other.vertex[Y],
				-(this.vertex[X] * other.vertex[Z] - this.vertex[Z]
						* other.vertex[X]), this.vertex[X] * other.vertex[Y]
						- this.vertex[Y] * other.vertex[X]);
		return r;
	}

	Vertex middle(Vertex other) {
		Vertex mid = new Vertex();
		mid.vertex[X] = (this.vertex[X] + other.vertex[X]) / 2;
		mid.vertex[Y] = (this.vertex[Y] + other.vertex[Y]) / 2;
		mid.vertex[Z] = (this.vertex[Z] + other.vertex[Z]) / 2;
		return mid;
	}

	float distance(Vertex other) {
		float xd = this.vertex[X] - other.vertex[X];
		float yd = this.vertex[Y] - other.vertex[Y];
		float zd = this.vertex[Z] - other.vertex[Z];
		float d =  FloatMath.sqrt(xd * xd + yd * yd + zd * zd);
		return d;
	}
}
