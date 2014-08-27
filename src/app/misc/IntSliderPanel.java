package app.misc;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An Int Slider Panel is intended for setting integral parameters. 
 * It consists of a JSlider, and a JLabel denoting the selected value.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
public class IntSliderPanel extends JPanel {
	
	private static final int H_GAP = 5; // Defaut horizontal Spacing

	// Instance Fields
	private JSlider slider;
	private JLabel value;

	private Component horizontalStrut;
	
	// Constructors
	/**
	 * Creates a new Int Slider Panel with given title name, min/max values,
	 * and starting value. 
	 * @param min  The lowest selectable value
	 * @param max  The highest selectable value
	 * @param init The initially selected value
	 */
	public IntSliderPanel(String title, int min, int max, int init,
			boolean labelOnRight) {
		setBorder(new TitledBorder(title));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		if (labelOnRight) {
			initSlider(min, max, init);	
			addSpacing();
			initLabel();
		} else {
			initLabel();	
			addSpacing();
			initSlider(min, max, init);	
		}
	}
	
	public IntSliderPanel(String title, int min, int max, int init) {
		this(title, min, max, init, true);
	}
	
	public IntSliderPanel(String title, int min, int max) {
		this(title, min, max, min);
	}

	/**
	 * Initializes and adds slider, with visible snap-to ticks.
	 * @param min lowest number that can be selected
	 * @param max highest number that can be selected
	 * @param init number that slider is set to initially
	 */
	private void initSlider(int min, int max, int init) {
		slider = new JSlider(min, max, init);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		// Upon slide, label displaying selected value is updated
		slider.addChangeListener(new ChangeListener() {		
			@Override public void stateChanged(ChangeEvent e) {
				value.setText(String.valueOf(getValue()));
			}
		});
		add(slider);
	}
	
	/**
	 * Adds a horizontal strut, for creating space between components.
	 * @param size size in pixels of the strut
	 */
	private void addSpacing() {
		horizontalStrut = Box.createHorizontalStrut(H_GAP);
		add(horizontalStrut);
	}
	
	/**
	 * Initializes and adds the slider's label
	 */
	private void initLabel() {
		value = new JLabel(String.valueOf(getValue()));
		add(value);
	}
	
	
	// Public Methods
	/**
	 * Adds a change listener to the slider.
	 * @param l The ChangeListener object.
	 */
	public void addChangeListener(ChangeListener l) {
		slider.addChangeListener(l);
	}
	
	/**
	 * Gets the selected (numerical) value of the Int Slider Panel.
	 * @return The current value of the panel's slider.
	 */
	public int getValue() {
		return slider.getValue();
	}
	
	/**
	 * Sets the selected (numerical) value of the Int Slider Panel to a 
	 * new value, if that value is within the minimum/maximum bounds of the 
	 * slider's selectable values.
	 */
	public void setValue(int n) {
		if (n >= slider.getMinimum() && n <= slider.getMaximum())
			slider.setValue(n);
	}
	
	/**
	 * Sets the minimum selectable value in the Int Slider Panel.
	 * @param newMin The new minimum value
	 */
	public void setMin(int newMin) {
		slider.setMinimum(newMin);
	}
	
	/**
	 * Sets the maximum selectable value in the Int Slider Panel.
	 * @param newMax The new maximum value
	 */
	public void setMax(int newMax) {
		slider.setMaximum(newMax);
	}
	
	/**
	 * Sets the spacing between the numerical label and slider of the panel.
	 * @param spacing The new spacing in pixels
	 */
	public void setSpacing(int spacing) {
		if (spacing >= 0)
			horizontalStrut.setSize(horizontalStrut.getWidth(), spacing);
	}

}
