import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Detector {

	public static List<String> totalWords = new ArrayList<String>();
	public static String[] words = null;
	public static int wordCountPerSentence = 0;
	public static String fileName1 = ""; // insert a fileName here
	public static String fileName2 = ""; // and here to get statistics on both
	public static FileReader fileReader1 = null;
	public static FileReader fileReader2 = null;
	public static BufferedReader bufferedReader1 = null;
	public static BufferedReader bufferedReader2 = null;

	public static String[] sentence1 = null;
	public static String[] sentence2 = null;

	public static double engUSWordCount = 155000000000.0; // 155 billion
	public static double theWordCount = 16977752813.0; // 16.9 billion

	public static double mult = 9.17;

	public static void main(String[] args) {

		if (args.length > 2) {
			System.out.println("Too many arguments!");
		}
		// fileName1 = args[0];
		// fileName2 = args[1];
		fileName1 = "c:/users/mert/desktop/paragraph1.txt";
		fileName2 = "c:/users/mert/desktop/paragraph2.txt";
		splitSentence(fileReader1, fileName1, bufferedReader1, sentence1);
		splitWords(sentence1);
		System.out.println(
				"First file has " + getUniqueWordCount(sentence1) + " unique words. On average, this writer had "
						+ getAverageWordCountPerSentence(sentence1) + " words per sentence.");
		System.out.println("Unique word complexity of file 1: " + getAverageWordComplexity(sentence1));
		totalWords.clear();

		splitSentence(fileReader2, fileName2, bufferedReader2, sentence2);
		splitWords(sentence2);
		System.out.println(
				"Second file has " + getUniqueWordCount(sentence2) + " unique words. On average, this writer had "
						+ getAverageWordCountPerSentence(sentence2) + " words per sentence.");
		System.out.println("Unique word complexity of file 2: " + getAverageWordComplexity(sentence2));
		totalWords.clear();
	}

	public static String getHTML(String urlToRead, String query) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead + "?" + "corpus=eng-us" + "&query=" + query + "&topk=1");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	public static double searchWord(String word) {
		try {
			String toParse = getHTML("http://phrasefinder.io/search", word);
			int mcInt = toParse.indexOf("mc");
			String mc = toParse.substring(mcInt + 4, mcInt + 16); //this gets the numbers after the match count variable in the JSON file. gets 12 chars (999 billion)
			String[] getMc = mc.split(",");
			return Double.parseDouble(getMc[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static double getWordComplexity(double x) { // returns a value
														// between 0 and 1. 1 =
														// basic, 0 = complex
		return x / theWordCount;
	}

	public static void splitSentence(FileReader fReader, String fileName, BufferedReader bReader, String[] paragraph) {

		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);

			/*
			 * Sentence division
			 */

			String line = null;
			while ((line = bReader.readLine()) != null) {
				paragraph = line.split("\\.");
				sentence1 = paragraph;
				sentence2 = paragraph;
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void splitWords(String[] paragraph) {

		for (int i = 0; i < paragraph.length; i++) {
			if (i == 0) {
				words = paragraph[i].split("\\s+");
			} else {
				words = paragraph[i].substring(1, paragraph[i].length()).split("\\s+");
			}
			for (int j = 0; j < words.length; j++) {
				wordCountPerSentence++;
				words[j] = cleanTheWord(words[j]); //gets rid of commas clinging to words
				totalWords.add(words[j]);
			}
		}
	}

	public static double getAverageWordComplexity(String[] paragraph) {
		double sum = 0;
		for (int i = 0; i < totalWords.size(); i++) {
			sum += getWordComplexity(searchWord(totalWords.get(i)));
		}
		return sum / totalWords.size();
	}

	/*
	 * Word choice counter, counts the number of unique words in a paragraph.
	 * This will determine a the writer's size of their vocabulary set.
	 */

	public static int getUniqueWordCount(String[] paragraph) {
		List<String> uniqueWords = new ArrayList<String>();
		uniqueWords.add(" ");
		boolean isAdd = false;

		for (int i = 0; i < totalWords.size(); i++) {
			for (int j = 0; j < uniqueWords.size(); j++) {
				if (totalWords.get(i).equalsIgnoreCase(uniqueWords.get(j))) {
					isAdd = false;
					break;
				} else {
					isAdd = true;
				}
			}
			if (isAdd) {
				uniqueWords.add(totalWords.get(i));
			}
		}
		return uniqueWords.size();
	}

	public static double getAverageWordCountPerSentence(String[] paragraph) {
		return wordCountPerSentence / (double) paragraph.length;
	}
	
	public static String cleanTheWord(String word){
		StringBuilder sb = new StringBuilder(word);
		for (int i = 0; i < word.length(); i++){
			if (sb.charAt(i) == ','){
				sb.deleteCharAt(i);
			}
		}
		return sb.toString();
	}
}
