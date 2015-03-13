package cn.edu.buaa.sei.jdat.search.trie;

import java.util.HashMap;

import cn.edu.buaa.sei.jdat.search.IndexItem;

public class WordNode {
	/**
	 * Data with non-null value means this node is an end node.
	 */
	protected IndexItem data;
	protected char character;
	
	protected WordNode parent;
	protected HashMap<Character, WordNode> children;

	protected WordNode() {
		data = null;
		children = null;
	}
	
	protected void addChild(char ch, WordNode node) {
		if (children == null) {
			children = new HashMap<Character, WordNode>(4, 0.8f);
		}
		children.put(ch, node);
		node.character = ch;
		node.parent = this;
	}
	
	protected void removeChild(char ch) {
		children.remove(ch);
	}
	
	public IndexItem getData() {
		return data;
	}
	
	public char getCharacter() {
		return character;
	}
	
	protected String getWord() {
		StringBuilder sb = new StringBuilder();
		WordNode pnode = this;
		while (pnode.parent != null) {
			sb.append(pnode.character);
			pnode = pnode.parent;
		}
		return sb.reverse().toString();
	}
}
