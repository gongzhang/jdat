package cn.edu.buaa.sei.jdat.vt.widgets;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.text.TextView;

public class LabelCheckBox extends CheckBox {

	public LabelCheckBox() {
		super();
		setClipped(true);
		setLabelView(new TextView(0, 0, 0, 0) {
			{
				setBackgroundColor(null);
				setFont(Resources.FONT_LABEL);
				setColor(Resources.COLOR_TEXT_DEFAULT);
				setText("");
				setSize(getPreferredSize());
				setSelected(true);
			}
		});
	}
	
	public String getText() {
		return ((TextView) getLabelView()).getText();
	}
	
	public void setText(String text) {
		TextView label = (TextView) getLabelView();
		label.setText(text);
		label.setSize(label.getPreferredSize());
	}
	
}