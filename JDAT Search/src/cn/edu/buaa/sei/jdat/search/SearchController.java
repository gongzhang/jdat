package cn.edu.buaa.sei.jdat.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import com.cocotingo.snail.dispatcher.Dispatcher;

import cn.edu.buaa.sei.jdat.JarController;
import cn.edu.buaa.sei.jdat.exception.InvalidURIException;
import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.model.Member;
import cn.edu.buaa.sei.jdat.model.Package;
import cn.edu.buaa.sei.jdat.model.Type;
import cn.edu.buaa.sei.jdat.search.trie.DomainNode;
import cn.edu.buaa.sei.jdat.search.trie.WordNode;

public class SearchController {
	private InvertedIndex dictionary;
	
	private LinkedList<WordNode> wordSearchPath;
	private LinkedList<DomainNode> domainSearchPath;
	private String lastQuery = "";
	
	private List<IndexItem> result = new ArrayList<IndexItem>();
	
	public SearchController() {
		wordSearchPath = new LinkedList<WordNode>();
		domainSearchPath = new LinkedList<DomainNode>();
		dictionary = new InvertedIndex();
	}
	
	public void addElements(Jar jar) {
		for (Package pkg : jar.getPackages()) {
			dictionary.add(pkg);
			for (Type type : pkg.getTypes()) {
				dictionary.add(type);
				for (Member member : type.getMembers())
					dictionary.add(member);
			}
		}
	}
	
	public void removeElements(Jar jar) {
		for (Package pkg : jar.getPackages()) {
			dictionary.remove(pkg);
			for (Type type : pkg.getTypes()) {
				dictionary.remove(type);
				for (Member member : type.getMembers())
					dictionary.remove(member);
			}
		}
	}
	
	public List<IndexItem> search(Query query) {
		ArrayList<IndexItem> list = new ArrayList<IndexItem>();
		
		// use last result
		HashMap<String, IndexItem> map;
		if (query.isOptionFlagged(Query.OPT_SEARCH_IN_RESULT))
			map = query.getLastResult();
		else
			map = dictionary.getItems();
		
		// search
		if (!query.isOptionFlagged(Query.OPT_REGEX)) {
			String qs = query.get().toLowerCase();
			for (String key : map.keySet()) {
				if (key.contains(qs)) {
					list.add(map.get(key));
				}
			}
		} else {
			if (query.getRegex() == null)
				return null;
			Matcher matcher = query.getRegex().matcher("");
			for (String key : map.keySet()) {
				matcher.reset(key);
				if (matcher.matches()) 
					list.add(map.get(key));
			}
		}
		// refresh last result
		query.newLastResult();
		for (IndexItem item : list) {
			query.addLastResult(item);
		}
		return list;
	}
	
	public List<IndexItem> getHints(Query query) {
		if (query.get().equals(""))
			return null;
		if (query.isOptionFlagged(Query.OPT_REGEX) || query.isOptionFlagged(Query.OPT_SEARCH_IN_RESULT))
			return null;
		String queryString = query.get().toLowerCase();
		if (queryString.equals(lastQuery))
			return result;
		
		boolean isDomain = isDomain(queryString);
		if (lastQuery.length() == 0 || isQueryKindChanged(queryString)) {
			if (!isDomain)
				createWordPath(queryString);
			else
				createDomainPath(queryString);
		} else {
			if (!isDomain) {
				if (queryString.length() > lastQuery.length() && queryString.startsWith(lastQuery)) {
					String increment = queryString.substring(lastQuery.length());
					pushWordPath(increment);
				} else if (queryString.length() < lastQuery.length() && lastQuery.startsWith(queryString)) {
					popWordPath(lastQuery.length() - queryString.length());
				} else {
					createWordPath(queryString);
				}
			} else {
				String[] queryTokens = Utility.splitDomain(queryString);
				String[] lastQueryTokens = Utility.splitDomain(lastQuery);
				if (isSubDomain(lastQueryTokens, queryTokens)) {
					String[] increment = new String[queryTokens.length - lastQueryTokens.length];
					for (int i = lastQueryTokens.length - 1; i < queryTokens.length - 1; i++) {
						increment[i - lastQueryTokens.length + 1] = queryTokens[i];
					}
					pushDomainPath(increment);
				} else if (isSubDomain(queryTokens, lastQueryTokens)) {
					popDomainPath(lastQueryTokens.length - queryTokens.length);
				} else if (!isSimilarDomain(queryTokens, lastQueryTokens)) {
					createDomainPath(queryString);
				}
			}
		}
		if (!isDomain)
			getWords(queryString);
		else
			getDomains(queryString);
		lastQuery = queryString;
		return result;
	}
	
	private boolean isQueryKindChanged(String query) {
		return isDomain(lastQuery) ^ isDomain(query);
	}
	
	private static boolean isDomain(String query) {
		if (query == null)
			return false;
		return query.contains(".");
	}
	
	/**
	 * Determine whether a domain is a sub-domain of another. "com.a.b" is the prefix of 
	 * "com.a.b.c" and "com.a.c.c", but not of "com.a.c" nor "com.b".
	 * @param domain
	 * @param subdomain
	 * @return
	 */
	private static boolean isSubDomain(String[] domain, String[] subdomain) {
		if (domain.length >= subdomain.length)
			return false;
		for (int i = 0; i < domain.length - 1; i++) {
			if (!domain[i].equals(subdomain[i]))
				return false;
		}
		return true;
	}
	
	private static boolean isSimilarDomain(String[] domain1, String[] domain2) {
		if (domain1.length != domain2.length)
			return false;
		for (int i = 0; i < domain1.length - 1; i++) {
			if (!domain1[i].equals(domain2[i]))
				return false;
		}
		return true;
	}
	
