package cn.edu.buaa.sei.jdat.vt;

import javax.swing.JDialog;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.View;
import com.cocotingo.snail.ViewContext;

public class ImportDialog2 extends JDialog {
	
	private static final long serialVersionUID = -778732574170905623L;
	private ViewContext viewContext;

	public ImportDialog2(MainView mainView) {
		super();
		setBackground(Resources.COLOR_DEFAULT_BACKGROUND);
		this.viewContext = new ViewContext(this.getContentPane());
		View importView = ImportView.createImportView(this, mainView);
		this.viewContext.setRootView(importView);
	}
	
	public ViewContext getViewContext() {
		return viewContext;
	}
	
	@Override
	public void dispose() {
		if (viewContext != null) {
			viewContext.dispose();
			viewContext = null;
		}
		super.dispose();
	}

}
