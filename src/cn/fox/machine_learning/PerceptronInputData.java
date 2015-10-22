package cn.fox.machine_learning;

import java.io.Serializable;
import java.util.ArrayList;

public class PerceptronInputData implements Serializable{
	
	private static final long serialVersionUID = 4107472666142847362L;
	// Usually, "tokens" is like "Jobs" "founded" "Apple" "!" .
	public ArrayList<String> tokens;
	
	public PerceptronInputData() {
		tokens = new ArrayList<String>();
	}
	
	@Override
	public String toString() {
		String s = tokens.get(0);
		for(int i=1;i<tokens.size();i++)
			s += ", "+tokens.get(i);
		return s;
	}

}
