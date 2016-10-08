package com.bluenimble.apps.sdk.ui.renderer.impls;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.application.UILayer;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.renderer.Renderer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DefaultRenderer implements Renderer {

	private static final long serialVersionUID = -2346102131987611711L;
	
	public enum LifeCycleEvent {
		create,
		destroy,
		rotate,
		crash,
		warn,
		error
	}
	
	private PageSpec page;

	@Override
	public void render (PageSpec page, UIActivity activity) {

		// fragment manager transaction need to clear current layers and new news
		FragmentManager manager = activity.getSupportFragmentManager ();
		FragmentTransaction transaction = manager.beginTransaction ();

		// remove all content from the activity main layout
		clear (manager, transaction);
		
		// read layers, create layouts, create fragments, add to activity
			// each layer is a fragment having using a linear layout
		
		this.page = page;
		
		Iterator<String> layers = page.layers ();
		while (layers.hasNext ()) {
			String lyrId = layers.next ();
			LayerSpec layer = page.layer (lyrId);
			if (!layer.isRendered ()) {
				continue;
			}
			Fragment f = UILayer.create (page.layer (lyrId), null);
			transaction.add (activity.root ().getId (), f, lyrId);
		}
		
		transaction.commit ();
		
		if (page.style () != null) {
			page.style ().apply (page, activity.root (), null);
		}
		
	}

	@Override
	public View render (ApplicationSpec application, LayerSpec layer, DataHolder dh, ViewGroup container, UIActivity activity) {
		
		LinearLayout layout = new LinearLayout (activity);
		layout.setId (UIApplication.newId ());
		
		// Layer layout should be vertical
		layout.setOrientation (LinearLayout.VERTICAL);
		
		if (layer == null) {
			return layout;
		}
		
		if (layer.style () != null) {
			layer.style ().apply (layer, layout, container);
		}
		
		layout.setTag (layer.id ());
		
		if (layer.count () == 0) {
			return layout;
		}
		
		ComponentsRegistry registry = application.componentsRegistry ();
		
		// initial RelativeLayout
		RelativeLayout relativeLayout = new RelativeLayout (activity);
		RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams (
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		);
		relativeLayout.setLayoutParams (layoutparams);
		layout.addView (relativeLayout);

		// add components
		for (int i = 0; i < layer.count (); i++) {
			ComponentSpec spec = layer.component (i);
			
			String type = spec.type ();
			if (Lang.isNullOrEmpty (type)) {
				type = ComponentsRegistry.Default.Text;
			}
			
			Log.d (DefaultRenderer.class.getSimpleName (), "Render Component " + Lang.ARRAY_OPEN + type + Lang.SLASH + spec.id () + Lang.ARRAY_CLOSE);

			// back to new line
			if (type.equals (ComponentsRegistry.Default.Break)) {
				Log.d (DefaultRenderer.class.getSimpleName (), "\tCreate A Break - RelativeLayout");
				relativeLayout = new RelativeLayout (activity); 
				relativeLayout.setLayoutParams (layoutparams);
				layout.addView (relativeLayout);
			}
			
			// resolve the component factory
			ComponentFactory factory = registry.lookup (type.toLowerCase ());
			if (factory == null) {
				// TODO: log
				factory = registry.lookup (ComponentsRegistry.Default.Text);
			}
			
			// create the component view
			View view = factory.create (activity, relativeLayout, layer, spec);
			
			// set the id of the component as the tag of the view "layerId.componentId"
			if (view != null && spec.id () != null) {
				view.setTag (layer.id () + Lang.DOT + spec.id ());
			}
			
			// add component to the layer 
			if (view != null) {
				relativeLayout.addView (view);
			}
			
			Log.d (DefaultRenderer.class.getSimpleName (), "Component Added to Layout " + Lang.ARRAY_OPEN + type + Lang.SLASH + spec.id () + Lang.SPACE + (view != null ? view.getId () : "NullView") + Lang.ARRAY_CLOSE);

			// attach component events
			addEvents  (application, spec, factory, view, activity);
			
			// back to new line
			if (type.equals (ComponentsRegistry.Default.Break)) {
				Log.d (DefaultRenderer.class.getSimpleName (), "\tCreate A Break - RelativeLayout");
				relativeLayout = new RelativeLayout (activity); 
				relativeLayout.setLayoutParams (layoutparams);
				layout.addView (relativeLayout);
			}
		}
		
		JsonObject eventSpec = layer.event (LifeCycleEvent.create.name ());
		if (eventSpec != null) {
			ActionProcessor.process (LifeCycleEvent.create.name (), eventSpec, activity, layout, dh);
		}
		
		return layout;
		
	}
	
	private void addEvents (ApplicationSpec application, ComponentSpec spec, ComponentFactory factory, View view, UIActivity activity) {
		Iterator<String> events = spec.events ();
		if (events == null) {
			return;
		}
		while (events.hasNext ()) {
			String eventName = events.next ();
			factory.addEvent (activity, view, application, spec, eventName, spec.event (eventName));
		}
	}

	@Override
	public void clear (ApplicationSpec application, UIActivity activity) {
		
		// run page start event
		JsonObject eventSpec = page.event (LifeCycleEvent.destroy.name ());
		if (eventSpec != null) {
			ActionProcessor.process (LifeCycleEvent.destroy.name (), eventSpec, activity, activity.root (), null);
		}
		
		//TODO below, was causing crash
		// remove all content from the activity main layout
		/*FragmentManager fragmentManager = activity.getFragmentManager ();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
		
		clear (fragmentManager, fragmentTransaction);
		
		fragmentTransaction.commit ();*/

	}

	@Override
	public PageSpec current () {
		return page;
	}
	
	private void clear (FragmentManager fragmentManager, FragmentTransaction fragmentTransaction) {
		// remove all content from the activity main layout
		for (int i = 0; i < fragmentManager.getBackStackEntryCount (); i++) {
			String lyrId = fragmentManager.getBackStackEntryAt (i).getName ();
			if (lyrId == null) {
				continue;
			}
			
			// check if this layer is persistent (will stay in the activity forever)
			LayerSpec layer = page.layer (lyrId);
			if (layer.isGlobal ()) {
				continue;
			}

			Fragment f = fragmentManager.findFragmentByTag (lyrId);
			
			JsonObject eventSpec = layer.event (LifeCycleEvent.destroy.name ());
			if (eventSpec != null) {
				ActionProcessor.process (LifeCycleEvent.destroy.name (), eventSpec, (UIActivity)f.getView ().getContext (), f.getView (), null);
			}

			fragmentTransaction.remove (f);
		}
		
		// reset the page
		page = null;
		
	}

}
