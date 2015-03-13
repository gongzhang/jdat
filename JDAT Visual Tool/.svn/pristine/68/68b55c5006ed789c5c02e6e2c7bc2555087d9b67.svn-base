package cn.edu.buaa.sei.jdat.vt.widgets.graph;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import cn.edu.buaa.sei.jdat.graph.DependencyGraph;
import cn.edu.buaa.sei.jdat.graph.JarDependency;
import cn.edu.buaa.sei.jdat.graph.PackageDependency;
import cn.edu.buaa.sei.jdat.graph.TypeDependency;
import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.vt.layout.ForceDirectedLayout;
import cn.edu.buaa.sei.jdat.vt.res.Resources;
import cn.edu.buaa.sei.jdat.vt.widgets.ValueBubble;
import cn.edu.buaa.sei.jdat.vt.widgets.ViewHighlightAnimation;
import cn.edu.buaa.sei.jdat.vt.widgets.ViewShowHideAnimation;

import com.cocotingo.snail.MouseEvent;
import com.cocotingo.snail.Vector;
import com.cocotingo.snail.View;
import com.cocotingo.snail.dispatcher.Dispatcher;

public class GraphView extends View {
	
	private DependencyGraph graph;
	private final LineView lineView;
	private final Hashtable<String, JarBubble> jarBubbles;
	private final ForceDirectedLayout layout;
	private boolean automaticLayout = true;
	
	private static final int TAG_PACKAGE_TIP = 1;

	public GraphView(int left, int top, int width, int height) {
		super(left, top, width, height);
		setBackgroundColor(null);
		setPaintingEnabled(false);
		setClipped(true);
		layout = new ForceDirectedLayout(width / (double) height);
		jarBubbles = new Hashtable<String, JarBubble>();
		addSubview(lineView = new LineView(0, 0, 0, 0) {
			
			Arrow selectedLine = null;
			List<Line> old_mouse_on = new ArrayList<Line>();
			LineFilter filter = new LineFilter() {
				@Override
				public boolean accept(Line line) {
					return line instanceof Arrow;
				}
			};
			
			@Override
			protected void postMouseMoved(MouseEvent e) {
				List<Line> lines = getLinesOn(e.getPosition(this), filter);
				if (lines.size() == 0) {
					// clear
					for (Line line : old_mouse_on) {
						if (line != selectedLine) line.setColor(Resources.COLOR_GRAPH_ARROW);
					}
				} else if (!lines.equals(old_mouse_on)) {
					// clear
					for (Line line : old_mouse_on) {
						if (line != selectedLine) line.setColor(Resources.COLOR_GRAPH_ARROW);
					}
					// highlight new
					for (Line line : lines) {
						if (line != selectedLine) line.setColor(Resources.COLOR_GRAPH_ARROW_HIGHLIGHTED);
					}
					e.handle();
				} else {
					e.handle();
				}
				old_mouse_on = lines;
			}
			
			@Override
			protected void postMousePressed(MouseEvent e) {
				List<Line> lines = getLinesOn(e.getPosition(this), filter);
				if (lines.size() == 0) {
					// deselect
					deselectCurrent();
				} else if (lines.size() == 1) {
					deselectCurrent();
					selectLineAtIndex(lines, 0);
				} else if (lines.equals(old_mouse_on) && selectedLine != null) {
					// selection cycle
					int index = old_mouse_on.indexOf(selectedLine);
					if (index == -1) {
						deselectCurrent();
						selectLineAtIndex(lines, 0);
					} else {
						deselectCurrent();
						index = (index + 1) % old_mouse_on.size();
						selectLineAtIndex(old_mouse_on, index);
					}
				} else {
					deselectCurrent();
					selectLineAtIndex(lines, 0);
				}
			}
			
			private void deselectCurrent() {
				if (selectedLine != null) {
					selectedLine.setColor(old_mouse_on.contains(selectedLine) ? Resources.COLOR_GRAPH_ARROW_HIGHLIGHTED : Resources.COLOR_GRAPH_ARROW);
					selectedLine.deselected();
					selectedLine = null;
				}
			}
			
			private void selectLineAtIndex(List<Line> lines, int index) {
				if (lines.get(index) instanceof Arrow) {
					Arrow line = (Arrow) lines.get(index);
					line.setColor(Resources.COLOR_GRAPH_ARROW_SELECTED);
					line.selected();
					selectedLine = line;
				}
			}
			
		});
	}
	
