package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A Dict object represents a dictionary of words that a hangman game can
 * choose from. It can return a randomly chosen words of
 * a specified length. 
 * <p>
 * The Dict is built from a text file of word strings delimited by whitespace.
 * Only word strings containing solely alphabetic characters are accepted;
 * the rest are ignored, including "words" that contain numbers, apostrophes,
 * or other punctuation. 
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
public class Dict {
	
	// Default dictionary (see the dict package)
	private static final String DEFAULT = "/dict/Brown and LOB 5066.txt";
	private static Random rand = new Random();
	
	// A list of lists, each corresponding to different word lengths.
	private List<List<String>> wordLists;
	
	// Constructors
	/**
	 * Creates a new Dict object using a default word text file.
	 */
	public Dict() {
		this(new Scanner(Dict.class.getResourceAsStream(DEFAULT)));
	}
	
	/**
	 * Creates a new Dict object from a text file denoted by a given path.
	 * @param path String representing path of words text file.
	 * @throws FileNotFoundException if the path does not correspond to an 
	 *                               existing file.
	 */
	public Dict(String path) throws FileNotFoundException {
		this(new File(path));
	}
	
	/**
	 * Creates a new Dict object from a File object.
	 * @param file The File object.
	 * @throws FileNotFoundException if the file does not exist.
	 */
	public Dict(File file) throws FileNotFoundException {
		this(new Scanner(file));
	}
	
	/**
	 * Creates a new Dict object from a Scanner object. The Scanner 
	 * should have a stream of strings to be read as words.
	 * @param in The Scanner object.
	 */
	private Dict(Scanner in) {
		wordLists = new ArrayList<List<String>>();
		while (in.hasNext()) {
			String next = in.next().toLowerCase();
			if (!next.matches("[a-zA-Z]+"))
				continue;
			int index = next.length() - 1; // NOTE: index = word length - 1
			while (index >= wordLists.size()) {
				wordLists.add(new ArrayList<String>());
			}
			wordLists.get(index).add(next);
		}
		in.close();
	}
	
	// Public Methods
	/**
	 * Gets a random word from the Dict of a given length.
	 * @param length The length of the desired word.
	 * @return A string of the desired length, randomly chosen from the Dict.
	 */
	public String getRandom(int length) {
		if (!hasLength(length))
			throw new IllegalArgumentException("Dict has no words of " +
					                           "length " + length);
		List<String> words = wordLists.get(length - 1);
		return words.get(rand.nextInt(words.size()) );
	}
	
	/**
	 * Gets a list of all words in the Dict of a given word length.
	 * @param length The length of the desired words.
	 * @return A list of strings of the words in the Dict of the desired length.
	 */
	public List<String> getWordList(int length) {
		if (!hasLength(length))
			throw new IllegalArgumentException("Dict has no words of " +
					                           "length " + length);
		return wordLists.get(length - 1);
	}
	
	/**
	 * Gets the number of words this Dict contains.
	 * @return The total number of words in this Dict.
	 */
	public int getTotalWords() {
		int total = 0;
		for (List<String> wordList : wordLists) {
			total += wordList.size();
		}
		return total;
	}
	
	/**
	 * Checks if the Dict has any words of a given length.
	 * @param length The word length to check.
	 * @return True if there is at least one word of the desired length in
	 *         the Dict, else false.
	 */
	public boolean hasLength(int length) {
		if (length < 0 || length > wordLists.size())
			return false;
		return !wordLists.get(length - 1).isEmpty();
	}
	
	/**
	 * Gets the length of the shortest word(s) in the Dict.
	 * @return The Dict's shortest word length.
	 */
	public int getMinLength() {
		int min = 1;
		while (!hasLength(min))
			min++;
		return min;
	}
	
	/**
	 * Gets the length of the longest word(s) in the Dict.
	 * @return The Dict's longest word length.
	 */
	public int getMaxLength() {
		return wordLists.size();
	}
	
	/**
	 * Returns a word length based on a weighted random selection of this
	 * Dict's word length distributions. So word lengths corresponding
	 * to greater number of words have a greater chance of being chosen. Only
	 * values corresponding to word lengths that actually exist in the Dict
	 * can be returned.
	 * @return A random word length that the Dict has at least one word of.
	 */
	public int getRandomLength() {
		int totalWeight = getTotalWords();
		int weight = 1 + rand.nextInt(totalWeight);
		Iterator<List<String>> it = wordLists.iterator();
		int length = 0;
		while (weight > 0) {
			weight -= it.next().size();
			length++;
		}
		return length;
	}
	
//	public static void main(String[] args) {
//		try {
//			Dict dict = new Dict("words.txt");
//			String detail = "\n";
//			for (int i = 1; i <= dict.getMaxLength(); i++) {
//				if (!dict.hasLength(i)) {
//					continue;
//				}
//				detail += i + ": ";
//				for (int j = 0; j < dict.getWordList(i).size(); j++) {
//					if (j > dict.getMaxLength() / i) { // Limits line length
//						detail += "(...) ";
//						break;
//					}
//					detail += dict.getWordList(i).get(j) + " ";
//				}
//				detail += "\n";
//			}
//			System.out.println(dict.toString() + detail);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}
