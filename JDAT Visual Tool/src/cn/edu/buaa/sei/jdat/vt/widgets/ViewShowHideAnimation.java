package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.geom.AffineTransform;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.AnimationFunction;
import com.cocotingo.snail.View;

public abstract class ViewShowHideAnimation {

	private ViewShowHideAnimation() {
	}
	
	public static Animation createHideAnimation(final View view, final Runnable finish) {
		return new Animation(0.2f, AnimationFunction.EASE_OUT) {
			@Override
			protected void animation(float progress) {
				AffineTransform t = new AffineTransform();
				t.translate(view.getWidth() / 2, view.getHeight() / 2);
				t.scale(1.0f - progress / 2.0f, 1.0 - progress / 2.0f);
				t.translate(-view.getWidth() / 2, -view.getHeight() / 2);
				view.setTransform(t);
				view.setAlpha(1.0f - progress);
			}
			@Override
			protected void completed(boolean canceled) {
				view.setTransform(null);
				view.setHidden(true);
				view.setAlpha(1.0f);
				if (finish != null) finish.run();
			}
		};
	}
	
	public static Animation createShowAnimation(final View view, final Runnable finish) {
		view.setHidden(false);
		return new Animation(0.2f, AnimationFunction.EASE_OUT) {
			@Override
			protected void animation(float progress) {
				AffineTransform t = new AffineTransform();
				t.translate(view.getWidth() / 2, view.getHeight() / 2);
				t.scale(1.0f - (1.0f - progress) / 2.0f, 1.0 - (1.0f - progress) / 2.0f);
				t.translate(-view.getWidth() / 2, -view.getHeight() / 2);
				view.setTransform(t);
				view.setAlpha(1.0f - (1.0f - progress));
			}
			@Override
			protected void completed(boolean canceled) {
				view.setTransform(null);
				view.setAlpha(1.0f);
				if (finish != null) finish.run();
			}
		};
	}
	
}
