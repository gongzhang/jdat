package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.text.TextView;

public class ValueBubble extends View {
	
	private final TextView textView;

	public ValueBubble(int anchorX, int anchorY) {
		super(anchorX - 15, anchorY - 18, 31, 21);
		setBackgroundColor(null);
		addSubview(textView = new TextView(2, 0, 27, 15) {
			{
				setBackgroundColor(null);
				setAlignment(TextView.HORIZONTAL_CENTER | TextView.VERTICAL_CENTER);
				setFont(Resources.FONT_SMALL);
				setColor(Color.black);
				setAntialiased(false);
			}
		});
	}
	
	public TextView getTextView() {
		return textView;
	}
	
	public Vector getAnchor() {
		return new Vector(getLeft() + 15, getTop() + 18);
	}
	
	public void setAnchor(int anchorX, int anchorY) {
		setPosition(anchorX - 15, anchorY - 18);
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		g.drawImage(Resources.IMG_VALUEBUBBLE, 0, 0);
	}

}
