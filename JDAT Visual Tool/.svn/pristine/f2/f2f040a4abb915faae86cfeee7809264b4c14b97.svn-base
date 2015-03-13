package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.Color;
import java.awt.Font;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.StretchableImage;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.text.TextView;

public abstract class Bubble extends View {
	
	private final TextView textView;
	private Color textColor = Resources.COLOR_TEXT_ORANGE;
	private Color textShadowColor = Resources.COLOR_TEXT_ORANGE_SHADOW;
	private StretchableImage backgroundImage = Resources.IMG_ORANGE_PANEL;

	public Bubble(int left, int top, int width, int height) {
		super(left, top, width, height);
		setBackgroundColor(null);
		textView = new TextView(0, 0, width, height);
		textView.setPaintingEnabled(false);
		textView.setAlignment(TextView.VERTICAL_CENTER | TextView.HORIZONTAL_CENTER);
		textView.setFont(Resources.FONT_MONO);
	}
	
	public void makeOrange() {
		setTextColor(Resources.COLOR_TEXT_ORANGE);
		setTextShadowColor(Resources.COLOR_TEXT_ORANGE_SHADOW);
		setBackgroundImage(Resources.IMG_ORANGE_PANEL);
	}
	
	public void makeBrown() {
		setTextColor(Resources.COLOR_TEXT_BROWN);
		setTextShadowColor(Resources.COLOR_TEXT_BROWN_SHADOW);
		setBackgroundImage(Resources.IMG_BROWN_PANEL);
	}
	
	public void makePink() {
		setTextColor(Resources.COLOR_TEXT_PINK);
		setTextShadowColor(Resources.COLOR_TEXT_PINK_SHADOW);
		setBackgroundImage(Resources.IMG_PINK_PANEL);
	}
	
	public TextView getTextView() {
		return textView;
	}

	public String getText() {
		return textView.getText();
	}
	
	public Bubble setText(String text) {
		textView.setText(text);
		setNeedsRepaint();
		return this;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public Bubble setTextColor(Color textColor) {
		this.textColor = textColor;
		setNeedsRepaint();
		return this;
	}
	
	public Color getTextShadowColor() {
		return textShadowColor;
	}
	
	public Bubble setTextShadowColor(Color textShadowColor) {
		this.textShadowColor = textShadowColor;
		setNeedsRepaint();
		return this;
	}
	
	public Font getFont() {
		return textView.getFont();
	}
	
	public Bubble setFont(Font font) {
		textView.setFont(font);
		setNeedsRepaint();
		return this;
	}
	
	public StretchableImage getBackgroundImage() {
		return backgroundImage;
	}
	
	public Bubble setBackgroundImage(StretchableImage backgroundImage) {
		this.backgroundImage = backgroundImage;
		setNeedsRepaint();
		return this;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		// paint background
		g.drawStrechableImage(backgroundImage, getWidth(), getHeight());
		
		// paint text
		g.translate(1, 1);
		textView.setColor(textShadowColor);
		textView.repaintView(g);
		g.translate(-1, -1);
		textView.setColor(textColor);
		textView.repaintView(g);
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		if (textView != null) textView.setSize(width, height - 2);
		return this;
	}
	
	public Vector getPreferredSize() {
		Vector rst = textView.getPreferredSize();
		rst.x += 8;
		rst.y += 10;
		return rst;
	}
	
}
