package cn.fox.biomedical;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Sider implements Serializable{

	private static final long serialVersionUID = 1858016880365410639L;
	public HashSet<Pair> set; 
	public HashSet<String> drug = new HashSet<>();
	public HashSet<String> disease = new HashSet<>();
	public ArrayList<Pair> list = new ArrayList<>();
	
	public Sider(String path) {
		set = new HashSet<Pair>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String thisLine = null;
			
			while ((thisLine = br.readLine()) != null) {
				if(!thisLine.isEmpty()) {
					String[] chunks = thisLine.split("\t");
					Pair pair = new Pair();
					pair.drug = chunks[3].toLowerCase();
					pair.sideEffect = chunks[4].toLowerCase();
					if(!set.contains(pair)) {
						list.add(pair);
						set.add(pair);
					}
					drug.add(pair.drug);
					disease.add(pair.sideEffect);
				}
					
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 0-not, 1-disease, 2-chemical
	public int contains(String something) {
		String lower = something.toLowerCase();
		if(drug.contains(lower))
			return 2;
		else if(disease.contains(lower))
			return 1;
		
		return 0;
	}
	
	/*
	 * Whether the dictionary contains the "word" with case ignoring
	 */
	public boolean contains(String drug, String sideEffect) {
		Pair pair = new Pair();
		pair.drug = drug.toLowerCase();
		pair.sideEffect = sideEffect.toLowerCase();
		return set.contains(pair);
		
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
