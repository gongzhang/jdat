package cn.edu.buaa.sei.jdat.vt.widgets;

import java.awt.Font;
import java.util.ArrayList;

import cn.edu.buaa.sei.jdat.model.Class;
import cn.edu.buaa.sei.jdat.model.Element;
import cn.edu.buaa.sei.jdat.model.Field;
import cn.edu.buaa.sei.jdat.model.IModifier;
import cn.edu.buaa.sei.jdat.model.Interface;
import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.model.Member;
import cn.edu.buaa.sei.jdat.model.Method;
import cn.edu.buaa.sei.jdat.model.Package;
import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.text.RichTextBlock;
import com.cocotingo.snail.text.RichTextView;

public class JavaElementLabel extends View {
	
	private Object element;
	private final RichTextView textView;
	
	public static final int DEFAULT_HEIGHT = 29;

	public JavaElementLabel(final Object element) {
		super(0, 0, 0, DEFAULT_HEIGHT);
		setBackgroundColor(null);
		addSubview(textView = new RichTextView(0, 0, 0, 0) {
			{
				setBackgroundColor(null);
				setDefaultFont(Resources.FONT_MONO);
				setDefaultColor(Resources.COLOR_TEXT_DEFAULT);
				setLinebreakMode(RichTextView.LINEBREAK_MODE_DISABLE);
			}
			@Override
			public RichTextView setText(RichTextBlock[] blocks) {
				super.setText(blocks);
				setWidth(getPreferredWidth());
				return this;
			}
		});
		setElement(element);
	}
	
	public void setElement(Object element) {
		if (!(element instanceof Element) &&
				!(element instanceof Jar)) {
				throw new IllegalArgumentException("JavaElementLabel only work with Jar and Element.");
			}
		this.element = element;
		textView.setText(getDisplayName(element));
		setNeedsRepaint();
	}
	
	private String getName() {
		if (element instanceof Jar) {
			Jar jar = (Jar) element;
			if (jar.isUnknown()) return "Unknown Jar";
			else return ((Jar) element).getName();
		}
		else return ((Element) element).getName();
	}

	private RichTextBlock[] getDisplayName(Object el) {
		ArrayList<RichTextBlock> rst = new ArrayList<RichTextBlock>();
		
		// basic name
		RichTextBlock textBlock = new RichTextBlock(getName());
		//textBlock.setFont(Resources.FONT_MONO);
		//textBlock.setColor(Resources.COLOR_TEXT_DEFAULT);
		
		if (element instanceof Method) {
			Method method = (Method) element;
			StringBuilder name = new StringBuilder();
			if (method.isConstructor()) name.append(method.getOwner().getName());
			else name.append(getName());
			
			name.append("(");
			// arguments
			String[] args = method.getArgumentNames();
			for (int i = 0; i < args.length; i++) {
				name.append(String.format("%s%s%s", args[i], method.isArgumentArray(i) ? "[]" : "",
						i == args.length - 1 ? "" : ", "));
			}
			
			name.append(")");
			textBlock.setText(name.toString());
			rst.add(textBlock);
			
			// return type
			if (!method.isConstructor()) {
				textBlock = new RichTextBlock(textBlock);
				textBlock.setText(String.format(" : %s%s", method.getReturnTypeName(), 
						method.isReturnArray() ? "[]" : ""));
				textBlock.setColor(Resources.COLOR_TEXT_GRAY);
				rst.add(textBlock);
			}
		} else if (element instanceof Field) {
			Field field = (Field) element;
			rst.add(textBlock);
			textBlock = new RichTextBlock(textBlock);
			textBlock.setText(String.format(" : %s%s", field.getTypeName(),
					field.isArray() ? "[]": ""));
			textBlock.setColor(Resources.COLOR_TEXT_GRAY);
			rst.add(textBlock);
		} else {
			rst.add(textBlock);
		}
		
		return rst.toArray(new RichTextBlock[0]);
	}
	
	public Object getElement() {
		return element;
	}
	
	@Override
	public View setSize(final int width, final int height) {
		super.setSize(width, height);
		if (textView != null) {
			textView.setPosition(30, 8);
			View.scaleViewWithBottom(textView, 0);
		}
		return this;
	}
	
