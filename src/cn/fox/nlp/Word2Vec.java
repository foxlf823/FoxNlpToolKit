package cn.fox.nlp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.fox.math.Matrix;
import edu.stanford.nlp.io.IOUtils;
import gnu.trove.TIntArrayList;
import gnu.trove.TObjectIntHashMap;

public class Word2Vec {
	private static final int MAX_SIZE = 50;
	public HashMap<String, WordVector> wordSet;
	public int dimension;
	public HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
	//public HashMap<String, float[]> secondWordMap = new HashMap<String, float[]>();
	
	public Word2Vec() {
		
	}
	
	public Word2Vec(String path) {
		loadWord2VecOutput(path);
	}
	
	public void loadModel(String path, boolean bNorm) throws Exception {
		DataInputStream dis = null;
		BufferedInputStream bis = null;
		float vector = 0;
		int words = 0;
		int size = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			dis = new DataInputStream(bis);
			// //读取词数
			words = Integer.parseInt(readString(dis));
			// //大小
			size = Integer.parseInt(readString(dis));
			System.out.println("words: "+words+", size: "+size);

			String word;
			double[] vecs = null;
			for (int i = 0; i < words; i++) {
				word = readString(dis);
				
				vecs = new double[size];
				double norm = 0;
				for (int j = 0; j < size; j++) {
					vector = readFloat(dis);
					norm += vector*vector;
					vecs[j] = vector;
				}
				
				
				if(bNorm) {
					norm = Math.sqrt(norm);
					for (int j = 0; j < size; j++) {
						vecs[j] /= norm;
					}
				}
				
				
				
				wordMap.put(normalize_to_lowerwithdigit(word), vecs);
				dis.read();
				
			}

		} finally {
			bis.close();
			dis.close();
		}
	}
	
	
	public void loadEmbeddingFromMemory(double[][] E, double initRange, List<String> knownWords) {
		//Random random = new Random(System.currentTimeMillis());
		TIntArrayList uninitialIds = new TIntArrayList();
		

		for (int i = 0; i < knownWords.size(); ++i) {
		    
		      String str = knownWords.get(i);
		      if (wordMap.containsKey(str))  {
		    	  for(int j=0;j<E[0].length;j++) {
		    		  E[i][j] = wordMap.get(str)[j];
		    	  }
		    	  
		      } else {
		    	  uninitialIds.add(i);
		      }
		      /*else if (wordMap.containsKey(str.toLowerCase())) {
		    	  for(int j=0;j<E[0].length;j++) {
		    		  E[i][j] = wordMap.get(str.toLowerCase())[j];
		    	  }
		      }else if (secondWordMap.containsKey(str))  {
		    	  for(int j=0;j<E[0].length;j++) {
		    		  E[i][j] = secondWordMap.get(str)[j];
		    	  }
		    	  
		      }
		      else if (secondWordMap.containsKey(str.toLowerCase())) {
		    	  for(int j=0;j<E[0].length;j++) {
		    		  E[i][j] = secondWordMap.get(str.toLowerCase())[j];
		    	  }
		      } 		      
		      else {
		    	  for (int j = 0; j < E[0].length; ++j)
				    E[i][j] = random.nextDouble() * initRange * 2 - initRange;
		      }*/
 
		}

	}
	
	public static String normalize_to_lowerwithdigit(String s)
	{
		String lowcase = "";
	  char [] chars = s.toCharArray();
	  for (int i = 0; i < chars.length; i++) {
	    if (Character.isDigit(chars[i])) {
	      lowcase = lowcase + "0";
	    } else if (Character.isLetter(chars[i])) {
	      if (Character.isLowerCase(chars[i]))
	      {
	        lowcase = lowcase + chars[i];
	      }
	      else
	      {
	        lowcase = lowcase + Character.toLowerCase(chars[i]) ;
	      }
	    }
	    else
	    {
	      lowcase = lowcase + chars[i];
	    }
	  }
	  return lowcase;
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
	
	private static String readString(DataInputStream dis) throws IOException {
		byte[] bytes = new byte[MAX_SIZE];
		byte b = dis.readByte();
		int i = -1;
		StringBuilder sb = new StringBuilder();
		while (b != 32 && b != 10) {
			i++;
			bytes[i] = b;
			b = dis.readByte();
			if (i == 49) {
				sb.append(new String(bytes));
				i = -1;
				bytes = new byte[MAX_SIZE];
			}
		}
		sb.append(new String(bytes, 0, i + 1));
		return sb.toString();
	}
	
	public static float getFloat(byte[] b) {
		int accum = 0;
		accum = accum | (b[0] & 0xff) << 0;
		accum = accum | (b[1] & 0xff) << 8;
		accum = accum | (b[2] & 0xff) << 16;
		accum = accum | (b[3] & 0xff) << 24;
		return Float.intBitsToFloat(accum);
	}
	
	public static float readFloat(InputStream is) throws IOException {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return getFloat(bytes);
	}
	
	
	
	/*public void loadSecondModel(String path) throws Exception {
		DataInputStream dis = null;
		BufferedInputStream bis = null;
		float vector = 0;
		int words = 0;
		int size = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			dis = new DataInputStream(bis);
			// //读取词数
			words = Integer.parseInt(readString(dis));
			// //大小
			size = Integer.parseInt(readString(dis));
			System.out.println("words: "+words+", size: "+size);
			
			String word;
			float[] vecs = null;
			int count = 0;
			for (int i = 0; i < words; i++) {
				word = readString(dis);
				
				vecs = new float[size];
				
				for (int j = 0; j < size; j++) {
					vector = readFloat(dis);
					vecs[j] = vector;
				}
				
				
				
				secondWordMap.put(word, vecs);
				dis.read();
				
			}

		} finally {
			bis.close();
			dis.close();
		}
	}*/
	
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
}
