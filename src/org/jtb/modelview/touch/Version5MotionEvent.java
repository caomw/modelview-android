package org.jtb.modelview.touch;


import android.view.MotionEvent;

class Version5MotionEvent extends VersionedMotionEvent {
	Version5MotionEvent(MotionEvent event) {
		super(event);
	}

	@Override
	public float getX(int pointerIndex) {
		return event.getX(pointerIndex);
	}

	@Override
	public float getY(int pointerIndex) {
		return event.getY(pointerIndex);
	}

}
