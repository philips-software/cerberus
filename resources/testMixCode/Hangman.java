public class Hangman {

	/**countAlphabet takes a word and an alphabet
	 * and returns the number of times the alphabet
	 * appears in the word
	 * @param word
	 * @param alphabet
	 * @return
	 */
	public int countAlphabet(String word, char alphabet) {
		int result = 0;
		
		for (char c : word.toCharArray()) {
			if (c == alphabet) result++;
		}
		return result;
	}

	public String fetchWord() {
		return "pizza";
	}

	public String fetchWord(int requestedLength) {
			switch (requestedLength) {
			case 5: return "pizza";
			case 6: return "cheese";
			case 7: return "chicken";
			case 8: return "tomatoes";
			case 9: return "pineapple";
			case 10: return "mozzarella";
			default: return null;
			}
		}
		
}
