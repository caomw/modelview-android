package org.jtb.modelview;

import android.view.MotionEvent;

abstract class VersionedMotionEvent {
	protected MotionEvent event;

	VersionedMotionEvent(MotionEvent event) {
		this.event = event;
	}

	static VersionedMotionEvent newInstance(MotionEvent event) {
		int v = android.os.Build.VERSION.SDK_INT;
		if (v < 5) {
			return new Version1MotionEvent(event);
		}
		return new Version5MotionEvent(event);
	}

	int getAction() {
		return event.getAction();
	}

	float getX() {
		return event.getX();
	}

	abstract float getX(int pointerIndex);

	public float getY() {
		return event.getY();
	}

	abstract float getY(int pointerIndex);
}
