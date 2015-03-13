package cn.edu.buaa.sei.jdat.vt.widgets;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.View;
import com.cocotingo.snail.text.TextView;

public class JarListView extends ListView {
	
	public static class Item extends View {
		
		private final TextView nameView;
		private final TextView detailView;
		private final TextView statusView;
		private final ProgressBar progressBar;
		private final Button btnRemove;
		
		private boolean selected;
		private JarListView owner;
		private final String url;

		public Item(JarListView owner, final String url) {
			super(0, 0, 0, 55);
			this.url = url;
			this.owner = owner;
			selected = false;
			setBackgroundColor(null);
			setKeyboardFocusable(true);
			nameView = new TextView(0, 0, 0, 0) {
				{
					setText("<Jar Name>");
					setFont(Resources.FONT_BIG_PLAIN);
					setColor(Resources.COLOR_TEXT_DEFAULT);
					setBackgroundColor(null);
				}
			};
			detailView = new TextView(0, 0, 0, 0) {
				{
					setText(url);
					setFont(Resources.FONT_DEFAULT);
					setColor(Resources.COLOR_TEXT_GRAY);
					setBackgroundColor(null);
					setHidden(true);
					setAlignment(TextView.VERTICAL_BOTTOM);
				}
			};
			statusView = new TextView(0, 0, 0, 0) {
				{
					setText("<Status>");
					setFont(Resources.FONT_BIG_PLAIN);
					setAlignment(TextView.HORIZONTAL_RIGHT);
					setColor(Resources.COLOR_TEXT_GRAY);
					setBackgroundColor(null);
				}
			};
			progressBar = new ProgressBar(0, 0, 0, 9);
			btnRemove = new Button() {
				{
					setText("Remove");
					setSize(getPreferredSize());
					setHidden(true);
				}
				@Override
				protected void postMouseClicked(MouseEvent e) {
					removeButtonClicked();
				}
			};
			addSubview(nameView);
			addSubview(detailView);
			addSubview(statusView);
			addSubview(progressBar);
			addSubview(btnRemove);
			layout();
		}
		
		public void showRemoveButton() {
			btnRemove.setHidden(false);
			btnRemove.setAlpha(0.0f);
			new Animation(0.2f) {
				@Override
				protected void animation(float progress) {
					btnRemove.setAlpha(progress);
				}
				@Override
				protected void completed(boolean canceled) {
					btnRemove.setAlpha(1.0f);
				}
			}.commit();
		}
		
		public void hideRemoveButton() {
			btnRemove.setHidden(false);
			btnRemove.setAlpha(1.0f);
			new Animation(0.2f) {
				@Override
				protected void animation(float progress) {
					btnRemove.setAlpha(1.0f - progress);
				}
				@Override
				protected void completed(boolean canceled) {
					btnRemove.setHidden(true);
				}
			}.commit();
		}
		
		public String getURL() {
			return url;
		}
		
		@Override
		public void removeFromSuperView() {
			if (selected) {
				owner.selectedItemChanged(null);
			}
			owner = null;
			super.removeFromSuperView();
		}
		
		public JarListView getOwner() {
			return owner;
		}
		
		private void layout() {
			final int nameViewWidth = getWidth() - statusView.getPreferredSize().x - 30 - 15;
			nameView.setFrame(25, 8, nameViewWidth, getHeight() - 38);
			statusView.setFrame(nameViewWidth + 20, 8, getWidth() - nameViewWidth - 30, getHeight() - 38);
			progressBar.setFrame(10, getHeight() - 18, getWidth() - 20, 9);
			detailView.setFrame(10, getHeight() - 20, getWidth() - 20, 12);
			View.putViewWithHorizontalCenterAndVerticalCenter(btnRemove, getWidth() - btnRemove.getWidth() / 2 - ((getHeight() - btnRemove.getHeight()) / 2), getHeight() / 2);
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setSelected(boolean selected) {
			setBackgroundColor(selected ? Resources.COLOR_LIGHT_BACKGROUND : null);
			if (!this.selected && selected) {
				this.selected = true;
				for (View item : owner.getContentView()) {
					if (item != this) {
						((Item) item).setSelected(false);
					}
				}
				owner.selectedItemChanged(this);
			} else if (this.selected && !selected) {
				this.selected = false;
			}
		}
		
		@Override
		protected void postMousePressed(MouseEvent e) {
			e.handle();
			setSelected(true);
		}

		@Override
		protected void repaintView(GraphicsX g) {
			g.drawImage(Resources.ICON_JAR, 0, 2);
			g.setColor(Resources.COLOR_LINE);
			g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
		}
		
		@Override
		public View setSize(int width, int height) {
			super.setSize(width, height);
			if (nameView != null) {
				layout();
			}
			return this;
		}
		
		public TextView getNameView() {
			return nameView;
		}
		
		public TextView getDetailView() {
			return detailView;
		}
		
		public TextView getStatusView() {
			return statusView;
		}
		
		public ProgressBar getProgressBar() {
			return progressBar;
		}
		
		public void showDetailText() {
			detailView.setHidden(false);
			progressBar.setHidden(true);
		}
		
		public void showProgressBar() {
			detailView.setHidden(true);
			progressBar.setHidden(false);
		}
		
		public void setName(String name) {
			nameView.setText(name);
		}
		
		public void setStatus(String status) {
			statusView.setText(status);
			layout();
		}
		
		public void setDetailText(String detail) {
			detailView.setText(detail);
		}
		
		public void setProgress(float p) {
			progressBar.setProgress(p);
		}
		
		public void setProgress(double p) {
			progressBar.setProgress((float) p);
		}
		
		protected void removeButtonClicked() {
		}
		
	}

	public JarListView(int left, int top, int width, int height) {
		super(left, top, width, height);
	}
	
	public Item getItem(int index) {
		return (Item) getContentView().getSubview(index);
	}
	
	public Item getItem(String url) {
		for (View view : getContentView()) {
			Item item = (Item) view;
			if (item.getURL().equals(url)) {
				return item;
			}
		}
		return null;
	}
	
	public Item getSelectedItem() {
		for (View view : getContentView()) {
			Item item = (Item) view;
			if (item.isSelected()) {
				return item;
			}
		}
		return null;
	}
	
	protected void selectedItemChanged(Item item) {}

	public void showRemoveButtons() {
		for (View view : getContentView()) {
			Item item = (Item) view;
			item.showRemoveButton();
		}
	}
	
	public void hideRemoveButtons() {
		for (View view : getContentView()) {
			Item item = (Item) view;
			item.hideRemoveButton();
		}
	}
	
}
