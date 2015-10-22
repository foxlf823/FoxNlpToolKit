package cn.fox.ace2005;

import java.io.Serializable;

/*
 * Trigger denotes the "anchor" tags in the *.apf.xml files.
 */
public class Trigger implements Serializable{

	private static final long serialVersionUID = 5038492255431175008L;
	public String value;
	public int start;
	public int end;
	public Trigger(String value, int start, int end) {
		super();
		this.value = value;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "trigger: "+value+" "+start+" "+end;
	}
	
	
	/*@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Trigger))
			return false;
		Trigger o = (Trigger)obj;
		return value.equals(o.value);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value.hashCode();
	}*/
}
