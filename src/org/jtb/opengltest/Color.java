package org.jtb.opengltest;

class Color {
	static final float ALPHA = .75f;

	static final Color WHITE = new Color(1f, 1f, 1f, ALPHA);
	static final Color GRAY = new Color(.6f, .6f, .6f, ALPHA);
	static final Color RED = new Color(1f, 0f, 0f, ALPHA);
	static final Color GREEN = new Color(0f, 1f, 0f, ALPHA);
	static final Color BLUE = new Color(0f, 0f, 1f, ALPHA);
	static final Color YELLOW = new Color(1f, 1f, 0f, ALPHA);
	static final Color MAGENTA = new Color(1f, 0f, 1f, ALPHA);
	static final Color CYAN = new Color(0f, 1f, 1f, ALPHA);

	float r, g, b, a;

	Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	float[] toFloatArray() {
		float[] fa = new float[4];
		fa[0] = this.r;
		fa[1] = this.g;
		fa[2] = this.b;
		fa[3] = this.a;

		return fa;
	}
}
