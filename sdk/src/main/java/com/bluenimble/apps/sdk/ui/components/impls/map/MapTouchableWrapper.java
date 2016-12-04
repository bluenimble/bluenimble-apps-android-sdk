package com.bluenimble.apps.sdk.ui.components.impls.map;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MapTouchableWrapper extends FrameLayout {

	private final static long SCROLL_TIME = 800L; // 800 Milliseconds, but you can adjust that to your liking

	private long 			lastTouched;
	private UserInteraction interaction;

	public MapTouchableWrapper (Context context) {
		super (context);
		// Force the host activity to implement the
		// UpdateMapAfterUserInterection Interface
		//try {
			//updateMapAfterUserInteraction = (FuelFinderActivity) context;
		//} catch (ClassCastException e) {
		//	throw new ClassCastException (context.toString () + " must implement UpdateMapAfterUserInterection");
		//}
	}

	@Override
	public boolean dispatchTouchEvent (MotionEvent ev) {
		int action  = ev.getAction ();
		int fingers = ev.getPointerCount ();

		if (fingers == 1 && action == MotionEvent.ACTION_DOWN) {
			lastTouched = SystemClock.uptimeMillis ();
		} else if (fingers == 1 && action == MotionEvent.ACTION_UP) {
			long now = SystemClock.uptimeMillis ();
			if (now - lastTouched > SCROLL_TIME) {
				// Update the map
				if (interaction != null) {
					interaction.onMapUpdated ();
				}
			}
		}

		return super.dispatchTouchEvent (ev);
	}

	public void setOnMapUpdated (UserInteraction interaction) {
		this.interaction = interaction;
	}

	// Map Activity must implement this interface
	public interface UserInteraction {
		void onMapUpdated ();
	}
}