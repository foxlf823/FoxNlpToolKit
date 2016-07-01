package cn.fox.stanford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.fox.stanford.DependencyTree.TreeNode;
import edu.stanford.nlp.ling.IndexedWord;

public class DependencyBinaryTree {
	// the first node is root and sorted with token index
	public List<BinaryTreeNode> btreeNodes = new ArrayList<>();
	
	public DependencyBinaryTree() {

	}
	
	public BinaryTreeNode getRoot() {
		return btreeNodes.get(0);
	}
	
	public void fromDependencyTree(DependencyTree tree) {
		// wide first to traverse the tree
		Queue<TreeNode> q = new LinkedList<>();
		q.add(tree.getRoot());
		
		while(!q.isEmpty()) {
			TreeNode node = q.poll();
			
			TreeNode parent = node;
			BinaryTreeNode bParent = null;
			for(int i=0;i<btreeNodes.size();i++) {
				if(parent.token.index()==btreeNodes.get(i).token.index()) {
					bParent = btreeNodes.get(i);
					break;
				}
			}
			if(bParent == null) {
				bParent = new BinaryTreeNode();
				bParent.token = parent.token.makeCopy(0);
				btreeNodes.add(bParent);
			}
			
			for(int i=0;i<parent.sons.size();i++) {
				BinaryTreeNode bNode = null;
				for(int j=0;j<btreeNodes.size();j++) {
					if(parent.sons.get(i).token.index()==btreeNodes.get(j).token.index()) {
						bNode = btreeNodes.get(j);
						break;
					}
				}
				if(bNode == null) {
					bNode = new BinaryTreeNode();
					bNode.token = parent.sons.get(i).token.makeCopy(0);
					btreeNodes.add(bNode);
				}

				
				if(i==0)
					bParent.left = bNode;
				else
					bParent.right = bNode;
				
				bNode.parent = bParent;
						
				bParent = bNode;
			}

			
			for(int i=0;i<node.sons.size();i++) {
				q.add(node.sons.get(i));
			}
		}
		
		Collections.sort(btreeNodes);  
	}
	
	public class BinaryTreeNode implements Comparable<BinaryTreeNode> {
		BinaryTreeNode parent;
		IndexedWord token;
		BinaryTreeNode left;
		BinaryTreeNode right;
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null || !(obj instanceof BinaryTreeNode))
				return false;
			BinaryTreeNode other = (BinaryTreeNode)obj;
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
		public int compareTo(BinaryTreeNode o) {
			return token.compareTo(o.token);
		}

		
	}
}