	public DependencyGraph getGraph() {
		return graph;
	}
	
	public void setGraph(DependencyGraph graph, final Runnable finishBlock) {
		this.graph = graph;
		
		layout.reset();
		
		// clear current graph
		lineView.removeAllLines();
		for (JarBubble bubble : jarBubbles.values()) {
			bubble.removeFromSuperView();
		}
		jarBubbles.clear();
		
		// generate new graph
		if (graph != null) generateGraph();
		
		// layout dependency graph
		if (finishBlock != null) {
			Dispatcher.execute(new Runnable() {
				@Override
				public void run() {
					Dispatcher.executeAndSyncWithGUI(new Runnable() {
						@Override
						public void run() {
							// random initial position
							Random r = new Random();
							for (JarBubble bubble : jarBubbles.values()) {
								View.putViewWithHorizontalCenterAndVerticalCenter(bubble,
										getWidth() / 4 + Math.abs(r.nextInt()) % (getWidth() / 2),
										getHeight() / 4 + Math.abs(r.nextInt()) % (getHeight() / 2));
								Vector vector = bubble.getPosition();
								bubble.verifyDraggingPosition(vector);
								bubble.setPosition(vector);
							}
							startAutomaticLayout();
							finishBlock.run();
						}
					});
				}
			});
		}
		
	}
	
	private void startAutomaticLayout() {
		if (!automaticLayout) return;
		for (JarBubble bubble : jarBubbles.values()) {
			if (bubble.edgeCount() > 0)
				layout.addNode(bubble);
		}
		// start up layout
		layout.start();
	}
	
	public void highlightJar(Jar jar) {
		if (jarBubbles.containsKey(jar.getURI())) {
			new ViewHighlightAnimation(jarBubbles.get(jar.getURI()), 0.4f).commit();
		}
	}
	
	private JarBubble createBubble(Jar jar) {
		JarBubble bubble = new JarBubble(jar) {
			@Override
			public View setPosition(int left, int top) {
				if (left >= 0 && top >= 0 &&
					left + getWidth() <= GraphView.this.getWidth() &&
					top + getHeight() <= GraphView.this.getHeight()) {
					super.setPosition(left, top);
				}
				return this;
			}
			@Override
			protected void verifyDraggingPosition(Vector v) {
				if (v.x < 0) v.x = 0;
				else if (v.x + getWidth() > GraphView.this.getWidth()) v.x = GraphView.this.getWidth() - getWidth();
				if (v.y < 0) v.y = 0;
				else if (v.y + getHeight() > GraphView.this.getHeight()) v.y = GraphView.this.getHeight() - getHeight();
			}
		};
		jarBubbles.put(jar.getURI(), bubble);
		return bubble;
	}
	
	private Arrow createArrow(JarBubble b1, JarBubble b2) {
		Arrow arrow = new Arrow(lineView, b1, b2) {
			private ValueBubble valueBubble = null;
			@Override
			protected void selected() {
				// get dependency
				Jar jar1 = getBubble1().getJar();
				Jar jar2 = getBubble2().getJar();
				JarDependency dep = graph.getDependency(jar1, jar2);
				
				if (dep != null) {
					PackageDependency[] pkgDeps = dep.getDependencies();
					
					valueBubble = createValueBubble(dep, pkgDeps);
					syncValueBubblePosition();
					GraphView.this.addSubview(valueBubble);
				}
			}
			@Override
			protected void deselected() {
				if (valueBubble != null) {
					valueBubble.removeFromSuperView();
					valueBubble = null;
				}
			}
			private void syncValueBubblePosition() {
				if (valueBubble != null) {
					double x = (x2 - x1) * 0.8;
					double y = (y2 - y1) * 0.8;
					valueBubble.setAnchor(x1 + (int) x, y1 + (int) y);
					View tipView = GraphView.this.getTaggedSubview(TAG_PACKAGE_TIP);
					if (x1 == x2 && y1 == y2) {
						if (tipView != null) tipView.setHidden(true);
					} else {
						if (tipView != null) tipView.setHidden(false);
					}
				}
			}
			@Override
			public void setEnd1(Vector v1) {
				super.setEnd1(v1);
				syncValueBubblePosition();
			}
			@Override
			public void setEnd2(Vector v2) {
				super.setEnd2(v2);
				syncValueBubblePosition();
			}
			@Override
			public void remove() {
				super.remove();
				deselected();
			}
		};
		b1.addArrowAsEnd1(arrow);
		b2.addArrowAsEnd2(arrow);
		return arrow;
	}
	
