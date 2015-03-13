package cn.edu.buaa.sei.jdat.vt.widgets;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.View;

public class ViewAlphaAnimation extends Animation {
	
	private final View target;
	private final float a1, a2;

	public ViewAlphaAnimation(View target, float duration, float alpha) {
		super(duration);
		this.target = target;
		this.a1 = target.getAlpha();
		this.a2 = alpha;
	}

	@Override
	protected void animation(float progress) {
		target.setAlpha(a1 + progress * (a2 - a1));
	}

}
