package app.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * The About Dialog displays background information about the Hangman program.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
class AboutDialog extends JDialog {

	private static final String TITLE = "About";
	private static final Icon ABOUT_ICON = 
			UIManager.getIcon("OptionPane.informationIcon");
	// HTML file containing the About information
	private static final URL INFO =
			AboutDialog.class.getResource("/info/about.html");
	private static final int MARGIN = 10;
	private static final int WIDTH = 560;
	private static final int HEIGHT = 370;

	// Instance fields
	private JPanel iconPanel;	
	private JPanel buttonPanel;
	private JButton closeButton;
	private JScrollPane scrollPane;
	private JEditorPane infoBox;

	// Constructors
	/**
	 * Creates the About Dialog.
	 * @param parent The parent frame of this dialog. Should be a Hangman Frame.
	 */
	public AboutDialog(Frame parent) {
		super(parent, TITLE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setIconImage(parent.getIconImage());
		
		getContentPane().setLayout(new BorderLayout());
		
		iconPanel = new JPanel();
		iconPanel.setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
		iconPanel.add(new JLabel(ABOUT_ICON));
		getContentPane().add(iconPanel, BorderLayout.WEST);
		
		// The main content
		scrollPane = new JScrollPane(infoBox);
		scrollPane.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane);
		{
			infoBox = new JEditorPane();
			infoBox.setBackground(new Color(0,0,0,0)); // Transparent color
			infoBox.setContentType("text/html");
			infoBox.setOpaque(false);
			infoBox.setFocusable(false);
			infoBox.setEditable(false);
			try {
				infoBox.setPage(INFO);
			} catch (IOException e) {
				String error = "Error: Could not find help infomation file.";
				infoBox.setText(error);
				System.err.println(error);
				e.printStackTrace();
			}
			scrollPane.setViewportView(infoBox);
		}
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		{
			closeButton = new JButton("Close");
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
			buttonPanel.add(closeButton);
		}
		
		// Default button = close dialog
		getRootPane().setDefaultButton(closeButton);
		
		pack();
	}

}