	private void getWords(String query) {
		if (wordSearchPath.size() == 0)
			return;
		else {
			result.clear();
			if (wordSearchPath.peek() == null)
				return;
			dictionary.getWordFastIndex().getItems(wordSearchPath.peek(), result);
		}
	}
	
	private void getDomains(String query) {
		if (domainSearchPath.size() == 0)
			return;
		else {
			result.clear();
			if (domainSearchPath.peek() == null)
				return;
			String tail = query.substring(query.lastIndexOf('.') + 1);
			List<DomainNode> nodes = dictionary.getDomainFastIndex().getMatchedNodes(
					domainSearchPath.peek(), tail);
			for (DomainNode node : nodes) {
				dictionary.getDomainFastIndex().getItems(node, result);
			}
		}
	}
	
	private void createWordPath(String query) {
		wordSearchPath.clear();
		if (query == null || query.length() == 0)
			return;
		WordNode node = dictionary.getWordFastIndex().getChild(null, query.charAt(0));
		wordSearchPath.push(node);
		if (node == null) {
			for (int i = 1; i < query.length(); i++)
				wordSearchPath.push(null);
			return;
		}
		for (int i = 1; i < query.length(); i++) {
			node = dictionary.getWordFastIndex().getChild(node, query.charAt(i));
			wordSearchPath.push(node);
		}
	}
	
	private void pushWordPath(String increment) {
		if (wordSearchPath.size() == 0)
			return;
		WordNode node = wordSearchPath.peek();
		if (node == null) {
			for (int i = 0; i < increment.length(); i++)
				wordSearchPath.push(null);
			return;
		}
		for (int i = 0; i < increment.length(); i++) {
			node = dictionary.getWordFastIndex().getChild(node, increment.charAt(i));
			wordSearchPath.push(node);
		}
	}
	
	private void popWordPath(int decrementLength) {
		for (int i = 0; i < decrementLength; i++) {
			wordSearchPath.pop();
		}
	}
	
	private void createDomainPath(String query) {
		domainSearchPath.clear();
		if (query == null || query.length() == 0)
			return;
		String[] tokens = Utility.splitDomain(query);
		DomainNode node = dictionary.getDomainFastIndex().getChild(null, tokens[0]);
		domainSearchPath.push(node);
		if (node == null) {
			for (int i = 1; i < tokens.length - 1; i++)
				domainSearchPath.push(null);
			return;
		}
		for (int i = 1; i < tokens.length - 1; i++) {
			node = dictionary.getDomainFastIndex().getChild(node, tokens[i]);
			domainSearchPath.push(node);
		}
	}
	
	private void pushDomainPath(String[] increment) {
		if (increment.length == 0)
			return;
		DomainNode node = domainSearchPath.peek();
		if (node == null) {
			for (int i = 0; i < increment.length; i++)
				domainSearchPath.push(null);
			domainSearchPath.push(null);
			return;
		}
		for (int i = 0; i < increment.length; i++) {
			node = dictionary.getDomainFastIndex().getChild(node, increment[i]);
			domainSearchPath.push(node);
		}
	}
	
	private void popDomainPath(int decrementLength) {
		if (decrementLength == 0)
			return;
		for (int i = 0; i < decrementLength; i++) {
			domainSearchPath.pop();
		}
	}
	
	@SuppressWarnings("unused")
	private void debug_printAllWords() {
		List<IndexItem> list = this.dictionary.getWordFastIndex().getAllItems();
		for (IndexItem item : list) {
			System.out.println(item.getName() + ":" + item.size());
		}
	}
	
	private void debug_printAllDomains() {
		List<IndexItem> list = this.dictionary.getDomainFastIndex().getAllItems();
		for (IndexItem item : list) {
			System.out.println(item.getName() + ":" + item.size());
		}
	}
	
	@SuppressWarnings("unused")
	private void debug_printDomainPath() {
		for (DomainNode node : domainSearchPath) {
			if (node == null)
				System.out.print("nil");
			else
				System.out.print(node.getName());
			System.out.print(" <- ");
		}
		System.out.println('#');
	}
	
	private void debug_printWordPath() {
		for (WordNode node : wordSearchPath) {
			if (node == null)
				System.out.print("nil");
			else
				System.out.print(node.getCharacter());
			System.out.print(" <- ");
		}
		System.out.println('#');
	}
	
	public static void main(String[] args) {
		final SearchController controller = new SearchController();
		JarController jarControl = new JarController() {

			@Override
			protected void importJarStarted(Jar jar) {
			}

			@Override
			protected void removeJarFinished(Jar jar) {
			}

			@Override
			protected void importJarFinished(Jar jar) {
				synchronized (controller) {
					controller.addElements(jar);
					imported(controller);
					Dispatcher.drainThreadPool();
				}
			}

			@Override
			protected void removeJarStarted(Jar jar) {
				synchronized (controller) {
					controller.removeElements(jar);
				}
			}

			@Override
			protected void jarStateChanged(Jar jar, int state) {
				
			}
			
		};
		try {
			jarControl.importJar("../JDAT Core/lib/bcel-5.2.jar");
		} catch (InvalidURIException e) {
			e.printStackTrace();
		}
	}
	
	static void imported(SearchController controller) {
		//controller.debug_printAllWords();
		controller.debug_printAllDomains();
		Query query = new Query();
		
		query.set("$$");
		List<IndexItem> items = controller.getHints(query);
		controller.debug_printWordPath();
		
		query.set("$$b");
		controller.getHints(query);
		controller.debug_printWordPath();
		
		System.out.println(items.size());
	}
}
