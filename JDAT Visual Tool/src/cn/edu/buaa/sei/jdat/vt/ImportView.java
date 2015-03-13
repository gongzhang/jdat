package cn.edu.buaa.sei.jdat.vt;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import cn.edu.buaa.sei.jdat.JarController.ProgressMonitor;
import cn.edu.buaa.sei.jdat.exception.InvalidURIException;
import cn.edu.buaa.sei.jdat.io.JarLoader;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.Button;
import cn.edu.buaa.sei.jdat.vt.widgets.JarListView.Item;
import cn.edu.buaa.sei.jdat.vt.widgets.LabelCheckBox;
import cn.edu.buaa.sei.jdat.vt.widgets.SealedLabel;
import cn.edu.buaa.sei.jdat.vt.widgets.TabButton;
import cn.edu.buaa.sei.jdat.vt.widgets.TextBox;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.View;
import com.cocotingo.snail.dispatcher.Dispatcher;
import com.cocotingo.snail.template.EventHandler;
import com.cocotingo.snail.template.SealedView;
import com.cocotingo.snail.template.TemplateCallback;
import com.cocotingo.snail.template.TemplateException;

public class ImportView extends SealedView {
	
	public static ImportView createImportView(ImportDialog2 owner, MainView mainView) {
		ImportView importView = null;
		try {
			importView = (ImportView) Resources.TEMPLATE_LOADER.createView("ImportView");
			importView.setOwner(owner);
			importView.setMainView(mainView);
			importView.init();
			importView.addEventHandler(new EventHandler() {
				@Override
				public void handleViewEvent(SealedView view, String eventName, Object arg) {
					if (eventName.equals(SealedView.REPAINT_VIEW)) {
						GraphicsX g = (GraphicsX) arg;
						Resources.fillBackground(g, view.getWidth(), view.getHeight());
					}
				}
			});
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return importView;
	}
	
	private MainView mainView;
	private ImportDialog2 owner;
	private JarLoader loader = new JarLoader();
	
	private View contentView0, contentView1;
	
	public ImportView() {
	}
	
	private void init() {
		final TabButton tabButton = (TabButton) getNamedView("tabButton");
		tabButton.setTexts(new String[] {"Import Local Jars", "Import via URL"});
		tabButton.setSelectedIndex(0);
		
		final Button btnClose = (Button) getNamedView("btnClose");
		btnClose.setText("Close");
		
		final Button btnImportJars = (Button) getNamedView("btnImportJars");
		btnImportJars.setText("Open Files...");
		
		final Button btnImportDir = (Button) getNamedView("btnImportDir");
		btnImportDir.setText("Open Directories...");
		
		final LabelCheckBox chkRecursively = (LabelCheckBox) getNamedView("chkRecursively");
		chkRecursively.setText("Recursively");
		
		final Button btnImportURL = (Button) getNamedView("btnImportURL");
		btnImportURL.setText("Import");
		
		final SealedLabel lblURL = (SealedLabel) getNamedView("lblURL");
		lblURL.getTextView().setText("URL");
		
		final SealedLabel lblError = (SealedLabel) getNamedView("lblError");
		lblError.getTextView().setAntialiased(false);
		lblError.getTextView().setColor(Color.red);
		lblError.getTextView().setFont(Resources.FONT_SMALL);
		lblError.getTextView().setText("");
		
		contentView0 = getNamedView("contentView0");
		contentView1 = getNamedView("contentView1");
	}
	
	@TemplateCallback
	public void closeButtonClicked(SealedView sender, Object arg) {
		owner.dispose();
	}
	
	@TemplateCallback
	public void tabButtonClicked(SealedView sender, Object arg) {
		final TabButton tabButton = (TabButton) sender;
		if (tabButton.getSelectedIndex() == 0) {
			contentView0.setHidden(false);
			contentView1.setHidden(true);
		} else {
			contentView0.setHidden(true);
			contentView1.setHidden(false);
		}
	}
	
	@TemplateCallback
	public void btnImportJarsClicked(SealedView sender, Object arg) {
		JFileChooser dlg = new JFileChooser();
		dlg.setMultiSelectionEnabled(true);
		dlg.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dlg.setControlButtonsAreShown(true);
		dlg.setAcceptAllFileFilterUsed(false);
		
		dlg.setFileFilter(new javax.swing.filechooser.FileFilter() {
			
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".jar") || arg0.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Jar Binary File (*.jar)";
			}
			
		});
		
		int result = dlg.showOpenDialog(owner);
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] files = dlg.getSelectedFiles();
			openFiles(files);
			owner.dispose();
		}
	}
	
	@TemplateCallback
	public void btnImportDirClicked(SealedView sender, Object arg) {
		JFileChooser dlg = new JFileChooser();
		dlg.setMultiSelectionEnabled(true);
		dlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dlg.setFileFilter(new javax.swing.filechooser.FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Directory";
			}
		
		});
		
		int result = dlg.showOpenDialog(owner);
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] directories = dlg.getSelectedFiles();
			openDirectories(directories);
			owner.dispose();
		}
	}
	
	@TemplateCallback
	public void btnImportURLClicked(SealedView sender, Object arg) {
		final String url = ((TextBox) getNamedView("textBox")).getText();
		try {
			mainView.getJarController().importJar(url, new ProgressMonitor() {
				@Override
				public void updateProgress(final double progress) {
					Dispatcher.executeAndSyncWithGUI(new Runnable() {
						@Override
						public void run() {
							Item item = mainView.getJarListView().getItem(url);
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
			owner.dispose();
		} catch (InvalidURIException e) {
			showError(e.getMessage());
		}
	}
	
	public MainView getMainView() {
		return mainView;
	}
	
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}
	
	public ImportDialog2 getOwner() {
		return owner;
	}
	
	public void setOwner(ImportDialog2 owner) {
		this.owner = owner;
	}
	
	private void openDirectories(File[] directories) {
		boolean isRecursively = ((LabelCheckBox) getNamedView("chkRecursively")).isSelected();
		ArrayList<File> files = new ArrayList<File>();
		for (File dir : directories) {
			File[] fs = loader.openDirectory(dir, isRecursively);
			for (File f : fs)
				files.add(f);
		}
		openFiles(files.toArray(new File[0]));
	}
	
	private void openFiles(File[] files) {
		try {
			for (File f : files) {
				final String url = f.getAbsolutePath();
				mainView.getJarController().importJar(url, new ProgressMonitor() {
					@Override
					public void updateProgress(final double progress) {
						Dispatcher.executeAndSyncWithGUI(new Runnable() {
							@Override
							public void run() {
								Item item = mainView.getJarListView().getItem(url);
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
		} catch (InvalidURIException e) {
			showError(e.getMessage());
		}
	}
	
	private void showError(String error) {
		SealedLabel label = (SealedLabel) getNamedView("lblError");
		label.getTextView().setText(error);
	}

}
