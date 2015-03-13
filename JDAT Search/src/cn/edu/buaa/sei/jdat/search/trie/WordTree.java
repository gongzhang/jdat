package cn.edu.buaa.sei.jdat.search.trie;

import java.util.ArrayList;
import java.util.List;

import cn.edu.buaa.sei.jdat.search.IndexItem;

public class WordTree {
	private WordNode root;
	
	public WordTree() {
		root = new WordNode();
	}
	
	/**
	 * Get all items in this tree.
	 * @return
	 */
	public List<IndexItem> getAllItems() {
		List<IndexItem> list = new ArrayList<IndexItem>();
		getItems(root, list);
		return list;
	}
	
	/**
	 * This method uses prefix to search the matched node, and get words below the
	 * node.
	 * @param word
	 * @return
	 */
	@Deprecated
	public List<IndexItem> getItems(String word) {
		List<IndexItem> list = new ArrayList<IndexItem>();
		WordNode node = getNode(word);
		getItems(node, list);
		return list;
	}
	
	/**
	 * This method doesn't search node with the prefix, while get words start from
	 * the given node.
	 * @param node
	 * @param prefix
	 * @return
	 */
	@Deprecated
	public List<IndexItem> getItems(WordNode node) {
		List<IndexItem> list = new ArrayList<IndexItem>();
		getItems(node, list);
		return list;
	}
	
	public void getItems(WordNode node, List<IndexItem> list) {
		if (node == null)
			return;
		if (node.data != null)
			list.add(node.data);
		if (node.children != null) {
			for (WordNode n : node.children.values()) {
				getItems(n, list);
			}
		}
	}
	
	/**
	 * This is an advanced method to accelerate searching. This method returns the node
	 * where the word should be.
	 * @param word
	 * @return
	 */
	private WordNode getNode(String word) {
		WordNode n = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (n.children == null || !n.children.containsKey(ch))
				return null;
			n = n.children.get(ch);
		}
		return n;
	}
	
	public WordNode getChild(WordNode parent, char character) {
		if (parent == null)
			parent = root;
		if (parent.children == null)
			return null;
		return parent.children.get(character);
	}
	
	public IndexItem get(String word) {
		WordNode node = getNode(word);
		return node == null ? null : node.data;
	}
	
	public boolean contains(String word) {
		return getNode(word) == null;
	}
	
	public void add(String word, IndexItem data) {
		WordNode node = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (node.children == null || !node.children.containsKey(ch)) {
				node.addChild(ch, new WordNode());
			}
			node = node.children.get(ch);
		}
		node.data = data;
	}
	
	public void remove(String word) {
		ArrayList<WordNode> path = new ArrayList<WordNode>();
		WordNode node = root;
		for (int i = 0; i < word.length(); i++) {
			path.add(node);
			char ch = word.charAt(i);
			if (node.children == null || !node.children.containsKey(ch))
				return;
			node = node.children.get(ch);
		}
		if (node.children != null && node.children.size() != 0) {
			node.data = null;
			return;
		}
		for (int i = path.size() - 1; i >= 0; i--) {
			WordNode n = path.get(i);
			n.children.remove(word.charAt(i));
			if (n.children.size() != 0 || n.data != null)
				return;
		}
	}
}
