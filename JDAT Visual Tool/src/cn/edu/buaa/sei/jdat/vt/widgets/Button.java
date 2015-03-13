package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.template.SealedView;
import com.cocotingo.snail.text.TextView;

public class Button extends SealedView {
	
	private float highlightedAlpha;
	private Animation highlightAnimation;
	private boolean pressed;
	private final TextView textView;
	private Color textColor = Resources.COLOR_TEXT_DEFAULT;
	private Color textShadowColor = Resources.COLOR_TEXT_SHADOW;
	
	public Button() {
		super();
		setBackgroundColor(null);
		setKeyboardFocusable(true);
		highlightedAlpha = 0.0f;
		highlightAnimation = null;
		pressed = false;
		textView = new TextView(0, 0, 0, 0);
		textView.setPaintingEnabled(false);
		textView.setAlignment(TextView.VERTICAL_CENTER | TextView.HORIZONTAL_CENTER);
		textView.setFont(Resources.FONT_LABEL);
	}
	
	public Button(int left, int top, int width, int height) {
		this();
		setFrame(left, top, width, height);
	}
	
	public String getText() {
		return textView.getText();
	}
	
	public Button setText(String text) {
		textView.setText(text);
		setNeedsRepaint();
		return this;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public Button setTextColor(Color textColor) {
		this.textColor = textColor;
		setNeedsRepaint();
		return this;
	}
	
	public Color getTextShadowColor() {
		return textShadowColor;
	}
	
	public Button setTextShadowColor(Color textShadowColor) {
		this.textShadowColor = textShadowColor;
		setNeedsRepaint();
		return this;
	}
	
	public Font getFont() {
		return textView.getFont();
	}
	
	public Button setFont(Font font) {
		textView.setFont(font);
		setNeedsRepaint();
		return this;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		// paint background
		if (pressed) {
			g.drawStrechableImage(Resources.IMG_BUTTON_PRESSED, getWidth(), getHeight());
		} else if (highlightedAlpha == 0.0f) {
			g.drawStrechableImage(Resources.IMG_BUTTON_NORMAL, getWidth(), getHeight());
		} else {
			final Composite composite = g.getComposite();
			g.drawStrechableImage(Resources.IMG_BUTTON_NORMAL, getWidth(), getHeight());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, highlightedAlpha));
			g.drawStrechableImage(Resources.IMG_BUTTON_HIGHLIGHTED, getWidth(), getHeight());
			g.setComposite(composite);
		}
		
		// paint text
		if (!pressed) g.translate(0, -1);
		textView.setColor(textShadowColor);
		textView.repaintView(g);
		g.translate(0, 1);
		textView.setColor(textColor);
		textView.repaintView(g);
		if (pressed) g.translate(0, -1);
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		if (textView != null) textView.setSize(width, height);
		return this;
	}
	
	@Override
	protected void mouseEntered() {
		if (highlightAnimation != null) highlightAnimation.interrupt();
		highlightAnimation = new Animation(0.12f) {
			@Override
			protected void animation(float progress) {
				highlightedAlpha = progress;
				setNeedsRepaint();
			}
			protected void completed(boolean canceled) {
				highlightAnimation = null;
				setNeedsRepaint();
			}
		};
		highlightAnimation.commit();
	}
	
	@Override
	protected void mouseExited() {
		if (highlightAnimation != null) highlightAnimation.interrupt();
		highlightAnimation = new Animation(0.10f) {
			@Override
			protected void animation(float progress) {
				highlightedAlpha = 1.0f - progress;
				setNeedsRepaint();
			}
			protected void completed(boolean canceled) {
				highlightAnimation = null;
				setNeedsRepaint();
			}
		};
		highlightAnimation.commit();
	}
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		e.handle();
		pressed = true;
		requestKeyboardFocus();
		setNeedsRepaint();
	}
	
	@Override
	protected void postMouseReleased(MouseEvent e) {
		e.handle();
		pressed = false;
		setNeedsRepaint();
	}
	
	@Override
	protected void postMouseMoved(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void postMouseDragged(MouseEvent e) {
		e.handle();
	}
	
	public Vector getPreferredSize() {
		Vector v = textView.getPreferredSize();
		v.x += 12;
		v.y += 12;
		return v;
	}

}
