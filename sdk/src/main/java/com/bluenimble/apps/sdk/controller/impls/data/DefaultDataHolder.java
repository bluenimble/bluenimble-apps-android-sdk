package com.bluenimble.apps.sdk.controller.impls.data;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.StreamSource;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;

import java.util.HashMap;
import java.util.Map;

public class DefaultDataHolder implements DataHolder  {

	private static final long serialVersionUID = 713158374746493539L;
	
	private Exception 	exception;
	private JsonObject 	data;

	private Map<String, StreamSource> streams;

	public DefaultDataHolder () {
		set (DataHolder.Namespace.View, new JsonObject ());
	}
	
	@Override
	public Exception exception () {
		return exception;
	}

	@Override
	public Object get (String namespace, String... property) {
		if (property == null || property.length == 0) {
			return Json.getObject (data, namespace);
		}
		return Json.find (Json.getObject (data, namespace), property);
	}

	@Override
	public DataHolder set (String namespace, Object value, String... property) {
		if (this.data == null) {
			this.data = new JsonObject ();
		}
		if (property == null || property.length == 0) {
			if (value == null) {
				this.data.remove (namespace);
			} else {
				this.data.set (namespace, value);
			}
			return this;
		}
		JsonObject nsData = Json.getObject (data, namespace);
		if (nsData == null) {
			nsData = new JsonObject ();
			data.set (namespace, nsData);
		}
		Json.set (nsData, value, property);
		return this;
	}

	@Override
	public void exception (Exception exception) {
		this.exception = exception;
		JsonObject error = Json.getObject (data, Namespace.Error);
		if (error == null) {
			error = new JsonObject ();
			set (Namespace.Error, error);
		}
		error.set (Message, exception.getMessage ());
	}

	@Override
	public Object resolve (String ns, String... name) {
		if (Lang.isNullOrEmpty (ns)) {
			ns = Namespace.View;
		}
		
		JsonObject section = Json.getObject (data, ns);

		if (name != null && name.length == 1 && Lang.STAR.equals (name [0])) {
			return section;
		}

		if (section == null) {
			return null;
		}

		Object v = Json.find (section, name);
		if (v == null) {
			return null;
		}
		return v;
	}

	@Override
	public Object valueOf (ApplicationSpec application, BindingSpec bindingSpec) {
		String source 		= bindingSpec.source ();
		String [] property 	= bindingSpec.property ();
		application.logger ().debug (DefaultDataHolder.class.getSimpleName (), "Source=" + source + ", property=" + Lang.join (property, Lang.DOT));
		if (source == null || Namespace.Static.equals (source)) {
			return application.i18nProvider ().get (property, application, this);
		}
		return get (bindingSpec.source (), property);
	}

	@Override
	public void stream (StreamSource stream) {
		if (streams == null) {
			streams = new HashMap<String, StreamSource> ();
		}
		streams.put (stream.id (), stream);
	}

	@Override
	public StreamSource stream (String name) {
		return streams.get (name);
	}

	@Override
	public String toString () {
		if (data == null) {
			return null;
		}
		return data.toString (2);
	}

	@Override
	public void destroy () {
		if (data == null && streams == null) {
			return;
		}
		// clear data
		if (data != null) {
			data.clear ();
			data = null;
		}
		// close/clear streams
		if (streams != null && !streams.isEmpty ()) {
			for (StreamSource ss : streams.values ()) {
				ss.close ();
			}
		}

		if (streams != null) {
			streams.clear ();
			streams = null;
		}
	}

	@Override
	public DataHolder clone () {
		DefaultDataHolder dh = new DefaultDataHolder ();
		dh.data = data == null ? null : data.duplicate ();
		dh.streams = streams;
		dh.exception = exception;
		return dh;
	}

}
