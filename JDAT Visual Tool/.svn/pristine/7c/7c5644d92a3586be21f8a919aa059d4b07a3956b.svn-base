package cn.edu.buaa.sei.jdat.vt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StateManager {
	
	public interface StateListener {
		public void shouldDisableUserInteraction();
		public void shouldEnableUserInteraction();
	}
	
	private final List<StateListener> listeners;
	private final List<String> importingList;
	private boolean removingJars;
	
	public StateManager() {
		listeners = new ArrayList<StateListener>();
		importingList = new LinkedList<String>();
		removingJars = false;
	}
	
	public synchronized void addListener(StateListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeListener(StateListener listener) {
		listeners.remove(listener);
	}
	
	public synchronized void startImportTask(String uri) {
		if (isRemoving()) throw new IllegalStateException();
		if (!isImporting()) {
			for (StateListener listener : listeners) {
				listener.shouldDisableUserInteraction();
			}
		}
		importingList.add(uri);
	}
	
	public synchronized void finishImportTask(String uri) {
		if (!importingList.contains(uri) || isRemoving()) throw new IllegalStateException();
		importingList.remove(uri);
		if (importingList.size() == 0) {
			for (StateListener listener : listeners) {
				listener.shouldEnableUserInteraction();
			}
		}
	}
	
	public synchronized void startRemoveTask() {
		if (isRemoving() || isImporting()) throw new IllegalStateException();
		removingJars = true;
		for (StateListener listener : listeners) {
			listener.shouldDisableUserInteraction();
		}
	}
	
	public synchronized void finishRemoveTask() {
		if (!isRemoving() || isImporting()) throw new IllegalStateException();
		removingJars = false;
		for (StateListener listener : listeners) {
			listener.shouldEnableUserInteraction();
		}
	}
	
	//// helper methods ////
	
	public synchronized boolean isImporting() {
		return importingList.size() > 0;
	}
	
	public synchronized boolean isRemoving() {
		return removingJars;
	}
	
	public synchronized boolean isIdle() {
		return !isImporting() && !isRemoving();
	}
	
}
