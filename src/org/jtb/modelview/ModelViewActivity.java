package org.jtb.modelview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class ModelViewActivity extends Activity implements OnClickListener,
		View.OnTouchListener {

	static final int LOADING_DIALOG = 0;
	static final int LOAD_ERROR_DIALOG = 1;

	private ProgressDialog loadingDialog;
	private AlertDialog loadErrorDialog;

	static final int SHOW_LOADING_WHAT = 0;
	static final int SHOW_LOAD_ERROR_WHAT = 1;
	static final int HIDE_LOADING_WHAT = 2;
	static final int HIDE_LOAD_ERROR_WHAT = 3;
	static final int PREPARE_SURFACE_WHAT = 4;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_LOADING_WHAT:
				showDialog(LOADING_DIALOG);
				break;
			case SHOW_LOAD_ERROR_WHAT:
				showDialog(LOAD_ERROR_DIALOG);
				break;
			case HIDE_LOADING_WHAT:
				dismissDialog(LOADING_DIALOG);
				break;
			case HIDE_LOAD_ERROR_WHAT:
				dismissDialog(LOAD_ERROR_DIALOG);
				break;
			case PREPARE_SURFACE_WHAT:
				prepareSurface();
				break;
			}
		}
	};

	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private float lastX, lastY;
	private MeshRenderer renderer;
	private GLSurfaceView surfaceView;
	private ModelLoadException loadException = null;

	private static class TestGestureDetector extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 500;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		private ModelViewActivity activity;
		private MeshRenderer testRenderer;

		TestGestureDetector(ModelViewActivity activity, MeshRenderer testRenderer) {
			this.activity = activity;
			this.testRenderer = testRenderer;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			activity.surfaceView
					.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

			activity.renderer.mesh.dySpeed = velocityX / 1000;
			activity.renderer.mesh.dxSpeed = velocityY / 1000;

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

		final BrowseElement be = browseElement;
		showDialog(LOADING_DIALOG);
		new Thread(new Runnable() {
			public void run() {
				try {
					renderer = new MeshRenderer(ModelViewActivity.this, be);
					handler.sendEmptyMessage(PREPARE_SURFACE_WHAT);
				} catch (ModelLoadException e) {
					setLoadException(e);
					handler.sendEmptyMessage(SHOW_LOAD_ERROR_WHAT);
				} finally {
					handler.sendEmptyMessage(HIDE_LOADING_WHAT);
				}

			}
		}).start();
	}

	private void setLoadException(ModelLoadException e) {
		loadException = e;
	}

	void setRenderMode(int mode) {
		surfaceView.setRenderMode(mode);
	}

	private void prepareSurface() {
		surfaceView = new GLSurfaceView(this);
		setContentView(surfaceView);
		surfaceView.setSoundEffectsEnabled(false);
		surfaceView.setRenderer(renderer);
		surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		surfaceView.setOnClickListener(this);
		surfaceView.setOnTouchListener(this);

		surfaceView.requestRender();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
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

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LOADING_DIALOG:
			if (loadingDialog == null) {
				loadingDialog = new ProgressDialog(this);
				loadingDialog.setMessage("Loading ...");
				// loadingDialog.setIndeterminate(true);
			}
			return loadingDialog;
		case LOAD_ERROR_DIALOG:
			if (loadErrorDialog == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Error Loading");
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage(loadException.getMessage());
				builder.setNeutralButton("Back",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dismissDialog(LOAD_ERROR_DIALOG);
								finish();
							}
						});

				loadErrorDialog = builder.create();
			}
			Log.e("modelview", "load error", loadException);
			return loadErrorDialog;
		}
		return null;
	}
}
