package com.bluenimble.apps.sdk.json;

public abstract class JsonAbstractEntity implements JsonEntity {
	
	private static final long serialVersionUID = -6420102827706235701L;
	
	protected boolean addQuotes (String key, String value) {
		return true;
	}
	
	protected String quote (String key, String value) {
		if (value == null || value.length() == 0) {
			return "\"\"";
		}
		char b;
		char c = 0;
		int i;
		int len = value.length();
		StringBuilder sb = new StringBuilder(len + 4);
		String t;

		if (addQuotes (key, value)) {
			sb.append('"');
		}
		for (i = 0; i < len; i += 1) {
			b = c;
			c = value.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
						|| (c >= '\u2000' && c < '\u2100')) {
					t = "000" + Integer.toHexString(c);
					sb.append("\\u" + t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
			}
		}
		if (addQuotes (key, value)) {
			sb.append('"');
	    }
		String s = sb.toString();
        sb.setLength (0);
        sb = null;
        return s;
	}

	protected void validate (Object o) {
		if (o != null) {
			if (o instanceof Double) {
				if (((Double) o).isInfinite() || ((Double) o).isNaN ()) {
					throw new RuntimeException (
							"JSON does not allow non-finite numbers.");
				}
			} else if (o instanceof Float) {
				if (((Float) o).isInfinite () || ((Float) o).isNaN ()) {
					throw new RuntimeException (
							"JSON does not allow non-finite numbers.");
				}
			}
		}
	}

	protected String numberToString (Number n) {
		if (n == null) {
			return NULL_VALUE;
		}
		
		validate (n);

		String s = n.toString();
		if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
			while (s.endsWith("0")) {
				s = s.substring(0, s.length() - 1);
			}
			if (s.endsWith(".")) {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}
	
	protected String valueToString (Object value) throws JsonException {
		if (value == null || NULL.equals(value)) {
			return NULL_VALUE;
		}
		if (value instanceof Number) {
			return numberToString((Number) value);
		}
		if (value instanceof Boolean || value instanceof JsonObject
				|| value instanceof JsonArray) {
			return value.toString ();
		}
		return quote (null, value.toString());
	}
	
	public abstract String toString (int indentFactor);
	
	public abstract void write (JsonEmitter emitter);

}
