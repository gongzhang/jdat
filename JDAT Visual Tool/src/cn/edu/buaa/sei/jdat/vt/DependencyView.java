package cn.edu.buaa.sei.jdat.vt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import cn.edu.buaa.sei.jdat.graph.DependencyGraph;
import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.vt.MainView.Tab;
import cn.edu.buaa.sei.jdat.vt.report.GraphReport;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.Button;
import cn.edu.buaa.sei.jdat.vt.widgets.CheckBox;
import cn.edu.buaa.sei.jdat.vt.widgets.JavaElementLabel;
import cn.edu.buaa.sei.jdat.vt.widgets.LabelCheckBox;
import cn.edu.buaa.sei.jdat.vt.widgets.ListView;
import cn.edu.buaa.sei.jdat.vt.widgets.ViewShowHideAnimation;
import cn.edu.buaa.sei.jdat.vt.widgets.graph.GraphView;

import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.text.TextView;

public class DependencyView extends Tab {
	
	private class OptionView extends View {
		
		final ListView jarListView;
		final CheckBox chkUnknownPackages, chkExternalJars;
		
		public OptionView() {
			super(0, 0, 516, 400);
			setBackgroundColor(null);
			setPaintingEnabled(false);
			TextView label1;
			addSubview(label1 = new TextView(8, 8, 0, 0) {
				{
					setText("Select Jars:");
					setBackgroundColor(null);
					setFont(Resources.FONT_LABEL);
					setColor(Resources.COLOR_TEXT_DEFAULT);
					setSize(getPreferredSize());
				}
			});
			addSubview(jarListView = new ListView(8, 16 + label1.getHeight(), 250, 0));
			View.scaleViewWithBottom(jarListView, 50);
			addSubview(new Button(8, 400 - 42, (250 - 8) / 2, 50 - 16) { // really bad numbers
				{
					setText("Select All");
				}
				@Override
				protected void postMouseClicked(MouseEvent e) {
					for (View view : jarListView.getContentView()) {
						CheckBox checkBox = (CheckBox) view;
						checkBox.setSelected(true);
					}
				}
			});
			addSubview(new Button(16 + (250 - 8) / 2, 400 - 42, (250 - 8) / 2, 50 - 16) {
				{
					setText("Invert");
				}
				@Override
				protected void postMouseClicked(MouseEvent e) {
					for (View view : jarListView.getContentView().getSubviews()) {
						CheckBox checkBox = (CheckBox) view;
						checkBox.setSelected(!checkBox.isSelected());
					}
				}
			});
			addSubview(new Button(250 + 16, 400 - 42, (250 - 8), 50 - 16) {
				{
					setText("Generate");
				}
				@Override
				protected void postMouseClicked(MouseEvent e) {
					generateButtonClicked();
				}
			});
			addSubview(chkUnknownPackages = new CheckBox() {
				{
					setLabelView(new TextView(0, 0, 0, 0) {
						{
							setText("Show Unknown Elements");
							setBackgroundColor(null);
							setFont(Resources.FONT_LABEL);
							setColor(Resources.COLOR_TEXT_DEFAULT);
							setSize(getPreferredSize());
						}
					});
					setSize(getPreferredSize());
					setSelected(true);
				}
			});
			addSubview(chkExternalJars = new CheckBox() {
				{
					setLabelView(new TextView(0, 0, 0, 0) {
						{
							setText("Show External Dependency");
							setBackgroundColor(null);
							setFont(Resources.FONT_LABEL);
							setColor(Resources.COLOR_TEXT_DEFAULT);
							setSize(getPreferredSize());
						}
					});
					setSize(getPreferredSize());
					setSelected(true);
				}
			});
			View.putViewWithHorizontalCenterAndVerticalCenter(chkExternalJars, 250 + 8 + 125, 0);
			chkExternalJars.setTop(400 - 50 - chkExternalJars.getHeight() - 8);
			chkUnknownPackages.setLeft(chkExternalJars.getLeft());
			chkUnknownPackages.setTop(chkExternalJars.getTop() - chkUnknownPackages.getHeight() - 8);
		}

		@Override
		protected void repaintView(GraphicsX g) {
			g.drawStrechableImage(Resources.IMG_DEPTH_BACKGROUND, getWidth(), getHeight());
		}
		
		Jar[] getSelectedJars() {
			ArrayList<Jar> jars = new ArrayList<Jar>();
			for (View view : jarListView.getContentView()) {
				CheckBox checkBox = (CheckBox) view;
				if (checkBox.isSelected()) {
					Jar jar = getOwner().getJarController().getJar(checkBox.getIndex());
					jars.add(jar);
				}
			}
			return jars.toArray(new Jar[0]);
		}
		
	}
	
	private final OptionView optionView;
	private final GraphView graphView;
	private final Button btnClose, btnGenerateReport;
	private final LabelCheckBox chkAutoLayout;
	private int state;
	public static final int STATE_CONFIG = 0;
	public static final int STATE_BUSY = 1;
	public static final int STATE_READY = 2;
	
	private DependencyGraph graph = null;
	
	public int getState() {
		return state;
	}