	private ValueBubble createValueBubble(final JarDependency jarDep, final PackageDependency[] pkgDeps) {
		ValueBubble vb = new ValueBubble(0, 0) {
			private AnchorDependencyTipBubble tipBubble = null;
			{
				getTextView().setText(String.valueOf(pkgDeps.length));
			}
			@Override
			protected void mouseEntered() {
				setIndex(getSuperView().count() - 1);
			}
			@Override
			protected void postMousePressed(MouseEvent e) {
				e.handle();
				setHidden(true);
				if (tipBubble != null) tipBubble.removeFromSuperView();
				tipBubble = createPackageDependencyTipBubble(jarDep, pkgDeps);
				GraphView.this.addSubview(tipBubble);
				layoutTipBubble();
				ViewShowHideAnimation.createShowAnimation(tipBubble, new Runnable() {
					@Override
					public void run() {
						tipBubble.setAnchor(getAnchor());
					}
				}).commit();
			}
			@Override
			public void removeFromSuperView() {
				super.removeFromSuperView();
				if (tipBubble != null) {
					final AnchorDependencyTipBubble tb = tipBubble;
					tb.willRemove();
					ViewShowHideAnimation.createHideAnimation(tb, new Runnable() {
						@Override
						public void run() {
							tb.removeFromSuperView();
						}
					}).commit();
					tipBubble = null;
				}
			}
			@Override
			public View setPosition(int left, int top) {
				super.setPosition(left, top);
				Vector anchor = getAnchor();
				if (anchor.x == 0 && anchor.y == 0) setHidden(true);
				else if (tipBubble == null) setHidden(false);
				
				// draw dash line
				if (tipBubble != null && !(anchor.x == 0 && anchor.y == 0)) {
					tipBubble.setAnchor(anchor);
				}
				return this;
			}
			private void layoutTipBubble() {
				if (tipBubble != null) {
					View.putViewWithHorizontalCenterAndVerticalCenter(tipBubble, getLeft() + getWidth() / 2, getTop() + getHeight() / 2);
					if (tipBubble.getLeft() < 0) tipBubble.setLeft(0);
					if (tipBubble.getRight() < 0) tipBubble.setLeft(GraphView.this.getWidth() - tipBubble.getWidth());
					if (tipBubble.getTop() < 0) tipBubble.setTop(0);
					if (tipBubble.getBottom() < 0) tipBubble.setLeft(GraphView.this.getHeight() - tipBubble.getHeight());
				}
			}
		};
		return vb;
	}
	
	private class AnchorDependencyTipBubble extends DependencyTipBubble {
		
		private LineView.Line line = null;

		public AnchorDependencyTipBubble(Object titleElement1,
				Object titleElement2) {
			super(titleElement1, titleElement2);
		}
		
		public void setAnchor(Vector anchor) {
			if (line == null) {
				line = new LineView.Line(lineView, anchor, new Vector(getLeft() + 2, getTop() + 2)) {
					@Override
					public void repaintLine(com.cocotingo.snail.GraphicsX g) {
						g.setStroke(Resources.STROKE_DASHLINE);
						super.repaintLine(g);
					}
				};
				line.setColor(Resources.COLOR_GRAPH_ARROW_HIGHLIGHTED);
			}
			line.setEnd1(anchor);
		}
		
		@Override
		public View setPosition(int left, int top) {
			super.setPosition(left, top);
			if (line != null) {
				line.setEnd2(new Vector(left + 2, top + 2));
			}
			return this;
		}
		
		@Override
		public View setHidden(boolean hidden) {
			super.setHidden(hidden);
			if (line != null) line.setHidden(hidden);
			return this;
		}
		
		@Override
		public void removeFromSuperView() {
			super.removeFromSuperView();
		}
		
		protected void willRemove() {
			if (line != null) {
				line.remove();
				line = null;
			}
		}
		
	}
	
