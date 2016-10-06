package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer.LifeCycleEvent;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UIActivity extends AppCompatActivity {
	
	public interface Exchange {
		String Page = "page";
	}
	
	protected ViewGroup root;
	
	@Override
	protected void onCreate (Bundle state) {
		super.onCreate (state);
		
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
		RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams (
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		);
		
		mainLayout.setId (UIApplication.newId ());

		mainLayout.setLayoutParams (layoutparams);
		
		setContentView (mainLayout);
		
		// remember the root view / layout
		root = mainLayout;

		spec.renderer ().render (page, this);

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
	    
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText (this, "turn to landscape", Toast.LENGTH_SHORT).show ();
	    } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText (this, "portrait", Toast.LENGTH_SHORT).show ();
	    }
	}
	
	public ApplicationSpec getSpec () {
		return ((UIApplication) getApplication ()).getSpec ();
	}
	
	public ViewGroup root () {
		return root;
	}
	
	public View component (String layerId, String componentId) {
		return root.findViewWithTag (layerId + Lang.DOT + componentId);
	}

	public View layer (String layerId) {
		return root.findViewWithTag (layerId);
	}
	
	public Location getLocation () {
		return null;
	}

}
