package cn.fox.utils;

public class Pair<A, B> {
	public A a;
	public B b;
	
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public Pair() {
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["+a.toString()+", "+b.toString()+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Pair))
			return false;
		Pair o = (Pair)obj;

		if(a.equals(o.a) && b.equals(o.b))
			return true;
		else 
			return false;
	}
	
	@Override
	public int hashCode() {
		int seed = 31; 
	    int hash = 0;  
	    hash = (hash * seed) + a.hashCode();  
	    hash = (hash * seed) + b.hashCode();  
	    return hash;  
	}
}
