package org.jtb.modelview;

import android.view.MotionEvent;

class Version1MotionEvent extends VersionedMotionEvent {
	Version1MotionEvent(MotionEvent event) {
		super(event);
	}

	@Override
	float getX(int pointerIndex) {
		return getX();
	}

	@Override
	float getY(int pointerIndex) {
		return getY();
	}

}
