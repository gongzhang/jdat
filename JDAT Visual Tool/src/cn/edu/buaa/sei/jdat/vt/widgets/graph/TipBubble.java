package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.View;

public class TipBubble extends View {

	public TipBubble(int left, int top, int width, int height) {
		super(left, top, width, height);
		setBackgroundColor(null);
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.drawStrechableImage(Resources.IMG_TIPBUBBLE, getWidth(), getHeight());
	}

}