	@Override
	protected void repaintView(GraphicsX g) {
		// draw icon
		if (element instanceof Jar) {
			g.drawImage(Resources.ICON_JAR, 0, 0);
			if (((Jar) element).isUnknown()) {
				g.drawImage(Resources.ICON_QUESTION, 0, 0);
			}
		} else if (element instanceof Package) {
			g.drawImage(Resources.ICON_PACKAGE, 0, 0);
			if (((Package) element).isUnknown()) {
				g.drawImage(Resources.ICON_QUESTION, 0, 0);
			}
		} else if (element instanceof cn.edu.buaa.sei.jdat.model.Type) {
			if (element instanceof cn.edu.buaa.sei.jdat.model.Class) {
				if (((cn.edu.buaa.sei.jdat.model.Class) element).getSuperclassName().equals("java.lang.Enum")) {
					g.drawImage(Resources.ICON_ENUM, 0, 0);
				} else {
					g.drawImage(Resources.ICON_CLASS, 0, 0);
				}
			} else if (element instanceof Interface) {
				g.drawImage(Resources.ICON_INTERFACE, 0, 0);
			} else {
				g.drawImage(Resources.ICON_UNKNOWN_TYPE, 0, 0);
			}
		} else if (element instanceof Method) {
			g.drawImage(Resources.ICON_METHOD, 0, 0);
			if (((Method) element).isConstructor()) {
				g.drawImage(Resources.ICON_CONSTRUCTOR, 0, 0);
			}
		} else if (element instanceof Field) {
			g.drawImage(Resources.ICON_FIELD, 0, 0);
		}
		
		if (element instanceof IModifier) {
			IModifier modifier = (IModifier) element;
			if (modifier.isAbstract() &&
				(element instanceof Class ||
				(element instanceof Member && !(((Member) element).getOwner() instanceof Interface)))) {
				g.drawImage(Resources.ICON_ABSTRACT, 0, 0);
			}
			if (modifier.isFinal()) g.drawImage(Resources.ICON_FINAL, 0, 0);
			if (modifier.isStatic()) g.drawImage(Resources.ICON_STATIC, 0, 0);
			if (modifier.getAccessModifiers() == IModifier.PROTECTED) {
				g.drawImage(Resources.ICON_PROTECTED, 0, 0);
			} else if (modifier.getAccessModifiers() == IModifier.DEFAULT) {
				g.drawImage(Resources.ICON_DEFAULT, 0, 0);
			} else if (modifier.getAccessModifiers() == IModifier.PRIVATE) {
				g.drawImage(Resources.ICON_PRIVATE, 0, 0);
			}
		}
	}
	
	public int getPreferredWidth() {
		return textView.getPreferredWidth() + 38;
	}
	
	private View tipView = null;
	
	@Override
	protected void mouseEntered() {
		if (textView.getRight() < 0) {
			// show tip
			final Vector pos = getPositionInRootView();
			final View rootView = textView.getViewContext().getRootView();
			rootView.addSubview(tipView = new View(0, 0, 0, 0) {
				{
					setPaintingEnabled(false);
					addSubview(new JavaElementLabel(element) {
						{
							setBackgroundColor(Resources.COLOR_DEFAULT_BACKGROUND);
							setFont(JavaElementLabel.this.getFont());
							setHeight(JavaElementLabel.this.getHeight());
							setWidth(getPreferredWidth());
							if (pos.x + getWidth() > rootView.getWidth()) pos.x = rootView.getWidth() - getWidth();
							if (pos.y + getHeight() > rootView.getHeight()) pos.y = rootView.getHeight() - getHeight();
							setPosition(pos);
						}
						@Override
						protected void repaintView(GraphicsX g) {
							super.repaintView(g);
							g.setColor(Resources.COLOR_LIGHT_BACKGROUND);
							g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
						}
					});
				}
			});
		}
	}
	
	@Override
	protected void mouseExited() {
		if (tipView != null) {
			tipView.removeFromSuperView();
			tipView = null;
		}
	}
	
	public Font getFont() {
		return textView.getDefaultFont();
	}
	
	public void setFont(Font font) {
		textView.setDefaultFont(font);
	}
	
}
