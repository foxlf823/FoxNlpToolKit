package cn.fox.stanford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.TypedDependency;

public class DependencyTree {
	// the first node is root and sorted with token index
	public List<TreeNode> treeNodes = new ArrayList<>();
	
	public DependencyTree() {

	}
	
	public TreeNode getRoot() {
		return treeNodes.get(0);
	}
	
	public void initial(List<TypedDependency> tdl) {
		// iterator to generate TreeNode
		for(TypedDependency td:tdl) {
			IndexedWord gov = td.gov().makeCopy(0);
			TreeNode govNode = new TreeNode();
			govNode.token = gov;
			
			TreeNode govInList = null;
			if(!treeNodes.contains(govNode)) {
				treeNodes.add(govNode);
				govInList = govNode;
			} else {
				for(int i=0;i<treeNodes.size();i++) {
					if(treeNodes.get(i).equals(govNode)) {
						govInList = treeNodes.get(i); // get the old
						break;
					}
				}
			}
				

			IndexedWord dep = td.dep().makeCopy(0);
			TreeNode depNode = new TreeNode();
			depNode.token = dep;
			
			TreeNode depInList = null;
			if(!treeNodes.contains(depNode)) {
				treeNodes.add(depNode);
				depInList = depNode;
			} else {
				for(int i=0;i<treeNodes.size();i++) {
					if(treeNodes.get(i).equals(depNode)) {
						depInList = treeNodes.get(i); // get the old
						break;
					}
				}
			}
			depInList.parent = govInList; // each node has only one parent
			
		}
		
		Collections.sort(treeNodes);  
		
		// now we can complete sons of each node
		for(int i=0;i<treeNodes.size();i++) {
			TreeNode current = treeNodes.get(i);
			if(current.parent!=null)
				current.parent.sons.add(current);
		}
		

	}
	
	public class TreeNode implements Comparable<TreeNode>{
		TreeNode parent;
		List<TreeNode> sons;
		IndexedWord token;
		
		public TreeNode() {
			sons = new ArrayList<>();
		}

		
		@Override
		public boolean equals(Object obj) {
			if(obj == null || !(obj instanceof TreeNode))
				return false;
			TreeNode other = (TreeNode)obj;
			if(token.index()==other.token.index())
				return true;
			else
				return false;
		}
		
		@Override
		public int hashCode() {
			return token.index();
		}
		
		@Override
		public String toString() {
			return token.word()+"-"+token.index();
		}


		@Override
		public int compareTo(TreeNode o) {
			return token.compareTo(o.token);
		}
	}

}
