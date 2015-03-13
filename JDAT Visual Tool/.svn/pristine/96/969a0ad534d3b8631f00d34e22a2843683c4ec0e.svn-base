package cn.edu.buaa.sei.jdat.vt.widgets;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.View;
import com.cocotingo.snail.template.SealedView;
import com.cocotingo.snail.text.TextView;

public class SealedLabel extends SealedView {
	
	private final TextView textView;

	public SealedLabel() {
		super();
		setBackgroundColor(null);
		addSubview(textView = new TextView() {
			{
				setBackgroundColor(null);
				setColor(Resources.COLOR_TEXT_DEFAULT);
				setFont(Resources.FONT_LABEL);
			}
		});
	}
	
	public TextView getTextView() {
		return textView;
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		textView.setSize(width, height);
		return this;
	}
	
}
