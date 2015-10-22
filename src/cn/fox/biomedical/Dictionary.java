package cn.fox.biomedical;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Dictionary {
	public HashSet<String> list;
	int maxNumOfWordPerEntry; // The entry whose numbers of words > this will be ignored

	// the entries depend on how you make the dictionary file
	public Dictionary(String path, int maxNumOfWordPerEntry) {
		this.maxNumOfWordPerEntry = maxNumOfWordPerEntry;
		list = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String thisLine = null;
			
			while ((thisLine = br.readLine()) != null) {
				if(thisLine.split(" ++").length<=maxNumOfWordPerEntry)
					list.add(thisLine);
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Whether the dictionary contains the "word" with case ignoring
	 */
	public boolean contains(String word) {
		return list.contains(word.trim().toLowerCase());
		
	}
	
	/*
	 * Whether the dictionary contains the "word" exactly.
	 */
	public boolean containsCaseSensitive(String word) {
		return list.contains(word.trim());
		
	}
}
