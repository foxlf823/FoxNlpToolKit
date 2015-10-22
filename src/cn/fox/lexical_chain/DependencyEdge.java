package cn.fox.lexical_chain;

import cn.fox.data_structure.GraphEdge;


/*
 * It connects two words(Lexical) in the same sentence. Note that there may be more than one identical words in a LexicalVertex,
 * so all the DependentEdge whose governors belong to the LexicalVertex will be connected onto that LexicalVertex. 
 */
public class DependencyEdge extends GraphEdge{
	public String relation;
	public Lexical governor; 
	public Lexical dependent; 

	public DependencyEdge(String relation, Lexical governor, Lexical dependent) {
		super();
		this.relation = relation;
		this.governor = governor;
		this.dependent = dependent;
	}
	
	@Override
	public String toString() {
		String s= relation+"("+governor.word+"-"+governor.index+", "+dependent.word+"-"+dependent.index+")";
		return s;
	}
}
