package app.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import engine.Dict;

/**
 * A Dict Choice Panel is the part of a settings dialog for selecting the
 * dictionary of words to be used in a hangman game.
 * 
 * @author Vance Zuo
 * Created: May 14, 2013
 *
 */
class DictChoicePanel extends JPanel implements SettingsDialog.Restorable {
	
	private static final int H_GAP = 20; // Horizontal spacing

	// Instance Fields
	private JCheckBox defaultCheckBox;
	private JFileChooser fileSelect;
	private JTextField fileText;
	private JButton openButton; // Open file button
	
	private Component horizontalStrut;
	
	private Dict currentDict;
	private Dict prevDict;
	private Dict standardDict;

	// Constructors
	/**
	 * Creates a new Dict Choice Panel with a given default Dict and 
	 * an initial Dict given by a file path. 
	 * @param defaultDict    Dict to be used as the default.
	 * @param customDictPath String file path to the Dict to be used currently.
	 */
	public DictChoicePanel(Dict defaultDict, String customDictPath) {
		this(defaultDict);
		changeDict(new File(customDictPath));
	}
	
	/**
	 * Creates a new Dict Choice Panel with a given default Dict, which
	 * will also be set as the initial Dict.
	 * @param defaultDict Dict to be used as the default.
	 */
	public DictChoicePanel(Dict defaultDict) {		
		this();
		currentDict = defaultDict;	
		prevDict = currentDict;
		standardDict = defaultDict;
		setCustomDict(false);
	}

	/**
	 * Creates a new Dict Choice Panel with all the GUI compenents.
	 * Initially, the "Use Default" option is checked, with no custom
	 * dictionary file selected.
	 */
	private DictChoicePanel() {
		setBorder(new TitledBorder("Dictionary"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		defaultCheckBox = new JCheckBox("Use Default");
		// Upon check, current Dict is changed to standard (default) Dict;
		// upon uncheck, if there was a previously selected file as the
		// Dict, the current Dict is changed to that, else current Dict stays
		// the same. Components are enabled/disabled accordingly.
		defaultCheckBox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				if (defaultCheckBox.isSelected()) {
					setCustomDict(false);
					Dict oldDict = currentDict;
					currentDict = standardDict;
					// Firing event so main GUI knows to update its Dict
					firePropertyChange("currentDict", oldDict, currentDict);
				} else {
					setCustomDict(true);
					String oldPath = fileText.getText();
					String defaultString = "(Default Dictionary)";
					if (oldPath.isEmpty() || oldPath.equals(defaultString)) {
						fileText.setText(defaultString);
						return;
					}
					changeDict(new File(oldPath));
				}					
			}
		});
		add(defaultCheckBox);
		
		horizontalStrut = Box.createHorizontalStrut(H_GAP);
		add(horizontalStrut);
		
		fileSelect = new JFileChooser();
		fileSelect.setCurrentDirectory(new File(".")); // Program's directory
		fileSelect.setFileFilter(new FileNameExtensionFilter(
				"Text Files", "txt"));
		
		fileText = new JTextField();
		fileText.setEditable(false);
		fileText.setMaximumSize(new Dimension(
				Integer.MAX_VALUE, 
				fileText.getPreferredSize().height) );
		// fileText.setHorizontalAlignment(JTextField.RIGHT);
		add(fileText);

		openButton = new JButton("Open");
		// Upon click, file chooser is opened, and current Dict is 
		// changed to the chosen file.
		openButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileSelect.showOpenDialog(DictChoicePanel.this);		 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                changeDict(fileSelect.getSelectedFile());
	            }
			}
		});
		add(openButton);
	}
	
	// Public Methods
	/**
	 * Gets the current Dict selected by the Dict Choice Panel.
	 * @return The currently selected Dict
	 */
	public Dict getDictionary() {
		return currentDict;
	}

	@Override
	public void save() {
		prevDict = currentDict;
	}

	@Override
	public void restore() {
		currentDict = prevDict;
	}	
	
	// Private Methods
	/**
	 * Enables/disables the components for choosing a custom dictionary.
	 * @param useCustom Whether the components' states should correspond to
	 *                  allowing custom dictionary to be chosen, or not.
	 */
	private void setCustomDict(boolean useCustom) {
		if (useCustom) {
			defaultCheckBox.setSelected(false);
			fileText.setEnabled(true);
			openButton.setEnabled(true);
		} else {
			defaultCheckBox.setSelected(true);
			fileText.setEnabled(false);
			openButton.setEnabled(false);
		}		
	}
	
	/**
	 * Changes the current Dict to a new Dict based on a given File. If 
	 * the file is not found or other exceptions occur, an error message dialog
	 * is displayed, and no changes the current Dict are made.
	 * @param newDict File to be basis for new current Dict
	 */
	private void changeDict(File newDict) {
		Dict old = currentDict;
		try {
			currentDict = new Dict(newDict);
			// Firing event so main game interface knows to update its Dict
			firePropertyChange("currentDict", old, currentDict);
			fileText.setText(newDict.getAbsolutePath());
			String msg = "Successfully loaded file " + newDict.getName();
			JOptionPane.showMessageDialog(getParent(), msg);
		} catch (FileNotFoundException e) {
			currentDict = old;
			String error = "Requested file was not found.";
			JOptionPane.showMessageDialog(getParent(), error,  "Error", 
					JOptionPane.ERROR_MESSAGE);
			System.err.println("Error: " + error);
			e.printStackTrace();
		} catch (Exception e) {
			currentDict = old;
			String error = "Unable to load file for some reason.";
			JOptionPane.showMessageDialog(getParent(), error,  "Error", 
					JOptionPane.ERROR_MESSAGE);
			System.err.println("Error: " + error);
			e.printStackTrace();
		}
	}
}
