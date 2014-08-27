package app.misc;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

/**
 * A File Chooser Panel provides a GUI for users to select files.
 * It is also capable of selecting a 'default' file (likely in the class path) 
 * if the user does not choose a file.
 * 
 * @author Vance Zuo
 * Created: Aug 9, 2014
 *
 */
public class FileChooserPanel extends JPanel {
	// Constants
	private static final String TOGGLE_TEXT = "Use Custom";
	private static final String NULL_FILE_TEXT = "(Nothing Selected)";
	private static final int H_GAP = 20; // Horizontal spacing
	
	// Private utility classes
	private class OpenFileListener implements ActionListener {
		@Override public void actionPerformed(ActionEvent arg0) {
			int returnVal = fileSelect.showOpenDialog(FileChooserPanel.this);		 
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    	setSelectedFile(fileSelect.getSelectedFile());
		    }
		}
	}

	private class ToggleListener implements ActionListener {		
		@Override public void actionPerformed(ActionEvent arg0) {
			if (toggleCheckBox.isSelected()) {
				setFileSelectable(true);
				String oldPath = fileText.getText();
				String defaultString = nullFileText;
				// Keep null selected if none previously selected
				if (oldPath.isEmpty() || oldPath.equals(defaultString)) {
					fileText.setText(defaultString);
					return;
				}
				setSelectedFile(new File(oldPath));
			} else {
				setFileSelectable(false);
				setSelectedFile(null);
			}					
		}
	}

	
	// Instance Fields
	private JCheckBox toggleCheckBox;
	private JFileChooser fileSelect;
	private JTextField fileText;
	private JButton openButton; // Open file button
	
	private String nullFileText;
	
	private File selectedFile;
	
	
	// Constructors
	/**
	 * Creates a new File Chooser Panel. 
	 * If toggable is true, then a checkbox is added giving the user the option
	 * to enable or disable file selection.
	 * 
	 * @param title the title of the Panel
	 * @param toggable whether to allow user to enable/disable file selection
	 */
	public FileChooserPanel(String title, boolean toggable) {
		setBorder(new TitledBorder(title));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		nullFileText = NULL_FILE_TEXT;
		
		selectedFile = null;
		
		if (toggable) {
			initToggleCheckbox();
			addSpacing(H_GAP);
		}
		
		initFileChooser();		
		
		initFilePathDisplay();
		initOpenButton();
		
		if (toggable)
			setFileSelectable(false);
	}
	
	/**
	 * Creates a new File Chooser Panel that is toggable.
	 * @param title the title of the Panel
	 */
	public FileChooserPanel(String title) {
		this(title, true);
	}
	
	/**
	 * Initializes and adds a toggle checkbox
	 */
	private void initToggleCheckbox() {
		toggleCheckBox = new JCheckBox(TOGGLE_TEXT);
		// Upon check, current Dict is changed to standard (default) Dict;
		// upon uncheck, if there was a previously selected file as the
		// Dict, the current Dict is changed to that, else current Dict stays
		// the same. Components are enabled/disabled accordingly.
		toggleCheckBox.addActionListener(new ToggleListener());
		add(toggleCheckBox);
	}

	/**
	 * Initializes the file chooser (starts in the current working directory).
	 */
	private void initFileChooser() {
		fileSelect = new JFileChooser();
		fileSelect.setCurrentDirectory(new File(".")); // cwd
	}	
	
	/**
	 * Initializes and add a text field displaying the selected file.
	 * It will fill up as much space as possible in the panel.
	 */
	private void initFilePathDisplay() {
		fileText = new JTextField();
		fileText.setEditable(false);
		fileText.setMaximumSize(new Dimension(
				Integer.MAX_VALUE, 
				fileText.getPreferredSize().height) );
		// fileText.setHorizontalAlignment(JTextField.RIGHT);
		add(fileText);
	}
	
	/**
	 * Initializes and adds the open button, which triggers the file chooser.
	 */
	private void initOpenButton() {
		openButton = new JButton("Open");
		// Upon click, file chooser is opened, and current Dict is 
		// changed to the chosen file.
		openButton.addActionListener(new OpenFileListener());
		add(openButton);
	}

	/**
	 * Adds a horizontal strut, for creating space between components.
	 * @param size size in pixels of the strut
	 */
	private void addSpacing(int size) {
		add(Box.createHorizontalStrut(size));
	}
		
	
	// Public methods
	/**
	 * Sets the file filter of the file chooser.
	 * @param ff The file filter
	 */
	public void setFileFilter(FileFilter ff) {
		fileSelect.setFileFilter(ff);
	}
	
	/**
	 * Sets the starting directory of the file chooser.
	 * @param dir The file directory
	 */
	public void setDirectory(File dir) {
		fileSelect.setCurrentDirectory(dir);
	}
	
	/**
	 * Sets the displayed title of the panel.
	 * @param title new title of the file chooser panel
	 */
	public void setTitle(String title) {
		setBorder(new TitledBorder(title));
	}
	
	/**
	 * Sets the displayed text when no file is selected by the file chooser.
	 * @param nullText the 'null' file text
	 */
	public void setNullText(String nullText) {
		nullFileText = nullText;
	}
	
	/**
	 * Gets the file selected by the file chooser, or null if none chosen.
	 * @return The selected file
	 */
	public File getSelectedFile() {
		return selectedFile;
	}
	
	/**
	 * Changes the selected file to a new file. Null parameter acceptable.
	 * @param file The new file
	 */
	public void setSelectedFile(File file) {
		String path = (file == null) ? nullFileText : file.getAbsolutePath();
		File oldFile = selectedFile;
		selectedFile = file;
		try {
			// Firing event so listeners can verify and confirm/veto change.
			fireVetoableChange("selectedFile", oldFile, file);
			fileText.setText(path);
			if (file != null) {
				String msg = "Selected file " + selectedFile.getName();
				JOptionPane.showMessageDialog(getParent(), msg);
			}
		} catch (PropertyVetoException e) {
			selectedFile = oldFile;			
			System.err.print("Error: Unable to select file " + path + ".");
			e.printStackTrace();
		}
	}
	
	
	// Private methods
	/**
	 * Enables/disables the components for enabling/disabling the file chooser.
	 * @param isEnabled whether to enable or disable the ability to choose
	 *                  files.
	 */
	private void setFileSelectable(boolean isEnabled) {
		if (isEnabled) {
			toggleCheckBox.setSelected(true);
			fileText.setEnabled(true);
			openButton.setEnabled(true);
		} else {
			toggleCheckBox.setSelected(false);
			fileText.setEnabled(false);
			openButton.setEnabled(false);
		}		
	}
}
