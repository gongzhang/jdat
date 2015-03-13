package cn.edu.buaa.sei.jdat.vt;

import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Point;

import javax.swing.JDialog;

import cn.edu.buaa.sei.jdat.JarController;
import cn.edu.buaa.sei.jdat.JarController.ProgressMonitor;
import cn.edu.buaa.sei.jdat.exception.InvalidURIException;
import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.search.SearchController;
import cn.edu.buaa.sei.jdat.vt.StateManager.StateListener;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.Button;
import cn.edu.buaa.sei.jdat.vt.widgets.DragView;
import cn.edu.buaa.sei.jdat.vt.widgets.JarListView;
import cn.edu.buaa.sei.jdat.vt.widgets.JarListView.Item;
import cn.edu.buaa.sei.jdat.vt.widgets.TabButton;
import cn.edu.buaa.sei.jdat.vt.widgets.ViewAlphaAnimation;

import com.cocotingo.snail.Animation;
import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.KeyEvent;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.MouseWheelEvent;
import com.cocotingo.snail.View;
import com.cocotingo.snail.dispatcher.Dispatcher;

public class MainView extends View implements StateListener {
	
	public static abstract class Tab extends View {
		
		private final MainView owner;

		public Tab(MainView owner) {
			super(0, 0, 0, 0);
			this.owner = owner;
			setHidden(true);
			setPaintingEnabled(false);
		}
		
		public MainView getOwner() {
			return owner;
		}
		
		public boolean isActivated() {
			return !isHidden();
		}
		
		@Override
		public View setHidden(boolean hidden) {
			if (!hidden) {
				layout();
			}
			return super.setHidden(hidden);
		}
		
		@Override
		public View setSize(int width, int height) {
			super.setSize(width, height);
			if (isActivated()) {
				layout();
			}
			return this;
		}
		
		protected abstract void activated();
		protected abstract void deactivated();
		protected abstract void layout();
		
