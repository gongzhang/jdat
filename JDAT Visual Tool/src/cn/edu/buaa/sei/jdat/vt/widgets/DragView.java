package cn.edu.buaa.sei.jdat.vt.widgets;

import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;

public class DragView extends View {
	
	private Vector position;

	public DragView(int left, int top, int width, int height) {
		super(left, top, width, height);
	}
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		e.handle();
		position = e.getPosition(getSuperView());
		position.inverse();
	}
	
	@Override
	protected void postMouseReleased(MouseEvent e) {
		e.handle();
		position = null;
	}
	
	@Override
	protected void postMouseDragged(MouseEvent e) {
		e.handle();
		Vector dv = e.getPosition(getSuperView());
		dv.increase(position);
		if (horizonalPositionWillChange(dv.x)) {
			setLeft(getLeft() + dv.x);
		}
		if (verticalPositionWillChange(dv.y)) {
			setTop(getTop() + dv.y);
		}
		position = e.getPosition(getSuperView());
		position.inverse();
	}
	
	protected boolean horizonalPositionWillChange(int dx) {
		return true;
	}
	
	protected boolean verticalPositionWillChange(int dy) {
		return true;
	}

}
