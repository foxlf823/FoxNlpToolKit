package cn.fox.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Shuttle the objects and split them by the proportions that you give.
 */
public class ObjectShuffle {
	public static void shuffle (List<? extends Object> collect, java.util.Random r) {
		Collections.shuffle (collect, r);
	}
	
	public static List<? extends Object>[] split(List<? extends Object> collect, double[] proportions) {
		List shuffled = new ArrayList<String>(Arrays.asList(new String[collect.size()]));  
		Collections.copy(shuffled, collect);  
		Collections.shuffle (shuffled, new java.util.Random(System.currentTimeMillis()));
		return splitInOrder(shuffled, proportions);
	}
	
	/*
	 *  split data based on proportions, if the sum of proportions[] is not 1, we put all the remaining data into
	 *  the last part
	 */
	public static List<? extends Object>[] splitInOrder (List<? extends Object> collect, double[] proportions) {
		/*if(proportions.length>2) 
			return null; */
		List[] ret = new ArrayList[proportions.length];
		int partBegin = 0;
		for(int k=0;k<proportions.length-1;k++) {
			ret[k] = new ArrayList<>();
			int elementsInPart = (int)(collect.size()*proportions[k]);
			for(int i=partBegin;i<partBegin+elementsInPart;i++) {
				ret[k].add(collect.get(i));
			}
			partBegin += elementsInPart;
		}
		// put the remaining into the last part
		ret[proportions.length-1] = new ArrayList<>();
		for(int i=partBegin;i<collect.size();i++) {
			ret[proportions.length-1].add(collect.get(i));
		}
		
		/*int firstPart = (int)(collect.size()*proportions[0]);
		
		ret[0] = new ArrayList<>();
		for(int i=0;i<firstPart;i++) {
			ret[0].add(collect.get(i));
		}
		ret[1] = new ArrayList<>();
		for(int i=firstPart;i<collect.size();i++) {
			ret[1].add(collect.get(i));
		}*/

		return ret;
	}
}
