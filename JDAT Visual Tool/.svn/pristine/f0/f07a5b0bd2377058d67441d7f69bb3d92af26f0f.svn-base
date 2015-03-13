package cn.edu.buaa.sei.jdat.vt;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cn.edu.buaa.sei.jdat.vt.res.Resources;

import com.cocotingo.snail.ViewContext;
import com.cocotingo.snail.dispatcher.Dispatcher;
import com.cocotingo.snail.template.TemplateException;

public class Launcher {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			// load resource, show a splash window if needed
			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			Resources.loadResource(gc);

			// load main window
			final JFrame mainFrame = new JFrame(gc);
			mainFrame.setTitle("JDAT Visual Tool");
			mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setMinimumSize(new Dimension(800, 500));
			
			Dispatcher.executeAndSyncWithGUI(new Runnable() {
				@Override
				public void run() {
					// load Snail GUI main view
					final ViewContext context = new ViewContext(mainFrame.getContentPane());
					context.setRootView(new MainView());
					
					// show up all the things
					context.prepareContent(mainFrame.getWidth(), mainFrame.getHeight());
					mainFrame.setVisible(true);
				}
			});
		
		} catch (IOException e) {
			ErrorDialog.showErrorDialog(e);
		} catch (TemplateException e) {
			ErrorDialog.showErrorDialog(e);
		} catch (ClassNotFoundException e) {
			ErrorDialog.showErrorDialog(e);
		} catch (InstantiationException e) {
			ErrorDialog.showErrorDialog(e);
		} catch (IllegalAccessException e) {
			ErrorDialog.showErrorDialog(e);
		} catch (UnsupportedLookAndFeelException e) {
			ErrorDialog.showErrorDialog(e);
		}
	}

}
