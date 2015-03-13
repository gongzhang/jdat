package cn.edu.buaa.sei.jdat.search.trie;

import java.util.ArrayList;
import java.util.List;

import cn.edu.buaa.sei.jdat.search.IndexItem;
import cn.edu.buaa.sei.jdat.search.Utility;

/**
 * Domain name (namespace) trie-tree.
 * @author Alven
 *
 */
public class DomainTree {
	private DomainNode root;
	
	public DomainTree() {
		root = new DomainNode(null);
	}
	
	/**
	 * Get all domains in this tree.
	 * @return
	 */
	public List<IndexItem> getAllItems() {
		List<IndexItem> list = new ArrayList<IndexItem>();
		getItems(root, list);
		return list;
	}
	
	/**
	 * Get all domains that have the specific prefix.
	 * @param prefix
	 * @return
	 */
	@Deprecated
	public List<IndexItem> getItems(String domain) {
		List<IndexItem> list = new ArrayList<IndexItem>();
		List<DomainNode> nodes = getMatchedNodes(domain);
		for (DomainNode node : nodes) {
			getItems(node, list);
		}
		return list;
	}
	
	/**
	 * Get domains that below (belong to) the given node's name-space.
	 * @param node
	 * @return
	 */
	@Deprecated
	public List<IndexItem> getItems(DomainNode node) {
		List<IndexItem> list = new ArrayList<IndexItem>();
		getItems(node, list);
		return list;
	}
	
	/**
	 * Get domains that must have an accurate prefix. For example, if there are
	 * "com.aa.bbb" and "com.ab.ccc" in this tree, if the prefix is "com.a", this method
	 * return none, while the prefix is "com.aa" this method return "com.aa.bbb".
	 * @param prefix
	 * @return
	 */
	@Deprecated
	public List<IndexItem> getItemsStrictly(String domain) {
		List<IndexItem> list = new ArrayList<IndexItem>();
		DomainNode node = getNode(domain);
		getItems(node, list);
		return list;
	}
	
	public void getItems(DomainNode node, List<IndexItem> list) {
		if (node == null)
			return;
		if (node.data != null)
			list.add(node.data);
		if (node.children != null) {
			for (DomainNode n : node.children) {
				getItems(n, list);
			}
		}
	}
	
	/**
	 * An advanced method for search optimization. Get node with correct name-space.
	 * @param domain
	 * @return
	 */
	private DomainNode getNode(String domain) {
		DomainNode node = root;
		boolean flag = false;
		String[] tokens = Utility.splitDomain(domain);
		for (int i = 0; i < tokens.length; i++) {
			if (node.children == null)
				return null;
			for (DomainNode n : node.children) {
				if (tokens[i].equals(n.name)) {
					node = n;
					flag = true;
					break;
				}
			}
			if (flag == false)
				return null;
			flag = false;
		}
		return node;
	}
	
	public DomainNode getChild(DomainNode parent, String token) {
		if (parent == null)
			parent = root;
		if (parent.children == null)
			return null;
		for (DomainNode n : parent.children) {
			if (token.equals(n.name))
				return n;
		}
		return null;
	}
	
	/**
	 * Get matched nodes with token, start from specific node.
	 * @param token
	 * @param start
	 * @return
	 */
	public List<DomainNode> getMatchedNodes(DomainNode start, String token) {
		if (start == null)
			return null;
		ArrayList<DomainNode> list = new ArrayList<DomainNode>();
		if (start.children == null)
			return null;
		for (DomainNode n : start.children) {
			if (n.name.contains(token))
				list.add(n);
		}
		return list;
	}
	
	/**
	 * Get matched nodes with domain.
	 * @param prefix
	 * @return
	 */
	@Deprecated
	public List<DomainNode> getMatchedNodes(String domain) {
		ArrayList<DomainNode> list = new ArrayList<DomainNode>();
		DomainNode node = root;
		boolean flag = false;
		String[] tokens = Utility.splitDomain(domain);
		for (int i = 0; i < tokens.length - 1; i++) {
			if (node.children == null)
				return null;
			for (DomainNode n : node.children) {
				if (tokens[i].equals(n.name)) {
					node = n;
					flag = true;
					break;
				}
			}
			if (flag == false)
				return null;
			flag = false;
		}
		if (node.children == null)
			return null;
		for (DomainNode n : node.children) {
			if (n.name.contains(tokens[tokens.length - 1]))
				list.add(n);
		}
		return list;
	}
	
	public IndexItem get(String domain) {
		DomainNode node = getNode(domain);
		return node == null ? null : node.data;
	}
	
	public boolean contains(String namespace) {
		return getNode(namespace) == null;
	}
	
	public void add(String domain, IndexItem data) {
		DomainNode node = root;
		boolean flag = false;
		String[] tokens = Utility.splitDomain(domain);
		for (int i = 0; i < tokens.length; i++) {
			if (node.children == null) {
				node.add(new DomainNode(tokens[i]));
				node = node.children.get(0);
				continue;
			}
			// search next token
			for (DomainNode n : node.children) {
				if (tokens[i].equals(n.name)) {
					node = n;
					flag = true;
					break;
				}
			}
			if (flag == false) {
				DomainNode n = new DomainNode(tokens[i]);
				node.add(n);
				node = n;
			}
			flag = false;
		}
		node.data = data;
	}
	
	public void remove(String domain) {
		ArrayList<DomainNode> path = new ArrayList<DomainNode>();
		DomainNode node = root;
		boolean flag = false;
		String[] tokens = Utility.splitDomain(domain);
		for (int i = 0; i < tokens.length; i++) {
			path.add(node);
			if (node.children == null )
				return;
			// search next token
			for (DomainNode n : node.children) {
				if (tokens[i].equals(n.name)) {
					node = n;
					flag = true;
					break;
				}
			}
			if (flag == false)
				return;
			flag = false;
		}
		if (node.children != null && node.children.size() != 0) {
			node.data = null;
			return;
		}
		for (int i = path.size() - 1; i >= 0; i--) {
			DomainNode n = path.get(i);
			n.children.remove(node);
			if (n.children.size() != 0 || n.data != null)
				return;
		}
	}
}
