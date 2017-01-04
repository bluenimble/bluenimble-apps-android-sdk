package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.controller.ActionInstance;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.renderer.ViewResolver;
import com.bluenimble.apps.sdk.ui.renderer.Renderer.LifeCycleEvent;
import com.bluenimble.apps.sdk.utils.EffectsHelper;
import com.bluenimble.apps.sdk.utils.SecurityHelper;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UIActivity extends AppCompatActivity implements ViewResolver {
	
	public interface Exchange {
		String Page = "page";
	}
	
	protected ViewGroup root;
	protected boolean 	resumed;

	protected Map<Integer, ActionInstance> secureActions = new ConcurrentHashMap<Integer, ActionInstance> (5);
	
	@Override
	protected void onCreate (Bundle state) {
		super.onCreate (state);

		// remove bar
		requestWindowFeature (Window.FEATURE_NO_TITLE);

		// get app spec
		ApplicationSpec spec = getSpec ();

		if (spec.security () != null) {
			PageSpec accessDeniedPage = spec.security ().accessDenied ();
			if (accessDeniedPage != null) {
				int requestCode = SecurityHelper.askForAllPermissions (this);
				if (requestCode != SecurityHelper.NoRequestCode) {
					return;
				}
			}
		}

		// find the startup page and render it
		render ();

	}

	private void render () {
		renderPage (page ());
	}

	private void renderPage (PageSpec page) {
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

		getSpec ().renderer ().render (page, this, null);
	}

	private PageSpec page () {
		PageSpec page = null;

		String pageId = null;

		// is this activity called using an intent, get the pageId to open
		Intent intent = getIntent ();
		if (intent != null) {
			pageId = intent.getStringExtra (Exchange.Page);
		}

		// get the page spec
		if (!Lang.isNullOrEmpty (pageId)) {
			page = getSpec ().page (pageId);
		}

		// get the main page spec
		if (page == null) {
			page = getSpec ().index ();
		}

		return page;
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
			PageSpec page = getSpec ().renderer ().current ();
			JsonObject eventSpec = page.event (LifeCycleEvent.rotate.name ());
			if (eventSpec != null) {
				getSpec ().controller ()
						.process (DefaultActionInstance.create (LifeCycleEvent.rotate.name (), eventSpec, getSpec (), null, root), this, false);
			}
		}
	}

	@Override
	public View findView (String id) {
		return root.findViewWithTag (id);
	}

	public View ancestor (View view, Class clazz, boolean levelDown) {
		if (view == null) {
			return null;
		}
		View vLevelDown;
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

	public void registerActionInstance (int requestCode, ActionInstance actionInstance) {
		secureActions.put (requestCode, actionInstance);
	}

	@Override
	public void onRequestPermissionsResult (int requestCode, @NonNull String [] anps, @NonNull int [] grantResults) {
		super.onRequestPermissionsResult (requestCode, anps, grantResults);

		if (requestCode == SecurityHelper.StartupRequestCode) {
			if (grantResults.length < anps.length) {
				renderPage (getSpec ().security ().accessDenied ());
			} else {
				render ();
			}
			return;
		}

		ActionInstance actionInstance = secureActions.get (requestCode);
		if (actionInstance == null) {
			return;
		}

		// remove acionInstance from the queue
		secureActions.remove (requestCode);

		if (grantResults.length < anps.length) {
			EffectsHelper.applyEffects (actionInstance, Spec.Action.OnAccessDenied, this, getSpec ().renderer ().current ());
			return;
		}
		getSpec ().controller ().process (actionInstance, this, false);
	}

	@Override
	public void onBackPressed () {
		JsonObject eventSpec = page ().event (EventListener.Event.back.name ());
		if (eventSpec == null) {
			super.onBackPressed ();
			return;
		}

		getSpec ().controller ().process (
			DefaultActionInstance.create (EventListener.Event.back.name (), eventSpec, getSpec (), null, root ()),
			this,
			false
		);
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