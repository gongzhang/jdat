package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.template.SealedView;
import com.cocotingo.snail.text.TextView;

public class TextBox extends SealedView {
	
	private final JTextField textField;
	private final TextView textView;
	
	public static final Color standardBackgroundColor = new Color(60, 54, 49);

	public TextBox() {
		super();
		setBackgroundColor(null);
		setKeyboardFocusable(true);
		textField = new JTextField("");
		textField.setBorder(null);
		textField.setBackground(standardBackgroundColor);
		textField.setCaretColor(Resources.COLOR_TEXT_DEFAULT);
		textView = new TextView(0, 0, 0, 0);
		addSubview(textView);
		View.scaleViewWithMarginToSuperView(textView, 6);
		textView.setBackgroundColor(standardBackgroundColor);
		syncPositionAndSize();
		setFont(Resources.FONT_LABEL);
		setNormalColor(Resources.COLOR_TEXT_DEFAULT);
		setHighlightedColor(Resources.COLOR_TEXT_LIGHT);
	}
	
	public JTextField getTextField() {
		return textField;
	}
	
	public String getText() {
		return textField.getText();
	}
	
	public void setText(String text) {
		textField.setText(text);
		textView.setText(text);
	}
	
	public Color getNormalColor() {
		return textField.getForeground();
	}
	
	public void setNormalColor(Color color) {
		textField.setForeground(color);
	}
	
	public Color getHighlightedColor() {
		return textView.getColor();
	}
	
	public void setHighlightedColor(Color color) {
		textView.setColor(color);
	}
	
	public Font getFont() {
		return textView.getFont();
	}
	
	public void setFont(Font font) {
		textView.setFont(font);
		textField.setFont(font);
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.drawStrechableImage(Resources.IMG_TEXTBOX_NORMAL, getWidth(), getHeight());
	}
	
	@Override
	protected void gotKeyboardFocus() {
		syncPositionAndSize();
		getViewContext().getAWTContainer().add(textField);
		textField.requestFocus();
	}
	
	@Override
	protected void lostKeyboardFocus() {
		textView.setText(textField.getText());
		textField.getParent().remove(textField);
	}
	
	private void syncPositionAndSize() {
		Vector pos = getPositionInRootView();
		pos.x += 6;
		pos.y += 6;
		textField.setLocation(pos.toPoint());
		textField.setSize(getWidth() - 12, getHeight() - 13);
		if (textView != null) {
			View.scaleViewWithMarginToSuperView(textView, 6);
		}
	}
	
	@Override
	public View setPosition(int left, int top) {
		super.setPosition(left, top);
		syncPositionAndSize();
		return this;
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		syncPositionAndSize();
		return this;
	}
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		requestKeyboardFocus();
		e.handle();
	}

}