	private AnchorDependencyTipBubble createPackageDependencyTipBubble(final JarDependency jarDep, final PackageDependency[] pkgDeps) {
		return new AnchorDependencyTipBubble(jarDep.getSource(), jarDep.getDestination()) {
			private DependencyTipBubble typeDepTipBubble = null;
			private Vector typeDepTipBubblePosition = null;
			{
				setTag(TAG_PACKAGE_TIP);
				for (PackageDependency pd : pkgDeps) {
					getListView().addDependency(pd.getSource(), pd.getDestination());
				}
				int pheight = getPreferredHeight();
				setSize(getPreferredWidth(), Math.min(pheight, GraphView.this.getHeight() / 2));
			}
			private void removeTypeDependencyTipBubble() {
				if (typeDepTipBubble != null) {
					final View targetView = typeDepTipBubble;
					ViewShowHideAnimation.createHideAnimation(typeDepTipBubble, new Runnable() {
						@Override
						public void run() {
							targetView.removeFromSuperView();
						}
					}).commit();
					typeDepTipBubblePosition = typeDepTipBubble.getPosition();
					typeDepTipBubble = null;
				}
			}
			@Override
			protected void willRemove() {
				super.willRemove();
				removeTypeDependencyTipBubble();
			}
			@Override
			protected void selectedItemChanged(final int index) {
				removeTypeDependencyTipBubble();
				TypeDependency[] typeDeps = pkgDeps[index].getDependencies(graph);
				if (typeDeps.length != 0) {
					typeDepTipBubble = createTypeDependencyTipBubble(pkgDeps[index], typeDeps);
					if (typeDepTipBubblePosition != null) {
						typeDepTipBubble.setPosition(typeDepTipBubblePosition);
					} else {
						typeDepTipBubble.setPosition(getLeft() + 30, getTop() + 30);
					}
					GraphView.this.addSubview(typeDepTipBubble);
					ViewShowHideAnimation.createShowAnimation(typeDepTipBubble, null).commit();
				}
			}
		};
	}
	
	private DependencyTipBubble createTypeDependencyTipBubble(final PackageDependency pkgDep, final TypeDependency[] typeDeps) {
		return new DependencyTipBubble(pkgDep.getSource(), pkgDep.getDestination()) {
			{
				for (TypeDependency td : typeDeps) {
					getListView().addDependency(td.getSource(), td.getDestination());
				}
				int pheight = getPreferredHeight();
				setSize(getPreferredWidth(), Math.min(pheight, GraphView.this.getHeight() / 2));
			}
		};
	}
	
	private void generateGraph() {
		
		// add jar node
		Jar[] internalJars = graph.getInternalJars();
		for (Jar jar : internalJars) {
			JarBubble bubble = createBubble(jar);
			bubble.makeOrange();
			addSubview(bubble);
		}
		if (graph.isOptionFlagged(DependencyGraph.OPT_EXTERNAL_JARS))
		{
			Jar[] externalJars = graph.getExternalJars();
			
			for (Jar jar : externalJars) {
				JarBubble bubble = createBubble(jar);
				bubble.makePink();
				addSubview(bubble);
			}
		}
		if (graph.isOptionFlagged(DependencyGraph.OPT_UNKNOWN_PACKAGES))
		{
			Jar jar = graph.getUnknownJar();
			if (jar != null && graph.getUnknownPackages().length > 0) {
				JarBubble bubble = createBubble(jar);
				bubble.makeBrown();
				addSubview(bubble);
			}
		}
		// add lines
		for (JarDependency jd : graph.getInternalDependencies()) {
			String u1 = jd.getSource().getURI();
			String u2 = jd.getDestination().getURI();
			if (jarBubbles.containsKey(u1) && jarBubbles.containsKey(u2)) {
				createArrow(jarBubbles.get(u1), jarBubbles.get(u2));
			}
		}
		if (graph.isOptionFlagged(DependencyGraph.OPT_EXTERNAL_JARS) || graph.isOptionFlagged(DependencyGraph.OPT_UNKNOWN_PACKAGES)) {
			for (JarDependency jd : graph.getExternalDependencies()) {
				String u1 = jd.getSource().getURI();
				String u2 = jd.getDestination().getURI();
				if (jarBubbles.containsKey(u1) && jarBubbles.containsKey(u2)) {
					createArrow(jarBubbles.get(u1), jarBubbles.get(u2));
				}
			}
		}

	}
	
	@Override
	public View setSize(int width, int height) {
		super.setSize(width, height);
		if (lineView != null) {
			lineView.setSize(width, height);
			layout.setArea(width, height);
		}
		return this;
	}
	
	public void enableAutomaticLayout() {
		automaticLayout = true;
		if (graph != null) {
			startAutomaticLayout();
		}
	}
	
	public void disableAutomaticLayout() {
		automaticLayout = false;
		layout.reset();
	}

}
