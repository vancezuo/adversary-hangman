Adversary Hangman
=================

Adversary Hangman is the classic hangman game with some added bells and whistles. It is written in Java using Swing and tries to follow a MVC/P pattern. The game's dictionary, word choice algorithm, word length, and lives can be customized in the settings dialog as detailed below.

Dictionary
----------

Any text file where words are separated by whitespace can be used by the program. Words containing non-letters and numbers are ignored. Note that duplicates are not culled. The default word list used is the Brown and LOB Corpus' 5066 most common words. For more varied and challenging sets, Alan Beale's 12dicts Word Lists (http://www.wyrdplay.org/12dicts.html) is a good resource.

Word Choice
-----------

Under this category, the user can change the word choice algorithm used by the program. Random is what it sounds like. Scrabble randomly selects 10 words from the dictionary, and chooses the one yielding the highest base score in the game of Scrabble. Adversary is the most challenging mode, inspired by the concept of adversary models. It doesn't choose a word in the beginning; instead, it 'constructs' its word based on the user's letter choices over the course of the game, attempting to force the user to make as many guesses as possible to solve the word.

Word Length and Lives
---------------------

These should be self-explanatory. Note that depending on the dictionary, there may be word lengths that are invalid, but are technically selectable in the settings interface. If an invalid length is selected, the program prompts the user to select something else. There is also the option, enabled by default, of randomizing the word length after each game.
