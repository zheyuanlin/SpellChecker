import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class TrieWrapper {
	
	TrieNode root;
	
	private class TrieNode {
		boolean isWord;
		char characterKey;
		HashMap<Character, TrieNode> suffixes;
		
		/**
		 * TrieNode constructor
		 * @param characterKey
		 * @param parent
		 */
		TrieNode(char characterKey, TrieNode parent) {
			this.characterKey = characterKey;
			isWord = false;
			suffixes = new HashMap<>();
		}
		
		/**
		 * Adds a word to the trie
		 * @param word: word to add
		 * @param i
		 */
		void add(String word, int i) {
			if (word.length() == i) this.isWord = true;
			else {
				char potentialNodeKey = word.charAt(i);
				TrieNode potentialNewNode = suffixes.get(potentialNodeKey);
				if (potentialNewNode == null) {
					potentialNewNode = new TrieNode(potentialNodeKey, this);
					suffixes.put(new Character(potentialNodeKey), potentialNewNode);
				}
				
				potentialNewNode.add(word, i + 1);
			}
		}
		
		boolean contains(String word, int i) {
			if (word.length() == i) return this.isWord;
			else {
				TrieNode n = suffixes.get(word.charAt(i));
				if (n != null) return n.contains(word, i + 1);
				else return false;
			}
		}
		
		/**
		 * Attempts to search for the closest match in the trie through depth first search... madting
		 * @param in
		 * @param i
		 * @return
		 */
		private String dfs(String in, int i) {
			
			if (i == in.length()) {
			
				if (this.isWord) {
					return "" + characterKey;
				}
				return null;
			} 
			if (inLastSeg(in, i - 1) && this.isWord) { 
				return "" + characterKey;
			}
			
			else {
				String ans;
				char ogChar = in.charAt(i); // Character at original index
				Stack<TrieNode> dfsStack = new Stack<>(); // Stack for DFS, each call gets a new stack
				  										  // because different indices ("i" values)
				Stack<Character> permutations = generatePermutations(ogChar);
				
				for (char c : permutations) {
					TrieNode nodeToSearch = suffixes.get(c);
					if (nodeToSearch != null) {
						dfsStack.push(nodeToSearch);
					}
				}
				
				while(!dfsStack.isEmpty()) {
					ans = dfsStack.pop().dfs(in, i + 1);
					if (ans != null) return this.characterKey + ans;
				}
				
				// case: no char before or after - STOP
				if (i >= in.length() - 1 || i < 1) {
					return null;
				}
				
				// case: dup letters causing window shifts
				if (ogChar == in.charAt(i - 1)) {
					char nextDifChar;
					while (++i < in.length()) {
						nextDifChar = in.charAt(i);
						if (nextDifChar != ogChar) {
							permutations = generatePermutations(nextDifChar);
							for (char ch : permutations) {
								TrieNode nodeToSearch = suffixes.get(ch);
								if (nodeToSearch != null) {
									dfsStack.push(nodeToSearch);
								} 
							}
							
							while (!dfsStack.isEmpty()) {
								ans = dfsStack.pop().dfs(in, i+1);
								if (ans != null) {
									return this.characterKey + ans;
								}
							}
							break;
	
						}
					}
					
				}
				
				// Still nothing? then no suggestions
				return null;
				
			}
		}
		
		private boolean inLastSeg(String in, int i) {
			int last = in.length() - 1;
			while (last > 0) {
				if (in.charAt(last) != in.charAt(last - 1)) break;
				--last;
			}
			return !(i < last);
		}
		
		
	}
	
	boolean contains(String s) {
		return root.contains(s, 0);
	}
	
	public String wordCorrection(String in) {
		if (in == null) return null;
		if (in.isEmpty()) return "No correction available.";
		if (root.contains(in, 0)) return in; // input exists, no need to correct
		String answer = root.dfs(in, 0);
		return (answer != null) ? answer : "No correction available.";
	}
	
	public static Stack<Character> generatePermutations(char c) {
		Stack<Character> ans = new Stack<>();
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
			for (char ch : "UOIEAuoiea".toCharArray()) {
				ans.push(ch);
			}
		}
		else if (Character.isAlphabetic(c)) {
			ans.push(Character.toUpperCase(c));
			ans.push(Character.toLowerCase(c));
		}
		else {
			ans.push(c);
		}
		return ans;
	}
	
	// Constructor
	public TrieWrapper(String dictPath) {
		root = new TrieNode('\u0000', null);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dictPath));
			for ( String word = reader.readLine(); word != null; word = reader.readLine()) {
				root.add(word.trim(), 0);
			}
			reader.close();
		}
		catch (FileNotFoundException fileExept) {
			System.out.println("dictPath does not contain valid file or does not exist: EXITING"); 
			System.exit(0);
		}
		catch (IOException ioExept) {
			System.out.println("dictionary io error: EXITING"); 
			System.exit(0);
		}
		
	}
		

	
}
