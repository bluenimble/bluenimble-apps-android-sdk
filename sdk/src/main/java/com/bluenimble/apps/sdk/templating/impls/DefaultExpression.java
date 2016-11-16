package com.bluenimble.apps.sdk.templating.impls;

import java.util.ArrayList;
import java.util.List;

import com.bluenimble.apps.sdk.templating.Expression;
import com.bluenimble.apps.sdk.templating.Node;
import com.bluenimble.apps.sdk.templating.TextNode;
import com.bluenimble.apps.sdk.templating.VariableNode;
import com.bluenimble.apps.sdk.templating.VariableResolver;

public class DefaultExpression implements Expression {

	private static final long serialVersionUID = 7705447418999628814L;
	
	private List<Node> nodes;
	
	@Override
	public void node (Node node) {
		if (nodes == null) {
			nodes = new ArrayList<Node> ();
		}
		nodes.add (node);
	}

	@Override
	public Node node (int index) {
		if (nodes == null) {
			return null;
		}
		return nodes.get (index);
	}

	@Override
	public int nodes () {
		if (nodes == null) {
			return 0;
		}
		return nodes.size ();
	}

	@Override
	public Object eval (VariableResolver vr) {
		if (nodes == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < nodes (); i ++) {
			Node node = node (i);
			if (node instanceof TextNode) {
				sb.append (node.token ());
			} if (node instanceof VariableNode) {
				VariableNode vNode = (VariableNode)node;
				
				List<VariableNode.Property> properties = vNode.vars ();
				
				Object value = null;
				
				for (VariableNode.Property p : properties) {
					if (p.value () != null) {
						value = p.value ();
						System.out.println (value);
						break;
					}
					value = vr.resolve (p.namespace (), p.keys ());
					if (value != null) {
						break;
					}
				}
				if (value == null) {
					value = node.token ();
				}
				
				sb.append (value);
			}
		}
		
		String s = sb.toString ();
		
		sb.setLength (0);
		sb = null;
		
		return s;
	}

}
