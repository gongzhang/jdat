package cn.edu.buaa.sei.jdat.vt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.cocotingo.snail.dispatcher.Dispatcher;

import cn.edu.buaa.sei.jdat.JarController.ProgressMonitor;
import cn.edu.buaa.sei.jdat.exception.InvalidURIException;
import cn.edu.buaa.sei.jdat.io.JarLoader;
import cn.edu.buaa.sei.jdat.vt.widgets.JarListView.Item;

public class ImportDialog extends JDialog {
	
	private static final long serialVersionUID = 8527585719763379907L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private boolean isRecursively = false;
	private JLabel lblErrorLabel;
	
	private JarLoader loader = new JarLoader();
	MainView mainView;

	/**
	 * Create the dialog.
	 */
	public ImportDialog(MainView mainview) {
		this.mainView = mainview;
		setResizable(false);
		setBounds(100, 100, 601, 245);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Import Local Files", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(6, 6, 589, 100);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnOpenFiles = new JButton("Open files...");
		btnOpenFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				
				int result = dlg.showOpenDialog(ImportDialog.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File[] files = dlg.getSelectedFiles();
					openFiles(files);
					dispose();
				}
			}
		});
		btnOpenFiles.setBounds(17, 21, 117, 29);
		panel.add(btnOpenFiles);
		
		JButton btnOpenDirectories = new JButton("Open directories...");
		btnOpenDirectories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				
				int result = dlg.showOpenDialog(ImportDialog.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File[] directories = dlg.getSelectedFiles();
					openDirectories(directories);
					dispose();
				}
			}
		});
		btnOpenDirectories.setBounds(17, 54, 160, 29);
		panel.add(btnOpenDirectories);
		
		JCheckBox chckbxOpenDirectoryRecursively = new JCheckBox("Open directory recursively");
		chckbxOpenDirectoryRecursively.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				isRecursively = arg0.getStateChange() == ItemEvent.SELECTED ? true : false;
			}
		});
		chckbxOpenDirectoryRecursively.setBounds(176, 55, 197, 23);
		panel.add(chckbxOpenDirectoryRecursively);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Import with URL", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(6, 107, 589, 77);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(20, 28, 494, 28);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openURL(textField.getText());
			}
		});
		btnImport.setBounds(512, 28, 71, 30);
		panel_1.add(btnImport);
		
		lblErrorLabel = new JLabel("");
		lblErrorLabel.setForeground(Color.RED);
		lblErrorLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblErrorLabel.setBounds(30, 55, 484, 16);
		panel_1.add(lblErrorLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("CANCEL");
				buttonPane.add(cancelButton);
				getRootPane().setDefaultButton(cancelButton);
			}
		}
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
	
	private void openDirectories(File[] directories) {
		ArrayList<File> files = new ArrayList<File>();
		for (File dir : directories) {
			File[] fs = loader.openDirectory(dir, isRecursively);
			for (File f : fs)
				files.add(f);
		}
		openFiles(files.toArray(new File[0]));
	}
	
	private void openURL(final String url) {
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
			dispose();
		} catch (InvalidURIException e) {
			showError(e.getMessage());
		}
	}
	
	private void showError(String error) {
		lblErrorLabel.setText(error);
	}
}