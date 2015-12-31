package cn.fox.nlp;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.stanford.nlp.io.IOUtils;
import gnu.trove.TObjectIntHashMap;

public class Glove {
	public static void useAllWord(String embedFile, List<String> knownWords) throws Exception {
		BufferedReader input = IOUtils.readerFromString(embedFile);
		
		for (String s; (s = input.readLine()) != null; ) {
			String[] splits = s.split("\\s+");
			knownWords.add(splits[0].toLowerCase());
		}
	}
	
	public static void loadEmbedding(String embedFile, double[][] E, double initRange, 
			List<String> knownWords) throws Exception {
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
			  
			  int nWords = lines.size();
			  int dim = splits.length-1;
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
