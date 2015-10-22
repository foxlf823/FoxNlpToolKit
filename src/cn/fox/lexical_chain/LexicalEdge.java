package cn.fox.lexical_chain;

import cn.fox.data_structure.GraphEdge;
/*
 * It connects two LexicalVertex in different sentences.
 */
public class LexicalEdge extends GraphEdge{

	public LexicalEdge() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		LexicalVertex tail = (LexicalVertex)this.tailVertex;
		LexicalVertex head = (LexicalVertex)this.headVertex;
		String s= tail.toString()+head.toString();
		return s;
	}
}
