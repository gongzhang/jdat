package cn.edu.buaa.sei.jdat.vt.widgets;

import java.util.ArrayList;
import java.util.List;

import cn.edu.buaa.sei.jdat.model.Element;
import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.View;

public class JavaElementListView extends ListView {
	
	private static final List<Item> itemPool = new ArrayList<JavaElementListView.Item>();
	private static final int ITEM_POOL_CAPACITY = 256;
	
	private static Item getItemFromPool(JavaElementListView owner, Object elememt) {
		if (itemPool.size() > 0) {
			Item item = itemPool.remove(itemPool.size() - 1);
			item.setSelected(false);
			item.setOwner(owner);
			item.setElement(elememt);
			return item;
		} else return new Item(owner, elememt);
	}
	
	private static void returnItemToPool(Item item) {
		if (itemPool.size() < ITEM_POOL_CAPACITY) {
			itemPool.add(item);
		}
	}
	
	public static class Item extends JavaElementLabel {
		
		private JavaElementListView owner;
		private boolean selected;
		
		private Item(JavaElementListView owner, final Object element) {
			super(element);
			this.owner = owner;
			selected = false;
			setKeyboardFocusable(true);
		}
		
		private void setOwner(JavaElementListView owner) {
			this.owner = owner;
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setSelected(boolean selected) {
			setBackgroundColor(selected ? Resources.COLOR_LIGHT_BACKGROUND : null);
			if (!this.selected && selected) {
				this.selected = true;
				if (owner != null) {
					for (View item : owner.getContentView()) {
						if (item != this) {
							((Item) item).setSelected(false);
						}
					}
					owner.selectedItemChanged(this);
				}
			} else if (this.selected && !selected) {
				this.selected = false;
			}
		}
		
		@Override
		public void removeFromSuperView() {
			super.removeFromSuperView();
			if (selected) {
				owner.selectedItemChanged(null);
			}
			owner = null;
			returnItemToPool(this);
		}
		
		@Override
		protected void postMousePressed(MouseEvent e) {
			e.handle();
			setSelected(true);
			requestKeyboardFocus();
		}
		
		@Override
		public String toString() {
			return "[" + getElement().toString() + "]";
		}
		
	}

	public JavaElementListView(int left, int top, int width, int height) {
		super(left, top, width, height);
	}
	
	public void addElement(Object element) {
		getContentView().addSubview(getItemFromPool(this, element));
		layout();
	}
	
	public void removeAllElements() {
		getContentView().removeAllSubviews();
		layout();
	}
	
	public void setElements(Object[] elements) {
		int c = getContentView().count();
		if (c > elements.length) {
			// remove
			for (int i = c - 1; i >= elements.length; i--) {
				getContentView().getSubview(i).removeFromSuperView();
			}
			c = elements.length;
		}
		// not enough
		for (int i = c; i < elements.length; i++) {
			addElement(elements[i]);
		}
		// reset exist
		for (int i = 0; i < c; i++) {
			Item item = ((Item) getContentView().getSubview(i));
			item.setElement(elements[i]);
			item.setSelected(false);
		}
		layout();
	}
	
	public Item getItem(int index) {
		return (Item) getContentView().getSubview(index);
	}
	
	public int indexOf(Element element) {
		for (View view : getContentView()) {
			Item item = (Item) view;
			if (item.getElement() == element) {
				return item.getIndex();
			}
		}
		return -1;
	}
	
	protected void selectedItemChanged(Item item) {}

}
