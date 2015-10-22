package cn.fox.machine_learning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import cn.fox.math.Matrix;


public class GroupAverageCluster {
	private int k; // the number of classes
	private ArrayList<Matrix> data; // vectors of input data
	private LinkedList<HashSet<Matrix>> classes; // each HashSet stores the data of the related class
	private static final int NO_CLASS = -1;
	/*
	 * We use the cosine similarity measure, and all the vectors must be normalized. 
	 */
	public GroupAverageCluster(int _k, ArrayList<Matrix> _vectors) throws Exception {
		if(_k<=0) {
			throw new Exception();
		}
		this.k = _k;
		this.data = _vectors;
		
		
	}
	
	public LinkedList<HashSet<Matrix>> doClustering() throws Exception {
		
		// treat every data as a class
		classes = new LinkedList<HashSet<Matrix>>();
		for(int i=0;i<data.size();i++) {
			HashSet<Matrix> set = new HashSet<Matrix>();
			set.add(data.get(i));
			classes.add(set);
		}
		// compute the sum of vectors of every class
		LinkedList<Matrix> vectorsSums = new LinkedList<Matrix>();
		for(HashSet<Matrix> c: classes) {
			Matrix sum = new Matrix(data.get(0).getRowNumber(), data.get(0).getColNumber());
			for(Matrix v: c) {
				sum.addThis(v);
			}
			vectorsSums.add(sum);
		}
		while(classes.size() > k) {
			double maxAverageSum = Double.MIN_VALUE;
			int ci = NO_CLASS;
			int cj = NO_CLASS;
			for(int i=0;i<classes.size();i++) {
				for(int j=0;j<classes.size();j++) {
					if(i==j) continue;
					double temp = (Matrix.innerProduct(Matrix.add(vectorsSums.get(i), vectorsSums.get(j)), Matrix.add(vectorsSums.get(i), vectorsSums.get(j)))-(classes.get(i).size()+classes.get(j).size()))
							/ ((classes.get(i).size()+classes.get(j).size())*(classes.get(i).size()+classes.get(j).size()-1));
					if(maxAverageSum < temp) {
						maxAverageSum = temp;
						ci = i;
						cj = j;
					}
				}
			}
			// merge class ci and cj
			HashSet<Matrix> classCi = classes.get(ci);
			HashSet<Matrix> classCj = classes.get(cj);
			classCi.addAll(classCj);
			classes.remove(cj);
			vectorsSums.remove(cj);
			// recompute the sum of vectors of changed classes
			computeVectorsSum(vectorsSums.get(ci),classCi);
		}
		
		return classes;
	}
	
	private void computeVectorsSum(Matrix sum, HashSet<Matrix> c) throws Exception {
		
		sum.fill(0);
		for(Matrix v: c) {
			sum.addThis(v);
		}
		
	}
	
	public void printResults() {

		for(int i=0;i<classes.size();i++) {
			System.out.println("The "+i+" class:");
			Iterator<Matrix> it = classes.get(i).iterator();
			while(it.hasNext()) {
				System.out.println(it.next());
			}
			System.out.println();
		}
	}
}
