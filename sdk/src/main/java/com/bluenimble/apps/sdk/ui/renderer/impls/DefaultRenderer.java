package com.bluenimble.apps.sdk.ui.renderer.impls;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.application.UILayer;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.spec.StyleSpec;
import com.bluenimble.apps.sdk.ui.components.ComponentFactory;
import com.bluenimble.apps.sdk.ui.components.ComponentsRegistry;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnPressListenerImpl;
import com.bluenimble.apps.sdk.ui.renderer.Renderer;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DefaultRenderer implements Renderer {

	private static final long serialVersionUID = -2346102131987611711L;

	protected static final Set<String> LayerEvents = new HashSet<String>();
	static {
		LayerEvents.add (EventListener.Event.press.name ());
		LayerEvents.add (EventListener.Event.longPress.name ());
	}

	private PageSpec page;

	@Override
	public void render (PageSpec page, UIActivity activity, DataHolder dh) {

		// fragment manager transaction need to clear current layers and new news
		FragmentManager manager = activity.getSupportFragmentManager ();
		FragmentTransaction transaction = manager.beginTransaction ();

		// remove all content from the activity main layout
		clear (activity, page, manager, transaction);

		// read layers, create layouts, create fragments, add to activity
			// each layer is a fragment / linear layout
		
		this.page = page;

		Iterator<String> layers = page.layers ();
		while (layers.hasNext ()) {
			String lyrId = layers.next ();
			LayerSpec layer = page.layer (lyrId);

			if (!layer.isRendered ()) {
				continue;
			}

			Fragment f = UILayer.create (page.layer (lyrId), dh);
			transaction.add (activity.root ().getId (), f, lyrId);
		}
		
		transaction.commit ();

		if (page.style () != null) {
			page.style ().apply (page, activity.root (), null, dh);
		}
		
	}

	@Override
	public View render (ApplicationSpec application, LayerSpec layer, DataHolder dh, ViewGroup container, UIActivity activity) {

		LayerLayout layout = new LayerLayout (activity);
		layout.setId (UIApplication.newId ());
		
		// Layer layout should be vertical
		layout.setOrientation (LinearLayout.VERTICAL);
		
		if (layer == null) {
			return layout;
		}
		
		if (dh == null || dh.get (DataHolder.Internal.NoTag) == null) {
			layout.setTag (layer.id ());
		}

		if (layer.count () == 0) {
			return layout;
		}

		boolean scrollable = layer.isScrollable ();

		ViewGroup layerView = layout;

		ScrollView scroll = null;

		if (scrollable) {
			scroll = new ScrollView (activity);
			LinearLayout.LayoutParams layerLayoutParams = new LinearLayout.LayoutParams (
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
			);
			scroll.addView (layerView, layerLayoutParams);
			layerView = scroll;
		}

		StyleSpec layerStyle = layer.style ();

		if (layerStyle != null) {
			layerStyle.apply (layer, layerView, container, dh);
		}

		ComponentsRegistry registry = application.componentsRegistry ();
		
		// line RelativeLayout
		RelativeLayout relativeLayout = new RelativeLayout (activity);

		// line Layoiut Params
		RelativeLayout.LayoutParams lineLayoutParams = layerLineLayout (application, layer, layerStyle, layerView.getLayoutParams ().height, dh);

		relativeLayout.setLayoutParams (lineLayoutParams);
		layout.addView (relativeLayout);

		// add components
		for (int i = 0; i < layer.count (); i++) {
			ComponentSpec spec = layer.component (i);
			
			String type = spec.type ();
			if (Lang.isNullOrEmpty (type)) {
				type = ComponentsRegistry.Default.Text;
			}

			application.logger ().debug (DefaultRenderer.class.getSimpleName (), "Render Component " + Lang.ARRAY_OPEN + type + Lang.SLASH + spec.id () + Lang.ARRAY_CLOSE);

			// back to new line
			if (type.equals (ComponentsRegistry.Default.Break)) {
				application.logger ().debug (DefaultRenderer.class.getSimpleName (), "\tCreate A Break - RelativeLayout");
				lineLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
				relativeLayout = new RelativeLayout (activity); 
				relativeLayout.setLayoutParams (lineLayoutParams);
				layout.addView (relativeLayout);
			}
			
			// resolve the component factory
			ComponentFactory factory = registry.lookup (type.toLowerCase ());
			if (factory == null) {
				// TODO: log
				factory = registry.lookup (ComponentsRegistry.Default.Text);
			}
			
			// create the component view
			View view = factory.create (activity, relativeLayout, layer, spec, dh);
			
			// set the id of the component as the tag of the view "componentId"
			if (view != null && spec.id () != null) {
				view.setTag (spec.id ());
			}
			
			// add component to the layer 
			if (view != null) {
				relativeLayout.addView (view);
			}

			application.logger ().debug (
				DefaultRenderer.class.getSimpleName (),
				"Component Added to Layout " + Lang.ARRAY_OPEN + type + Lang.SLASH + spec.id () + Lang.SPACE + (view != null ? view.getId () : "NullView") +
				Lang.SPACE + "tag: " + view.getTag () + Lang.ARRAY_CLOSE
			);

			// attach component events
			addComponentEvents  (application, spec, factory, view, activity);
			
			// back to new line
			if (type.equals (ComponentsRegistry.Default.Break)) {
				Log.d (DefaultRenderer.class.getSimpleName (), "\tCreate A Break - RelativeLayout");
				lineLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
				relativeLayout = new RelativeLayout (activity);
				relativeLayout.setLayoutParams (lineLayoutParams);
				layout.addView (relativeLayout);
			}
		}

		// add Layer UI events
		addLayerEvents (layer, layout);
		
		return layerView;
		
	}

	private void addLayerEvents (LayerSpec layer, View view) {
		Iterator<String> events = layer.events ();
		if (events == null) {
			return;
		}
		while (events.hasNext ()) {
			String eventName = events.next ();
			if (!LayerEvents.contains (eventName)) {
				continue;
			}

			EventListener.Event event = EventListener.Event.valueOf (eventName);
			switch (event) {
				case press:
					view.setOnClickListener (new OnPressListenerImpl (event, layer.event (eventName)));
					break;
				case longPress:
					view.setOnLongClickListener (new OnLongPressListenerImpl (event, layer.event (eventName)));
					break;
				default:
					break;
			}
		}
	}

	private void addComponentEvents (ApplicationSpec application, ComponentSpec spec, ComponentFactory factory, View view, UIActivity activity) {
		Iterator<String> events = spec.events ();
		if (events == null) {
			application.logger ().debug (DefaultRenderer.class.getSimpleName (), spec.id () + " Has NO Events!");
			return;
		}
		while (events.hasNext ()) {
			String eventName = events.next ();
			application.logger ().debug (DefaultRenderer.class.getSimpleName (), "Attach " + eventName + " Event to " + spec.id ());
			factory.addEvent (activity, view, application, spec, eventName, spec.event (eventName));
		}
	}

	@Override
	public void clear (ApplicationSpec application, UIActivity activity) {
		
		// run page start event
		JsonObject eventSpec = page.event (LifeCycleEvent.destroy.name ());
		if (eventSpec != null) {
			application.controller ().process (
				DefaultActionInstance.create (DefaultRenderer.LifeCycleEvent.destroy.name (), eventSpec, application, null, activity.root ()),
				activity,
				false
			);
		}

	}

	@Override
	public PageSpec current () {
		return page;
	}
	
	private void clear (UIActivity activity, PageSpec page, FragmentManager manager, FragmentTransaction transaction) {
		// remove all content from the activity main layout
		Iterator<String> layers = page.layers ();
		while (layers.hasNext ()) {
			String lyrId = layers.next ();
			LayerSpec layer = page.layer (lyrId);

			if (layer.isGlobal ()) {
				continue;
			}

			Fragment f = manager.findFragmentByTag (lyrId);
			if (f == null) {
				continue;
			}

			activity.getSpec ().logger ().debug (DefaultRenderer.class.getSimpleName (), "Destroy " + layer.id ());

			JsonObject eventSpec = layer.event (LifeCycleEvent.destroy.name ());
			if (eventSpec != null) {
				SpecHelper.application (f.getView ()).controller ().process (
					DefaultActionInstance.create (DefaultRenderer.LifeCycleEvent.destroy.name (), eventSpec, activity.getSpec (), null, f.getView ()),
					activity,
					false
				);
			}

			transaction.remove (f);
		}
		// reset the page
		page = null;
		
	}

	private RelativeLayout.LayoutParams layerLineLayout (ApplicationSpec application, LayerSpec layer, StyleSpec layerStyle, int layerHieght, DataHolder dh) {
		// Line Layout Params
		// depending on lineHeight style property
		// if all: means this layer will have only 1 line
		// if xPx: means that the height of each line will be xPx
		// if fit: this only applies if a layer has a fixed height,
			// if 2 breaks are declared in this layer and the height of this layer is 300,
			// then the line height in PX will be 100

		String sLineHeight = null;
		if (layerStyle != null) {
			sLineHeight = (String) Json.resolve (layerStyle.get (StyleSpec.LineHeight), application.expressionCompiler (), dh);
		}

		int lineHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
		if (!Lang.isNullOrEmpty (sLineHeight)) {
			sLineHeight = sLineHeight.trim ();

			StyleSpec.LineHeightValue lhv = null;

			try {
				lhv = StyleSpec.LineHeightValue.valueOf (sLineHeight);
			} catch (Exception ex) {
				// Ignore
			}
			if (lhv != null) {
				switch (lhv) {
					case all:
						lineHeight = ViewGroup.LayoutParams.MATCH_PARENT;
						break;
					case fit:
						// TODO: # of breaks in this layer....
						int lines = layer.lines ();
						if (layerHieght > 0 && lines > 0) {
							lineHeight = (int)(layerHieght / lines);
						}
						break;
				}
			} else {
				int iLineHeight = -1;
				try {
					iLineHeight = Integer.valueOf (sLineHeight);
				} catch (Exception ex) {
					// Ignore
				}
				if (iLineHeight > 0) {
					lineHeight = iLineHeight;
				}
			}
		}

		return new RelativeLayout.LayoutParams (
				ViewGroup.LayoutParams.MATCH_PARENT,
				lineHeight
		);
	}

}
