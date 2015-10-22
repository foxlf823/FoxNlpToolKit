package cn.fox.nlp;

public class ChinesePos {
	// with chinese-distsim.tagger set, it's similar with Chinese TreeBank Tag Set

	public enum Type {
		ADVERB(1),NOUN(2), VERB(3),NUMBER(4),PARTICLE(5),PRONOUN(6), CONJ(7),MEASURE(8),PUNCTUATION(9), LOCALIZER(10),
		DETERMINER(11),NOUN_MODIFIER(12), INTERJECTION(13),PREP(14), URL(15),BA(16),BEI(17),OTHER(18);
		
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
	
	// we only capture the very explicit and meaningful pos
	public static Type getType(String pos) {
		
		
		if(pos.equals("NR") || pos.equals("NN") || pos.equals("NT")) return Type.NOUN; // 名词
		else if(pos.equals("VA") || pos.equals("VC") || pos.equals("VE") || pos.equals("VV")) return Type.VERB; // 动词
		else if(pos.equals("AS") /*着*/ || pos.indexOf("DE") != -1 /*的*/ || pos.equals("ETC")/*等等*/ || pos.equals("SP")/*句子结尾分词，吗*/)
			return Type.PARTICLE;
		else if(pos.equals("PN")) return Type.PRONOUN; // 代词
		else if(pos.equals("DT")) return Type.DETERMINER; // 这
		else if(pos.equals("JJ")) return Type.NOUN_MODIFIER; // 修饰名词的前缀
		else if(pos.equals("P")) return Type.PREP; // 介词
		else if(pos.equals("BA"))
			return Type.BA; // 把
		else if(pos.equals("LB") || pos.equals("SB"))
			return Type.BEI; // 被
		else if(pos.equals("AD")) return Type.ADVERB;  // 副词
		else if(pos.equals("LC")) return Type.LOCALIZER; // 里 外
		else if(pos.equals("CD") || pos.equals("OD"))
			return Type.NUMBER; // 数词
		else if(pos.equals("M")) return Type.MEASURE; // 量词
		else if(pos.equals("CC") || pos.equals("CS"))
			return Type.CONJ; // 连词
		else if(pos.equals("PU")) return Type.PUNCTUATION; // 标点符号
		else if(pos.equals("IJ"))
			return Type.INTERJECTION; // 感叹词
		else if(pos.equals("URL")) return Type.URL; // 网址
		else return Type.OTHER;
				
		
	}
	
}
