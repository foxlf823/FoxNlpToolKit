package cn.fox.data_structure;



/*
 * This class can be used by NonDirectGraph and DirectGraph.
 * For NonDirectGraph, in and out should be considered as the same.
 */
public class GraphVertex {
	public GraphEdge firstin; // point to the first edge whose head is this vertex
	public GraphEdge firstout; // point to the first edge whose tail is this vertex
	public Integer index; // just for debug
	public boolean visited; // for traversing
	
	public GraphVertex() {
		super();
		visited = false;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null || !(arg0 instanceof GraphVertex))
			return false;
		GraphVertex o = (GraphVertex)arg0;
		if(this == o) // we compare the reference address.
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return index.toString();
	}
}