		@Override
		protected void preKeyPressed(KeyEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preKeyReleased(KeyEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preKeyTyped(KeyEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preMouseClicked(MouseEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preMouseDragged(MouseEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preMouseMoved(MouseEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preMousePressed(MouseEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preMouseReleased(MouseEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		@Override
		protected void preMouseWheelMoved(MouseWheelEvent e) { if (!getOwner().getStateManager().isIdle()) e.handle(); }
		
	}
	
	// controllers
	private final JarController jarController;
	private final SearchController searchController;
	
	// controls
	private final JarListView jarListView;
	private final DragView spliter;
	private final Button btnImportJars, btnEditJarList, btnRemoveAllJars, btnGC;
	private final TabButton tabMain;
	private final NavigateView navigateView;
	private final DependencyView dependencyView;
	private final SearchView searchView;
	
	// state
	private final StateManager stateManager;
	private int jarListState = JAR_LIST_STATE_NORMAL;
	private static final int JAR_LIST_STATE_NORMAL = 0;
	private static final int JAR_LIST_STATE_EDITING = 1;
	
	// layout vars
	private int margin = 8;
	private int jarlistWidth = 300;
	private int buttonHeight = 40;

	public MainView() {
		super(0, 0, 0, 0);
		setKeyboardFocusable(true);
		stateManager = new StateManager();
		stateManager.addListener(this);
		
		// define controls
		jarListView = new JarListView(0, 0, 0, 0) {
			@Override
			protected void selectedItemChanged(Item item) {
				if (item != null) {
					final Jar jar = jarController.getJar(item.getIndex());
					if (getStateManager().isIdle()) {
						if (navigateView.isActivated()) {
							navigateView.showJar(jar);
						} else if (dependencyView.isActivated()) {
							dependencyView.highlightJar(jar);
						}
					}
				} else {
					navigateView.clear();
				}
			}
		};
		
		spliter = new DragView(0, 0, 0, 0) {
			{
				setPaintingEnabled(false);
			}
			@Override
			protected void mouseEntered() {
				getViewContext().getAWTContainer().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			}
			@Override
			protected void mouseExited() {
				getViewContext().getAWTContainer().setCursor(Cursor.getDefaultCursor());
			}
			@Override
			protected boolean horizonalPositionWillChange(int dx) {
				jarlistWidth += dx;
				layout();
				return false;
			}
			@Override
			protected boolean verticalPositionWillChange(int dy) { return false; }
			protected void postMousePressed(MouseEvent e) {
				super.postMousePressed(e);
				jarlistWidth = jarListView.getWidth();
			}
		};
		
		btnImportJars = new Button(0, 0, 0, 0) {
			{
				setText("Import Jars");
			}
			@Override
			protected void postMouseClicked(MouseEvent e) {
				e.handle();
				btnImportJarClicked();
			}
		};
		
		btnEditJarList = new Button(0, 0, 0, 0) {
			{
				setText("Edit");
			}
			@Override
			protected void postMouseClicked(MouseEvent e) {
				e.handle();
				btnEditJarListClicked();
			}
		};
		
		btnRemoveAllJars = new Button(0, 0, 0, 0) {
			{
				setText("Remove All");
				setHidden(true);
			}
			@Override
			protected void postMouseClicked(MouseEvent e) {
				e.handle();
				btnRemoveAllJarsClicked();
			}
		};
		
		btnGC = new Button(0, 0, 0, 0) {
			{
				setText("GC");
			}
			@Override
			protected void postMouseClicked(MouseEvent e) {
				e.handle();
				System.gc();
			}
		};
		
		tabMain = new TabButton() {
			{
				setTexts(new String[] {"Navigate", "Dependency Graph", "Search"});
			}
			@Override
			protected void selectedTabChanged(int selectedIndex) {
				MainView.this.selectedTabChanged(selectedIndex);
			}
		};
		
		navigateView = new NavigateView(this) {
		};
		dependencyView = new DependencyView(this) {
		};
		searchView = new SearchView(this) {
		};
		
		// add to root view
		addSubview(jarListView);
		addSubview(spliter);
		addSubview(btnImportJars);
		addSubview(btnEditJarList);
		addSubview(btnRemoveAllJars);
		addSubview(btnGC);
		addSubview(tabMain);
		addSubview(navigateView);
		addSubview(dependencyView);
		addSubview(searchView);
		tabMain.setSelectedIndex(0);
		layout();
		
		// create controller
		jarController = new JarController() {
			
			@Override
			protected void importJarStarted(final Jar jar) {
				Dispatcher.executeAndSyncWithGUI(new Runnable() {
					@Override
					public void run() {
						getStateManager().startImportTask(jar.getURI());
						JarListView.Item item = new JarListView.Item(jarListView, jar.getURI()) {
							@Override
							protected void removeButtonClicked() {
								removeJarFromJarListItem(this);
							}
						};
						jarListView.getContentView().addSubview(item);
						jarListView.layout();
					}
				});
			}

			@Override
			protected void importJarFinished(final Jar jar) {
				synchronized (searchController) {
					searchController.addElements(jar);
				}
				Dispatcher.executeAndSyncWithGUI(new Runnable() {
					@Override
					public void run() {
						getStateManager().finishImportTask(jar.getURI());
					}
				});
			}

			@Override
			protected void removeJarStarted(final Jar jar) {
				synchronized (searchController) {
					searchController.removeElements(jar);
				}
			}

			@Override
			protected void removeJarFinished(Jar jar) {
			}

			@Override
			protected void jarStateChanged(final Jar jar, final int state) {
				Dispatcher.executeAndSyncWithGUI(new Runnable() {
					@Override
					public void run() {
						final JarListView.Item item = jarListView.getItem(jar.getURI());
						switch (state) {
						case Jar.LOADING:
							item.setName("");
							item.setStatus("");
							item.setDetailText(jar.getURI());
							item.setStatus("Downloading");
							item.showProgressBar();
							break;
						case Jar.PARSING:
							item.setProgress(0.0f);
							item.setName(jar.getName());
							item.setStatus("Parsing");
							break;
						case Jar.BUILDING_STRUCTURE:
							item.setProgress(0.0f);
							break;
						case Jar.BUILDING_DEPENDENCY:
							item.setProgress(0.0f);
							item.setStatus("Analyzing");
							break;
						case Jar.READY:
							item.setStatus("");
							item.showDetailText();
							break;
						case Jar.ERROR:
							getStateManager().finishImportTask(jar.getURI());
							item.setName(jar.getName());
							item.setStatus("Error");
							item.setDetailText(jar.getException().getMessage());
							item.showDetailText();
							break;
						}
					}
				});
			}
			
		};
		
		searchController = new SearchController();
	}
	
	private void layout() {
		// limitation
		int jarlistWidth = this.jarlistWidth;
		if (jarlistWidth < 200) jarlistWidth = 200;
		else if (getWidth() - jarlistWidth < 600) jarlistWidth = getWidth() - 600;
		
		// layout
		btnEditJarList.setSize(80, buttonHeight);
		btnImportJars.setFrame(margin, margin, jarlistWidth - margin - btnEditJarList.getWidth(), buttonHeight);
		btnEditJarList.setPosition(margin * 2 + btnImportJars.getWidth(), margin);
		btnRemoveAllJars.setPosition(btnImportJars.getPosition());
		btnRemoveAllJars.setSize(btnImportJars.getSize());
		
		jarListView.setPosition(margin, margin * 2 + btnImportJars.getHeight());
		jarListView.setWidth(jarlistWidth);
		View.scaleViewWithBottom(jarListView, margin);
		
		spliter.setPosition(margin + jarlistWidth, 2 * margin + buttonHeight);
		spliter.setSize(margin, getHeight() - buttonHeight - 3 * margin);
		
		tabMain.setSize(Math.max((getWidth() - jarlistWidth - margin * 3) * 7 / 10, 550), buttonHeight);
		tabMain.setPosition(margin + jarlistWidth + (jarListView.getRight() - tabMain.getWidth()) / 2, margin);
		
		navigateView.setPosition(jarListView.getWidth() + 2 * margin, tabMain.getHeight() + 2 * margin);
		View.scaleViewWithRightAndBottom(navigateView, margin, margin);
		dependencyView.setPosition(navigateView.getPosition());
		dependencyView.setSize(navigateView.getSize());
		searchView.setPosition(navigateView.getPosition());
		searchView.setSize(navigateView.getSize());
		
		btnGC.setHidden(tabMain.getRight() < buttonHeight + margin * 2);
		btnGC.setPosition(getWidth() - buttonHeight - margin, margin);
		btnGC.setSize(buttonHeight, buttonHeight);
		
		setNeedsRepaint();
	}
	
	@Override
	protected void postMousePressed(MouseEvent e) {
		requestKeyboardFocus();
	}

	@Override
	protected void repaintView(GraphicsX g) {
		Resources.fillBackground(g, getWidth(), getHeight());
	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		if (jarListView != null) {
			layout();
		}
		return this;
	}
	
	public JarController getJarController() {
		return jarController;
	}
	
	public SearchController getSearchController() {
		return searchController;
	}
	
	public JarListView getJarListView() {
		return jarListView;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}
	
	//// delegates ////
	
	@Override
	protected void preKeyPressed(KeyEvent e) {
		// debug mode
		if (e.getKeyChar() == 'd') {
			getViewContext().setDebugMode(!getViewContext().isDebugMode());
		} else if (getViewContext().isDebugMode()) {
			View view = getViewContext().getDebugTarget();
			if (view == null) return;
			if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
				if (e.getAWTEvent().isShiftDown()) view.setHeight(view.getHeight() - 1);
				else view.setTop(view.getTop() - 1);
			} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
				if (e.getAWTEvent().isShiftDown()) view.setHeight(view.getHeight() + 1);
				else view.setTop(view.getTop() + 1);
			} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT) {
				if (e.getAWTEvent().isShiftDown()) view.setWidth(view.getWidth() - 1);
				else view.setLeft(view.getLeft() - 1);
			} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT) {
				if (e.getAWTEvent().isShiftDown()) view.setWidth(view.getWidth() + 1);
				else view.setLeft(view.getLeft() + 1);
			} else if (e.getKeyChar() == ' ') {
				getViewContext().setDebugTarget(view.getSuperView());
			}
		} else if (e.getKeyChar() == 'i') {
			final String[] urls = {
					"lib/snail-gui-5.jar",
					"Sample Jars/org.eclipse.text.jar",
					"Sample Jars/org.eclipse.ui.browser.jar",
					"Sample Jars/org.eclipse.ui.console.jar",
					"Sample Jars/org.eclipse.ui.editors.jar",
					"Sample Jars/org.eclipse.ui.externaltools.jar",
					"Sample Jars/org.eclipse.ui.forms.jar",
					"Sample Jars/org.eclipse.ui.ide.application.jar",
					"Sample Jars/org.eclipse.ui.jar",
					"Sample Jars/org.eclipse.ui.navigator.jar",
					"Sample Jars/org.eclipse.ui.net.jar",
					"Sample Jars/org.eclipse.ui.presentations.jar",
					"Sample Jars/org.eclipse.ui.views.jar",
					"Sample Jars/org.eclipse.ui.views.log.jar",
			};
			try {
				for (String u : urls) {
					final String url = u;
					getJarController().importJar(url, new ProgressMonitor() {
						@Override
						public void updateProgress(final double progress) {
							Dispatcher.executeAndSyncWithGUI(new Runnable() {
								@Override
								public void run() {
									Item item = getJarListView().getItem(url);
									if (item != null) {
										item.getProgressBar().setProgress((float) progress);
									}
								}
							});
						}
						@Override
						public void finished() {
						}
					});
				}
			} catch (InvalidURIException e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	private void btnImportJarClicked() {
		try {
			ImportDialog2 dialog = new ImportDialog2(this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			Point p = getViewContext().getAWTContainer().getLocationOnScreen();
			dialog.setLocation(p.x + 60, p.y + 60);
			dialog.setSize(500, 250);
			dialog.setResizable(false);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnEditJarListClicked() {
		if (jarListState == JAR_LIST_STATE_NORMAL) {
			if (!getStateManager().isIdle()) return;
			getStateManager().startRemoveTask();
			btnEditJarList.setText("Done");
			jarListView.showRemoveButtons();
			btnRemoveAllJars.setHidden(false);
			new Animation(0.2f) {
				@Override
				protected void animation(float progress) {
					btnImportJars.setAlpha(1.0f - progress);
					btnRemoveAllJars.setAlpha(progress);
				}
				@Override
				protected void completed(boolean canceled) {
					btnImportJars.setHidden(true);
				}
			}.commit();
			jarListState = JAR_LIST_STATE_EDITING;
		} else if (jarListState == JAR_LIST_STATE_EDITING) {
			btnEditJarList.setText("Edit");
			jarListView.hideRemoveButtons();
			btnImportJars.setHidden(false);
			new Animation(0.2f) {
				@Override
				protected void animation(float progress) {
					btnImportJars.setAlpha(progress);
					btnRemoveAllJars.setAlpha(1.0f - progress);
				}
				@Override
				protected void completed(boolean canceled) {
					btnRemoveAllJars.setHidden(true);
					System.gc();
				}
			}.commit();
			jarListState = JAR_LIST_STATE_NORMAL;
			getStateManager().finishRemoveTask();
		}
	}
	
	private void removeJarFromJarListItem(final Item item) {
		Jar jar = jarController.getJar(item.getIndex());
		jarController.removeJar(jar, new ProgressMonitor() {
			@Override
			public void updateProgress(double progress) {
			}
			@Override
			public void finished() {
				item.removeFromSuperView();
				jarListView.layout();
			}
		});
	}
	
	private void btnRemoveAllJarsClicked() {
		while (jarListView.getContentView().count() > 0) {
			Item item = jarListView.getItem(0);
			removeJarFromJarListItem(item);
		}
	}
	
	private void selectedTabChanged(int index) {
		setNeedsRepaint();
		if (!navigateView.isHidden()) {
			navigateView.setHidden(true);
			navigateView.deactivated();
		}
		if (!dependencyView.isHidden()) {
			dependencyView.setHidden(true);
			dependencyView.deactivated();
		}
		if (!searchView.isHidden()) {
			searchView.setHidden(true);
			searchView.deactivated();
		}
		switch (index) {
		case 0:
			navigateView.layout();
			navigateView.activated();
			navigateView.setHidden(false);
			break;
		case 1:
			dependencyView.layout();
			dependencyView.activated();
			dependencyView.setHidden(false);
			break;
		case 2:
			searchView.layout();
			searchView.activated();
			searchView.setHidden(false);
			break;
		}
	}

	@Override
	public void shouldDisableUserInteraction() {
		new ViewAlphaAnimation(navigateView, 0.25f, 0.35f).commit();
		new ViewAlphaAnimation(dependencyView, 0.25f, 0.35f).commit();
		new ViewAlphaAnimation(searchView, 0.25f, 0.35f).commit();
	}

	@Override
	public void shouldEnableUserInteraction() {
		// update navigateView
		Item selelctedItem = jarListView.getSelectedItem();
		if (selelctedItem != null) {
			navigateView.showJar(jarController.getJar(selelctedItem.getIndex()));
		}
		
		// update dependencyView
		if (dependencyView.getState() == DependencyView.STATE_CONFIG) {
			dependencyView.updateJarList();
		}
		
		// update searchView
		searchView.clear();
		
		new ViewAlphaAnimation(navigateView, 0.25f, 1.0f).commit();
		new ViewAlphaAnimation(dependencyView, 0.25f, 1.0f).commit();
		new ViewAlphaAnimation(searchView, 0.25f, 1.0f).commit();
	}
	
}
