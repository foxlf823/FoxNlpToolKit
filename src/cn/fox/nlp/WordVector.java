package cn.fox.nlp;

import cn.fox.math.Matrix;

public class WordVector {
	public String word;
	public Matrix vector;
	public WordVector(String word, Matrix vector) {
		super();
		this.word = word;
		this.vector = vector;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof WordVector))
			return false;
		WordVector o = (WordVector)obj;
		if(o.word.equals(this.word))
			return true;
		else 
			return false;
	}
	
	@Override
	public int hashCode() {
		
	    return word.hashCode();  
	}
	
	@Override
	public String toString() {
		return word;
	}
}
