package com.bluenimble.apps.sdk.templating.impls;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.templating.Expression;
import com.bluenimble.apps.sdk.templating.ExpressionCompiler;
import com.bluenimble.apps.sdk.templating.TextNode;
import com.bluenimble.apps.sdk.templating.VariableNode;

public class DefaultExpressionCompiler implements ExpressionCompiler {
	
	private static final long serialVersionUID = 4204895240965684512L;
	
	protected int 		cacheSize 		= 50;
	
	protected char 		expStart 		= '[';
	protected char 		expEnd 			= ']';
	
	protected Map<String, Expression> cached; 
	
	@Override
	public Expression compile (String text, String id) {
		if (Lang.isNullOrEmpty (text)) {
			return null;
		}
		
		String key = id == null ? text : id;
		if (cached != null && cached.containsKey (key)) {
			return cached.get (key);
		}
		
		// compile
		Expression expression = compile (text);
		
		// cache
		if (cacheSize > 0 && expression != null) {
			if (cached == null) {
				cached = new ConcurrentHashMap<String, Expression> ();
			}
			cached.put (key, expression);
		}
		
		return expression;
	}
	
	private Expression compile (String text) {
		
		Expression expression = new DefaultExpression ();
		
		int indexOfStart = text.indexOf (expStart);
		if (indexOfStart < 0) {
			expression.node (new TextNode (text));
			return expression;
		} else {
			// create a text node for the starting part of the text 
			expression.node (new TextNode (text.substring (0, indexOfStart)));
			text = text.substring (indexOfStart + 1);
		}
		
		while (indexOfStart > -1) {
			int indexOfEnd = text.indexOf (expEnd);
			if (indexOfEnd <= -1) {
				expression.node (new TextNode (expStart + text));
				return expression;
			}
			
			// add a var node
			String var = text.substring (0, indexOfEnd);
			expression.node (new VariableNode (var));
			
			text = text.substring (indexOfEnd + 1);

			indexOfStart = text.indexOf (expStart);
			
			// add text before var
			if (indexOfStart < 0) {
				expression.node (new TextNode (text));
				break;
			} else {
				expression.node (new TextNode (text.substring (0, indexOfStart)));
				text = text.substring (indexOfStart + 1);
			}
			
		}
		
		return expression;
	}
	
	public DefaultExpressionCompiler cacheSize (int cacheSize) {
		this.cacheSize = cacheSize;
		return this;
	}

}
