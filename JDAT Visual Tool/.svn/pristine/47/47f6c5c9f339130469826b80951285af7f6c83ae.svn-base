package cn.edu.buaa.sei.jdat.vt.widgets;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseWheelEvent;
import com.cocotingo.snail.View;

public class ListView extends View {
	
	private final View clipView;
	private final View contentView;
	private final View indicator;
	
	private int indicatorState;
	private Animation indicatorAnimation;
	private boolean mouseIsOnIndicator;
	private static final int INDICATOR_HIDDEN = 1;
	private static final int INDICATOR_FADING_IN = 2;
	private static final int INDICATOR_SHOWN = 3;
	private static final int INDICATOR_COUNTING = 4;
	private static final int INDICATOR_FADING_OUT = 5;
	
	private class IndicatorFadeInAnimation extends Animation {

		public IndicatorFadeInAnimation() {
			super(0.2f);
		}
		
		@Override
		protected void animation(float progress) {
			indicator.setAlpha(progress);
		}
		@Override
		protected void completed(boolean canceled) {
			if (!canceled) {
				synchronized (indicator) {
					indicatorAnimation = null;
					if (mouseIsOnIndicator) {
						indicatorState = INDICATOR_SHOWN;
					} else {
						indicatorState = INDICATOR_COUNTING;
						indicatorAnimation = new IndicatorCountingAnimation();
						indicatorAnimation.commit();
					}
				}
			}
		}
		
	}
	
	private class IndicatorFadeOutAnimation extends Animation {

		public IndicatorFadeOutAnimation() {
			super(0.55f);
		}
		
		@Override
		protected void animation(float progress) {
			indicator.setAlpha(1.0f - progress);
		}
		@Override
		protected void completed(boolean canceled) {
			if (!canceled) {
				synchronized (indicator) {
					indicatorAnimation = null;
					indicatorState = INDICATOR_HIDDEN;
				}
			}
		}
		
	}
	
	private class IndicatorCountingAnimation extends Animation {

		public IndicatorCountingAnimation() {
			super(0.8f);
		}
		
		@Override
		protected void animation(float progress) {
		}
		
		@Override
		protected void completed(boolean canceled) {
			if (!canceled) {
				synchronized (indicator) {
					indicatorState = INDICATOR_FADING_OUT;
					indicatorAnimation = new IndicatorFadeOutAnimation();
					indicatorAnimation.commit();
				}
			}
		}
		
	}

	public ListView(int left, int top, int width, int height) {
		super(left, top, width, height);
		setBackgroundColor(null);
		mouseIsOnIndicator = false;
		
		clipView = new View(0, 0, 0, 0) {
			@Override
			public View setSize(int width, int height) {
				super.setSize(width, height);
				if (contentView != null && contentView.getWidth() != width) {
					contentView.setWidth(width);
					ListView.this.layout();
				} else {
					updateContentsVisibilities();
				}
				return this;
			}
		};
		clipView.setPaintingEnabled(false);
		clipView.setClipped(true);
		addSubview(clipView);
		
		contentView = new View(0, 0, width, height) {
			@Override
			public View setSize(int width, int height) {
				boolean height_changed = false;
				if (getHeight() != height) height_changed = true;
				super.setSize(width, height);
				if (height_changed) updateIndicator();
				return this;
			}
			@Override
			public View setPosition(int left, int top) {
				boolean top_changed = false;
				if (getTop() != top) top_changed = true;
				super.setPosition(left, top);
				if (top_changed) {
					updateIndicator();
					updateContentsVisibilities();
				}
				return this;
			}
			@Override
			protected void subviewAdded(View subview) {
				subview.setHidden(true);
			}
		};
		contentView.setPaintingEnabled(false);
		clipView.addSubview(contentView);
		
		indicator = new View(0, 0, 6, 20) {
			@Override
			protected void repaintView(GraphicsX g) {
				g.drawStrechableImage(Resources.IMG_SCROLL_INDICATOR, getWidth(), getHeight());
			}
			@Override
			protected void mouseEntered() {
				mouseIsOnIndicator = true;
				showIndicator();
			}
			@Override
			protected void mouseExited() {
				mouseIsOnIndicator = false;
				switch (indicatorState) {
				case INDICATOR_SHOWN:
					synchronized (indicator) {
						indicatorState = INDICATOR_COUNTING;
						indicatorAnimation = new IndicatorCountingAnimation();
						indicatorAnimation.commit();
					}
					break;
				}
			}
			int mouse_y0;
			@Override
			protected void postMousePressed(com.cocotingo.snail.MouseEvent e) {
				e.handle();
				mouse_y0 = e.getPosition(ListView.this).y;
			}
			@Override
			protected void postMouseReleased(com.cocotingo.snail.MouseEvent e) { e.handle(); }
			@Override
			protected void postMouseMoved(com.cocotingo.snail.MouseEvent e) { e.handle(); }
			@Override
			protected void postMouseDragged(com.cocotingo.snail.MouseEvent e) {
				e.handle();
				int mouse_y = e.getPosition(ListView.this).y;
				float dy = (mouse_y - mouse_y0) / (float) (ListView.this.getHeight() - 8);
				contentView.setTop(contentView.getTop() - (int) (dy * contentView.getHeight()));
				limitContentViewTop();
				mouse_y0 = mouse_y;
			}
			@Override
			protected void postMouseClicked(com.cocotingo.snail.MouseEvent e) { e.handle(); }
		};
		indicator.setBackgroundColor(null);
		addSubview(indicator);
		
		// sync size
		setSize(width, height); 
		
		// indicator state
		synchronized (indicator) {
			indicatorState = INDICATOR_FADING_OUT;
			indicatorAnimation = new IndicatorFadeOutAnimation();
			indicatorAnimation.commit();
		}
	}
	
