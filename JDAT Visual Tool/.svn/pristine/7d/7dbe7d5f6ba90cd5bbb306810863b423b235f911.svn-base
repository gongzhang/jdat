package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import java.awt.Cursor;

import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.DragView;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;


public class DependencyTipBubble extends TipBubble {

	private final DependencyLabel title;
	private final DependencyListView listView;
	private final View resizer;
	
	public DependencyTipBubble(Object titleElement1, Object titleElement2) {
		super(0, 0, 0, 0);
		addSubview(title = new DependencyLabel(titleElement1, titleElement2) {
			{
				setFont(Resources.FONT_MONO_BOLD);
				setArrowColor(Resources.COLOR_GRAPH_ARROW_SELECTED);
			}
			Vector mouse;
			protected void postMousePressed(com.cocotingo.snail.MouseEvent e) {
				mouse = e.getPosition(DependencyTipBubble.this);
				mouse.inverse();
				e.handle();
			}
			protected void postMouseDragged(com.cocotingo.snail.MouseEvent e) {
				View superView = DependencyTipBubble.this.getSuperView();
				Vector v = e.getPosition(superView);
				v.increase(mouse);
				if (v.x < 0) v.x = 0;
				if (v.y < 0) v.y = 0;
				DependencyTipBubble.this.setPosition(v);
				if (DependencyTipBubble.this.getRight() < 0) {
					DependencyTipBubble.this.setLeft(superView.getWidth() - DependencyTipBubble.this.getWidth());
				}
				if (DependencyTipBubble.this.getBottom() < 0) {
					DependencyTipBubble.this.setTop(superView.getHeight() - DependencyTipBubble.this.getHeight());
				}
				e.handle();
			}
			protected void postMouseReleased(com.cocotingo.snail.MouseEvent e) {
				e.handle();
			}
		});
		addSubview(resizer = new DragView(0, 0, 40, 40) {
			{
				setPaintingEnabled(false);
			}
			@Override
			protected void mouseEntered() {
				getViewContext().getAWTContainer().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			}
			@Override
			protected void mouseExited() {
				getViewContext().getAWTContainer().setCursor(Cursor.getDefaultCursor());
			}
			@Override
			protected boolean horizonalPositionWillChange(int dx) {
				DependencyTipBubble.this.setWidth(DependencyTipBubble.this.getWidth() + dx);
				return false;
			}
			@Override
			protected boolean verticalPositionWillChange(int dy) {
				DependencyTipBubble.this.setHeight(DependencyTipBubble.this.getHeight() + dy);
				return false;
			}
		});
		addSubview(listView = new DependencyListView(0, 0, 0, 0) {
			@Override
			protected void selectedItemChanged(Item item) {
				if (item != null) {
					DependencyTipBubble.this.selectedItemChanged(item.getIndex());
				}
			}
		});
	}
	
	protected void selectedItemChanged(int index) {
		
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		if (listView != null) {
			title.setPosition(6, 6);
			View.scaleViewWithRight(title, 5);
			listView.setPosition(4, title.getTop() + title.getHeight() + 6);
			View.scaleViewWithRightAndBottom(listView, 3, 3);
			resizer.setPosition(width - 40, height - 40);
		}
		return this;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		super.repaintView(g);
		g.setColor(Resources.COLOR_LINE);
		g.drawLine(6, 38, getWidth() - 6, 38);
	}
	
	public DependencyListView getListView() {
		return listView;
	}
	
	public DependencyLabel getTitle() {
		return title;
	}
	
	public int getPreferredWidth() {
		return title.getPreferredWidth() + 17;
	}
	
	public int getPreferredHeight() {
		return listView.getPreferredHeight() + 9 + 6 + 30 + 6;
	}

}
