package cn.fox.nlp;

import cn.fox.nlp.ChinesePos.Type;

public class EnglishPos {
	// english-left3words-distsim.tagger
	// 1=,,5=MD,7=DT,10=.,11=.$$.,14=CC,19=PRP,21=WDT,23=RP,24=PRP$,26=POS,27=``,28=EX,29='',30=WP,31=:,33=WRB,34=$,36=WP$,37=-LRB-,38=-RRB-,39=PDT,41=FW,42=UH,43=SYM,44=LS,45=#] 
	public enum Type {
		NOUN(1), VERB(2),ADJ(3), ADV(4), PREP(5), CD(6), OTHER(7);
		
		private int _value;
		private Type(int value)
		{
			_value = value;
		}
		
		public int value()
		{
			return _value;
		}
    }
	
	public static Type getType(String pos) {

		if(pos.equals("NNP") || pos.equals("NNS") || pos.equals("NN") || pos.equals("NNPS")) return Type.NOUN; // 名词
		else if(pos.equals("VB") || pos.equals("VBZ") || pos.equals("VBG") || pos.equals("VBD") || pos.equals("VBN") || pos.equals("VBP")) return Type.VERB; // 动词
		else if(pos.equals("JJ") || pos.equals("JJS")  || pos.equals("JJR"))
			return Type.ADJ;
		else if(pos.equals("RB") || pos.equals("RBR")  || pos.equals("RBS"))
			return Type.ADV;
		else if(pos.equals("IN") || pos.equals("TO"))
			return Type.PREP;
		else if(pos.equals("CD"))
			return Type.CD; // ordinal number are tagged as JJ
		else
			return Type.OTHER;
				
		
	}
	
}
