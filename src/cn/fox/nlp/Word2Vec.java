package cn.fox.nlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.fox.math.Matrix;
import edu.stanford.nlp.io.IOUtils;
import gnu.trove.TObjectIntHashMap;

public class Word2Vec {
	public HashMap<String, WordVector> wordSet;
	public int dimension;
	
	public Word2Vec(String path) {
		loadWord2VecOutput(path);
	}
	
	public void loadWord2VecOutput(String path) {
		
		try {
			wordSet = new HashMap<>();
			BufferedReader positive = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String thisLine = null;
			int count = 1;
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
	
	public static void loadEmbedding(String embedFile, double[][] E, double initRange, List<String> knownWords)
			throws Exception {
		Random random = new Random(System.currentTimeMillis());
		TObjectIntHashMap<String> embedID;
		double[][] embeddings;
		
		if(embedFile!=null && !embedFile.isEmpty()) { // try to load off-the-shelf embeddings
		    embedID = new TObjectIntHashMap<String>();
		    BufferedReader input = null;
		    
			  input = IOUtils.readerFromString(embedFile);
			  List<String> lines = new ArrayList<String>();
			  for (String s; (s = input.readLine()) != null; ) {
			    lines.add(s);
			  }
			
			  
			  String[] splits = lines.get(0).split("\\s+");
			  
			  int nWords = Integer.parseInt(splits[0]);
			  int dim = Integer.parseInt(splits[1]);
			  lines.remove(0);
			  embeddings = new double[nWords][dim];
			
			  
			  for (int i = 0; i < lines.size(); ++i) {
			    splits = lines.get(i).split("\\s+");
			    embedID.put(splits[0], i);
			    for (int j = 0; j < dim; ++j)
			      embeddings[i][j] = Double.parseDouble(splits[j + 1]);
			  }
			  
			  // using loaded embeddings to initial E
			  
			  for (int i = 0; i < E.length; ++i) {
			    int index = -1;
			    if (i < knownWords.size()) {
			      String str = knownWords.get(i);
			      //NOTE: exact match first, and then try lower case..
			      if (embedID.containsKey(str)) index = embedID.get(str);
			      else if (embedID.containsKey(str.toLowerCase())) index = embedID.get(str.toLowerCase());
			    }
			
			    if (index >= 0) {
			      for (int j = 0; j < E[0].length; ++j)
			        E[i][j] = embeddings[index][j];
			    } else {
			      for (int j = 0; j < E[0].length; ++j)
			        E[i][j] = random.nextDouble() * initRange * 2 - initRange;
			    }
			  }
			
		} else { // initialize E randomly
			System.out.println("No Embedding File, so initialize E randomly!");
			for(int i=0;i<E.length;i++) {
				for(int j=0;j<E[0].length;j++) {
					E[i][j] = random.nextDouble() * initRange * 2 - initRange;
				}
			}
		}
	}
}
