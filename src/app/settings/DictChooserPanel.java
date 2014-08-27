package app.settings;

import java.io.FileNotFoundException;

import javax.swing.filechooser.FileNameExtensionFilter;

import app.misc.FileChooserPanel;
import engine.Dict;

/**
 * A Dict Choice Panel is the part of a settings dialog for selecting the
 * dictionary of words to be used in a hangman game.
 * 
 * @author Vance Zuo
 * Created: May 14, 2013
 *
 */
public class DictChooserPanel extends FileChooserPanel {	
	// Constructors
	/**
	 * Creates a new Dict Choice Panel with all the GUI compenents.
	 */
	public DictChooserPanel() {
		super("Dictionary");
		setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		setNullText("(Default Dictionary)");
	}
	
	
	// Public methods
	/**
	 * Gets the current Dict selected by the Dict Choice Panel.
	 * @return The currently selected Dict
	 * @throws FileNotFoundException
	 */
	public Dict getDict() throws FileNotFoundException {
		if (getSelectedFile() == null)
			return new Dict();
		return new Dict(getSelectedFile());
	}
	
}
