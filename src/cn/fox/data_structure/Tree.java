package cn.fox.data_structure;

import java.util.Stack;

public class Tree<T> {
	public TreeNode<T> root;
	
	public Tree(TreeNode<T> _root) {
		root = _root;
	}
	
	public void addNode(TreeNode<T> parent, TreeNode<T> t) {
		parent.sons.add(t);
		t.parent = parent;
	}
	
	public int getMinDistance(TreeNode<T> a, TreeNode<T> b) {
		Stack<TreeNode<T>> s_a = new Stack<TreeNode<T>>();
		Stack<TreeNode<T>> s_b = new Stack<TreeNode<T>>();
		
		TreeNode<T> current = a;
		s_a.push(current);
		while(current!= root) {
			current = current.parent;
			s_a.push(current);
		}
		
		current = b;
		s_b.push(current);
		while(current!= root) {
			current = current.parent;
			s_b.push(current);
		}
		
		// find the common parent that is farthest from the root
		TreeNode<T> commonParent = root;
		while(!s_a.empty() && !s_b.empty()) {
			TreeNode<T> p_a = s_a.peek();
			TreeNode<T> p_b = s_b.peek();
			if(p_a != p_b)
				break;
			else
				commonParent = p_a; // or p_b
			s_a.pop();
			s_b.pop();
		}
		if(s_a.empty()) {
			commonParent = a;
			return s_b.size();
		}
		if(s_b.empty()) {
			commonParent = b;
			return s_a.size();
		}
		
		return s_a.size()+s_b.size();
	}
}
