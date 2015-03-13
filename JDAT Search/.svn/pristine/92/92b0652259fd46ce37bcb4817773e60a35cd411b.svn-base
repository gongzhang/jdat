package cn.edu.buaa.sei.jdat.search.trie;

import java.util.LinkedList;
import java.util.List;

import cn.edu.buaa.sei.jdat.search.IndexItem;

public class DomainNode {
	protected String name;
	protected IndexItem data;
	
	protected DomainNode parent;
	protected List<DomainNode> children;
	
	protected DomainNode(String name) {
		this.data = null;
		this.name = name;
		children = null;
	}
	
	protected String getDomain() {
		StringBuilder sb = new StringBuilder();
		DomainNode pnode = this;
		while (pnode.parent != null) {
			sb.insert(0, pnode.name);
			pnode = pnode.parent;
			if (pnode.parent != null)
				sb.insert(0, '.');
		}
		return sb.toString();
	}
	
	protected void add(DomainNode node) {
		if (children == null)
			children = new LinkedList<DomainNode>();
		for (DomainNode n : children) {
			int cmp = node.name.compareToIgnoreCase(n.name);
			if (cmp == 0)
				return;
			else if (cmp < 0) {
				int loc = children.indexOf(n);
				children.add(loc, node);
				return;
			}
		}
		children.add(node);
		node.parent = this;
	}
	
	protected void remove(DomainNode node) {
		children.remove(node);
	}
	
	public IndexItem getData() {
		return data;
	}
	
	public String getName() {
		return name;
	}
}
