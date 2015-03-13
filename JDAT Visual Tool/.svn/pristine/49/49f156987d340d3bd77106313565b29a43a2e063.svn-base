package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.geom.AffineTransform;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.View;

public class ViewHighlightAnimation extends Animation {
	
	private final View target;

	public ViewHighlightAnimation(View target, float duration) {
		super(duration);
		this.target = target;
	}

	@Override
	protected void animation(float progress) {
		double scale = 1.0 + 0.5 * Math.sin(progress * Math.PI);
		AffineTransform t = new AffineTransform();
		t.translate(target.getWidth() / 2, target.getHeight() / 2);
		t.scale(scale, scale);
		t.translate(-target.getWidth() / 2, -target.getHeight() / 2);
		target.setTransform(t);
	}
	
	@Override
	protected void completed(boolean canceled) {
		target.setTransform(null);
	}

}
