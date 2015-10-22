package cn.fox.bootstrapping;

import java.io.Serializable;

public abstract class PatternObject implements Serializable{
	private static final long serialVersionUID = -4413846852783557671L;
	
	protected Pattern pattern;

	public PatternObject(Pattern pattern) {
		super();
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

	

}
