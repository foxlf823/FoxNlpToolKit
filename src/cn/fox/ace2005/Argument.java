package cn.fox.ace2005;

import java.io.Serializable;

/*
 * Argument denotes the "event_mention_argument" tags in the *.apf.xml files.
 */

public class Argument implements Serializable {

	private static final long serialVersionUID = 8190611791535202489L;
	public String value;
	public int start;
	public int end;
	public String refID;
	public String role;
	public Mention mention; // mention's id is equal to refID
	
	public Argument(String value, int start, int end, String refID, String role, Mention mention) {
		super();
		this.value = value;
		this.start = start;
		this.end = end;
		this.refID = refID;
		this.role = role;
		this.mention = mention;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "argument: "+role+" "+value+" "+refID+" "+start+" "+end;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Argument))
			return false;
		Argument o = (Argument)obj;
		if(value.equals(o.value) && role.equals(o.role))
			return true;
		else return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value.hashCode()+role.hashCode();
	}*/
}
