package cn.edu.buaa.sei.jdat.vt.layout;

import java.util.ArrayList;
import java.util.List;

import com.cocotingo.snail.dispatcher.Dispatcher;


public class ForceDirectedLayout {
	
	private class LayoutTask implements Runnable {
		
		boolean stop = false;;

		@Override
		public void run() {
			stop = false;
			Runnable target = new Runnable() {
				@Override
				public void run() {
					update();
				}
			};
			try {
				while (!stop) {
					Dispatcher.executeAndWaitAndSyncWithGUI(target);
					Thread.sleep(40);
				}
			} catch (InterruptedException e) {
			}
		}
		
	}
	
	private final List<Node> nodes;
	private double width, height;
	private LayoutTask task;
	
	public ForceDirectedLayout(double aspectRatio) {
		nodes = new ArrayList<Node>();
		task = null;
	}
	
	public void setArea(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public synchronized void reset() {
		if (task != null) {
			task.stop = true;
			task = null;
		}
		nodes.clear();
	}
	
	public synchronized void start() {
		if (task != null) {
			task.stop = true;
			task = null;
		} else {
			task = new LayoutTask();
			Dispatcher.execute(task);
		}
	}
	
	public synchronized void addNode(Node node) {
		nodes.add(node);
	}
	
	public synchronized void removeNode(Node node) {
		nodes.remove(node);
	}
	
	private synchronized boolean update() {
		boolean not_finish = true;
		Point delta = new Point();
		for (Node node : nodes) {
			Point nodePos = node.position();
			// reset delta vector
			delta.set(0, 0);
			
			// skip isolated node
			if (node.edgeCount() == 0) continue;
			
			// skip captured node
			if (node.isCapturedByMouse()) continue;
			
			// sum up all forces pushing this item away
		    for (Node n : nodes) {
		    	Point vec = Point.minus(nodePos, n.position());
		    	double l = 5.0 * vec.getLengthSquare();
		    	
		    	// overlay?
		    	if (Math.abs(vec.x) < node.getWidth() && Math.abs(vec.y) < node.getHeight() * 2) {
		    		vec.multiply(3.0);
		    	}
		    	
		    	if (l > 0.0) {
		            delta.x += (vec.x * width) / l;
		            delta.y += (vec.y * height) / l;
		    	}
			}

		    // now subtract all forces pulling items together
		    double weight = (node.edgeCount() + 1) * (height / 20.0);
		    for (Edge edge : node.edgeIteratable()) {
				Node target = edge.getNode1() == node ? edge.getNode2() : edge.getNode1();
				Point vec = Point.minus(target.position(), nodePos);
				delta.x += vec.x / weight;
	            delta.y += vec.y / weight;
			}
		    
		    // update position
		    if (delta.getLengthSquare() > 1.5) {
		    	not_finish = false;
		    	nodePos.add(delta);
		    	node.sync();
		    }
		}
		return not_finish;
	}

}
