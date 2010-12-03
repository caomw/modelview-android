package org.jtb.opengltest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class TestActivity extends Activity implements OnClickListener,
		View.OnTouchListener {
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private float lastX, lastY;
	private MeshRenderer renderer;
	private GLSurfaceView surfaceView;

	private static class TestGestureDetector extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 500;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		private TestActivity activity;
		private MeshRenderer testRenderer;

		TestGestureDetector(TestActivity activity, MeshRenderer testRenderer) {
			this.activity = activity;
			this.testRenderer = testRenderer;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			activity.surfaceView
					.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

			activity.renderer.mesh.dySpeed = velocityX / 2000;
			activity.renderer.mesh.dxSpeed = velocityY / 2000;

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// full screen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

		surfaceView = new GLSurfaceView(this);
		surfaceView.setSoundEffectsEnabled(false);
		
		BrowseElement browseElement = savedInstanceState != null ? (BrowseElement) savedInstanceState
				.get("browseElement") : null;
		if (browseElement == null) {
			Bundle extras = getIntent().getExtras();
			browseElement = extras != null ? (BrowseElement) extras
					.get("browseElement") : null;
		}
		if (browseElement == null) {
			return; // error
		}
		browseElement.setContext(this);

		renderer = new MeshRenderer(this, surfaceView, browseElement);
		surfaceView.setRenderer(renderer);
		surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		setContentView(surfaceView);

		// Gesture detection
		gestureDetector = new GestureDetector(new TestGestureDetector(this,
				renderer));
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

		surfaceView.setOnClickListener(this);
		surfaceView.setOnTouchListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		surfaceView.requestRender();
	}

	/*
	 * @Override public boolean onTouch(View v, MotionEvent event) {
	 * gestureDetector.onTouchEvent(event);
	 * 
	 * switch (event.getAction()) { case MotionEvent.ACTION_DOWN: lastX =
	 * event.getX(); lastY = event.getY(); return true; case
	 * MotionEvent.ACTION_MOVE: // Log.d("opengl-test", "event x: " +
	 * event.getX() + ", last x: " // + lastX); float xChange = (lastX -
	 * event.getX()) / 2; // Log.d("opengl-test", "event y: " + event.getY() +
	 * ", last y: " // + lastY); float yChange = (lastY - event.getY()) / 2;
	 * 
	 * testRenderer.mesh.rx += -yChange; testRenderer.mesh.ry += -xChange;
	 * view.requestRender();
	 * 
	 * // Log.d("opengl-test", "xChange: " + xChange + ", yChange: " // +
	 * yChange);
	 * 
	 * lastX = event.getX(); lastY = event.getY();
	 * 
	 * return true; }
	 * 
	 * return false; }
	 */

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

			renderer.mesh.dxSpeed = 0.0f;
			renderer.mesh.dySpeed = 0.0f;
			lastX = event.getX();
			lastY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			surfaceView.requestRender();

			renderer.mesh.ry += event.getX() - lastX;
			renderer.mesh.rx += event.getY() - lastY;
			lastX = event.getX();
			lastY = event.getY();
			break;
		}

		return super.onTouchEvent(event);
	}
}
