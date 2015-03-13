package cn.edu.buaa.sei.jdat.vt;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cn.edu.buaa.sei.jdat.model.Class;
import cn.edu.buaa.sei.jdat.model.Element;
import cn.edu.buaa.sei.jdat.model.Field;
import cn.edu.buaa.sei.jdat.model.Interface;
import cn.edu.buaa.sei.jdat.model.Member;
import cn.edu.buaa.sei.jdat.model.Method;
import cn.edu.buaa.sei.jdat.model.Package;
import cn.edu.buaa.sei.jdat.model.Type;
import cn.edu.buaa.sei.jdat.search.IndexItem;
import cn.edu.buaa.sei.jdat.search.Query;
import cn.edu.buaa.sei.jdat.search.SearchController;
import cn.edu.buaa.sei.jdat.search.exception.InvalidQueryException;
import cn.edu.buaa.sei.jdat.vt.MainView.Tab;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.Button;
import cn.edu.buaa.sei.jdat.vt.widgets.CheckBox;
import cn.edu.buaa.sei.jdat.vt.widgets.JavaElementLabel;
import cn.edu.buaa.sei.jdat.vt.widgets.JavaElementListView;
import cn.edu.buaa.sei.jdat.vt.widgets.TextBox;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.dispatcher.DispatchQueue;
import com.cocotingo.snail.dispatcher.Dispatcher;
import com.cocotingo.snail.text.TextView;


public class SearchView extends Tab {
	
	class SearchTextBox extends View {
		
		final TextBox textBox;
		final Button btnSearch;

