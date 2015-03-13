package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.Color;
import java.awt.Font;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.StretchableImage;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.template.SealedView;
import com.cocotingo.snail.text.TextView;

public class TabButton extends SealedView {
	
	private int selectedIndex;
	private int mouseOnIndex;
	private int mousePressedIndex;
	private String[] texts;
	private final TextView textView;
	private Color textColor = Resources.COLOR_TEXT_DEFAULT;
	private Color textShadowColor = Resources.COLOR_TEXT_SHADOW;

	public TabButton() {
		super();
		setBackgroundColor(null);
		setKeyboardFocusable(true);
		selectedIndex = -1;
		mouseOnIndex = -1;
		mousePressedIndex = -1;
		textView = new TextView(0, 0, 0, 0);
		textView.setPaintingEnabled(false);
		textView.setAlignment(TextView.VERTICAL_CENTER | TextView.HORIZONTAL_CENTER);
		textView.setFont(Resources.FONT_LABEL);
		texts = new String[] {"", ""};
	}
	
	public String[] getTexts() {
		return texts.clone();
	}
	
	public TabButton setTexts(String[] texts) {
		if (texts == null) this.texts = new String[] {"", ""};
		else if (texts.length < 2) this.texts = new String[] {"", ""};
		else this.texts = texts.clone();
		return this;
	}
	
	public int getButtonCount() {
		return texts.length;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public TabButton setTextColor(Color textColor) {
		this.textColor = textColor;
		setNeedsRepaint();
		return this;
	}
	
	public Color getTextShadowColor() {
		return textShadowColor;
	}
	
	public TabButton setTextShadowColor(Color textShadowColor) {
		this.textShadowColor = textShadowColor;
		setNeedsRepaint();
		return this;
	}
	
	public Font getFont() {
		return textView.getFont();
	}
	
	public TabButton setFont(Font font) {
		textView.setFont(font);
		setNeedsRepaint();
		return this;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.drawStrechableImage(Resources.IMG_DEPTH_BACKGROUND, getWidth(), getHeight());
		int left = 2;
		final int top = 3;
		final int width = (getWidth() - 4) / texts.length;
		final int height = getHeight() - 5;
		StretchableImage img;
		g.translate(2, top);
		textView.setFrame(0, 0, width, height);
		for (int i = 0; i < texts.length; i++) {
			final boolean pressed = mousePressedIndex == -1 ? (i== selectedIndex) : (i == mousePressedIndex);
			if (i == 0) {
				if (pressed) img = Resources.IMG_TABBUTTON_LEFT_PRESSED;
				else if (i == mouseOnIndex) img = Resources.IMG_TABBUTTON_LEFT_HIGHLIGHTED;
				else img = Resources.IMG_TABBUTTON_LEFT_NORMAL;
			} else if (i == texts.length - 1) {
				if (pressed) img = Resources.IMG_TABBUTTON_RIGHT_PRESSED;
				else if (i == mouseOnIndex) img = Resources.IMG_TABBUTTON_RIGHT_HIGHLIGHTED;
				else img = Resources.IMG_TABBUTTON_RIGHT_NORMAL;
			} else {
				if (pressed) img = Resources.IMG_TABBUTTON_MIDDLE_PRESSED;
				else if (i == mouseOnIndex) img = Resources.IMG_TABBUTTON_MIDDLE_HIGHLIGHTED;
				else img = Resources.IMG_TABBUTTON_MIDDLE_NORMAL;
			}
			
			if (i != texts.length - 1) g.drawStrechableImage(img, width, height);
			else g.drawStrechableImage(img, getWidth() - 2 - left, height);
			
			left += width;
			
			// paint text
			textView.setText(texts[i]);
			if (!pressed) g.translate(0, -1);
			textView.setColor(textShadowColor);
			textView.repaintView(g);
			g.translate(0, 1);
			textView.setColor(textColor);
			textView.repaintView(g);
			if (pressed) g.translate(0, -1);
			
			g.translate(width, 0);
		}
		g.translate(-2 - width * texts.length, -top);
	}
	
	public TabButton setSelectedIndex(int selectedIndex) {
		if (this.selectedIndex != selectedIndex) {
			this.selectedIndex = selectedIndex;
			setNeedsRepaint();
			selectedTabChanged(this.selectedIndex);
		}
		return this;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	@Override
	protected void mouseExited() {
		mouseOnIndex = -1;
		setNeedsRepaint();
	}
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		e.handle();
		requestKeyboardFocus();
		mousePressedIndex = getMouseOnIndexByVector(e.getPosition(this));
		setNeedsRepaint();
	}
	
	@Override
	protected void postMouseReleased(MouseEvent e) {
		e.handle();
		mouseOnIndex = getMouseOnIndexByVector(e.getPosition(this));
		if (mouseOnIndex != -1 && mouseOnIndex == mousePressedIndex) {
			setSelectedIndex(mousePressedIndex);
		}
		mousePressedIndex = -1;
		setNeedsRepaint();
	}
	
	private int getMouseOnIndexByVector(Vector v) {
		if (!isInside(v)) return -1;
		else return (v.x - 2) / ((getWidth() - 4) / texts.length);
	}
	
	@Override
	protected void postMouseMoved(MouseEvent e) {
		e.handle();
		int i = getMouseOnIndexByVector(e.getPosition(this));
		if (i != mouseOnIndex) {
			mouseOnIndex = i;
			setNeedsRepaint();
		}
	}
	
	@Override
	protected void postMouseDragged(MouseEvent e) {
		e.handle();
	}
	
	protected void selectedTabChanged(int selectedIndex) {
		
	}

}
