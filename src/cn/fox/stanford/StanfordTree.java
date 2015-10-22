package cn.fox.stanford;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.IntPair;

public class StanfordTree {
	public static HashSet<String> phrase = new HashSet<String>(Arrays.asList("ADJP","ADVP","CONJP","PP","QP","VP","WHADJP","WHADVP"
			,"WHNP","WHPP","NP"));
	
	// get the phrasal ancestor of node1 and node2
	public static Tree getCommonAncestor(Tree root, Tree node1, Tree node2) {
		if(node1==node2) {
			if(node1.isLeaf())
				return node1.ancestor(2, root);
			else
				return node1;
			/*Tree ancestor = node1.ancestor(2, root);
			if(ancestor==null)
				return root;
			else
				return ancestor;*/
		}
		List<Tree> path1 = root.pathNodeToNode(root, node1);
		List<Tree> path2 = root.pathNodeToNode(root, node2);
		// find the lowerest common ancestor
		for(int i=path1.size()-1;i>=0;i--) {
			for(int j=path2.size()-1;j>=0;j--) {
				if(path1.get(i)==path2.get(j))
					return path1.get(i);
			}
		}
		// this is a exception because they at least have root as their common ancestor
		return null;	
	}
	
	// beginIndex: the index of begin token
	// endIndex: the index of end token
	public static Tree getLowestCommonParrentOfTokens(Tree root, int beginIndex, int endIndex) {
		Tree parent = root;
		IntPair span = root.getSpan();
		List<Tree> nodes= root.preOrderNodeList();
		for(Tree node:nodes) {
			if(!node.isPhrasal()) continue;
			IntPair tempSpan = node.getSpan();
			/*if(tempSpan==null)
				System.out.print("");*/
			if(tempSpan.getSource()<=beginIndex && endIndex<=tempSpan.getTarget() &&
				(tempSpan.getSource()>=span.getSource() || tempSpan.getTarget()<=span.getTarget())	) {
				span = tempSpan;
				parent = node;
			}
					
		}
		return parent;
	}
	
	// find the path from node to the common ancestor of node and triggerSpan
	public static LinkedList<Tree> getPhrasePathFromTriggerToNode(Tree root, IntPair triggerSpan, Tree node) {
		List<Tree> path = root.pathNodeToNode(root, node);
		LinkedList<Tree> llPath = new LinkedList<Tree>();
		int i=path.size()-1;
		for(;i>0;i--) { // no = because we don't consider root
			Tree temp = path.get(i);
			if(temp.isPhrasal()) {
				llPath.addFirst(temp);
				if(temp.getSpan().getSource()<=triggerSpan.getSource() && triggerSpan.getTarget()<=temp.getSpan().getTarget()) {
					// trigger is in this phrase
					break;
				}
			}
		}
		
		return llPath;
	}

}
