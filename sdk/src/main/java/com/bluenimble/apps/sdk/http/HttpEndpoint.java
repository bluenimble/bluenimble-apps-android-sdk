package com.bluenimble.apps.sdk.http;

import java.io.Serializable;

public class HttpEndpoint implements Serializable {
	
	private static final long serialVersionUID = 8352062844479208019L;

	public static final String HTTP_SCHEME = "http";
	public static final String HTTPS_SCHEME = "https";
	
	protected String 	scheme = "http";
	protected String 	host;
	protected int 		port;
	protected String 	path;
	protected String 	query;
	
	public HttpEndpoint (String scheme, String host, int port, String path, String query) {
		if (scheme == null) {
			scheme = HTTP_SCHEME;
		}
		this.host = host;
		this.port = port;
		this.scheme = scheme;
		this.path = path;
		this.query = query;
	}
	
	public HttpEndpoint (String scheme, String host, String query) {
		this (scheme, host, 0, null, query);
	}
	
	public HttpEndpoint (String scheme, String host, int port, String path) {
		this (scheme, host, port, path, null);
	}
	
	public HttpEndpoint (String scheme, String host, int port) {
		this (scheme, host, port, null);
	}
	
	public HttpEndpoint (String scheme, String host) {
		this (scheme, host, 0);
	}
	
	public HttpEndpoint (String host) {
		this (HTTP_SCHEME, host);
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}
	
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
