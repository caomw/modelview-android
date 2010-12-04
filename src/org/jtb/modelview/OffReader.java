package org.jtb.modelview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import static org.jtb.modelview.Vertex.*;

class OffReader extends ModelReader {
	OffReader(Context context) {
		super(context);
	}

	Mesh readMesh(InputStream is) throws ModelLoadException {
		String line = null;
		int lineNumber = 0;

		try {
			List<Triangle> triangles;
			Vertex min = new Vertex();
			Vertex max = new Vertex();
			Vertex mid = new Vertex();

			Reader r = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(r, BUFFER_SIZE);

			// consume until we get the OFF header
			while (!(line = br.readLine()).contains("OFF")) {
				lineNumber++;
			}
			// consume until we get the counts
			while ((line = br.readLine()).trim().startsWith("#")
					|| line.trim().length() == 0) {
				lineNumber++;
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
				lineNumber++;
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
					if (x > max.vertex[X]) {
						max.vertex[X] = x;
					} else if (x < min.vertex[X]) {
						min.vertex[X] = x;
					}
					float y = Float.parseFloat(tok.nextToken());
					if (y > max.vertex[Y]) {
						max.vertex[Y] = y;
					} else if (y < min.vertex[Y]) {
						min.vertex[Y] = y;
					}
					float z = Float.parseFloat(tok.nextToken());
					if (z > max.vertex[Z]) {
						max.vertex[Z] = z;
					} else if (z < min.vertex[Z]) {
						min.vertex[Z] = z;
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
				}
				lc++;
			}
			if (triangles.size() == 0) {
				throw new ModelLoadException("No triangles read");
			}

			mid.vertex[X] = (min.vertex[X] + max.vertex[X]) / 2;
			mid.vertex[Y] = (min.vertex[Y] + max.vertex[Y]) / 2;
			mid.vertex[Z] = (min.vertex[Z] + max.vertex[Z]) / 2;

			Mesh mesh = new Mesh();
			mesh.radius = Triangle.boundingRadius(triangles, min, max);
			mesh.setTriangles(triangles);
			mesh.max = max;
			mesh.min = min;
			mesh.mid = mid;

			return mesh;
		} catch (Throwable t) {
			ModelLoadException mle = new ModelLoadException(t);
			mle.setLineNumber(lineNumber);
			mle.setLine(line);
			throw mle;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}
}
