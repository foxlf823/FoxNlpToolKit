package cn.fox.nlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
 * We use the thesaurus of Harbin Institute of Technology.
 * The format is like "Ba01A02= 物质质 素"
 */
public class ChineseThesaurus {
	private ArrayList<ThesaurusCluster> clusters;
	
	
	public ChineseThesaurus(String thesaurusPath) {
		clusters = new ArrayList<ThesaurusCluster>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(thesaurusPath), "utf-8"));
			String thisLine = null;
			
			while ((thisLine = br.readLine()) != null) {
				if(thisLine.trim().isEmpty()) continue;
				
				ThesaurusCluster c = new ThesaurusCluster();
				int pos1 = thisLine.indexOf(" ");
				c.cluster = thisLine.substring(0, pos1-1);
				c.type = thisLine.substring(pos1-1, pos1);
				c.dict = new HashMap<String, String>() ;
				String[] tokens = thisLine.substring(pos1+1).split(" ");
				for(String token : tokens)
					c.dict.put(token,token);
				
				
				clusters.add(c);
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Give two words "a" and "b", judge the distance based on the clusters.
	 * The distance is 0~6 and if -1, means a or b is not in the Thesaurus.
	 * If a word has more than one meanings, we take the smallest distance.
	 */
	public double getDistance(String a, String b) {
		if(a.equals(b)) // equal or @
			return 0;
		ArrayList<ThesaurusCluster> tClustersA = getThesaurusCluster(a);
		ArrayList<ThesaurusCluster> tClustersB = getThesaurusCluster(b);
		if(tClustersA.isEmpty() || tClustersB.isEmpty())
			return 10;
		double min = Double.MAX_VALUE;
		double distance;
		for(ThesaurusCluster tClusterA:tClustersA) {
			String clusterA = tClusterA.cluster;
			for(ThesaurusCluster tClusterB:tClustersB) {
				String clusterB = tClusterB.cluster;
				if(clusterA.substring(0,1).equals(clusterB.substring(0,1))) {
					if(clusterA.substring(1,2).equals(clusterB.substring(1,2))) {
						if(clusterA.substring(2,4).equals(clusterB.substring(2,4))) {
							if(clusterA.substring(4,5).equals(clusterB.substring(4,5))) {
								if(clusterA.substring(5,7).equals(clusterB.substring(5,7))) {
									if(tClusterA.type.equals("=")) {
										distance  = 0;
									} else { // #
										distance = 1;
									}
								} else {
									distance = 2;
								}
							} else {
								distance = 3;
							}
						} else {
							distance = 4;
						}
					} else {
						distance = 5;
					}
				} else {
					distance = 6;
				}
				
				if(distance < min)
					min = distance;
			}
		}
		
		return min;
	}
	
	/*
	 * Given a word, and get the cluster like "Ba01A02".
	 * A word may have several clusters.
	 */
	public ArrayList<String> getStringCluster(String a) {
		ArrayList<String> list = new ArrayList<String>();
		for(ThesaurusCluster cluster:clusters) {
			if(cluster.dict.containsValue(a))
				list.add(cluster.cluster);
		}
		return list;
	}
	
	/*
	 * Given a word, and get a entry number. When two words have the same cluster sets, their entry numbers are the same.
	 * You can use this entry number as the feature value of a semantic feature.
	 * For a single meaning word, this semantic feature is useful because if a word whose meaning is 
	 * identical or similar to it, their feature values will be identical or similar.
	 * For a multi-meaining word, the use of this semantic feature is unknown.
	 * The return value is a big positive integer, so you can choose to normalize it to 0~1. 
	 * If this word isn't in thesaurus, return 0.
	 */
	public double getEntryNumber(String a) {
		ArrayList<String> clusters = getStringCluster(a);
		if(clusters.isEmpty())
			return 0;
		
		long entryNumber = 0;
		for(String cluster:clusters) {
			entryNumber += transferClusterToNumber(cluster);
		}
		
		return entryNumber;
	}
	
	private ArrayList<ThesaurusCluster> getThesaurusCluster(String a) {
		ArrayList<ThesaurusCluster> list = new ArrayList<ThesaurusCluster>();
		for(ThesaurusCluster cluster:clusters) {
			if(cluster.dict.containsValue(a))
				list.add(cluster);
		}
		return list;
	}
	
	// The extent is between 101010101 and 1201060403.
	private long transferClusterToNumber(String cluster) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<cluster.length();i++) {
			char c = cluster.charAt(i);
			if((c>=0x0041 && c<=0x005A) || (c>=0x0061 && c<=0x007A)) {
				if(c>=0x0061 && c<=0x007A)
					c = (char) (c-0x0020);
				switch(c) {
				case 'A':
					sb.append("01");
					break;
				case 'B':
					sb.append("02");
					break;
				case 'C':
					sb.append("03");
					break;
				case 'D':
					sb.append("04");
					break;
				case 'E':
					sb.append("05");
					break;
				case 'F':
					sb.append("06");
					break;
				case 'G':
					sb.append("07");
					break;
				case 'H':
					sb.append("08");
					break;
				case 'I':
					sb.append("09");
					break;
				case 'J':
					sb.append("10");
					break;
				case 'K':
					sb.append("11");
					break;
				case 'L':
					sb.append("12");
					break;
				case 'M':
					sb.append("13");
					break;
				case 'N':
					sb.append("14");
					break;
				case 'O':
					sb.append("15");
					break;
				case 'P':
					sb.append("16");
					break;
				case 'Q':
					sb.append("17");
					break;
				case 'R':
					sb.append("18");
					break;
				}
			} else {
				sb.append(c);
			}
		}
		String ret = new String(sb);
		return Long.parseLong(ret);
	}
}

class ThesaurusCluster {
	String cluster; // Ba01A02
	String type; // =
	HashMap<String, String> dict; // 物质质 素
}