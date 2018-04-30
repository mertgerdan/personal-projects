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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;

@SuppressWarnings("unused")
public class Detector {

	
	public static List<String> paragraphs = new ArrayList<String>();
	public static List<String[]> sentences = new ArrayList<String[]>();
	public static String[] words = null;
	

	public static List<String> totalWords = new ArrayList<String>();
	public static int wordCountPerSentence = 0;
	
	
	public static String fileName = ""; // insert a fileName here
	public static FileReader fileReader = null;
	public static BufferedReader bufferedReader = null;


	public static double engUSWordCount = 155000000000.0; // 155 billion
	public static double theWordCount = 16977752813.0; // 16.9 billion

	public static double mult = 9.17; //155 divided by 16.9
	

	public static List<Pair<Integer,Integer>> combinations = new ArrayList<Pair<Integer,Integer>>(); 
	
	public static void main(String[] args) {
		

		if (args.length > 1) {
			System.out.println("Too many arguments!");
		}
		// fileName1 = args[0]; normally this is how we use it, but I wanted to test it out with a .txt file of my own.
		
		
		fileName = "c:/users/mert/desktop/essay.txt"; 
		splitParagraph(fileReader, fileName, bufferedReader);
		System.out.println(paragraphs.size() + " paragraphs detected.");
		System.out.println(getCombination(paragraphs.size(),2) + " possible paragraph combinations found.");
		
		/*
		 * Figuring out possible paragraph combinations
		 * and removing if there is the same combination with their pairs reversed
		 */
		
		int counter = 0;
		for (int i = 0; i < paragraphs.size(); i++){
			for (int j = 0; j < paragraphs.size(); j++){
				if (i == j){
					continue;
				}
				else{
					Pair<Integer, Integer> pair = new Pair<Integer, Integer>(i,j);
					combinations.add(pair);
					counter++;              
				}
			}
		}
		
		for (int i = 0; i < combinations.size(); i++){
			int firstInt = combinations.get(i).getKey();
			int secondInt = combinations.get(i).getValue();
			for (int j = 0; j < combinations.size(); j++){
				if (firstInt == combinations.get(j).getValue() && secondInt == combinations.get(j).getKey()){
					combinations.remove(j);
				}
			}
		}
		
		for (int i = 0; i < combinations.size(); i++){
			System.out.println(combinations.get(i).getKey() + " " +combinations.get(i).getValue());
		}
		
		
		for (int i = 0; i < paragraphs.size(); i++){
			if (counter > getCombination(paragraphs.size(),2)){
				break;
			}
		}
		for (int i = 0; i < paragraphs.size(); i++){
			splitSentence(paragraphs, i);
			splitWords(sentences.get(i));
			
			System.out.println(
					"Paragraph number: " + (i+1) + " has " + getUniqueWordCount(sentences.get(i)) + " unique words. On average, this paragraph had "
							+ getAverageWordCountPerSentence(sentences.get(i)) + " words per sentence.");
			System.out.println("Unique word complexity of paragraph number: " + (i+1) + " is " + getAverageWordComplexity(sentences.get(i)));
			totalWords.clear();
			wordCountPerSentence = 0;
		}
		
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

	public static void splitParagraph(FileReader fReader, String fileName, BufferedReader bReader){
		
		String[] paragraph;
		try {
			fReader = new FileReader(fileName);
			bReader = new BufferedReader(fReader);

			/*
			 * Sentence division
			 */

			String line = null;
			while ((line = bReader.readLine()) != null) {			
				paragraphs.add(line);
			}
			
	}	catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void splitSentence(List<String> paragraph, int i) {
		String[] sentences;
		String[] toStringArray = paragraph.toArray(new String[0]);
		sentences = toStringArray[i].split("\\.");
		Detector.sentences.add(sentences);
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
		
		return uniqueWords.size()-1;
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
	
	
	public static int factorial(int x){
	    int result;

	   if(x==1)
	     return 1;

	   result = factorial(x-1) * x;
	   return result;
}

public static int getCombination(int n, int r){
	return factorial(n) / (factorial(r) * factorial(n-r)); //get possible combinations of an essay
}
}


