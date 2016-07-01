package cn.fox.stanford;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreLabel;



public class Dependency {
	
	// a and b is the token idx, and they must be in the same sentence
	// return the token idx of common ancestor, sdpA is the node idx of sdp from a to common ancestor
	// if return -1, it should be root, if return -2, no common ancestor
	public static int getCommonAncestor(List<CoreLabel> sent, int a, int b, 
			List<Integer> sdpA, List<Integer> sdpB) {
		if(a==b)
			return a;
		
		// if a or b are not in the graph
		if(sent.get(a).get(DepGovAnnotation.class)==-1 || sent.get(b).get(DepGovAnnotation.class)==-1)
			return -2;
		
		// get all the paths from a to root, deep first search
		List<List<Integer>> pathsA = new ArrayList<>();
		List<Integer> temp = new ArrayList<>();
		Dependency.deepFirstSearch(a, sent, pathsA, temp);
		// get all the paths from b to root
		temp.clear();
		List<List<Integer>> pathsB = new ArrayList<>();
		Dependency.deepFirstSearch(b, sent, pathsB, temp);
		
		int[] dist = new int[pathsA.size()*pathsB.size()]; // dist = a to common ancestor + b to common ancestor
		for(int i=0;i<dist.length;i++)
			dist[i] = Integer.MAX_VALUE;
		int[] commonAncestor = new int[pathsA.size()*pathsB.size()]; // save the node index of common ancestor
		for(int i=0;i<commonAncestor.length;i++)
			commonAncestor[i] = -1;
		
		for(int i=0;i<pathsA.size();i++) {
			List<Integer> path1 = pathsA.get(i);
OUTER:		for(int j=0;j<pathsB.size();j++) {
				List<Integer> path2 = pathsB.get(j);
				
				for(int k=0;k<path1.size();k++) {
					for(int m=0;m<path2.size();m++) {
						if(path1.get(k)==path2.get(m)) {
							dist[i*pathsB.size()+j] = k+m;
							commonAncestor[i*pathsB.size()+j] = path1.get(k);
							continue OUTER;
						}
					}
				}
			}
		}
		
		int shortest = Integer.MAX_VALUE;
		int k = -1;
		for(int i=0;i<dist.length;i++) {
			if(dist[i]<shortest) {
				shortest = dist[i];
				k = i;
			}
		}
		
		if(k==-1) // can't find common ancestor in the path
			return -2;
		
		int aPathIdx = k/pathsB.size();
		for(int i=0;i<pathsA.get(aPathIdx).size();i++) {
			sdpA.add(pathsA.get(aPathIdx).get(i));
			
			if(pathsA.get(aPathIdx).get(i)==commonAncestor[k])
				break;
			
		}
		
		int bPathIdx = k%pathsB.size();
		for(int i=0;i<pathsB.get(bPathIdx).size();i++) {
			sdpB.add(pathsB.get(bPathIdx).get(i));
			
			if(pathsB.get(bPathIdx).get(i)==commonAncestor[k])
				break;
			
		}
		
		return commonAncestor[k]-1;
	}
	
	// current is the token index, paths save all the paths(node index) from current to root
	public static void deepFirstSearch(int current, List<CoreLabel> sent, List<List<Integer>> paths, List<Integer> temp) {
		if(current==-1) { // root
			// System.out.println("0");
			temp.add(0);
			List<Integer> path = new ArrayList<>(temp);
			paths.add(path);
			return;
		}
		
		if(sent.get(current).get(DepGovAnnotation.class)==-1) // no governor, so not in the dep graph 
			return;
		


		
		// find cycle
		for(int i=temp.size()-1;i>=0;i--) {
			if(temp.get(i)==current+1) {
				temp.add(current+1);
				return;
			}
		}	
		
		//System.out.print((current+1)+" ");
		temp.add(current+1);


		
		Integer govIdx = sent.get(current).get(DepGovAnnotation.class);
		deepFirstSearch(govIdx-1, sent, paths, temp);
		temp.remove(temp.size()-1);
		
	}
	
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
