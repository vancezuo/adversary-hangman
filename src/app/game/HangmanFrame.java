package app.game;

import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * A Hangman Frame is the main interface for the Hangman game application. 
 * It contains a menu, the hangman, the hangman word, and an on-screen 
 * keyboard. It also resizes these elements accordingly as the window is
 * resized. 
 * 
 * @author Vance Zuo
 * Created: May 23, 2013
 *
 */
public class HangmanFrame extends JFrame {
	// Constants
	private static final String TITLE = "Adversary Hangman";
	private static final URL ICON = 
			HangmanFrame.class.getResource("/img/icon.png");
	private static final String LOOK_AND_FEEL = "Nimbus";	
	
	// Instance Fields
	private HangmanImagePanel hangman;
	private HangmanWordPanel word;
	private HangmanKeyboardPanel alphabet;
	private HangmanMenu menu;
	
	// Heigh and width parameters used to scale Man Panel graphics correctly.
	private int height;
	private int width;
	
	
	// Private utility classes
	/**
	 * The Resize Adapter updates the size of the Hangman Frame components
	 * as the frame's size changes. 
	 * @author Vance Zuo
	 * Created: May 23, 2013
	 *
	 */
	private class ResizeAdapter extends ComponentAdapter {
		@Override public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			int newWidth = getContentPane().getWidth();
			int newHeight = getContentPane().getHeight();
			double widthRatio = (double) newWidth / width;
			double heightRatio = (double) newHeight / height;
			height = newHeight;
			width = newWidth;
			alphabet.updateSize(widthRatio, heightRatio);
			hangman.updateSize(widthRatio, heightRatio);
			word.updateSize(widthRatio, heightRatio);
		}
	}
	
	
	// Constuctors
	/**
	 * Creates the application.
	 * @param initLives 
	 * @param initLength 
	 */
	public HangmanFrame(int initLives, int initLength) {
		initLookAndFeel();
		initGuiComponents(initLives, initLength);
		initGuiProperties();
		height = getContentPane().getHeight();
		width = getContentPane().getWidth();
	}
	

	/**
	 * Sets various properties of the Hangman Frame like its size, location,
	 * and close operation.
	 */
	private void initGuiProperties() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setMinimumSize(getSize());
	}

	/**
	 * Initialize the contents of the Hangman Frame.
	 * @param initLives 
	 * @param initLength 
	 */
	private void initGuiComponents(int initLives, int initLength) {	
		setIconImage(Toolkit.getDefaultToolkit().getImage(ICON));
		setTitle(TITLE);
		
		hangman = new HangmanImagePanel(initLives);
		
		word = new HangmanWordPanel(initLength);
		
		alphabet = new HangmanKeyboardPanel();
		
		menu = new HangmanMenu();
		setJMenuBar(menu);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(hangman)
					.addComponent(word) )
				.addComponent(alphabet)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(hangman)
						.addComponent(word) )
					.addComponent(alphabet) )
		);
		getContentPane().setLayout(layout);
		
		// Does't work for scaling hangman image
		// layout.linkSize(SwingConstants.VERTICAL, hangman, alphabet, word);
		
		// When the widow is resized, this listener records the dimensions
		// and updates the image sizes the the frame's components accordingly.
		getContentPane().addComponentListener(new ResizeAdapter());
	}
	
	/**
	 * Sets the Look and Feel for the GUI of this application. 
	 */
	private void initLookAndFeel() {
        if (LOOK_AND_FEEL == null)
        	return;
        
        String lookAndFeel = null; 
        if (LOOK_AND_FEEL.equals("System")) { // "System" being a custom option
        	lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        } else {
        	for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        		if (LOOK_AND_FEEL.equals(info.getName())) {
        			lookAndFeel = info.getClassName();
        			break;
        		}
        	}
        }
        if (lookAndFeel == null)
        	return;

        try {        	
        	UIManager.setLookAndFeel(lookAndFeel);          
        } catch (Exception e) {
        	System.err.printf("Look and feel (%s) not found. ", lookAndFeel);
        	System.err.println("Using the default look and feel.");
        	e.printStackTrace();
        } 
    }
	
	// Public Methods
	/**
	 * Gets the image panel of the frame.
	 * @return the Hangman Frame's hangman image panel instance.
	 */
	public HangmanImagePanel getImagePanel() {
		return hangman;
	}

	/**
	 * Gets the word panel of the Hangman Frame.
	 * @return the Hangman Frame's hangman word panel instance.
	 */
	public HangmanWordPanel getWordPanel() {
		return word;
	}

	/**
	 * Gets the keyboard panel of the Hangman Frame.
	 * @return the Hangman Frame's hangman keyboard panel instance.
	 */
	public HangmanKeyboardPanel getAlphabetPanel() {
		return alphabet;
	}

	/**
	 * Gets the menu of the Hangman Frame.
	 * @return the Hangman Frame's menu instance.
	 */
	public HangmanMenu getMenu() {
		return menu;
	}
}
