package com.bluenimble.apps.sdk.ui.components.impls.tabs;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.button.ButtonFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnPressListenerImpl;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TabsFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;

	private static final String Id = "tabs";

	private static final String DefaultText = "Tab ";

	interface Custom {
		String Template = "template";
	}

	public TabsFactory () {
		// register events
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {

		String [] templates = Lang.split ((String)spec.get (Custom.Template), Lang.SPACE, true);

		LayerSpec [] layers = new LayerSpec [templates.length];
		for (int i = 0; i < templates.length; i++) {
			layers [i] = SpecHelper.template (activity.getSpec (), String.valueOf (templates [i]));
		}

		RelativeLayout layout = new RelativeLayout (activity);

		// create the ViewPager
		ViewPagerAdapter adapter =  new ViewPagerAdapter (activity, layers);

		ViewPager pager = new ViewPager (activity);
		pager.setId (UIApplication.newId ());

		pager.setAdapter (adapter);

		// Assiging the Sliding Tab Layout View
		SlidingTabLayout tabs = new SlidingTabLayout (activity);
		tabs.setId (UIApplication.newId ());

		tabs.setDistributeEvenly (true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer (new SlidingTabLayout.TabColorizer () {

			@Override
			public int getIndicatorColor (int position) {
				return 0;  // style colors
			}

		});

		// add tabs to layout
		layout.addView (tabs);

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager (pager);

		// add "bellow" layout rule
		RelativeLayout.LayoutParams pagerParams = new RelativeLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		pagerParams.addRule (RelativeLayout.BELOW, tabs.getId ());

		// add pager to layout
		layout.addView (pager, pagerParams);

		return applyStyle (group, layout, spec, dh);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec application, ComponentSpec spec, DataHolder dh) {
		if (view == null || !(view instanceof RelativeLayout)) {
			// TODO: log
			return;
		}

		RelativeLayout layout = (RelativeLayout)view;

		BindingSpec bindingSpec = spec.binding (binding);

		String [] property = bindingSpec.property ();

		switch (binding) {
			case Set:
				if (dh == null) {
					return;
				}

				JsonArray titles = (JsonArray)dh.valueOf (application, bindingSpec);
				if (titles == null || titles.isEmpty ()) {
					return;
				}

				SlidingTabLayout tabs = (SlidingTabLayout)layout.getChildAt (0);

				for (int i = 0; i < titles.count (); i++) {
					String title = (i >= tabs.getTabsCount ()) ? DefaultText + i : String.valueOf (titles.get (i));
					tabs.getTab (i).setText (title);
				}

				break;
			default:
				break;
		}
	}

}
