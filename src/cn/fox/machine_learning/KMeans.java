package cn.fox.machine_learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import cn.fox.math.Matrix;

/*
 * if you use the Buckshot to get the inital centers, make sure normalize the input data
 */
public class KMeans {
	public int k; // the number of classes
	public ArrayList<Matrix> vectors; // vectors of input data
	public int[] vectors2classes; // the i'st Integer denotes i belongs to class Integer 
	public ArrayList<Matrix> centroids; // k centroids
	public int iterationTimes;
	
	private static final int NO_CLASS = -1;
	
	// modify this function with various similarity metric
	public double similarity(Matrix a, Matrix b) throws Exception{
		return 1.0/(1.0+Matrix.distanceEuclidean(a,b));
		
	}
	
	// compute the centroid of the i'st class, modify this function if necessary
	protected Matrix mean(int indexOfK) throws Exception {
		Matrix old = centroids.get(indexOfK);
		
		Matrix temp = new Matrix(old.getRowNumber(), old.getColNumber());
		
		int count = 0;
		for(int i=0;i<vectors2classes.length;i++) {
			Integer indexOfClass = vectors2classes[i];
			if(indexOfK != indexOfClass)
				continue;
			Matrix vector = vectors.get(i);
        	temp.addThis(vector);
        	count++;
		}
        temp.numMulThis(1.0/count);
        
        return temp;
	}
	
	public KMeans(int _k, ArrayList<Matrix> _vectors, ArrayList<Matrix> _centroids, int _iterationTimes) throws Exception {
		if(_k<=0 || _k != _centroids.size()) {
			throw new Exception();
		}
		this.k = _k;
		this.vectors = _vectors;
		this.centroids = _centroids;
		this.vectors2classes = new int[vectors.size()];
		Arrays.fill(vectors2classes, NO_CLASS);
		this.iterationTimes = _iterationTimes;
	}
	
	// this function can only run once, because the centroids and vectors2classes have changed
	public int[] getResults() throws Exception{
		int count = 0;
		boolean changed = false;
		
		do {
			changed = false;
			// assign each element to a class based on the distance
			for(int i=0;i<vectors.size();i++) {
				double max = Double.MIN_VALUE;
				int indexOfClass = NO_CLASS;
				for(int j=0;j<centroids.size();j++) {
					double temp = similarity(vectors.get(i), centroids.get(j));
					if(max < temp) {
						max = temp;
						indexOfClass = j; 
					}
				}
				
				if(vectors2classes[i] != indexOfClass) {
					vectors2classes[i] = indexOfClass;
					changed = true;
				}
				
			}
			if(changed == false)
				break;
			// recompute centroids
			for(int i=0;i<centroids.size();i++) {
				Matrix newCentroid = mean(i);
				centroids.set(i, newCentroid);
			}
			
			count++;
		} while(count<iterationTimes);
		
		
		return vectors2classes;
	}
	
	public void printResults() {
		ArrayList<HashSet<Matrix>> out = new ArrayList<HashSet<Matrix>>();
		for(int i=0;i<k;i++) {
			out.add(new HashSet<Matrix>());
		}
		for(int i=0;i<vectors2classes.length;i++) {
			HashSet<Matrix> set = out.get(vectors2classes[i]);
			Matrix m = vectors.get(i);
			set.add(m);
		}
		for(int i=0;i<out.size();i++) {
			System.out.println("The "+i+" class:");
			Iterator<Matrix> it = out.get(i).iterator();
			while(it.hasNext()) {
				System.out.println(it.next());
			}
			System.out.println();
		}
	}
}
