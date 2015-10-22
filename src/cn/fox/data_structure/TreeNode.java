package cn.fox.data_structure;

import java.util.LinkedList;

public class TreeNode<T> {
	public TreeNode<T> parent;
	public LinkedList<TreeNode<T>> sons;
	public T data;
	
	public TreeNode(T data) {
		sons = new LinkedList<TreeNode<T>>();
		this.data = data;
	}
}
