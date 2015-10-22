package cn.fox.stanford;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Dependency {
	/*
	 * After writing List<TypedDependency> into a file, you can use this function to recover the dependent relations.
	 * "thisLine" denotes a line of a file.
	 * Note that we ignore the "ROOT" relation.
	 */
	public static ArrayList<DependencyTriple> stringLineToDependencyTriple(String thisLine) {
		if(thisLine.trim().isEmpty()) return null;
		ArrayList<DependencyTriple> triples = new ArrayList<DependencyTriple>();
		thisLine = thisLine.substring(thisLine.indexOf("[")+1, thisLine.lastIndexOf("]")); 
		String[] tokens = thisLine.split(", "); 
		String relation = null;
		String tailData = null;
		String tailTokenNumber = null;
		String headData = null;
		String headTokenNumber = null;
		for(int i1=0;i1<tokens.length;i1++) {
			
			if(i1%2 == 0) { // odd
				int seperator1 = tokens[i1].indexOf("(");
				int seperator2 = tokens[i1].lastIndexOf("-");
				relation = tokens[i1].substring(0, seperator1);
				tailData = tokens[i1].substring(seperator1+1, seperator2);
				
				String s = tokens[i1].substring(seperator2+1);
				String x = new String("\\d*"); // solve dependency error
				Pattern p1 = Pattern.compile(x, Pattern.CASE_INSENSITIVE);
				Matcher m1 = p1.matcher(s);
				if(m1.find())
					s = m1.group();
				tailTokenNumber = s;
				
				
			} else { 
				int seperator1 = tokens[i1].lastIndexOf("-");
				headData = tokens[i1].substring(0, seperator1);
				String s = tokens[i1].substring(seperator1+1, tokens[i1].length()-1);
				String x = new String("\\d*"); // solve dependency error
				Pattern p1 = Pattern.compile(x, Pattern.CASE_INSENSITIVE);
				Matcher m1 = p1.matcher(s);
				if(m1.find())
					s = m1.group();
				headTokenNumber = s;
				
				if(!relation.equalsIgnoreCase("root"))
					triples.add(new DependencyTriple(relation, tailData, tailTokenNumber, headData, headTokenNumber));
				
			}
		}
		
		return triples;
	}
}
