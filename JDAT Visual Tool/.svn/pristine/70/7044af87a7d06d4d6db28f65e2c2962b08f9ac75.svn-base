package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;

import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.vt.layout.Edge;
import cn.edu.buaa.sei.jdat.vt.layout.Node;
import cn.edu.buaa.sei.jdat.vt.layout.Point;
import cn.edu.buaa.sei.jdat.vt.widgets.Bubble;

public class JarBubble extends Bubble implements Node {
	
	private final Jar jar;
	private final List<Arrow> arrows1;
	private final List<Arrow> arrows2;

	public JarBubble(Jar jar) {
		super(0, 0, 0, 0);
		this.jar = jar;
		arrows1 = new ArrayList<Arrow>();
		arrows2 = new ArrayList<Arrow>();
		if (jar.isUnknown()) {
			setText("Unknown Jar");
		} else {
			setText(jar.getName());
		}
		setSize(getPreferredSize());
	}
	
	public Jar getJar() {
		return jar;
	}
	
	public void addArrowAsEnd1(Arrow arrow) {
		arrows1.add(arrow);
	}
	
	public void addArrowAsEnd2(Arrow arrow) {
		arrows2.add(arrow);
		arrow.updatePosition();
	}

	@Override
	public View setPosition(int left, int top) {
		super.setPosition(left, top);
		if (arrows1 != null && arrows2 != null) {
			for (Arrow arrow : arrows1) {
				arrow.updatePosition();
			}
			for (Arrow arrow : arrows2) {
				arrow.updatePosition();
			}
		}
		return this;
	}
	
	//// mouse dragging ////
	
	private Vector mouse_position;
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		e.handle();
		dragging_flag = true;
		setIndex(getSuperView().count() - 1);
		mouse_position = e.getPosition(this);
		mouse_position.inverse();
	}
	
	@Override
	protected void postMouseReleased(MouseEvent e) {
		e.handle();
		dragging_flag = false;
	}
	
	@Override
	protected void postMouseMoved(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void postMouseDragged(MouseEvent e) {
		e.handle();
		Vector v = e.getPosition(getSuperView()).add(mouse_position);
		verifyDraggingPosition(v);
		setPosition(v);
	}
	
	protected void verifyDraggingPosition(Vector v) {
	}
	
	//// Node interface methods ////

	private final Point point = new Point();
	private boolean dragging_flag = false;
	
	@Override
	public Point position() {
		point.x = getLeft();
		point.y = getTop();
		return point;
	}

	@Override
	public int edgeCount() {
		return arrows1.size() + arrows2.size();
	}

	@Override
	public void sync() {
		setPosition((int) point.x, (int) point.y);
	}
	@Override
	public Iterable<Edge> edgeIteratable() {
		return new Iterable<Edge>() {
			@Override
			public Iterator<Edge> iterator() {
				return new Iterator<Edge>() {
					private int i = 0;
					
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
					
					@Override
					public Edge next() {
						return get(i++);
					}
					
					private Edge get(int index) {
						if (index < arrows1.size()) {
							return arrows1.get(index);
						} else {
							index -= arrows1.size();
							return arrows2.get(index);
						}
					}
					
					@Override
					public boolean hasNext() {
						return i < edgeCount();
					}
				};
			}
		};
	}

	@Override
	public boolean isCapturedByMouse() {
		return dragging_flag;
	}

}
