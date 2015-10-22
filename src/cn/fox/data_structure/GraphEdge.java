package cn.fox.data_structure;


/*
 * This class can be used by NonDirectGraph and DirectGraph.
 * For NonDirectGraph, tail and head should be considered as left and right.
 */
public class GraphEdge {
	public GraphVertex tailVertex; // this edge's tail
	public GraphVertex headVertex; // this edge's head 
	public GraphEdge samehead; // point to the next edge whose head is the same with this edge
	public GraphEdge sametail; // point to the next edge whose tail is the same with this edge
	
	public GraphEdge() {
		super();
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null || !(arg0 instanceof GraphEdge))
			return false;
		GraphEdge o = (GraphEdge)arg0;
		if(this == o) // we compare the reference address.
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
