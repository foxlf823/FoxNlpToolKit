package cn.fox.nlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import cn.fox.utils.CharCode;

/*
 * Split a document into some sentences.
 */
public class SentenceSplitter {
	/*
	 * \u3002 - chinese full step
	 * \uff01 - fullwidth EXCLAMATION MARK
	 * \uff1f - FULLWIDTH QUESTION MARK
	 * not \uff0e - FULLWIDTH FULL STOP(too often separates English first/last name, etc.)
	 */
	private Set<Character> chineseDelimiters = new HashSet<Character>(Arrays.asList(
			new Character[]{'\u3002', '\uff01', '\uff1f', '!', '?'}));
	
	private Set<Character> englishDelimiters = new HashSet<Character>(Arrays.asList(
			new Character[]{'.', '!', '?'}));
	
	private boolean isChinese;
	
	private ArrayList<String> englishAbbr;
	
	private boolean isEmptyChar(char pp) {
		if(pp==' ' || pp=='\n' || pp=='\r' || pp=='\t')
			return true;
		else
			return false;
	}
	
	private void loadEnglishAbbr(String filePathEnglishAbbr) {
		englishAbbr = new ArrayList<String>();
		try {
			String line = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathEnglishAbbr), "utf-8"));
			while ((line = br.readLine()) != null) {
				if(line.charAt(0) == '#')
					continue;
				englishAbbr.add(line);
			}    
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	// If it's not in the english mode, "filePathEnglishAbbr" can be ignored.
	public SentenceSplitter(boolean isChinese, String filePathEnglishAbbr) {
		this.isChinese = isChinese;
		if(!isChinese) {
			loadEnglishAbbr(filePathEnglishAbbr);
		}
	}
	
	// "supplement" means additional sentence delimiter
	public SentenceSplitter(Character[] supplement, boolean isChinese, String filePathEnglishAbbr) {
		this.isChinese = isChinese;
		if(isChinese) {
			for(Character c:supplement)
				chineseDelimiters.add(c);
		} else {
			for(Character c:supplement)
				englishDelimiters.add(c);
			loadEnglishAbbr(filePathEnglishAbbr);
		}
	}
	/*
	 * Split the document only with the delimiters with nothing changed.
	 */
	public List<String> simpleSplit(String s) {
		List<String> sentences = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<s.length();i++) {
			char c = s.charAt(i);
			sb.append(c);
			if(isChinese) {
				if(chineseDelimiters.contains(c)) {
					sentences.add(new String(sb));
					sb.delete(0, sb.length());
				}
			} else {
				// english
				if(englishDelimiters.contains(c)) {
					sentences.add(new String(sb));
					sb.delete(0, sb.length());
				}
			}
		}
		if(sb.length()>0)
			sentences.add(new String(sb));
		return sentences;
	}
	
	/*
	 * Split the document with the delimiters, but if "." hits a abbr. word, we will not split.
	 * This function is only used in the English text. 
	 */
	public List<String> splitWithFilters(String s) {
		LinkedList<String> sentences = new LinkedList<String>();
		int sBeginIndex = s.length()-1; // from end to start
		int sEndIndex = s.length();
		
		for(;sBeginIndex>=0;sBeginIndex--) { // for each character
			char c = s.charAt(sBeginIndex);
			
			if(englishDelimiters.contains(c)) { // c is a delimiter
				if(c == '.') {
					boolean split = true;
					int step = 0;
					String matchedAbbr = null;
					if(filterFullStopWithNumbers(sBeginIndex, s)) {
						split = false;
						step = 2;
					} else if(filterOneUpperWord(sBeginIndex, s)) {
						split = false;
						step = 1;
					}
					else if((matchedAbbr = filterAbbr(sBeginIndex, s)) != null) {
						split = false;
						step = matchedAbbr.length();
					}
					
					if(split) {
						if(sBeginIndex+1 < sEndIndex)
							sentences.addFirst(s.substring(sBeginIndex+1, sEndIndex));
						sEndIndex = sBeginIndex+1;
					} else {
						sBeginIndex = sBeginIndex-step+1; // +1 because sBeginIndex--
					}
					
				} else {
					if(sBeginIndex+1 < sEndIndex)
						sentences.addFirst(s.substring(sBeginIndex+1, sEndIndex));
					sEndIndex = sBeginIndex+1;
				}
				
			}

		}
		sentences.addFirst(s.substring(sBeginIndex+1, sEndIndex));
		return sentences;
	}
	
	// Ones below are some heuristic rules to help the splitter.
	
	// If there is only one uppercased word with a dot(current), we don't split it.
	private boolean filterOneUpperWord(int currentIndex, String s) {
		if(currentIndex >= 2) { 
			char previous = s.charAt(currentIndex-1);
			char pp = s.charAt(currentIndex-2);
			if(CharCode.isUpperCase(previous) && (isEmptyChar(pp) || Punctuation.isEnglishPunc(pp)))
				return true;
			else
				return false;
		} else if(currentIndex == 1) {
			char previous = s.charAt(currentIndex-1);
			if(CharCode.isUpperCase(previous))
				return true;
			else
				return false;
		} else
			return false;
			
	}
	
	// If there are words which match the abbr, we don't split it.
	private String filterAbbr(int currentIndex, String s) {
		String matchedAbbr = null;
		for(String abbr:englishAbbr) {
			int tempIndex = currentIndex;
			int j=abbr.length()-1;
			for(;j>=0;j--,tempIndex--) {
				if(tempIndex<0) // the string in sentence is less than the string in abbr.
					break;
				if(Character.toLowerCase(abbr.charAt(j)) != Character.toLowerCase(s.charAt(tempIndex)))
					break;
				
			}
			if(j<0 && (tempIndex<0 || (tempIndex>=0 && (isEmptyChar(s.charAt(tempIndex)) || Punctuation.isEnglishPunc(s.charAt(tempIndex))) ))) {
				// match
				matchedAbbr = abbr;
				break;
			} 
		}
		return matchedAbbr;
	}
	
	// If there are only numbers before and after the token ".", it means a decimal and we don't split it.
	private boolean filterFullStopWithNumbers(int currentIndex, String s) {
		if(currentIndex==0 || currentIndex==s.length()-1)
			return false;
		char previous = s.charAt(currentIndex-1);
		char next = s.charAt(currentIndex+1);
		if(CharCode.isNumber(previous) && CharCode.isNumber(next))
			return true;
		else 
			return false;
	}
}
