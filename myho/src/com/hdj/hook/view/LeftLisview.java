package com.hdj.hook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class LeftLisview extends ListView {

	public LeftLisview(Context context) {
		super(context);
		init();

	}

	public LeftLisview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();

	}

	public LeftLisview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	private void init() {
//		getParent().requestDisallowInterceptTouchEvent(true);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
		// return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			return true;

		default:
			break;
		}
		return true;
	}

}
