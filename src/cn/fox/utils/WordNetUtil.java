package cn.fox.utils;

import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;


public class WordNetUtil {
	public static final int MAX_OFFSET = 99999999;
	// Get the most frequent synset, and it may return null.
	public static ISynset getMostSynset(IDictionary dict, String lemma, POS pos) {
		IIndexWord idxWord= dict.getIndexWord(lemma, pos);
		if(idxWord == null) 
			return null;
		List<IWordID> wordids = idxWord.getWordIDs();
		ISynset synset = null;
		if(!wordids.isEmpty())
			synset = dict.getWord(wordids.get(0)).getSynset();
		
		return synset;
	}
	// Get the all synset, and it may return null or empty set.
	public static List<ISynset> getAllSynset(IDictionary dict, String lemma, POS pos) {
		IIndexWord idxWord= dict.getIndexWord(lemma, pos);
		if(idxWord == null) 
			return null;
		List<ISynset> ret = new ArrayList<ISynset>();
		List<IWordID> wordids = idxWord.getWordIDs();
		for(IWordID wordID:wordids) {
			ISynset synset = dict.getWord(wordID).getSynset();
			ret.add(synset);
		}
		return ret;
	}
	// Get the first hypernym of the most frequent synset, and it may return null
	public static ISynset getMostHypernym(IDictionary dict, String lemma, POS pos) {
		IIndexWord idxWord= dict.getIndexWord(lemma, pos);
		if(idxWord == null) 
			return null;
		List<IWordID> wordids = idxWord.getWordIDs();
		ISynset hypernym = null;
		if(!wordids.isEmpty()) {
			ISynset synset = dict.getWord(wordids.get(0)).getSynset();
			List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
			if(!hypernyms.isEmpty())
				hypernym = dict.getSynset(hypernyms.get(0));
		}
			 
		return hypernym;
	}
	// Get the all hypernym, and it may return null or empty set.
	public static List<ISynset> getAllHypernym(IDictionary dict, String lemma, POS pos) {
		IIndexWord idxWord= dict.getIndexWord(lemma, pos);
		if(idxWord == null) 
			return null;
		List<ISynset> ret = new ArrayList<ISynset>();
		List<IWordID> wordids = idxWord.getWordIDs();
		for(IWordID wordID:wordids) {
			ISynset synset = dict.getWord(wordID).getSynset();
			List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
			for(ISynsetID sid : hypernyms){
				ISynset hypernym = dict.getSynset(sid);
				ret.add(hypernym);
			}
		}
		return ret;
	}
}
