package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.renderer.ViewResolver;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer.LifeCycleEvent;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.RelativeLayout;

public class UIActivity extends AppCompatActivity implements ViewResolver {
	
	public interface Exchange {
		String Page = "page";
	}
	
	protected ViewGroup root;
	protected boolean 	resumed;
	
	@Override
	protected void onCreate (Bundle state) {
		super.onCreate (state);

		// remove bar
		requestWindowFeature (Window.FEATURE_NO_TITLE);

		//getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// get app spec
		ApplicationSpec spec = getSpec ();

		PageSpec page = null;
		
		String pageId = null;
		
		// is this activity called using an intent, get the pageId to open 
		Intent intent = getIntent ();
		if (intent != null) {
			pageId = intent.getStringExtra (Exchange.Page);
		}
		
		// get the page spec
		if (!Lang.isNullOrEmpty (pageId)) {
			page = spec.page (pageId);
		}
		
		// get the main page spec
		if (page == null) {
			page = spec.main ();
		}

		if (page == null) {
			return;
		}
		
		// create a relative layout and attach to the activity

		RelativeLayout mainLayout = new RelativeLayout (this);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams (
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		);
		
		mainLayout.setId (UIApplication.newId ());

		mainLayout.setLayoutParams (layoutParams);
		
		setContentView (mainLayout);
		
		// remember the root view / layout
		root = mainLayout;

		// remove action bar
		ActionBar actionBar = getSupportActionBar ();
		if (actionBar != null) {
			actionBar.hide ();
		}

		spec.renderer ().render (page, this);

	}

	@Override
	protected void onResume () {
		super.onResume ();

		if (!resumed) {
			PageSpec page = getSpec ().renderer ().current ();
			SpecHelper.fireCreateEvent (page, page.id (), this, root (), false, null);
			resumed = true;
		}

	}

	@Override
	public void onConfigurationChanged (Configuration config) {
		super.onConfigurationChanged (config);
		
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE || config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			PageSpec page = ((UIApplication)getApplication ()).getSpec ().renderer ().current ();
			JsonObject eventSpec = page.event (LifeCycleEvent.rotate.name ());
			if (eventSpec != null) {
				ActionProcessor.process (LifeCycleEvent.rotate.name (), eventSpec, this, root, null);
			}
		}
	}

	@Override
	public View layer (String layerId) {
		return root.findViewWithTag (layerId);
	}

	@Override
	public View component (String layerId, String componentId) {
		return root.findViewWithTag (layerId + Lang.DOT + componentId);
	}

	public View ancestor (View view, Class clazz, boolean levelDown) {
		if (view == null) {
			return null;
		}
		View vLevelDown = null;
		View current = view;
		while (true) {
			vLevelDown = current;
			current = (View)current.getParent ();
			if (root ().equals (current)) {
				return null;
			}
			if (current.getClass ().equals (clazz)) {
				if (levelDown) {
					return vLevelDown;
				}
				return current;
			}
		}
	}

	public ApplicationSpec getSpec () {
		return ((UIApplication) getApplication ()).getSpec ();
	}
	
	public ViewGroup root () {
		return root;
	}

	public Location getLocation () {
		return null;
	}

}