	public void layout() {
		int cur_top = 0;
		for (View view : contentView) {
			view.setWidth(contentView.getWidth());
			view.setPosition(0, cur_top);
			cur_top += view.getHeight();
		}
		contentView.setHeight(cur_top);
		limitContentViewTop();
		updateContentsVisibilities();
	}
	
	public int getPreferredHeight() {
		return contentView.getHeight() + 4;
	}
	
	private void updateContentsVisibilities() {
		for (View view : contentView) {
			if (contentView.getTop() + view.getTop() + view.getHeight() > 0) {
				if (contentView.getTop() + view.getTop() > clipView.getHeight()) {
					break;
				}
				view.setHidden(false);
			}
		}
	}
	
	public View getContentView() {
		return contentView;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.drawStrechableImage(Resources.IMG_DEPTH_BACKGROUND, getWidth(), getHeight());
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		if (clipView != null) {
			View.scaleViewWithMarginToSuperView(clipView, 2);
		}
		if (contentView != null) {
			limitContentViewTop();
		}
		if (indicator != null) {
			indicator.setLeft(width - 10);
			updateIndicator();
		}
		return this;
	}
	
	@Override
	protected void postMouseWheelMoved(MouseWheelEvent e) {
		e.handle();
		contentView.setTop(contentView.getTop() - e.getRotation() * 4);
		limitContentViewTop();
	}

	private void limitContentViewTop() {
		if (contentView.getHeight() <= clipView.getHeight()) {
			contentView.setTop(0);
		} else if (contentView.getTop() > 0) {
			contentView.setTop(0);
		} else if (contentView.getTop() + contentView.getHeight() < clipView.getHeight()) {
			contentView.setTop(clipView.getHeight() - contentView.getHeight());
		}
	}
	
	private void updateIndicator() {
		float total = contentView.getHeight();
		if (total <= 0.0f) total = 1.0f;
		float window = clipView.getHeight();
		if (window > total) window = total;
		else if (window <= 0.0f) window = 1.0f;
		float top = -contentView.getTop();
		window /= total;
		top /= total;
		total = getHeight() - 8;
		indicator.setTop((int) (4 + top * total));
		indicator.setHeight((int) (window * total));
		if (contentView.getHeight() <= clipView.getHeight()) {
			indicator.setHidden(true);
		} else {
			indicator.setHidden(false);
			showIndicator();
		}
	}
	
	private void showIndicator() {
		if (contentView.getHeight() <= clipView.getHeight()) return;
		synchronized (indicator) {
			switch (indicatorState) {
			case INDICATOR_HIDDEN:
				indicatorState = INDICATOR_FADING_IN;
				indicatorAnimation = new IndicatorFadeInAnimation();
				indicatorAnimation.commit();
				break;
			case INDICATOR_COUNTING:
			case INDICATOR_FADING_OUT:
				if (indicatorAnimation != null) {
					indicatorAnimation.interrupt();
				}
				indicator.setAlpha(1.0f);
				indicatorState = INDICATOR_COUNTING;
				indicatorAnimation = new IndicatorCountingAnimation();
				indicatorAnimation.commit();
				break;
			}
		}
	}
	
}
