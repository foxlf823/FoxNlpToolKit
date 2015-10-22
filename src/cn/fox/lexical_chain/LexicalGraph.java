package cn.fox.lexical_chain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import cn.fox.data_structure.Graph;
import cn.fox.data_structure.GraphEdge;
import cn.fox.data_structure.GraphVertex;
/*
 * It denotes a document with all its lexical chains and each chain can be seen as a  connected component of this LexicalGraph.
 * In a chain, LexicalVertex which emerge in different sentences connect with LexicalEdge.
 * In a sentence, a few identical Lexical with different positions in a LexicalVertex connect other Lexical in other LexicalVertex
 * with DependentcyEdge.
 */
public class LexicalGraph extends Graph{
	public int sentenceNumber; // this may be not accurate, because we may remove some vertexes.
	
	public LexicalGraph() {
		super(true);
		
	}
	
		
	/*
	 * Add all the lexicons of one sentence into the LexicalGraph.
	 */
	public void addByLexicons(LinkedList<Lexical> lineLexicons) {
		if(lineLexicons.isEmpty()) return;
		else {
			int sentIndex = lineLexicons.get(0).line;
			if(sentenceNumber<sentIndex)
				sentenceNumber = sentIndex;
		}
	
		ArrayList<LexicalVertex> newVertexes = new ArrayList<LexicalVertex>();
		// Prepare the LexicalVertex and the default method is to merge the lexicons with the same word.
		while(!lineLexicons.isEmpty()) {
			
			LexicalVertex vertex = new LexicalVertex();
			for(int i=0;i<lineLexicons.size();i++) {
				Lexical lineLexicon = lineLexicons.get(i);
				if(vertex.lexicons.isEmpty()) {
					vertex.lexicons.add(lineLexicon);
					lineLexicons.remove(i);
					i--;  // the following lexicon will move to the head one step, so we do i-- or we will miss one element.
				} else {
					if(areTwoLexicalMerged(lineLexicon, vertex.getDelegate())) 
					{
						vertex.lexicons.add(lineLexicon);
						lineLexicons.remove(i);
						i--;
					}
				}
					
			}
			
			newVertexes.add(vertex);
		}
		/*
		 * Now, we put these vertexes into the graph, and connect the new vertexes to the old.
		 * By default, a new vertex will be connected onto a old vertex whose "word" is the same with the new's.
		 */ 
		for(int i=0;i<newVertexes.size();i++) {
			LexicalVertex newVertex = (LexicalVertex)newVertexes.get(i);
			if(this.vertexes.size() > 0) {
				// find the lexical vertex which satisfies the function "areTwoLexicalVertexConnected" and
				// has the largest "line" and "index" lexical
				LexicalVertex largestLexicalVertex = null;
				int largestLine = Integer.MIN_VALUE;
				int largestIndex = Integer.MIN_VALUE;
				Iterator it =  this.vertexes.keySet().iterator();
				while(it.hasNext()) {
					LexicalVertex temp = (LexicalVertex)this.vertexes.get(it.next());
					if(areTwoLexicalVertexConnected(temp, newVertex)) {
						for(Lexical lex:temp.lexicons) {
							if(lex.line>largestLine) {
								largestLine = lex.line;
								largestIndex = lex.index;
								largestLexicalVertex = temp;
							}
							else if(largestLine == lex.line && lex.index>largestIndex) {
								largestIndex = lex.index;
								largestLexicalVertex = temp;
							}
						}
					}
				}
				if(largestLexicalVertex == null) { // no one can be connected with
					this.addVertex(newVertex);
				} else {
					// make a edge and connect
					LexicalEdge edge = new LexicalEdge();
					this.addByEdge(largestLexicalVertex, newVertex, edge);
				}

			}
			else  { // the first vertex
				this.addVertex(newVertex);
			}
			
			
		}
		
		
	}
	
	/*
	 * By default, two lexical with the same "word" in the same sentence will be merged as one LexicalVertex.
	 * You can override this method.
	 */
	public boolean areTwoLexicalMerged(Lexical a, Lexical b) {
		if(a.word.equals(b.word))
			return true;
		else return false;
	}
	
	/*
	 * By default, two LexicalVertex's "delegate" with the same "word" will be connected.
	 */
	public boolean areTwoLexicalVertexConnected(LexicalVertex a, LexicalVertex b) {
		if(a.getDelegate().word.equals(b.getDelegate().word))
			return true;
		else return false;
	}
	
