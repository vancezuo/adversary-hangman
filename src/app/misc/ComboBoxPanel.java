package app.misc;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

/**
 * A Combo Box Panel provides a GUI for selecting list items from a combo box.
 * The panel, besides containing a Swing Combo Box, also displays a description
 * about the option as specified by a map from items to info strings.
 * 
 * @author Vance Zuo
 * Created: Aug 11, 2014
 *
 */
public class ComboBoxPanel<E> extends JPanel {
	// Constants
	private static final int H_GAP = 10; // Horizontal Spacing
	
	// Private utility classes
	private class ComboSelectListener implements ActionListener {
		@Override public void actionPerformed(ActionEvent arg0) {
			info.setText(itemInfoMap.get(comboBox.getSelectedItem()));
		}
	}

	
	// Instance Fields
	private JComboBox<E> comboBox;
	private JLabel info;
	
	private Map<E, String> itemInfoMap;
	

	// Constructors
	/**
	 * Creates a new Mode Choice Panel with given initial Mode and title.
	 * 
	 * @param title 
	 * @param itemMap
	 * @param initItem
	 */
	public ComboBoxPanel(String title, Map<E, String> itemMap, E initItem) {
		setBorder(new TitledBorder(title));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		itemInfoMap = itemMap;

		initComboBox(initItem);
		addSpacing(H_GAP);
		initDescriptionLabel();
		addSpacing(H_GAP);
		addLineFiller();
	}


	/**
	 * Adds a separator that fills in unused space between components.
	 * Note this is a horizontal separator filling in horizontal space.
	 */
	private void addLineFiller() {
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(
				Integer.MAX_VALUE, 
				separator.getPreferredSize().height) );
		add(separator);
	}


	/**
	 * Initializes and adds a label describing the selected item.
	 * @param itemMap
	 */
	private void initDescriptionLabel() {
		info = new JLabel(itemInfoMap.get(comboBox.getSelectedItem()));
		add(info);
	}


	/**
	 * Adds a horizontal strut, for creating space between components.
	 * @param size size in pixels of the strut
	 */
	private void addSpacing(int size) {
		add(Box.createHorizontalStrut(size));
	}


	/**
	 * Initializes and adds the combo box. 
	 * @param initItem The to be selected item in the combo box
	 */
	private void initComboBox(E initItem) {
		comboBox = new JComboBox<E>();
		for (E e : itemInfoMap.keySet())
			comboBox.addItem(e);
		comboBox.setSelectedItem(initItem);
		comboBox.setMaximumSize(comboBox.getPreferredSize());
		// When a new item is selected, the description label is updated.
		comboBox.addActionListener(new ComboSelectListener());
		add(comboBox);
	}

	
	// Public methods
	/**
	 * Gets the currently selected item.
	 * @return the item selected in the combo box
	 */
	@SuppressWarnings("unchecked")
	public E getSelectedItem() {
		return (E) comboBox.getSelectedItem();
	}
	
	/**
	 * Adds an action listener for changes in the combo box's selection.
	 * @param l the Action Listener object
	 */
	public void addActionListener(ActionListener l) {
		comboBox.addActionListener(l);
	}
	
	/**
	 * Sets the currently selected item.
	 * @param item the item to select
	 */
	public void setSelectedItem(E item) {
		// Note: automatically fires action event
		comboBox.setSelectedItem(item);
	}
}
