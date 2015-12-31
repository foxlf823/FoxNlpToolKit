package cn.fox.nlp;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.stanford.nlp.io.IOUtils;
import gnu.trove.TObjectIntHashMap;

public class Senna {
	public static void loadEmbedding(String embedFile, String wordListFile, double[][] E, double initRange, 
			List<String> knownWords) throws Exception {
		Random random = new Random(System.currentTimeMillis());
		
		if(embedFile!=null && !embedFile.isEmpty()) {
			TObjectIntHashMap<String> embedID = new TObjectIntHashMap<String>();
			
			BufferedReader brEmb = IOUtils.readerFromString(embedFile);
			List<String> linesEmb = new ArrayList<String>();
			for (String s; (s = brEmb.readLine()) != null; ) {
				linesEmb.add(s);
			}
			BufferedReader brWord = IOUtils.readerFromString(wordListFile);
			List<String> linesWord = new ArrayList<String>();
			for (String s; (s = brWord.readLine()) != null; ) {
				linesWord.add(s);
			}
			
			String[] splits = linesEmb.get(0).split("\\s+");
			int dim = splits.length;
			int nWords = linesEmb.size();
			double[][] embeddings = new double[nWords][dim];
			
			for (int i = 0; i < linesEmb.size(); ++i) {
			    splits = linesEmb.get(i).split("\\s+");
			    embedID.put(linesWord.get(i), i);
			    for (int j = 0; j < dim; ++j)
			      embeddings[i][j] = Double.parseDouble(splits[j]);
			}
			
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