	/*
	 * Because there are two types of edge, LexicalEdge and DependencyEdge.
	 * The direction of LexicalEdge can be seen as vertical and that of DependencyEdge can be seen as horizontal.
	 * So when you use "next" to walk in the graph, it treats all the edges the same.
	 * Alternatively, you can use the functions below.
	 */
	public LexicalVertex nextVertical(LexicalVertex v) {
		GraphEdge currentEdge = v.firstout;
		while(currentEdge!=null && ((currentEdge instanceof DependencyEdge) || currentEdge.headVertex.visited)) {
			currentEdge = currentEdge.sametail;
		}
		if(currentEdge != null) {
			return (LexicalVertex) currentEdge.headVertex;
		}
		else
			return null;
	}
	/*
	 * Traverse along vertical direction and return all the vertexes traversed.
	 */
	public ArrayList<LexicalVertex> traverseVertical(LexicalVertex v) {
		ArrayList<LexicalVertex> components = new ArrayList<LexicalVertex>();
		LexicalVertex currentVertex = (LexicalVertex)v;
		//System.out.print(currentVertex);
		currentVertex.visited = true;
		components.add(currentVertex);
		ArrayList<GraphVertex> trace = new ArrayList<GraphVertex>();
		trace.add(currentVertex);
		do {
			currentVertex = (LexicalVertex) nextVertical(currentVertex);
			
			if(currentVertex !=null)
			{	
				currentVertex.visited = true;
				trace.add(currentVertex);
				//System.out.print(currentVertex);
				components.add(currentVertex);
			}
			else {
				trace.remove(trace.size()-1);
				if(trace.size() != 0)
					currentVertex = (LexicalVertex) trace.get(trace.size()-1);
				else
					break;
			}
			
						
		}while(true);
		//System.out.println();
		return components;
	}
	
	// If there are no vertex in this sentence, return a empty list.
	public ArrayList<LexicalVertex> getVertexBySentenceNumber(int sentenceNumber) {
		ArrayList<LexicalVertex> list = new ArrayList<LexicalVertex>();
		Iterator it =  this.vertexes.keySet().iterator();
		while(it.hasNext()) {
			LexicalVertex vertex = (LexicalVertex)this.vertexes.get(it.next());
			if(vertex.getDelegate().line == sentenceNumber)
				list.add(vertex);
		}
		
		return list;
		
	}
	
	public ArrayList<DependencyEdge> getOutgoingDependentEdge(LexicalVertex v) {
		ArrayList<DependencyEdge> edges = new ArrayList<DependencyEdge>();
		GraphEdge current = v.firstout;
		while(current != null) {
			if(current instanceof DependencyEdge)
				edges.add((DependencyEdge)current);
			current = current.sametail;
		}
		return edges;
	}
	
	public void printLexicalChains(boolean reset) {
		if(reset)
			this.resetVisited();
		int connectedCount = 1;
		for(int i=1;i<=sentenceNumber;i++) {
			ArrayList<LexicalVertex> vertexInASentence= getVertexBySentenceNumber(i);
			if(vertexInASentence.isEmpty()) continue;
			for(int j=0;j<vertexInASentence.size();j++) {
				LexicalVertex current = vertexInASentence.get(j);
				if(!current.visited) {
					System.out.println("The "+connectedCount+" lexical chain:");
					ArrayList<LexicalVertex> component = traverseVertical(current);
					for(LexicalVertex vertex:component)
						System.out.print(vertex);
					System.out.println();
					connectedCount++;
				}
			}
		}
		
	}
	
	public void printDependantRelations() {
		
		for(int i=1;i<=sentenceNumber;i++) {
			ArrayList<LexicalVertex> verticesInThisSentence = this.getVertexBySentenceNumber(i);
			if(verticesInThisSentence.isEmpty()) continue;
			System.out.println("The "+i+" sentence dependent relations:");
			for(int j=0;j<verticesInThisSentence.size();j++) {
				LexicalVertex current= (LexicalVertex) verticesInThisSentence.get(j);
				ArrayList<DependencyEdge> edges = this.getOutgoingDependentEdge(current);
				for(DependencyEdge e:edges)
					System.out.print(e+" ");
			}
			System.out.println();
		}
	}
}
