package cn.fox.lexical_chain;

/*
 * It denotes a word or token in the document. We assume that a document has been formated as one sentence a line and
 * segmented, so "line" denotes the sentence index and "index" denotes the word index in the sentence.
 */
public class Lexical {
	public String word;
	public int line; // which line this lexical emerges, start at 1
	public int index; // the index in the line, start at 1
	
	public Lexical(String word, int line, int index) {
		super();
		this.word = word;
		this.line = line;
		this.index = index;
	}
	
	@Override
	public String toString() {
		return word+"-"+line+"-"+index;
	}
}
