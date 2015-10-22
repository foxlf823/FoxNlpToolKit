package cn.fox.lexical_chain;

import java.util.ArrayList;

import cn.fox.data_structure.GraphVertex;
/*
 * It denotes a lexical vertex in the graph. Because there may be more than one identical words with different positions in a sentence,
 * we use a arraylist "lexicons" to include these words.
 */
public class LexicalVertex extends GraphVertex {
	public ArrayList<Lexical> lexicons;

	public LexicalVertex() {
		super();
		lexicons = new ArrayList<Lexical>();
	}
	
	@Override
	public String toString() {
		String s = "(";
		for(int i=0;i<lexicons.size();i++) {
			if(i==0)
				s += lexicons.get(i);
			else
				s += ", "+lexicons.get(i);
		}
		s +=")";
		return s;
	}
	
	/*
	 * Now the words in lexicon of vertex.lexicons are all the same.
	 */
	public Lexical getDelegate() {
		return lexicons.get(0);
	}
}
