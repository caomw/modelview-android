package org.jtb.opengltest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import android.util.Log;

class OffReader extends ModelReader {
	OffReader(InputStream is) throws IOException {
		super();
		
		List<Triangle> triangles;
		Vertex min = new Vertex();
		Vertex max = new Vertex();
		Vertex mid = new Vertex();

		Reader r = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(r);

		String line = null;
		try {
			// consume until we get the OFF header
			while (!(line = br.readLine()).contains("OFF")) {
			}
			// consume until we get the counts
			while ((line = br.readLine()).trim().startsWith("#")
					|| line.trim().length() == 0) {
			}

			StringTokenizer tok = new StringTokenizer(line);
			int vertexCount = Integer.parseInt(tok.nextToken());
			@SuppressWarnings("unused")
			int faceCount = Integer.parseInt(tok.nextToken());
			@SuppressWarnings("unused")
			int edgeCount = Integer.parseInt(tok.nextToken());

			List<Vertex> vertices = new ArrayList<Vertex>();
			triangles = new ArrayList<Triangle>();

			int lc = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				if (line.startsWith("#")) {
					continue;
				}
				int commentIndex = line.indexOf('#');
				if (commentIndex != -1) {
					line = line.substring(0, commentIndex);
				}

				tok = new StringTokenizer(line);
				if (lc < vertexCount) {
					// reading vertices
					float x = Float.parseFloat(tok.nextToken());
					if (x > max.x) {
						max.x = x;
					} else if (x < min.x) {
						min.x = x;
					}
					float y = Float.parseFloat(tok.nextToken());
					if (y > max.y) {
						max.y = y;
					} else if (y < min.y) {
						min.y = y;
					}
					float z = Float.parseFloat(tok.nextToken());
					if (z > max.z) {
						max.z = z;
					} else if (z < min.z) {
						min.z = z;
					}

					Color color = Color.GRAY;
					if (tok.hasMoreTokens()) {
						if (tok.countTokens() >= 3) {
							// can't handle pointer to color map case
							// TODO
							float red = Float.parseFloat(tok.nextToken());
							float green = Float.parseFloat(tok.nextToken());
							float blue = Float.parseFloat(tok.nextToken());
							float alpha = 1.0f;
							if (tok.hasMoreTokens()) {
								alpha = Float.parseFloat(tok.nextToken());
							}
							color = new Color(red, green, blue, alpha);
						}
					}
					Vertex v = new Vertex(color, x, y, z);
					vertices.add(v);
				} else {
					// reading indices
					int nv = Integer.parseInt(tok.nextToken());
					int[] indices = new int[nv];
					for (int i = 0; i < nv; i++) {
						indices[i] = Integer.parseInt(tok.nextToken());
					}
					Color color = null;
					if (tok.hasMoreTokens()) {
						if (tok.countTokens() >= 3) {
							// can't handle pointer to color map case
							// TODO
							float red = Float.parseFloat(tok.nextToken());
							float green = Float.parseFloat(tok.nextToken());
							float blue = Float.parseFloat(tok.nextToken());
							float alpha = Color.ALPHA;
							if (tok.hasMoreTokens()) {
								alpha = Float.parseFloat(tok.nextToken());
							}
							color = new Color(red, green, blue, alpha);
						}
					}
					int i = 0;
					try {
						for (i = 1; i < nv - 1; i++) {
							Vertex v1 = vertices.get(indices[0]);
							Vertex v2 = vertices.get(indices[i]);
							Vertex v3 = vertices.get(indices[i + 1]);

							if (color != null) {
								v1.color = color;
								v2.color = color;
								v3.color = color;
							}
							Triangle t = new Triangle(v1, v2, v3);
							triangles.add(t);
							triangles.add(t.reverse());
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						Log.e("opengl-test", "could not parse line: " + line + ", i=" + i + ", indices.length=" + indices.length);
						throw e;
					}
				}
				lc++;
			}
		} catch (IndexOutOfBoundsException e) {
			Log.e("opengl-test", "could not parse line: " + line);
			throw e;
		} catch (NoSuchElementException e) {
			Log.e("opengl-test", "could not parse line: " + line);
			throw e;
		} catch (NumberFormatException e) {
			Log.e("opengl-test", "could not parse line: " + line);
			throw e;
		}

		mid.x = (min.x + max.x) / 2;
		mid.y = (min.y + max.y) / 2;
		mid.z = (min.z + max.z) / 2;

		getMesh().radius = Triangle.boundingRadius(triangles, min, max);
		getMesh().setTriangles(triangles);
		getMesh().max = max;
		getMesh().min = min;
		getMesh().mid = mid;
	}
}
