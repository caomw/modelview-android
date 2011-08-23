package org.jtb.modelview.touch;


import android.view.MotionEvent;

class Version1MotionEvent extends VersionedMotionEvent {
	Version1MotionEvent(MotionEvent event) {
		super(event);
	}

	@Override
	public 	float getX(int pointerIndex) {
		return getX();
	}

	@Override
	public float getY(int pointerIndex) {
		return getY();
	}

}
