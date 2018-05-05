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
			
			
			fileName = "c:/users/mert/desktop/essay.txt"; 
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
			
			for (int i = 0; i < combinations.size(); i++){
				System.out.println(combinations.get(i).getKey() + " " +combinations.get(i).getValue());
			}
			
			
			for (int i = 0; i < paragraphs.size(); i++){
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
			}
			
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




