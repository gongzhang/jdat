package cn.edu.buaa.sei.jdat.vt;

import java.awt.Cursor;

import com.cocotingo.snail.View;

import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.model.Package;
import cn.edu.buaa.sei.jdat.model.Type;
import cn.edu.buaa.sei.jdat.vt.widgets.Bubble;
import cn.edu.buaa.sei.jdat.vt.widgets.DragView;
import cn.edu.buaa.sei.jdat.vt.widgets.JavaElementListView;
import cn.edu.buaa.sei.jdat.vt.widgets.JarListView.Item;


public class NavigateView extends MainView.Tab {
	
	// controls
	private final Bubble titleBubble;
	private final Bubble typeBubble;
	private final JavaElementListView packageList;
	private final JavaElementListView typeList;
	private final JavaElementListView memberList;
	private final DragView hSpliter, vSpliter;
	
	// layout
	private int margin = 5;
	private int listViewWidth = 260;
	private int packageListViewHeight = 220;

	public NavigateView(MainView owner) {
		super(owner);
		addSubview(titleBubble = new Bubble(0, 0, 0, 0) {
			{
				setHeight(getPreferredSize().y);
				makeBrown();
			}
		});
		addSubview(packageList = new JavaElementListView(0, 0, 0, 0) {
			@Override
			protected void selectedItemChanged(Item item) {
				// show types
				if (item != null) {
					typeList.setElements(((Package) item.getElement()).getTypes().toArray());
					memberList.removeAllElements();
				}
			}
		});
		addSubview(typeList = new JavaElementListView(0, 0, 0, 0) {
			@Override
			protected void selectedItemChanged(Item item) {
				// show type members
				if (item != null) {
					typeBubble.setText(((Type) item.getElement()).getSignature());
					memberList.setElements(((Type) item.getElement()).getMembers().toArray());
				} else {
					typeBubble.setText("No Type Selected");
					memberList.removeAllElements();
				}
			}
		});
		addSubview(typeBubble = new Bubble(0, 0, 0, 0) {
			{
				setHeight(getPreferredSize().y);
				makeBrown();
			}
		});
		addSubview(memberList = new JavaElementListView(0, 0, 0, 0) {
		});
		addSubview(hSpliter = new DragView(0, 0, 0, 0) {
			{
				setPaintingEnabled(false);
			}
			protected void mouseEntered() {
				getViewContext().getAWTContainer().setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			}
			protected void mouseExited() {
				getViewContext().getAWTContainer().setCursor(Cursor.getDefaultCursor());
			}
			@Override
			protected boolean horizonalPositionWillChange(int dx) { return false; }
			@Override
			protected boolean verticalPositionWillChange(int dy) {
				packageListViewHeight += dy;
				layout();
				return false;
			}
			@Override
			protected void postMouseReleased(com.cocotingo.snail.MouseEvent e) {
				super.postMouseReleased(e);
				packageListViewHeight = packageList.getHeight();
			}
		});
		addSubview(vSpliter = new DragView(0, 0, 0, 0) {
			{
				setPaintingEnabled(false);
			}
			protected void mouseEntered() {
				getViewContext().getAWTContainer().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			}
			protected void mouseExited() {
				getViewContext().getAWTContainer().setCursor(Cursor.getDefaultCursor());
			}
			@Override
			protected boolean horizonalPositionWillChange(int dx) {
				listViewWidth += dx;
				layout();
				return false;
			}
			@Override
			protected boolean verticalPositionWillChange(int dy) { return false; }
			@Override
			protected void postMouseReleased(com.cocotingo.snail.MouseEvent e) {
				super.postMouseReleased(e);
				listViewWidth = typeList.getWidth();
			}
		});
	}

	public void showJar(Jar jar) {
		clear();
		titleBubble.setText(jar.getName());
		
		// show package
		packageList.setElements(jar.getPackages().toArray());
	}
	
	public void clear() {
		titleBubble.setText("No Jar Selected");
		packageList.removeAllElements();
		typeList.removeAllElements();
		typeBubble.setText("No Type Selected");
		memberList.removeAllElements();
	}

	@Override
	protected void activated() {
		if (getOwner().getStateManager().isIdle()) {
			Item selectedItem = getOwner().getJarListView().getSelectedItem();
			if (selectedItem != null) {
				showJar(getOwner().getJarController().getJar(selectedItem.getIndex()));
			} else {
				clear();
			}
		}
	}

	@Override
	protected void deactivated() {
	}
	
	@Override
	protected void layout() {
		// limitation
		int listViewWidth = this.listViewWidth;
		if (listViewWidth < 150) listViewWidth = 150;
		else if (getWidth() - listViewWidth < 300) listViewWidth = getWidth() - 300;
		int packageListViewHeight = this.packageListViewHeight;
		if (packageListViewHeight < 100) packageListViewHeight = 100;
		else if (getHeight() - packageListViewHeight < 140) packageListViewHeight = getHeight() - 140;
		
		// layout
		titleBubble.setPosition(-1, 0);
		titleBubble.setWidth(listViewWidth + 2);
		packageList.setPosition(0, titleBubble.getTop() + titleBubble.getHeight() + margin);
		packageList.setSize(listViewWidth, packageListViewHeight);
		typeList.setPosition(0, packageList.getTop() + packageListViewHeight + margin);
		typeList.setWidth(listViewWidth);
		View.scaleViewWithBottom(typeList, 0);
		
		typeBubble.setPosition(listViewWidth + margin - 1, 0);
		View.scaleViewWithRight(typeBubble, -1);
		memberList.setPosition(listViewWidth + margin, typeBubble.getTop() + typeBubble.getHeight() + margin);
		View.scaleViewWithRightAndBottom(memberList, 0, 0);
		
		hSpliter.setPosition(0, titleBubble.getHeight() + packageListViewHeight + margin);
		hSpliter.setSize(listViewWidth, margin);
		vSpliter.setPosition(listViewWidth, 0);
		View.scaleViewWithBottom(vSpliter, 0);
		vSpliter.setWidth(margin);
	}

}
