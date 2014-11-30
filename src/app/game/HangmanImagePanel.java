package app.game;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The Man Panel contains an image of the hangman in a hangman game. As the
 * player loses lives, the image can be updated to display the hangman
 * in progressively more dire states (until the final state where he is
 * fully drawn and hanged).
 * 
 * @author Vance Zuo
 * Created: May 24, 2013
 *
 */
class HangmanImagePanel extends JPanel {
	// Constants
	private static final String PATH = "/img/h"; // Prefix for all the images
	// Where the number of lives is fewer than the number of hangman images,
	// images with higher priority (lower number) are preferred.
	private static final int[] PRIORITY = {12, 18, 16, 20, 15, 13, 17, 14,
		19, 11, 10, 9, 8, 7, 2, 0, 3, 4, 5, 6, 1};
	private static final int NUM_IMG = 21;

	private static final String FONT = "Goudy Stout";
	private static final double INIT_SCALE = 1.0;
	private static final int INIT_HEIGHT = 108; // In pixels
	private static final int INIT_FONT_SIZE = 14;


	// Instance Variables
	private double heightScale; // For resizing image upon window resize
	private float fontScale;

	private ImageIcon[] images;
	private JButton man;
	private int totalLives;
	private int lives;
	private int current; // Current state, or image #


	// Constructors
	/**
	 * Creates the panel with a starting image based on the starting
	 * number of lives.
	 * @param initLives The starting number of lives the player has.
	 */
	public HangmanImagePanel(int initLives) {
		setLayout(new GridLayout());
		initImages();
		initScalingVars();
		initHangmanImage();
		reset(initLives); // Sets to correct starting image
	}

	/**
	 * Initializes the base image icons.
	 */
	private void initImages() {
		images = new ImageIcon[NUM_IMG];
		for (int i = 0; i < NUM_IMG; i++) {
			URL current = HangmanImagePanel.class.getResource(PATH + i + ".png");
			System.out.println(current);
			images[i] = new ImageIcon(current);
		}
	}

	/**
	 * Initializes the variables involved in rescaling the hangman image.
	 * Base images icons must be initialized first.
	 */
	private void initScalingVars() {
		// Note the image scale is set so its initial height is INIT_HEIGHT.
		int imgHeight = images[0].getIconHeight();
		heightScale = INIT_SCALE * INIT_HEIGHT / imgHeight;
		fontScale = (float) INIT_SCALE;
	}

	/**
	 * Initializes the actual hangman image.
	 * Scaling variables must be initialized first.
	 */
	private void initHangmanImage() {
		man = new JButton();
		man.setFont(new Font(
				FONT, Font.PLAIN, (int) (fontScale * INIT_FONT_SIZE)));
		man.setVerticalTextPosition(SwingConstants.BOTTOM);
		man.setHorizontalTextPosition(SwingConstants.CENTER);
		// Next three methods make button effectively invisible other than
		// image and text. Undo to see to border for debugging.
		man.setOpaque(false);
		man.setContentAreaFilled(false);
		man.setBorderPainted(false);
		add(man);
	}

	// Public Methods
	/**
	 * Resets the Man Panel for a new game, based on the starting number of
	 * lives for that game. The image is reset accordingly.
	 * @param initLives The starting number of lives the player has.
	 */
	public void reset(int initLives) {
		updateIcon(images[0]);
		man.setText(String.valueOf(initLives)); // Resets lives text
		totalLives = initLives;
		lives = initLives + 1;
		current = -1;
		displayNext();
	}

	/**
	 * Displays the last image in the hangman sequence (corresponding to
	 * surrendering and ending the game). The number of lives text is
	 * set to 0.
	 */
	public void displayLast() {
		current = NUM_IMG - 2;
		lives = 1;
		displayNext();
	}

	/**
	 * Display the next image in the hangman sequence, updating the lives
	 * remaining text accordingly.
	 */
	public void displayNext() {
		if (lives <= 0)
			throw new IllegalStateException("No hangman images left.");
		// Updates number of lives
		lives--;
		man.setText(String.valueOf(lives));
		// More lives than images => keep image same for now
		if (lives >= NUM_IMG)
			return;
		// Ignores images that must be skipped (since there may be more images
		// than total lives, lower priority ones are passed over).
		do { current++; } while (PRIORITY[current] > totalLives);
		// Sets new icon
		updateIcon(images[current]);
	}

	/**
	 * Updates the size of the button graphics based on a previous width
	 * and height change of the content pane this Man Panel is contained in.
	 * @param widthChangeRatio  The ratio of the current content pane width to
	 *                          the width when this panel size was last updated.
	 * @param heightChangeRatio The ratio of the current content pane height to
	 *                          the width when this panel size was last updated.
	 */
	public void updateSize(double widthChangeRatio, double heightChangeRatio) {
		heightScale *= heightChangeRatio;
		fontScale *= heightChangeRatio;
		// Sets new icon (with correct size)
		updateIcon(images[current]);
	}

	// Private Methods
	/**
	 * Sets the image to a new Image Icon. The size may be different from the
	 * previous displayed icon.
	 * @param icon Image Icon to display.
	 */
	private void updateIcon(ImageIcon icon) {
		int width = (int) (heightScale * icon.getIconWidth());
		int height = (int) (heightScale * icon.getIconHeight());
		Image scaled = icon.getImage().getScaledInstance(
				width, height, Image.SCALE_SMOOTH);
		man.setIcon(new ImageIcon(scaled));
		man.setFont(man.getFont().deriveFont(fontScale * INIT_FONT_SIZE));
		// man.setPreferredSize(new Dimension(width, height));
	}
}
