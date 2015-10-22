package cn.fox.nlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


import java.util.HashMap;

import cn.fox.math.Matrix;

public class Word2Vec {
	public HashMap<String, WordVector> wordSet;
	
	public Word2Vec() {
		
	}
	
	public void loadWord2VecOutput(String path) {
		
		try {
			wordSet = new HashMap<>();
			BufferedReader positive = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String thisLine = null;
			int count = 1;
			int dimension = -1;
			while ((thisLine = positive.readLine()) != null ) {
				if(thisLine.isEmpty())
					continue;
				if(count==1) {
					dimension = Integer.parseInt(thisLine.substring(thisLine.lastIndexOf(" ")+1));
					count++;
					continue;
				}
				
				String word = null;
				Matrix vector = new Matrix(1, dimension);
				String[] tokens = thisLine.split(" ");
				for(int i=0;i<tokens.length;i++) {
					if(i==0)
						word = tokens[i];
					else {
						vector.setElement(0, i-1, Double.parseDouble(tokens[i]));
					}
				}
				WordVector wordVector = new WordVector(word, vector);
				wordSet.put(word, wordVector);
				
				count++;
			}
			
			positive.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WordVector get(String s) {
		return this.wordSet.get(s);
	}
}
