package cn.fox.data_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/*
 * This is a graph structure for both the direct and non-direct graph.
 * Because for most functions, they are the same. 
 */
public class Graph {
	public boolean isDirect; // if ture, it's a direct graph
	public HashMap<GraphVertex, GraphVertex> vertexes;  
	public HashMap<GraphEdge, GraphEdge> edges;
	
	/*
	 * This will make a empty graph and call addByEdge one by one.
	 */
	public Graph(boolean isDirect) {
		this.isDirect = isDirect;
		vertexes = new HashMap<GraphVertex, GraphVertex>();
		edges = new HashMap<GraphEdge, GraphEdge>();
		 
	}
	
	public void addVertex(GraphVertex vertex) {
		if(!vertexes.containsValue(vertex)) {
			vertexes.put(vertex,vertex);
		}
	}
	
	/*
	 * Just new a tailVertex and headVertex, then call this function. 
	 * The relations between vertex and edge will be handled automaticly.
	 * For a non-direct graph, we copy a edge with reverse direction.
	 */
	public void addByEdge(GraphVertex tailVertex, GraphVertex headVertex, GraphEdge edge) {
		if(edges.containsValue(edge)) {
			return;
		}
		
		// tail vertex
		if(!vertexes.containsValue(tailVertex)) {
			vertexes.put(tailVertex, tailVertex);
		}
		// head vertex
		if(!vertexes.containsValue(headVertex)) {
			vertexes.put(headVertex, headVertex);
		}
		// edge
		edge.tailVertex = tailVertex;
		edge.headVertex = headVertex;
		edges.put(edge,edge);
		if(tailVertex.firstout == null) {
			tailVertex.firstout = edge;
		}
		else {
			GraphEdge current = tailVertex.firstout;
			while(current.sametail != null)
				current = current.sametail;
			current.sametail = edge;
		}
		
		if(headVertex.firstin == null) {
			headVertex.firstin = edge;
		}
		else {
			GraphEdge current = headVertex.firstin;
			while(current.samehead != null)
				current = current.samehead;
			current.samehead = edge;
		}
		
		if(!isDirect) {
			GraphEdge reverseEdge = new GraphEdge();
			reverseEdge.tailVertex = headVertex;
			reverseEdge.headVertex = tailVertex;
			edges.put(reverseEdge,reverseEdge);
			
			if(tailVertex.firstin == null) {
				tailVertex.firstin = reverseEdge;
			}
			else {
				GraphEdge current = tailVertex.firstin;
				while(current.samehead != null)
					current = current.samehead;
				current.samehead = reverseEdge;
			}
			
			if(headVertex.firstout == null) {
				headVertex.firstout = reverseEdge;
			}
			else {
				GraphEdge current = headVertex.firstout;
				while(current.sametail != null)
					current = current.sametail;
				current.sametail = edge;
			}
			
		}
	}
	
	/*
	 * For DirectGraph, v go forward(when call next) or go backward(when call pre) a step according to the edge.
	 * For NonDirectGraph, next and pre are no different.
	 * If cannot, return null
	 * Set visited by yourself.
	 */
	public GraphVertex next(GraphVertex v) {
		GraphEdge currentEdge = v.firstout;
		while(currentEdge!=null && currentEdge.headVertex.visited) {
			currentEdge = currentEdge.sametail;
		}
		if(currentEdge != null) {
			return currentEdge.headVertex;
		}
		else
			return null;
	}
	public GraphVertex pre(GraphVertex v) {
		GraphEdge currentEdge = v.firstin;
		while(currentEdge!=null && currentEdge.tailVertex.visited) {
			currentEdge = currentEdge.samehead;
		}
		if(currentEdge != null)
			return currentEdge.tailVertex;
		else return null;
	}
	
	/*
	 * Traverse the graph from v with deep first method and return the vertex list according to the traversed order.
	 */
	public ArrayList<GraphVertex> traverse(GraphVertex v) {
		ArrayList<GraphVertex> component = new ArrayList<GraphVertex>();
		GraphVertex currentVertex = v;
		//System.out.println(currentVertex.index);
		component.add(currentVertex);
		currentVertex.visited = true;
		ArrayList<GraphVertex> trace = new ArrayList<GraphVertex>();
		trace.add(currentVertex);
		do {
			currentVertex = next(currentVertex);
			if(currentVertex !=null)
			{	
				currentVertex.visited = true;
				trace.add(currentVertex);
				//System.out.println(currentVertex.index);
				component.add(currentVertex);
			}
			else {
				trace.remove(trace.size()-1);
				if(trace.size() != 0)
					currentVertex = trace.get(trace.size()-1);
				else
					break;
			}
			
						
		}while(true);
		
		return component;
	}
	
	/*
	 * This function sets all the vertexes' visited states to false.
	 */
	public void resetVisited() {
		Iterator it = vertexes.keySet().iterator();
		while(it.hasNext()) {
			GraphVertex vertex = vertexes.get(it.next());
			vertex.visited = false;
		}
	}
	
	/*
	 * Find all the outgoing edges of this vertex.
	 * If no, return a empty arraylist.
	 */
	public ArrayList<GraphEdge> getOutgoingEdge(GraphVertex v) {
		ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
		GraphEdge current = v.firstout;
		while(current != null) {
			edges.add(current);
			current = current.sametail;
		}
		return edges;
	}
	
	/*
	 * Remove a vertex
	 */
	public void removeVertex(GraphVertex vertex) {
		
		// cut all the connections with the tail vertex
		GraphEdge edgeIn = vertex.firstin;
		while(edgeIn != null) {
			GraphVertex tailVertex = edgeIn.tailVertex;
			if(tailVertex.firstout == edgeIn)
				tailVertex.firstout = edgeIn.sametail;
			else {
				GraphEdge tailVertexOutEdge = tailVertex.firstout;
				while(tailVertexOutEdge.sametail != edgeIn) {
					tailVertexOutEdge = tailVertexOutEdge.sametail;
				}
				tailVertexOutEdge.sametail = edgeIn.sametail;
			}
			// remove this edgeIn
			this.edges.remove(edgeIn);
			
			edgeIn = edgeIn.samehead;
		}
			
		// cut all the connections with the head vertex
		GraphEdge edgeOut = vertex.firstout;
		while(edgeOut != null) {
			GraphVertex headVertex = edgeOut.headVertex;
			if(headVertex.firstin == edgeOut)
				headVertex.firstin = edgeOut.samehead;
			else {
				GraphEdge headVertexInEdge = headVertex.firstin;
				while(headVertexInEdge.samehead != edgeOut) {
					headVertexInEdge = headVertexInEdge.samehead;
				}
				headVertexInEdge.samehead = edgeOut.samehead;
			}
			// remove this edgeOut
			this.edges.remove(edgeOut);
			
			edgeOut = edgeOut.sametail;
		}
		
		// remove this vertex
		this.vertexes.remove(vertex);
	}
	

	
	
	
	
}
