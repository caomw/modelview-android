package org.jtb.modelview;

import android.view.MotionEvent;

class Version5MotionEvent extends VersionedMotionEvent {
	Version5MotionEvent(MotionEvent event) {
		super(event);
	}

	@Override
	float getX(int pointerIndex) {
		return event.getX(pointerIndex);
	}

	@Override
	float getY(int pointerIndex) {
		return event.getY(pointerIndex);
	}

}
