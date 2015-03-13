package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import cn.edu.buaa.sei.jdat.vt.layout.Edge;
import cn.edu.buaa.sei.jdat.vt.layout.Node;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.Bubble;
import cn.edu.buaa.sei.jdat.vt.widgets.graph.LineView.Line;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.Vector;

public class Arrow extends Line implements Edge {
	
	private final JarBubble bubble1;
	private final JarBubble bubble2;

	public Arrow(LineView owner, JarBubble b1, JarBubble b2) {
		super(owner, new Vector(), new Vector());
		setColor(Resources.COLOR_GRAPH_ARROW);
		bubble1 = b1;
		bubble2 = b2;
	}
	
	public JarBubble getBubble1() {
		return bubble1;
	}
	
	public JarBubble getBubble2() {
		return bubble2;
	}
	
	/**
	 * Callback. Set end1 and end2 base on b1 and b2.
	 */
	public void updatePosition() {
		int x1 = bubble1.getLeft() + bubble1.getWidth() / 2;
		int y1 = bubble1.getTop() + bubble1.getHeight() / 2;
		int x2 = bubble2.getLeft() + bubble2.getWidth() / 2;
		int y2 = bubble2.getTop() + bubble2.getHeight() / 2;
		Vector v = getIntersectionPoint(bubble1, x2 - x1, y2 - y1);
		v.x += x1 - bubble2.getLeft();
		v.y += y1 - bubble2.getTop();
		if (bubble2.isInside(v)) {
			v.x = v.y = 0;
			setEnd1(v);
			setEnd2(v);
			return;
		}
		v.x += bubble2.getLeft();
		v.y += bubble2.getTop();
		setEnd1(v);
		v = getIntersectionPoint(bubble2, x1 - x2, y1 - y2);
		v.x += x2;
		v.y += y2;
		setEnd2(v);
	}
	
	private Vector v = new Vector();
	
	private Vector getIntersectionPoint(Bubble b, double x, double y) {
		v.x = v.y = 0;
		if (x == 0.0) {
			if (y != 0.0) {
				if (y > 0.0) { v.y = b.getHeight() / 2; }
				else { v.y = -b.getHeight() / 2; }
			}
		} else {
			double k = y / x;
			double k0 = b.getHeight() / (double) b.getWidth();
			if (Math.abs(k) > k0) {
				v.y = y > 0.0 ? b.getHeight() / 2 : -b.getHeight() / 2;
				v.x = (int) (v.y / k);
			} else {
				v.x = x > 0.0 ? b.getWidth() / 2 : -b.getWidth() / 2;
				v.y = (int) (v.x * k);
			}
		}
		return v;
	}
	
	@Override
	public void repaintLine(GraphicsX g) {
		
		if (x1 == x2 && y1 == y2) return;
		
		super.repaintLine(g);
		
		// angle
		double x = x2 - x1;
		double y = y2 - y1;
		if (x == 0.0 && y == 0.0) return;
		double a = Math.acos(x / Math.sqrt(x * x + y * y));
		if (y < 0.0) a = Math.PI * 2.0 - a;
		
		g.translate(x2, y2);
		g.rotate(a);
		g.fillPolygon(
				new int[] {0, -8, -8},
				new int[] {0, 3, -3},
		3);
		g.rotate(-a);
		g.translate(-x2, -y2);
		
	}
	
	protected void selected() {
	}
	
	protected void deselected() {
	}

	@Override
	public Node getNode1() {
		return bubble1;
	}

	@Override
	public Node getNode2() {
		return bubble2;
	}

}
