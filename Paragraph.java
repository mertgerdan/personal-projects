package hisarai.plagiarism.detector;

import java.util.ArrayList;
import java.util.List;

public class Paragraph extends Utilities {
	
	private String paragraph;
	private int UID; //unique ID
	private double WPS; //words per sentence
	private int uniqueWordCount;
	private double UWCPX; //unique word complexity
	
	private int wordCountPerSentence = 0;
	
	public List<String> sentences = new ArrayList<String>();
	public String[] words = null;
	public List<String> totalWords = new ArrayList<String>();
	
	public Paragraph(int UID, String paragraph){
		this.UID = UID;
		this.paragraph = paragraph;
	}
	
	
	public void splitSentence() {
		String[] sentence;
		sentence = paragraph.split("\\.");
		for (int i = 0; i < sentence.length; i++){
			sentences.add(sentence[i]);
		}
	}

	public void splitWords() {
		String s[] = toStrArray(sentences);
		for (int i = 0; i < sentences.size(); i++) {
			if (i == 0) {
				words = s[i].split("\\s+");
			} else {
				words = s[i].substring(1, s[i].length()).split("\\s+");
			}
			for (int j = 0; j < words.length; j++) {
				wordCountPerSentence++;
				words[j] = cleanTheWord(words[j]); //gets rid of commas clinging to words
				totalWords.add(words[j]);
			}
		}
	}
	
	
	public void setUWCPX() {
		double sum = 0;
		for (int i = 0; i < totalWords.size(); i++) {
			sum += getWordComplexity(searchWord(totalWords.get(i)));
		}
		UWCPX = sum / totalWords.size();
	}

	/*
	 * Word choice counter, counts the number of unique words in a paragraph.
	 * This will determine a the writer's size of their vocabulary set.
	 */

	public void setUniqueWordCount() {
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
		
		uniqueWordCount = uniqueWords.size()-1;
	}

	public void setAverageWordCountPerSentence() {
		WPS = wordCountPerSentence / (double) sentences.size();
	}
	
	
	public int getUID(){
		return UID;
	}
	
	
	public double getWPS(){
		return WPS;
	}
	
	
	public int getUWC(){
		return uniqueWordCount;
	}
	
	
	public double getUWCPX(){
		return UWCPX;
	}
	
	
	
}
