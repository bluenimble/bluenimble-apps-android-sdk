package com.bluenimble.apps.sdk.http.request.impls;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpHeader;
import com.bluenimble.apps.sdk.http.HttpHeaders;
import com.bluenimble.apps.sdk.http.HttpMethods;
import com.bluenimble.apps.sdk.http.HttpParameter;
import com.bluenimble.apps.sdk.http.impls.HttpMessageImpl;
import com.bluenimble.apps.sdk.http.request.HttpRequest;
import com.bluenimble.apps.sdk.http.request.HttpRequestVisitor;
import com.bluenimble.apps.sdk.http.utils.HttpUtils;

public abstract class AbstractHttpRequest extends HttpMessageImpl implements HttpRequest {
	
	private static final long serialVersionUID = 3605433083623476644L;
	
	private static final String UserAgent = "BlueNimble Http Client / Version 1.1";
	
	private static final Set<String> DefaultSuccessCodes = new HashSet<String> ();
	static {
		DefaultSuccessCodes.add ("200");
		DefaultSuccessCodes.add ("201");
		DefaultSuccessCodes.add ("202");
		DefaultSuccessCodes.add ("203");
		DefaultSuccessCodes.add ("204");
		DefaultSuccessCodes.add ("205");
		DefaultSuccessCodes.add ("206");
	}
	
	protected String id;
	protected String name;
	protected HttpEndpoint endpoint;
	protected boolean cachingEnabled;
	protected String contentType;
	
	protected Proxy 	proxy;
	
	protected boolean 	debugMode;
	
	protected int 		connectTimeout 	= 30000;
	protected int 		readTimeout 	= 30000;

	protected Set<String> successCodes = DefaultSuccessCodes;
	
	protected List<HttpParameter> parameters;
	
	protected HttpRequestVisitor visitor;
	
	protected String [] sniHosts;
	
	protected AbstractHttpRequest (String name, HttpEndpoint endpoint) {
		setName (name);
		this.endpoint = endpoint;
		this.id = Lang.UUID (20);
	}

	@Override
	public String getId () {
		return id;
	}

	public void setName (String name) {
		if (name == null) {
			name = HttpMethods.GET;
		}
		this.name = name;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setSuccessCodes (Set<String> successCodes) {
		this.successCodes = successCodes;
	}
	@Override
	public Set<String> getSuccessCodes () {
		return successCodes;
	}

	@Override
	public URI getURI () throws UnsupportedEncodingException, URISyntaxException {
		return HttpUtils.createURI (endpoint, dumpParameters ());
	}

	public HttpEndpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(HttpEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public void setParameters (List<HttpParameter> parameters) {
		this.parameters = parameters;
	}

	@Override
	public List<HttpParameter> getParameters () {
		return parameters;
	}

	@Override
	public Proxy getProxy () {
		return proxy;
	}

	public void setProxy (Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public boolean isCachingEnabled() {
		return cachingEnabled;
	}

	@Override
	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}
	
	@Override
	public boolean isDebugMode () {
		return debugMode;
	}

	@Override
	public void setDebugMode (boolean debugMode) {
		this.debugMode = debugMode;
	}

	@Override
	public void setVisitor (HttpRequestVisitor visitor) {
		this.visitor = visitor;
	}
	@Override
	public HttpRequestVisitor getVisitor () {
		return visitor;
	}

	@Override
	public int getConnectTimeout () {
		return connectTimeout;
	}
	@Override
	public void setConnectTimeout (int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	@Override
	public int getReadTimeout () {
		return readTimeout;
	}
	@Override
	public void setReadTimeout (int readTimeout) {
		this.readTimeout = readTimeout;
	}

	@Override
	public String [] getSniHosts () {
		return sniHosts;
	}
	@Override
	public void	setSniHosts (String [] sniHosts) {
		this.sniHosts = sniHosts;
	}

	protected boolean hasParameters () {
		return parameters != null && !parameters.isEmpty ();
	}

	protected void addHeaders (HttpURLConnection hc) {
		if (headers == null || headers.isEmpty ()) {
			addDefaultHeaders (hc);
			return;
		}
		boolean userAgentPresents = false;
		for (HttpHeader h : headers) {
			String name = h.getName ();
			if (HttpHeaders.USER_AGENT.equals (name)) {
				userAgentPresents = true;
			}
			String [] values = h.getValues ();
			if (values == null || values.length == 0) {
				continue;
			}
			for (int i = 0; i < values.length; i++) {
				String v = values [i];
				if (v == null) {
					continue;
				}
				hc.setRequestProperty (
					name,
					v
				);
			}
		}
		if (!userAgentPresents) {
			hc.setRequestProperty (
				HttpHeaders.USER_AGENT,
				UserAgent
			);
		}
	}
	
	protected String dumpParameters () throws UnsupportedEncodingException {
		if (parameters == null || parameters.isEmpty ()) {
			return null;
		} 
		StringBuilder sb = new StringBuilder ();
		for (HttpParameter p : parameters) {
			p.dump (sb, charset);
			if (parameters.indexOf (p) < (parameters.size () - 1)) {
				sb.append ("&");
			}
		}
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}
	
	private void addDefaultHeaders (HttpURLConnection hc) {
		hc.setRequestProperty (
			HttpHeaders.USER_AGENT,
			UserAgent
		);
	}
	
}
