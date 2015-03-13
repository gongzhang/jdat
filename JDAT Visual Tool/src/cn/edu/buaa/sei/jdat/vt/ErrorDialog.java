package cn.edu.buaa.sei.jdat.vt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ErrorDialog extends JDialog {

	private static final long serialVersionUID = 4294613334585035128L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane textPane;
	private final Exception exception;
	
	public static void showErrorDialog(Exception e) {
		ErrorDialog dialog = new ErrorDialog(e);
		Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width / 2;
        int screenHeight = screenSize.height / 2;
        int height = dialog.getHeight();
        int width = dialog.getWidth();
        dialog.setLocation(screenWidth - width / 2, screenHeight - height / 2);
		dialog.setVisible(true);
	}

	public ErrorDialog(Exception e) {
		super();
		this.exception = e;
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Error");
		setResizable(false);
		setBounds(100, 100, 450, 239);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(8, 8));
		{
			JLabel lblNewLabel = new JLabel("Sorry, JDAT encountered with a fatal error. !?(\uFF65_\uFF65;?");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, BorderLayout.NORTH);
		}
		{
			textPane = new JTextPane();
			textPane.setEditable(false);
			contentPanel.add(textPane, BorderLayout.CENTER);
			StringBuffer buf = new StringBuffer(exception.toString());
			for (StackTraceElement element : exception.getStackTrace()) {
				buf.append("\n\t");
				buf.append(element.toString());
			};
			textPane.setText(buf.toString());
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
			{
				JButton btnCrash = new JButton("Crash Now");
				btnCrash.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
				btnCrash.setActionCommand("OK");
				buttonPane.add(btnCrash);
				getRootPane().setDefaultButton(btnCrash);
			}
			{
				JButton btnContinue = new JButton("Continue");
				btnContinue.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				btnContinue.setActionCommand("Cancel");
				buttonPane.add(btnContinue);
			}
		}
	}

}