		public SearchTextBox(int left, int top, int width, int height) {
			super(left, top, width, height);
			setPaintingEnabled(false);
			addSubview(textBox = new TextBox() {
				{
					setFont(Resources.FONT_BIG_PLAIN);
					getTextField().getDocument().addDocumentListener(new DocumentListener() {
						@Override
						public void removeUpdate(DocumentEvent arg0) {
							textChanged(getText());
						}
						@Override
						public void insertUpdate(DocumentEvent arg0) {
							textChanged(getText());
						}
						@Override
						public void changedUpdate(DocumentEvent arg0) {
							textChanged(getText());
						}
					});
					getTextField().addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							if (e.getKeyChar() == '\n') {
								doSearch(getText());
							}
						}
					});
				}
			});
			addSubview(btnSearch = new Button(0, 0, 0, 0) {
				{
					setText("Search");
					setSize(getPreferredSize());
					setWidth(getWidth() + 48);
				}
				@Override
				protected void postMouseClicked(com.cocotingo.snail.MouseEvent e) {
					doSearch(textBox.getText());
				}
			});
		}
		
		@Override
		public View setSize(int width, int height) {
			super.setSize(width, height);
			if (textBox != null) {
				btnSearch.setHeight(height);
				btnSearch.setLeft(width - btnSearch.getWidth());
				textBox.setHeight(height);
				textBox.setWidth(btnSearch.getLeft() - 4);
			}
			return this;
		}
		
		private void textChanged(String text) {
			SearchView.this.textChanged(text);
		}

		private void doSearch(String text) {
			SearchView.this.doSearch(text);
		}

	}
	
	class OptionView extends View {
		
		class LabelCheckBox extends CheckBox {

			public LabelCheckBox(final String text) {
				super();
				setLabelView(new TextView(0, 0, 0, 0) {
					{
						setBackgroundColor(null);
						setFont(Resources.FONT_LABEL);
						setColor(Resources.COLOR_TEXT_DEFAULT);
						setText(text);
						setSize(getPreferredSize());
						setSelected(true);
					}
				});
				setSize(getPreferredSize());
			}
			
			@Override
			public CheckBox setSelected(boolean selected) {
				super.setSelected(selected);
				checkBoxValueChanged();
				return this;
			}
			
		}
		
		final CheckBox chkPackage, chkClass, chkInterface, chkField, chkMethod;
		private boolean ready = false;
		
		public OptionView(int left, int top, int width, int height) {
			super(left, top, width, height);
			setPaintingEnabled(false);
			addSubview(chkPackage = new LabelCheckBox("Package"));
			addSubview(chkClass = new LabelCheckBox("Class"));
			addSubview(chkInterface = new LabelCheckBox("Interface"));
			addSubview(chkField = new LabelCheckBox("Field"));
			addSubview(chkMethod = new LabelCheckBox("Method"));
			setSize(getPreferredSize());
			ready = true;
		}
		
		public Vector getPreferredSize() {
			Vector v = new Vector();
			chkPackage.setLeft(v.x);
			v.x += chkPackage.getWidth() + 8;
			chkClass.setLeft(v.x);
			v.x += chkClass.getWidth() + 8;
			chkInterface.setLeft(v.x);
			v.x += chkInterface.getWidth() + 8;
			chkField.setLeft(v.x);
			v.x += chkField.getWidth() + 8;
			chkMethod.setLeft(v.x);
			v.x += chkMethod.getWidth();
			v.y = chkPackage.getHeight();
			return v;
		}
		
		@Override
		public View setSize(int width, int height) {
			super.setSize(width, height);
			if (chkPackage != null) {
				int curLeft = 0;
				chkPackage.setLeft(curLeft);
				curLeft += chkPackage.getWidth() + 8;
				chkClass.setLeft(curLeft);
				curLeft += chkClass.getWidth() + 8;
				chkInterface.setLeft(curLeft);
				curLeft += chkInterface.getWidth() + 8;
				chkField.setLeft(curLeft);
				curLeft += chkField.getWidth() + 8;
				chkMethod.setLeft(curLeft);
				curLeft += chkMethod.getWidth() + 8;
			}
			return this;
		}
		
		protected void checkBoxValueChanged() {
			if (ready) optionChanged();
		}
		
	}
	
	class DetailsView extends View {
		
		private JavaElementLabel jarLabel, packageLabel, typeLabel, memberLabel;

		public DetailsView(int left, int top, int width, int height) {
			super(left, top, width, height);
			setBackgroundColor(null);
		}
		
		public void setElement(Object element) {
			if (jarLabel != null) {
				jarLabel.removeFromSuperView();
				jarLabel = null;
			}
			if (packageLabel != null) {
				packageLabel.removeFromSuperView();
				packageLabel = null;
			}
			if (typeLabel != null) {
				typeLabel.removeFromSuperView();
				typeLabel = null;
			}
			if (memberLabel != null) {
				memberLabel.removeFromSuperView();
				memberLabel = null;
			}
			if (element == null) return;
			if (element instanceof Member) {
				memberLabel = new JavaElementLabel(element);
				Type type = ((Member) element).getOwner();
				typeLabel = new JavaElementLabel(type);
				Package package1 = type.getOwner();
				packageLabel = new JavaElementLabel(package1);
				jarLabel = new JavaElementLabel(package1.getOwner());
			} else if (element instanceof Type) {
				typeLabel = new JavaElementLabel(element);
				Package package1 = ((Type) element).getOwner();
				packageLabel = new JavaElementLabel(package1);
				jarLabel = new JavaElementLabel(package1.getOwner());
			} else if (element instanceof Package) {
				packageLabel = new JavaElementLabel(element);
				jarLabel = new JavaElementLabel(((Package) element).getOwner());
			}
			if (jarLabel != null) addSubview(jarLabel);
			if (packageLabel != null) addSubview(packageLabel);
			if (typeLabel != null) addSubview(typeLabel);
			if (memberLabel != null) addSubview(memberLabel);
			layout();
		}
		
		@Override
		protected void repaintView(GraphicsX g) {
			g.drawStrechableImage(Resources.IMG_DEPTH_BACKGROUND, getWidth(), getHeight());
		}
		
		@Override
		public View setSize(int width, int height) {
			super.setSize(width, height);
			layout();
			return this;
		}
		
		private void layout() {
			if (jarLabel != null) {
				jarLabel.setPosition(2, 2);
				View.scaleViewWithRight(jarLabel, 2);
			}
			if (packageLabel != null) {
				packageLabel.setPosition(2 + 30, 2 + JavaElementLabel.DEFAULT_HEIGHT);
				View.scaleViewWithRight(packageLabel, 2);
			}
			if (typeLabel != null) {
				typeLabel.setPosition(2 + 30 * 2, 2 + JavaElementLabel.DEFAULT_HEIGHT * 2);
				View.scaleViewWithRight(typeLabel, 2);
			}
			if (memberLabel != null) {
				memberLabel.setPosition(2 + 30 * 3, 2 + JavaElementLabel.DEFAULT_HEIGHT * 3);
				View.scaleViewWithRight(memberLabel, 2);
			}
		}
		
	}
	
	private SearchTextBox textBox;
	private OptionView optionView;
	private JavaElementListView resultList;
	private DetailsView detailsView;
	
	private Query query;
	private Object[] currentResult;
	
	private static final int LIST_MAX_SIZE = 256;
	private final DispatchQueue search_queue;
	
	public SearchView(MainView owner) {
		super(owner);
		query = new Query();
		search_queue = new DispatchQueue();
		addSubview(textBox = new SearchTextBox(0, 0, 0, 0));
		addSubview(optionView = new OptionView(0, 0, 0, 0));
		addSubview(resultList = new JavaElementListView(0, 0, 0, 0) {
			protected void selectedItemChanged(Item item) {
				if (item == null) detailsView.setElement(null);
				else detailsView.setElement(item.getElement());
			}
		});
		addSubview(detailsView = new DetailsView(0, 0, 0, 0));
	}
	
	private void textChanged(final String text) {
		
		// Execute "search" in another thread, so it won't block the ui.
		
		search_queue.execute(new Runnable() {
			@Override
			public void run() {
				
				SearchController sc = getOwner().getSearchController();
				query.set(text);
				try {
					query.validate();
				} catch (InvalidQueryException e) {
					// clear list
					Dispatcher.executeAndWaitAndSyncWithGUI(new Runnable() {
						@Override
						public void run() {
							currentResult = null;
							resultList.removeAllElements();
						}
					});
				}
				List<IndexItem> result = sc.getHints(query);
				if (result == null)
					return;
				
				int allcount = 0;
				for (IndexItem it : result) {
					allcount += it.size();
				}

				int count = 0;
				final Object[] items = new Object[allcount];
				for (IndexItem it : result) {
					for (Element e : it) {
						items[count] = e;
						count++;
					}
				}
				
				// update ui.
				Dispatcher.executeAndWaitAndSyncWithGUI(new Runnable() {
					@Override
					public void run() {
						currentResult = items;
						showCurrentResult();
					}
				});
			}
		});
		
	}
	
	private void showCurrentResult() {
		if (currentResult != null) {
			ArrayList<Object> rst = new ArrayList<Object>();
			for (Object object : currentResult) {
				if (object instanceof Package && optionView.chkPackage.isSelected()) rst.add(object);
				else if (object instanceof Class && optionView.chkClass.isSelected()) rst.add(object);
				else if (object instanceof Interface && optionView.chkInterface.isSelected()) rst.add(object);
				else if (object instanceof Field && optionView.chkField.isSelected()) rst.add(object);
				else if (object instanceof Method && optionView.chkMethod.isSelected()) rst.add(object);
				if (rst.size() == LIST_MAX_SIZE) break;
			}
			resultList.setElements(rst.toArray());
		}
	}

	private void doSearch(final String text) {
		
		search_queue.execute(new Runnable() {
			@Override
			public void run() {
				
				SearchController sc = getOwner().getSearchController();
				query.set(text);
				try {
					query.validate();
				} catch (InvalidQueryException e) {
					// clear list
					Dispatcher.executeAndWaitAndSyncWithGUI(new Runnable() {
						@Override
						public void run() {
							currentResult = null;
							resultList.removeAllElements();
						}
					});
				}
				List<IndexItem> result = sc.search(query);
				if (result == null)
					return;
				
				int allcount = 0;
				for (IndexItem it : result) {
					allcount += it.size();
				}

				int count = 0;
				final Object[] items = new Object[allcount];
				for (IndexItem it : result) {
					for (Element e : it) {
						items[count] = e;
						count++;
					}
				}
				
				// update ui.
				Dispatcher.executeAndWaitAndSyncWithGUI(new Runnable() {
					@Override
					public void run() {
						currentResult = items;
						showCurrentResult();
					}
				});
			}
		});
	}
	
	private void optionChanged() {
		showCurrentResult();
	}
	
	@Override
	protected void activated() {
		if (getOwner().getStateManager().isIdle()) {
			textBox.textBox.requestKeyboardFocus();
		}
	}

	@Override
	protected void deactivated() {
	}
	
	public void clear() {
		textBox.textBox.setText("");
	}

	@Override
	protected void layout() {
		textBox.setHeight(41);
		textBox.setWidth(Math.max(getWidth() * 4 / 6, 500));
		textBox.setTop(16);
		textBox.setLeft((getWidth() - textBox.getWidth()) / 2);
		
		optionView.setPosition((getWidth() - optionView.getWidth()) / 2, textBox.getTop() + textBox.getHeight() + 8);
		
		detailsView.setSize(textBox.getWidth(), JavaElementLabel.DEFAULT_HEIGHT * 4 + 4);
		detailsView.setPosition(textBox.getLeft(), getHeight() - detailsView.getHeight());

		resultList.setWidth(textBox.getWidth());
		resultList.setPosition(textBox.getLeft(), optionView.getTop() + optionView.getHeight() + 8);
		View.scaleViewWithBottom(resultList, detailsView.getHeight() + 8);
	}

}
