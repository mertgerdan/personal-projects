package hisarai.plagiarism.detector;
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
public class Detector extends Utilities{


		
		public static List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		
		public static String fileName = ""; // insert a fileName here
		public static FileReader fileReader = null;
		public static BufferedReader bufferedReader = null;


		public static List<Pair<Integer,Integer>> combinations = new ArrayList<Pair<Integer,Integer>>(); 
		
		public static void main(String[] args) {
			
			System.out.println("This file's ID is set as: " + generateNewID());

			if (args.length > 1) {
				System.out.println("Too many arguments!");
			}
			// fileName = args[0];
			
			
			fileName = "/Users/macbook/Desktop/paragraph1.txt"; 
			splitParagraph(fileReader, fileName, bufferedReader);
			System.out.println(paragraphs.size() + " paragraphs detected.");
			System.out.println(getCombination(paragraphs.size(),2) + " possible paragraph combinations found.");
			
			/*
			 * Figuring out possible paragraph combinations
			 * and removing them if there is the same combination with their pairs reversed 1-0 / 0-1 kind of thing.
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
			
			/*for (int i = 0; i < combinations.size(); i++){ this prints the possible combinations onto the screen
				System.out.println(combinations.get(i).getKey() + " " +combinations.get(i).getValue());
			}*/
			
			for (int i = 0; i < getCombination(paragraphs.size(), 2); i++) {
				Paragraph p1 = paragraphs.get(combinations.get(i).getKey());
				p1.splitSentence();
				p1.splitWords();
				p1.setUniqueWordCount();
				p1.setAverageWordCountPerSentence();
				p1.setUWCPX();
				
				Paragraph p2 = paragraphs.get(combinations.get(i).getValue());
				p2.splitSentence();
				p2.splitWords();
				p2.setUniqueWordCount();
				p2.setAverageWordCountPerSentence();
				p2.setUWCPX();
				
				if (p1.words.length < 50 || p2.words.length < 50) {
					System.out.println("The program needs more than 50 words to have a reasonable comparison.");
					break;
				}
				else {
				System.out.println("Difference in Unique Word Count: " + Math.abs(p1.getUWC() - p2.getUWC()));
				System.out.println("Difference in Average Words Per Sentence: " + Math.abs(p1.getWPS() - p2.getWPS()));
				System.out.println("Difference in Unique Word Complexity: " + Math.abs(p1.getUWCPX() - p2.getUWCPX()));
				
				System.out.println("");
				}
			}
			
			
			/*for (int i = 0; i < paragraphs.size(); i++){
				Paragraph p = paragraphs.get(i);
				p.splitSentence();
				p.splitWords();
				p.setUniqueWordCount();
				p.setAverageWordCountPerSentence();
				p.setUWCPX();
				
				System.out.println(
						"Paragraph number: " + (i+1) + " has " + p.getUWC() + " unique words. On average, this paragraph had "
								+ p.getWPS() + " words per sentence.");
				System.out.println("Unique word complexity of paragraph number: " + (i+1) + " is " + p.getUWCPX());
			}*/
			
		}


		public static void splitParagraph(FileReader fReader, String fileName, BufferedReader bReader){

			try {
				fReader = new FileReader(fileName);
				bReader = new BufferedReader(fReader);

				/*
				 * Sentence division
				 */

				String line = null;
				int counter = 0;
				while ((line = bReader.readLine()) != null) {
					Paragraph paragraph = new Paragraph(counter, line);
					paragraphs.add(paragraph);
				}
				
		}	catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		



		
		
		
		

	}
