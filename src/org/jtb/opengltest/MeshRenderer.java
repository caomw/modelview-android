package org.jtb.opengltest;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import static org.jtb.opengltest.Vertex.*;

public class MeshRenderer implements Renderer {
	public Mesh mesh;

	private TestActivity activity;
	private long lastMillis;

	public MeshRenderer(TestActivity activity, BrowseElement browseElement)
			throws ModelLoadException {
		this.activity = activity;
		mesh = browseElement.getMesh();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);

		float[] lights;

		lights = new float[] { 0f, 0f, 0f, 1f };
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lights, 0);
		lights = new float[] { 1f, 1f, 1f, 1f };
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lights, 0);
		lights = new float[] { 0f, 0f, 0f, 1f };
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lights, 0);

		float matAmbient[] = { 0f, 0f, 0f, 1f };
		float matDiffuse[] = { 1f, 1f, 1f, 1f };
		float matSpecular[] = { 0f, 0f, 0f, 1f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, matSpecular,
				0);

		float lightPosition[] = { mesh.mid.vertex[X], mesh.mid.vertex[Y], 100f,
				1f };
		float lightDirection[] = { 0f, 0f, 1f };

		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, lightDirection, 0);

		// gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 180f);
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		GLU.gluLookAt(gl, 0f, 0f, 2f * mesh.radius, 0f, 0f, 0f, 0f, 1f, 0f);

		gl.glPushMatrix();
		gl.glTranslatef(mesh.mid.vertex[X], mesh.mid.vertex[Y],
				mesh.mid.vertex[Z]);
		gl.glRotatef(mesh.rx + mesh.dx, 1, 0, 0);
		gl.glRotatef(mesh.ry + mesh.dy, 0, 1, 0);
		gl.glTranslatef(-mesh.mid.vertex[X], -mesh.mid.vertex[Y],
				-mesh.mid.vertex[Z]);
		mesh.draw(gl);
		gl.glPopMatrix();

		// get current millis
		long currentMillis = System.currentTimeMillis();
		if (lastMillis != 0) {
			long delta = currentMillis - lastMillis;
			mesh.dx += mesh.dxSpeed * delta;
			mesh.dy += mesh.dySpeed * delta;
			mesh.dampenSpeed(delta);
		}
		lastMillis = currentMillis;

		if (!isMoving()) {
			activity.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
	}

	boolean isMoving() {
		if (mesh.dxSpeed != 0.0f) {
			return true;
		}
		if (mesh.dySpeed != 0.0f) {
			return true;
		}
		return false;
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		float zNear = .01f;
		float zFar = zNear + 4f * mesh.radius; // 2x diameter

		float aspect = (float) width / (float) height;

		float left = mesh.mid.vertex[X] - mesh.radius;
		float right = mesh.mid.vertex[X] + mesh.radius;
		float bottom = mesh.mid.vertex[Y] - mesh.radius;
		float top = mesh.mid.vertex[Y] + mesh.radius;

		/*
		 * if (aspect < 1.0) { // window taller than wide bottom /= aspect; top
		 * /= aspect; } else { bottom *= aspect; top *= aspect; }
		 */

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 90.0f, aspect, zNear, zFar);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(left, right, bottom, top, zNear, zFar);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
