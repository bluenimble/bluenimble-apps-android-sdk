package com.bluenimble.apps.sdk.ui.components.impls.list;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.utils.BindingHelper;
import com.bluenimble.apps.sdk.ui.utils.SpecHelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 Multi Template spec

 "template": {
 	"property": "a.b.c",
 	"values": {
 		"admin": "tpl1",
 		"developer": "tpl2",
 		"tester": "tpl3"
 	}
 }

 Single Template spec

 "template": "aPage.tpl1"

 **/
public class DefaultListAdapter extends RecyclerView.Adapter<DefaultListAdapter.GenericViewHolder> {

	private static final String LogTag = DefaultListAdapter.class.getSimpleName ();

	private static final String Default = "default";

	interface Spec {
		String Property = "property";
		String Values 	= "values";
	}

	protected JsonArray 				records;

	protected DataHolder 				one;
	protected String					recordNs;

	protected UIActivity 				activity;

	protected Map<String, LayerSpec>	templates 			= new HashMap<String, LayerSpec> ();
	protected Map<String, Integer>		viewTypesById 		= new HashMap<String, Integer> ();
	protected Map<Integer, String>		viewTypesByIndex 	= new HashMap<Integer, String> ();

	protected String []					property;
	
	public DefaultListAdapter (UIActivity activity, ComponentSpec component, String recordNs) {
		this.activity 		= activity;
		this.recordNs		= recordNs;
		this.one 			= new DefaultDataHolder ();

		// initialize templates
		Object templateSpec = component.get (ListFactory.Custom.Template);
		if (templateSpec instanceof String) {
			templates.put (Default, SpecHelper.template (activity.getSpec (), (String)templateSpec));
			viewTypesById.put (Default, 0); viewTypesByIndex.put (0, Default);
			return;
		}
		if (!(templateSpec instanceof JsonObject)) {
			return;
		}

		JsonObject oTemplate = (JsonObject)templateSpec;
		if (Json.isNullOrEmpty (oTemplate)) {
			return;
		}

		String sProperty = Json.getString (oTemplate, Spec.Property);
		if (!Lang.isNullOrEmpty (sProperty)) {
			property = Lang.split (sProperty, Lang.DOT, true);
		}

		int counter = 0;

		JsonObject oValues = Json.getObject (oTemplate, Spec.Values);

		Iterator<String> values = oValues.keys ();
		while (values.hasNext ()) {
			String value = values.next ();
			String vTemplate = String.valueOf (oValues.get (value));
			if (Lang.isNullOrEmpty (vTemplate)) {
				continue;
			}
			templates.put (value, SpecHelper.template (activity.getSpec (), vTemplate));
			viewTypesById.put (value, counter);
			viewTypesByIndex.put (counter, value);
			counter++;
		}

	}

	void load (JsonArray records) {
		this.records = records;
	}

	JsonArray getRecords () {
		return records;
	}

	@Override
	public int getItemCount () {
		if (records == null) {
			return 0;
		}
		return records.count ();
	}

	@Override
	public int getItemViewType (int position) {
		if (templates.size () == 1) {
			return 0;
		}

		JsonObject record = (JsonObject) records.get (position);

		Object value = Json.find (record, property);

		Integer viewType = viewTypesById.get (value);
		if (viewType == null) {
			return 0;
		}

		return viewType;
	}

	@Override
	public long getItemId (int position) {
		return position;
	}

	@Override
	public GenericViewHolder onCreateViewHolder (ViewGroup group, int itemViewType) {

		String template = viewTypesByIndex.get (itemViewType);

		return new GenericViewHolder (
			activity.getSpec ().renderer ().render (activity.getSpec (), templates.get (template), null /* DataHolder in here */, group, activity),
			group,
			template
		);
	}

	@Override
	public void onBindViewHolder (GenericViewHolder viewHolder, int position) {
		BindingHelper.bindLayer (
			LogTag,
			activity.getSpec (),
			templates.get (viewHolder.template), viewHolder.itemView, one.set (recordNs, (JsonObject) records.get (position)),
			true
		);
	}

	public class GenericViewHolder extends RecyclerView.ViewHolder {
		String 		template;
		ViewGroup 	group;
		public GenericViewHolder (View view, ViewGroup group, String template) {
            super (view);
			view.setTag (null);
			this.group = group;
			this.template = template;
        }
    }
}