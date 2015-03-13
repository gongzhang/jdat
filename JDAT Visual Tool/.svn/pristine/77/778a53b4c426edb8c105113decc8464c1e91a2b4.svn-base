package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import java.awt.Color;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;

public class LineView extends View {
	
	public static interface LineFilter {
		public boolean accept(Line line);
	}
	
	public static class Line {
		
		private final LineView owner;
		protected int x1, y1, x2, y2;
		private Color color;
		private boolean hidden = false;
		
		public Line(LineView owner, Vector v1, Vector v2) {
			this.owner = owner;
			x1 = v1.x;
			y1 = v1.y;
			x2 = v2.x;
			y2 = v2.y;
			color = Color.black;
			owner.addLine(this);
		}
		
		public boolean isHidden() {
			return hidden;
		}
		
		public void setHidden(boolean hidden) {
			this.hidden = hidden;
			owner.setNeedsRepaint();
		}
		
		public Color getColor() {
			return color;
		}
		
		public void setColor(Color color) {
			this.color = color;
			owner.setNeedsRepaint();
		}
		
		public Vector getEnd1() {
			return new Vector(x1, y1);
		}
		
		public Vector getEnd2() {
			return new Vector(x2, y2);
		}
		
		public void setEnd1(Vector v1) {
			x1 = v1.x;
			y1 = v1.y;
			owner.setNeedsRepaint();
		}
		
		public void setEnd2(Vector v2) {
			x2 = v2.x;
			y2 = v2.y;
			owner.setNeedsRepaint();
		}
		
		public void remove() {
			owner.removeLine(this);
		}
		
		public boolean isInside(Vector v) {
			if (x1 == x2 && y1 == y2) return false;
			double x0 = x2 - x1, y0 = y2 - y1;
			double xv = v.x - x1, yv = v.y - y1;
			double a, b;
			if (x0 == 0.0) {
				a = 1;
				b = 0;
			} else {
				a = -y0 / x0;
				b = 1;
			}
			double aa_bb = a * a + b * b;
			double c = Math.abs(a * xv + b * yv) / Math.sqrt(aa_bb);
			if (c < 6) {
				c = a * yv - b * xv;
				xv = -b * c / aa_bb;
				yv = a * c / aa_bb;
				if (x0 < 0) {
					x0 = -x0;
					xv = -xv;
				}
				if (y0 < 0) {
					y0 = -y0;
					yv = -yv;
				}
				return xv >= 0 && xv <= x0 && yv >= 0 && yv <= y0;
			} else {
				return false;
			}
		}
		
		public void repaintLine(GraphicsX g) {
			g.setColor(color);
			g.drawLine(x1, y1, x2, y2);
		}
		
		public void setNeedsRepaint() {
			owner.setNeedsRepaint();
		}
		
		public void bringToTop() {
			owner.bringToTop(this);
		}
		
		public LineView getOwner() {
			return owner;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			else if (obj instanceof Line) {
				Line line = (Line) obj;
				return line.x1 == x1 && line.x2 == x2 && line.y1 == y1 && line.y2 == y2;
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return x1 + (y1 << 8) + (x2 << 16) + (y2 << 24);
		}
		
	}
	
	private final List<Line> lines;

	public LineView(int left, int top, int width, int height) {
		super(left, top, width, height);
		lines = new LinkedList<Line>();
		setBackgroundColor(null);
	}
	
	private void addLine(Line line) {
		lines.add(line);
		setNeedsRepaint();
	}
	
	private void removeLine(Line line) {
		lines.remove(line);
		setNeedsRepaint();
	}
	
	public Line[] getLines() {
		return lines.toArray(new Line[0]);
	}
	
	public void removeAllLines() {
		while (lines.size() > 0) {
			lines.get(0).remove();
		}
		setNeedsRepaint();
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Line line : lines) {
			if (!line.hidden) line.repaintLine(g);
		}
	}
	
	private void bringToTop(Line line) {
		lines.remove(line);
		lines.add(line);
		setNeedsRepaint();
	}
	
	public List<Line> getLinesOn(Vector v, LineFilter filter) {
		ArrayList<Line> rst = new ArrayList<Line>();
		for (Line line : lines) {
			if (!line.hidden && line.isInside(v)) {
				if (filter != null && !filter.accept(line)) continue;
				rst.add(0, line);
			}
		}
		return rst;
	}

}
