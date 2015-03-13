package cn.edu.buaa.sei.jdat.search;

import java.util.HashMap;
import cn.edu.buaa.sei.jdat.model.*;
import cn.edu.buaa.sei.jdat.model.Package;
import cn.edu.buaa.sei.jdat.search.trie.DomainTree;
import cn.edu.buaa.sei.jdat.search.trie.WordTree;

public class InvertedIndex {
	
	private WordTree wordFastIndex;
	
	private DomainTree domainFastIndex;
	
	private HashMap<String, IndexItem> items;
	
	public WordTree getWordFastIndex() {
		return wordFastIndex;
	}
	
	public DomainTree getDomainFastIndex() {
		return domainFastIndex;
	}
	
	public HashMap<String, IndexItem> getItems() {
		return items;
	}
	
	public InvertedIndex() {
		wordFastIndex = new WordTree();
		domainFastIndex = new DomainTree();
		items = new HashMap<String, IndexItem>();
	}
	
	public void add(Element element) {
		String fastName = element.getName().toLowerCase();
		//String fullName = element.getSimpleSignature().toLowerCase();
		if (element instanceof Package) {
			IndexItem item = domainFastIndex.get(fastName);
			if (item == null) {
				item = new IndexItem();
				domainFastIndex.add(fastName, item);
				item.add(element);
			} else {
				item.add(element);
			}
		} else {
			IndexItem item = wordFastIndex.get(fastName);
			if (item == null) {
				item = new IndexItem();
				wordFastIndex.add(fastName, item);
				item.add(element);
			} else {
				item.add(element);
			}
		}
		if (items.containsKey(fastName)) {
			IndexItem item = items.get(fastName);
			item.add(element);
		} else {
			IndexItem item = new IndexItem();
			items.put(fastName, item);
			item.add(element);
		}
	}
	
	public void remove(Element element) {
		String fastName = element.getName().toLowerCase();
		//String fullName = element.getSimpleSignature().toLowerCase();
		if (element instanceof Package) {
			IndexItem item = domainFastIndex.get(fastName);
			if (item == null) {
				return;
			} else {
				item.remove(element);
				if (item.size() == 0)
					domainFastIndex.remove(fastName);
			}
		} else {
			IndexItem item = wordFastIndex.get(fastName);
			if (item == null) {
				return;
			} else {
				item.remove(element);
				if (item.size() == 0)
					wordFastIndex.remove(fastName);
			}
		}
		if (items.containsKey(fastName)) {
			IndexItem item = items.get(fastName);
			item.remove(element);
			if (item.size() == 0)
				items.remove(fastName);
		}
	}
}
