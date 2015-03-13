package cn.edu.buaa.sei.jdat.vt.widgets;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.template.SealedView;

public class CheckBox extends SealedView {
	
	private View labelView;
	private int margin;
	private boolean selected;
	private boolean mouseOver;

	public CheckBox() {
		super();
		setBackgroundColor(null);
		labelView = null;
		selected = false;
		mouseOver = false;
		margin = 4;
	}
	
	public int getLabelMargin() {
		return margin;
	}
	
	public CheckBox setLabelMargin(int labelMargin) {
		this.margin = labelMargin;
		layout();
		return this;
	}
	
	public View getLabelView() {
		return labelView;
	}
	
	public CheckBox setLabelView(View labelView) {
		if (this.labelView != null) {
			this.labelView.removeFromSuperView();
		}
		this.labelView = labelView;
		if (labelView != null) {
			addSubview(labelView);
		}
		layout();
		setNeedsRepaint();
		return this;
	}
	
	private void layout() {
		if (labelView != null) {
			View.putViewWithVerticalCenter(labelView, getHeight() / 2);
			labelView.setLeft(16 + margin * 2);
		}
	}
	
	public Vector getPreferredSize() {
		if (labelView == null) return new Vector(16, 17);
		else return new Vector(labelView.getWidth() + 16 + margin * 2, Math.max(labelView.getHeight(), 17));
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		layout();
		return this;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public CheckBox setSelected(boolean selected) {
		this.selected = selected;
		setNeedsRepaint();
		return this;
	}
	
	@Override
	protected void mouseEntered() {
		mouseOver = true;
		setNeedsRepaint();
	}
	
	@Override
	protected void mouseExited() {
		mouseOver = false;
		setNeedsRepaint();
	}
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		e.handle();
		setSelected(!selected);
	}
	
	@Override
	protected void postMouseReleased(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void postMouseMoved(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void postMouseDragged(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		int left, top = getHeight() / 2 - 8;
		if (labelView == null) {
			left = getWidth() / 2 - 8;
		} else {
			left = margin;
		}
		if (selected) {
			g.drawImage(Resources.IMG_CHECKBOX_SELECTED, left, top);
		} else if (mouseOver) {
			g.drawImage(Resources.IMG_CHECKBOX_HIGHLIGHTED, left, top);
		} else {
			g.drawImage(Resources.IMG_CHECKBOX_NORMAL, left, top);
		}
	}

}