	public DependencyView(MainView owner) {
		super(owner);
		state = STATE_CONFIG;
		addSubview(graphView = new GraphView(0, 0, 0, 0) {
			{
				setHidden(true);
			}
		});
		addSubview(optionView = new OptionView());
		addSubview(btnClose = new Button(8, 8, 0, 0) {
			@Override
			protected void postMouseClicked(MouseEvent e) {
				closeButtonClicked();
			}
		});
		btnClose.setText("Close");
		btnClose.setFont(Resources.FONT_LABEL);
		btnClose.setTextColor(Resources.COLOR_TEXT_DEFAULT);
		btnClose.setHidden(true);
		addSubview(btnGenerateReport = new Button(0, 0, 0, 0) {
			@Override
			protected void postMouseClicked(MouseEvent e) {
				generateReportButtonClicked();
			}
		});
		btnGenerateReport.setText("Generate Report");
		btnGenerateReport.setFont(Resources.FONT_LABEL);
		btnGenerateReport.setTextColor(Resources.COLOR_TEXT_DEFAULT);
		Vector size = btnGenerateReport.getPreferredSize();
		size.x += 16;
		size.y += 4;
		btnGenerateReport.setSize(size);
		btnClose.setSize(size);
		btnGenerateReport.setHidden(true);
		addSubview(chkAutoLayout = new LabelCheckBox() {
			@Override
			protected void postMouseClicked(MouseEvent e) {
				super.postMouseClicked(e);
				if (chkAutoLayout.isSelected()) {
					graphView.enableAutomaticLayout();
				} else {
					graphView.disableAutomaticLayout();
				}
			}
		});
		chkAutoLayout.setText("Automatic Layout");
		chkAutoLayout.setSize(chkAutoLayout.getPreferredSize());
		chkAutoLayout.setHeight(btnGenerateReport.getHeight());
		chkAutoLayout.setHidden(true);
	}

	public void updateJarList() {
		optionView.jarListView.getContentView().removeAllSubviews();
		Jar[] jars = getOwner().getJarController().getAllJars();
		for (Jar jar : jars) {
			CheckBox checkBox = new CheckBox() {
				{
					setHeight(30);
				}
				@Override
				public View setSize(int width, int height) {
					super.setSize(width, height);
					if (getLabelView() != null) {
						View.scaleViewWithRight(getLabelView(), 0);
						getLabelView().setHeight(30);
					}
					return this;
				}
			};
			checkBox.setSelected(true);
			checkBox.setLabelView(new JavaElementLabel(jar));
			optionView.jarListView.getContentView().addSubview(checkBox);
		}
		optionView.jarListView.layout();
	}
	
	public void highlightJar(Jar jar) {
		if (state == STATE_READY) {
			graphView.highlightJar(jar);
		}
	}

	@Override
	protected void activated() {
		if (state == STATE_CONFIG && getOwner().getStateManager().isIdle()) {
			updateJarList();
		}
	}
	
	@Override
	protected void deactivated() {
	}

	@Override
	protected void layout() {
		View.putViewAtCenterOfSuperView(optionView);
		graphView.setSize(getWidth(), getHeight() - btnGenerateReport.getHeight());
		
		View.putViewWithLeftAndBottom(chkAutoLayout, 8, 0);
		View.putViewWithRightAndBottom(btnClose, 8, 0);
		View.putViewAtLeftSideOfView(btnGenerateReport, btnClose, 8);
		View.putViewWithBottom(btnGenerateReport, 0);
	}
	
	private void generateButtonClicked() {
		
		ViewShowHideAnimation.createHideAnimation(optionView, null).commit();

		int opt = optionView.chkUnknownPackages.isSelected() ? DependencyGraph.OPT_UNKNOWN_PACKAGES : 0;
		opt |= optionView.chkExternalJars.isSelected() ? DependencyGraph.OPT_EXTERNAL_JARS : 0;
		graph = getOwner().getJarController().generateGraph(optionView.getSelectedJars(), opt);
		
		state = STATE_BUSY;
		
		// show graph
		graphView.setGraph(graph, new Runnable() {
			@Override
			public void run() {
				// finished
				graphView.setHidden(false);
				btnClose.setHidden(false);
				btnGenerateReport.setHidden(false);
				chkAutoLayout.setHidden(false);
				state = STATE_READY;
			}
		});

	}
	
	private void closeButtonClicked() {
		graph = null;
		graphView.setGraph(null, null);
		
		graphView.setHidden(true);
		btnClose.setHidden(true);
		btnGenerateReport.setHidden(true);
		chkAutoLayout.setHidden(true);
		
		state = STATE_CONFIG;
		
		updateJarList();
		optionView.setHidden(false);
		optionView.setAlpha(0.0f);
		ViewShowHideAnimation.createShowAnimation(optionView, null).commit();
	}
	
	private void generateGraphImageFile(File outputFile) throws IOException {
		BufferedImage buf = new BufferedImage(graphView.getWidth(), graphView.getHeight(), BufferedImage.TYPE_INT_RGB);
		GraphicsX g = new GraphicsX(buf.createGraphics());
		
		Resources.fillBackground(g, graphView.getWidth(), graphView.getHeight());
		graphView.paintOnCustomizedTarget(g);
		g.dispose();
		
		ImageIO.write(buf, "jpg", outputFile);
		buf.flush();
	}
	
	private void generateReportButtonClicked() {
		GraphReport report = new GraphReport() {

			@Override
			public void generateGraphImage(File file) {
				try {
					generateGraphImageFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		};
		
		JFileChooser dlg = new JFileChooser();
		dlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = dlg.showDialog(null, "Save");
		if (result == JFileChooser.APPROVE_OPTION) {
			File dir = dlg.getSelectedFile();
			if (!dir.exists() || !dir.isDirectory())
				dir.mkdir();
			report.generateReport(graph, dir);
		}
	}

}
