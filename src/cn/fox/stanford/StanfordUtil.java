package cn.fox.stanford;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.stats.IntCounter;

public class StanfordUtil {
	public static List<String> generateDict(List<String> str, int cutOff)
	  {
	    Counter<String> freq = new IntCounter<>();
	    for (String aStr : str)
	      freq.incrementCount(aStr);

	    List<String> keys = Counters.toSortedList(freq, false);
	    List<String> dict = new ArrayList<>();
	    for (String word : keys) {
	      if (freq.getCount(word) >= cutOff)
	        dict.add(word);
	    }
	    return dict;
	  }
}
