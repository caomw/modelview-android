package org.jtb.modelview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import static org.jtb.modelview.Vertex.*;

public class ModelViewActivity extends Activity implements OnClickListener,
		View.OnTouchListener {

	static final int RESULT_INIT = 0;
	static final int RESULT_NONE = 1;

	static final int LOADING_DIALOG = 0;
	static final int LOAD_ERROR_DIALOG = 1;

	private static final int TOUCHMODE_DRAG = 0;
	private static final int TOUCHMODE_ZOOM = 1;
	private static final int TOUCHMODE_NONE = -1;

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
				if (loadingDialog.isShowing()) {
					loadingDialog.hide();
				}
				break;
			case HIDE_LOAD_ERROR_WHAT:
				if (loadErrorDialog.isShowing()) {
					loadErrorDialog.hide();
				}
				break;
			case PREPARE_SURFACE_WHAT:
				prepareSurface();
				break;
			}
		}
	};

	/*
	 * @Override public void onBackPressed() { super.onBackPressed(); finish();
	 * }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private Vertex lastRotate = new Vertex();
	private MeshRenderer renderer;
	private GLSurfaceView surfaceView;
	private ModelLoadException loadException = null;
	private BrowseElement browseElement;
	private OptionHandler optionHandler;
	private int touchMode = TOUCHMODE_DRAG;
	private float oldDist = 1f;

	private static class ModelViewGestureDetector extends
			SimpleOnGestureListener {
		private ModelViewActivity activity;

		ModelViewGestureDetector(ModelViewActivity activity) {
			this.activity = activity;
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

		browseElement = savedInstanceState != null ? (BrowseElement) savedInstanceState
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
		gestureDetector = new GestureDetector(
				new ModelViewGestureDetector(this));
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

		optionHandler = new OptionHandler(this);
		init();
	}

	private void init() {
		showDialog(LOADING_DIALOG);
		new Thread(new Runnable() {
			public void run() {
				try {
					renderer = new MeshRenderer(ModelViewActivity.this,
							browseElement);
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

		surfaceView.setRenderer(renderer);
		surfaceView.setSoundEffectsEnabled(false);
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

	public boolean onTouch(View view, MotionEvent rawEvent) {
		WrapMotionEvent event = WrapMotionEvent.wrap(rawEvent);

		if (gestureDetector.onTouchEvent(rawEvent)) {
			return true;
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

			renderer.mesh.dxSpeed = 0.0f;
			renderer.mesh.dySpeed = 0.0f;
			lastRotate.vertex[X] = event.getX();
			lastRotate.vertex[Y] = event.getY();

			touchMode = TOUCHMODE_DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				touchMode = TOUCHMODE_ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			touchMode = TOUCHMODE_NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			surfaceView.requestRender();
			if (touchMode == TOUCHMODE_DRAG) {
				renderer.mesh.ry += event.getX() - lastRotate.vertex[X];
				renderer.mesh.rx += event.getY() - lastRotate.vertex[Y];
				lastRotate.vertex[X] = event.getX();
				lastRotate.vertex[Y] = event.getY();
			} else if (touchMode == TOUCHMODE_ZOOM) {
	            float newDist = spacing(event);
	            if (newDist > 10f) {
	               renderer.mesh.scale *= newDist / oldDist;
	            }
	            oldDist = newDist;
			}

			break;
		}

		return super.onTouchEvent(rawEvent);
	}

	private float spacing(WrapMotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
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
				loadingDialog.setIndeterminate(true);
				loadingDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								finish();
							}
						});
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.modelview_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return optionHandler.handle(item.getItemId());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		if (requestCode == OptionHandler.PREFERENCES_REQUEST) {
			switch (resultCode) {
			case RESULT_INIT:
				init();
				break;
			default:
				break;
			}
		}
	}
}
