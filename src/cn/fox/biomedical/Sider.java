package cn.fox.biomedical;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;

public class Sider implements Serializable{

	private static final long serialVersionUID = 1858016880365410639L;
	public HashSet<Pair> list; 
	
	public Sider(String path) {
		list = new HashSet<Pair>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String thisLine = null;
			
			while ((thisLine = br.readLine()) != null) {
				if(!thisLine.isEmpty()) {
					String[] chunks = thisLine.split("\t");
					Pair pair = new Pair();
					pair.drug = chunks[3].toLowerCase();
					pair.sideEffect = chunks[4].toLowerCase();
					list.add(pair);
				}
					
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Whether the dictionary contains the "word" with case ignoring
	 */
	public boolean contains(String drug, String sideEffect) {
		Pair pair = new Pair();
		pair.drug = drug.toLowerCase();
		pair.sideEffect = sideEffect.toLowerCase();
		return list.contains(pair);
		
	}
	
	public class Pair implements Serializable{
		public String drug;
		public String sideEffect;
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null || !(obj instanceof Pair))
				return false;
			Pair o = (Pair)obj;
			if(this.drug.equals(o.drug) && this.sideEffect.equals(o.sideEffect))
				return true;
			else 
				return false;
		}
		
		@Override
		public int hashCode() {
		    return drug.hashCode()+sideEffect.hashCode();
			
		}
		
		@Override
		public String toString() {
			return drug+" , "+sideEffect;
		}
	}
}
