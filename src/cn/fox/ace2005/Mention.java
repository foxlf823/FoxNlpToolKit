package cn.fox.ace2005;

import java.io.Serializable;

/*
 * Mention is the parent class of Entity, Time and Value.
 */
public class Mention implements Serializable{

	private static final long serialVersionUID = -8671873737315446638L;
	protected String value; 
	protected int start;
	protected int end;
	protected String id;
	protected String type;
	public Mention(String value, int start, int end, String id, String type) {
		super();
		this.value = value;
		this.start = start;
		this.end = end;
		this.id = id;
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	public String getId() {
		return id;
	}
	public String getType() {
		return type;
	}

	
}
