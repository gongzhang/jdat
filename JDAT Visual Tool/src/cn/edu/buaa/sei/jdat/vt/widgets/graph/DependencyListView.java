package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.View;

import cn.edu.buaa.sei.jdat.model.Element;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.ListView;

public class DependencyListView extends ListView {
	
	public class Item extends DependencyLabel {
		
		private boolean selected;

		public Item(Element e1, Element e2) {
			super(e1, e2);
			selected = false;
			setKeyboardFocusable(true);
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setSelected(boolean selected) {
			setBackgroundColor(selected ? Resources.COLOR_LIGHT_BACKGROUND : null);
			if (!this.selected && selected) {
				this.selected = true;
				for (View item : DependencyListView.this.getContentView()) {
					if (item != this) {
						((Item) item).setSelected(false);
					}
				}
				DependencyListView.this.selectedItemChanged(this);
			} else if (this.selected && !selected) {
				this.selected = false;
			}
		}
		
		@Override
		public void removeFromSuperView() {
			super.removeFromSuperView();
			if (selected) {
				DependencyListView.this.selectedItemChanged(null);
			}
		}
		
		@Override
		protected void postMousePressed(MouseEvent e) {
			e.handle();
			setSelected(true);
			requestKeyboardFocus();
		}
	}

	public DependencyListView(int left, int top, int width, int height) {
		super(left, top, width, height);
		setPaintingEnabled(false);
	}
	
	public void addDependency(Element e1, Element e2) {
		Item item = new Item(e1, e2);
		getContentView().addSubview(item);
		layout();
	}
	
	protected void selectedItemChanged(Item item) {
		
	}

}
