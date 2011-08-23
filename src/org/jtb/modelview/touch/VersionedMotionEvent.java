package org.jtb.modelview.touch;

import android.view.MotionEvent;

public abstract class VersionedMotionEvent {
	protected MotionEvent event;

	public VersionedMotionEvent(MotionEvent event) {
		this.event = event;
	}

	public static VersionedMotionEvent newInstance(MotionEvent event) {
		int v = android.os.Build.VERSION.SDK_INT;
		if (v < 5) {
			return new Version1MotionEvent(event);
		}
		return new Version5MotionEvent(event);
	}

	public int getAction() {
		return event.getAction();
	}

	public float getX() {
		return event.getX();
	}

	public abstract float getX(int pointerIndex);

	public float getY() {
		return event.getY();
	}

	public abstract float getY(int pointerIndex);
}
