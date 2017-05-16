import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;


public class Main {
	public static void main(String[] args) {
		String dictionaryDir;
		
		// Optional parameter for using a different dictionary
		if (args.length > 0) {
			dictionaryDir = args[0];
		}
		else {
			dictionaryDir = "words";
		}
		
		TrieWrapper trie = new TrieWrapper(dictionaryDir);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.print("> ");
				System.out.println(trie.wordCorrection(reader.readLine()));
			}
		}
		catch (IOException ioex) {
			System.out.println(ioex);
		}
	}
}
