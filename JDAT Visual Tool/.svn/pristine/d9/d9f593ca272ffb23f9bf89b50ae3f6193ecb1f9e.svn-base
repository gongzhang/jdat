package cn.edu.buaa.sei.jdat.vt.widgets;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.View;

public class ProgressBar extends View {
	
	private float progress;

	public ProgressBar(int left, int top, int width, int height) {
		super(left, top, width, height);
		progress = 0.0f;
		setBackgroundColor(null);
	}
	
	public float getProgress() {
		return progress;
	}
	
	public ProgressBar setProgress(float progress) {
		this.progress = progress;
		setNeedsRepaint();
		return this;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.drawStrechableImage(Resources.IMG_PROGRESSBAR_BACKGROUND, getWidth(), getHeight());
		g.translate(0, 1);
		g.drawStrechableImage(Resources.IMG_PROGRESSBAR_FOREGROUND, (int) ((getWidth() - 2) * progress) + 2, getHeight() - 1);
		g.translate(0, -1);
	}

}
