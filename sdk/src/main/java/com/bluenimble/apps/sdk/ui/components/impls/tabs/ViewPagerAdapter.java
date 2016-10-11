package com.bluenimble.apps.sdk.ui.components.impls.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UILayer;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.LayerSpec;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

	protected LayerSpec [] 	layers;

	// Build a Constructor and assign the passed Values to appropriate values in the class
	public ViewPagerAdapter (UIActivity activity, LayerSpec [] layers) {
		super (activity.getSupportFragmentManager ());
		this.layers = layers;
	}

	//This method return the fragment for the every position in the View Pager
	@Override
	public Fragment getItem (int position) {
		check (position);
		return UILayer.create (layers [position], null);
	}

	// This method return the titles for the Tabs in the Tab Strip
	@Override
	public CharSequence getPageTitle (int position) {
		check (position);
		return Lang.BLANK;
	}

	// This method return the Number of tabs for the tabs Strip
	@Override
	public int getCount () {
		if (layers == null || layers.length == 0) {
			throw new IllegalArgumentException ("Tabs component has no content");
		}
		return layers.length;
	}

	private void check (int position) {
		if (layers == null || layers.length == 0) {
			throw new IllegalArgumentException ("Tabs component has no content");
		}

		if (position >= layers.length) {
			throw new IllegalArgumentException ("Requesting tab at posiution " + position + " while tabs count is " + layers.length);
		}
	}
}
