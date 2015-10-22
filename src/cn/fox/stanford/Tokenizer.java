package cn.fox.stanford;

import java.util.ArrayList;
import java.util.HashSet;

import cn.fox.nlp.Punctuation;
import edu.stanford.nlp.ling.CoreLabel;

public class Tokenizer {
	boolean usePunctuation; // whether use punctuations as splitter
	HashSet<Character> splitters = new HashSet<>();
	
	public Tokenizer(boolean usePunctuation, Character...splitters) {
		this.usePunctuation = usePunctuation;
		for(Character ch: splitters) {
			this.splitters.add(ch);
		}
	}
	
	/*
	 *  Tokenize "s" with whitespace and punctuation.
	 *  "offset" is the start position of this string.
	 */
	public ArrayList<CoreLabel> tokenize(int offset, String s) {
		ArrayList<CoreLabel> ret = new ArrayList<>();
		char[] chs = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<chs.length; ) {
			Character ch = chs[i];
			
			if(splitters.contains(ch)) {
				if(sb.length()!=0) {
					CoreLabel token = new CoreLabel();
					token.setWord(new String(sb));
					token.setValue(new String(sb));
					token.setBeginPosition(offset-sb.length());
					token.setEndPosition(offset);
					ret.add(token);
					sb.delete(0, sb.length());
				}
			} else {
				if(usePunctuation && Punctuation.isEnglishPunc(ch)) {
					if(sb.length()!=0) {
						CoreLabel token = new CoreLabel();
						token.setWord(new String(sb));
						token.setValue(new String(sb));
						token.setBeginPosition(offset-sb.length());
						token.setEndPosition(offset);
						ret.add(token);
						sb.delete(0, sb.length());
					}
					CoreLabel token = new CoreLabel();
					token.setWord(ch.toString());
					token.setValue(ch.toString());
					token.setBeginPosition(offset);
					token.setEndPosition(offset+1);
					ret.add(token);
				} else {
					sb.append(ch);
				}
			}
			offset++;
			i++;

		}
		
		if(sb.length()!=0) {
			CoreLabel token = new CoreLabel();
			token.setWord(new String(sb));
			token.setValue(new String(sb));
			token.setBeginPosition(offset-sb.length());
			token.setEndPosition(offset);
			ret.add(token);
			sb.delete(0, sb.length());
		}
		
		return ret;
	}
}
