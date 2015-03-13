package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;

import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.JavaElementLabel;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.View;

public class DependencyLabel extends View {

	private final JavaElementLabel label1, label2;
	private Color arrowColor;

	public DependencyLabel(Object e1, Object e2) {
		super(0, 0, 0, JavaElementLabel.DEFAULT_HEIGHT);
		setBackgroundColor(null);
		label1 = new JavaElementLabel(e1);
		label1.setClipped(true);
		label2 = new JavaElementLabel(e2);
		label2.setClipped(true);
		addSubview(label1);
		addSubview(label2);
		arrowColor = Resources.COLOR_GRAPH_ARROW;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(arrowColor);
		g.translate(getWidth() / 2 + 16, getHeight() / 2);
		g.drawLine(-24, 0, -8, 0);
		g.fillPolygon(
				new int[] {0, -8, -8},
				new int[] {0, 3, -3},
		3);
		g.translate(-getWidth() / 2 - 16, -getHeight() / 2);
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		label1.setPosition(0, 0);
		label1.setWidth(width / 2 - 20);
		label2.setPosition(width / 2 + 20, 0);
		View.scaleViewWithRight(label2, 0);
		return this;
	}
	
	public void setFont(Font font) {
		label1.setFont(font);
		label2.setFont(font);
	}
	
	public Color getArrowColor() {
		return arrowColor;
	}
	
	public void setArrowColor(Color arrowColor) {
		this.arrowColor = arrowColor;
	}
	
	public int getPreferredWidth() {
		return Math.max(label1.getPreferredWidth(), label2.getPreferredWidth()) * 2 + 40;
	}
	
}
