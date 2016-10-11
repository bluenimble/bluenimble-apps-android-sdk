package com.bluenimble.apps.sdk.ui.components.impls.image;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnPressListenerImpl;
import com.bluenimble.apps.sdk.utils.AppResources;
import com.squareup.picasso.Picasso;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

public class ImageFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "image";

	private static final String Assets = "file:///android_asset/";
	
	interface Protocol {
		String File 	= "file://";
		String Http 	= "http://";
		String Https 	= "https://";
	}

	public ImageFactory () {
		supportEvent (EventListener.Event.press);
		supportEvent (EventListener.Event.longPress);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		return applyStyle (group, new ImageView (activity), spec);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec application, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof ImageView)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		ImageView image = (ImageView)view;
		
		switch (binding) {
			case Set:
				if (dh == null) {
					// default image
					return;
				}
				Object value = dh.valueOf (application, bindingSpec);
				if (value == null) {
					return;
				}
				String url = value.toString ().trim ();
				
				if (url.startsWith (Protocol.File) || url.startsWith (Protocol.Http) || url.startsWith (Protocol.Https)) {
					Picasso.with (view.getContext ()).load (url).into (image);
				} else {
					if (AppResources.exists (url)) {
						Picasso.with (view.getContext ()).load (AppResources.id (url)).into (image);
					} else {
						if (application.isDiskBased ()) {
							Picasso.with (view.getContext ()).load (
								new File (UIApplication.Resources.Apps + Lang.SLASH + application.id () + Lang.SLASH + UIApplication.Resources.Themes + Lang.SLASH + url)
							).into (image);
						} else {
							Picasso.with (view.getContext ()).load (Assets + application.id () + Lang.SLASH + UIApplication.Resources.Themes + Lang.SLASH + url).into (image);
						}
 					}
				}
				break;
			case Get:
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof ImageView)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}
		
		ImageView image = (ImageView)view;
		
		if (!image.isClickable ()) {
			image.setClickable (true);
		}
		
		EventListener.Event event = EventListener.Event.valueOf (eventName);
		switch (event) {
			case press:
				image.setOnClickListener (new OnPressListenerImpl (event, eventSpec));
				break;
			case longPress:
				image.setOnLongClickListener (new OnLongPressListenerImpl (event, eventSpec));
				break;
			default:
				break;
		}
		
	}

}
