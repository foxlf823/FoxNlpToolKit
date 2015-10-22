package cn.fox.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;



public class StopWord {
	// not belong to any part of an entity
	public HashSet<String> stopWord1 = new HashSet<String>();
	// not belong to any part of an entity no matter lowercase or uppercase
	public HashSet<String> stopWord2 = new HashSet<String>();
	// not an entity as a whole part no matter lowercase or uppercase
	public HashSet<String> stopWord3 = new HashSet<String>();
	
	public StopWord(String filePath) {
		try {
			int flag = 1;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
			String thisLine = null;
			while ((thisLine = br.readLine()) != null ) {
				if(thisLine.isEmpty()) {
					flag++;
					continue;
				}
				
				if(thisLine.indexOf("##")!=-1)
					continue;
				
				if(flag==1)
					stopWord1.add(thisLine);
				else if(flag==2)
					stopWord2.add(thisLine);
				else if(flag==3)
					stopWord3.add(thisLine);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public boolean contains(String word) {
		
		String[] tokens = word.split(" ++");
		for(String token:tokens) {
			if(stopWord1.contains(token))
				return true;
		}

		String lower = word.toLowerCase();
		String[] lowerTokens = lower.split(" ++");
		for(String lowerToken:lowerTokens) {
			if(stopWord2.contains(lowerToken))
				return true;
		}

		if(stopWord3.contains(lower))
			return true;
		
		return false;
		
	}
}
