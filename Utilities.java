package hisarai.plagiarism.detector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utilities {
	
	static List<Integer> idList = new ArrayList<Integer>();
	
	static double theWordCount = 16977752813.0;
	
	public static String[] toStrArray(List<String> str){
		String[] result = new String[str.size()];
		for (int i = 0; i < str.size(); i++){
			result[i] = str.get(i);
		}	
		return result;
	}

	public static int factorial(int x) {
		int result;

		if (x == 1)
			return 1;

		result = factorial(x - 1) * x;
		return result;
	}

	public static int getCombination(int n, int r) {
		return factorial(n) / (factorial(r) * factorial(n - r)); // get possible combinations
	}
	
	
 	public static String cleanTheWord(String word){
        String cleanWord = word;
  		for (int i = 0; i < word.length(); i++){
  			if (word.charAt(i) == ',' || word.charAt(i) == '\''){
                cleanWord = word.substring(0,i);
  			}
  		}
  		return cleanWord;
  	}
	
    private static int binarySearch(int arr[], int l, int r, int x){
        if (r>=l){
            int mid = l + (r - l)/2;
            if (arr[mid] == x){
               return mid;
            }
            if (arr[mid] > x){
               return binarySearch(arr, l, mid-1, x);
            }
            return binarySearch(arr, mid+1, r, x);
        }
 
        return -1;
    }
	
	public static int generateNewID(){ //generates a six digit unique ID
		int[] idListArray = idList.stream().mapToInt(i -> i).toArray();
		Random rand = new Random();
		StringBuilder ID = new StringBuilder();
		for (int i = 0; i < 6; i++){
			ID.append(rand.nextInt(10));
		}
		int realID = Integer.parseInt(ID.toString());
		Collections.sort(idList);
		if (binarySearch(idListArray, 0, idList.size()-1, realID) == -1){
			idList.add(realID);
			return realID;
		}
		else{
			return generateNewID();
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

}
