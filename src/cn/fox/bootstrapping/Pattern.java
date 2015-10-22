package cn.fox.bootstrapping;

import java.io.Serializable;


public abstract class Pattern implements Serializable {

	private static final long serialVersionUID = 1056342106579004297L;
	private String[] positions;
	
	public Pattern(String [] positions) {
		this.positions = positions;
	}
	
	public String getPosition(int i) {
		if(i<0 || i>= positions.length)
			return null;
		return positions[i];
	}
	
	public int getLength() {
		return positions.length;
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null || !(arg0 instanceof Pattern))
			return false;
		Pattern o = (Pattern)arg0;
		if(this.positions.length != o.positions.length)
			return false;
		for(int i=0;i<positions.length;i++) {
			if(!this.positions[i].equals(o.positions[i]))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash=0;
	    for(int i = 0; i < positions.length; i++)  
	    	hash += (i+1)*(int)(positions[i].hashCode());  
	    return hash;  
	}

	
	@Override
	public String toString() {
		String ret = positions[0].toString();
		for(int i=1;i<positions.length;i++) {
			ret += " "+positions[i].toString();
		}
		return ret;
	}
	
	
}