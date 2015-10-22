package cn.fox.machine_learning;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
 * This class uses the results of brown-cluster-master
 */
public class BrownCluster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2216183668100922639L;
	private HashMap<String, String> map; // key is the word, value is the cluster prefix
	public int count;
	/*
	 * path - the file path of the result
	 * prefixLength - the maximum length of the prefix
	 */
	public BrownCluster(String path, int maxPrefixLength) {
		map = new HashMap<>();
		try {
			BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String thisLine = null;
			while ((thisLine = file.readLine()) != null) {
				if(thisLine.isEmpty()) continue;
				int pos1 = thisLine.indexOf("\t");
				int pos2 = thisLine.lastIndexOf("\t");
				// case insensitive
				if(!map.containsValue(thisLine.substring(0, pos1<maxPrefixLength?pos1:maxPrefixLength)))
					count++;
				map.put(thisLine.substring(pos1+1, pos2).toLowerCase(), thisLine.substring(0, pos1<maxPrefixLength?pos1:maxPrefixLength));
				
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// case insensitive
	public String getPrefix(String word) {
		return map.get(word.toLowerCase());
	}
	
}
