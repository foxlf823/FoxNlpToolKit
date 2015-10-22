package cn.fox.machine_learning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import cn.fox.machine_learning.GroupAverageCluster;
import cn.fox.math.Matrix;
/*
 * find the initial centers
 * make sure normalize the input data
 */
public class Buckshot {
	private int k;
	private ArrayList<Matrix> data; // vectors of input data
	
	public Buckshot(int _k, ArrayList<Matrix> _data) {
		data = _data;
		k = _k;
	}
	
	public ArrayList<Matrix> doBuckshot() throws Exception {
		// randomly selecting sqrt(kn) data spends much time, so we select sqrt(n) instead
		int number = (int)Math.sqrt(data.size());
		if(number < k)
			number = k;
		ArrayList<Matrix> samples = new ArrayList<Matrix>();
		Random random = new Random();
		for(int i=0;i<number;i++) {
			samples.add(data.get(random.nextInt(data.size())));
		}
		
		// call GroupAverageCluster to cluster the sample data
		GroupAverageCluster gac = new GroupAverageCluster(k, samples);
		LinkedList<HashSet<Matrix>> clusters = gac.doClustering();
		// compute the center of each cluster as the inital center
		ArrayList<Matrix> centroids = new ArrayList<Matrix>();
		for(int i=0;i<clusters.size();i++) {
			HashSet<Matrix> set = clusters.get(i);
			centroids.add(getCentroid(set));
		}
		return centroids;
	}
	
	
	private Matrix getCentroid(HashSet<Matrix> set) throws Exception {
		Matrix temp = new Matrix(data.get(0).getRowNumber(), data.get(0).getColNumber());

		for(Matrix m:set) {
			temp.addThis(m);
		}
		temp.numMulThis(1.0/set.size());
        
        return temp;
	}
}
