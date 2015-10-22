package cn.fox.machine_learning;

import java.io.Serializable;
import java.util.ArrayList;

import cc.mallet.types.SparseVector;

/*
 * You have to implement this class depending on the specific question.
 * The two abstract functions are used from the inner methods.
 * You can decide how to implement them event just meaningless implement.
 */
public abstract class PerceptronOutputData implements Serializable{
	
	private static final long serialVersionUID = 8669519647030994555L;
	/*
	 * This is only used by gold, because every token corresponds to a feature vector.
	 */
	public ArrayList<SparseVector> featureVectors1;
	public ArrayList<SparseVector> featureVectors2;
	/*
	 * This is only used by predict, last token corresponds to a feature vector.
	 */
	//public TObjectDoubleHashMap<String> featureVector;
	public SparseVector featureVector1;
	public SparseVector featureVector2;
	/* 
	 * Whether the current output data is the same with "other"
	 */
	public abstract boolean isIdenticalWith(PerceptronInputData input, PerceptronOutputData other, PerceptronStatus status);
	public boolean isGold; // Indicate whether this object is gold or not
	public PerceptronOutputData(boolean isGold, int tokenNumber) {
		this.isGold = isGold;
		/*if(isGold) {
			featureVectors = new ArrayList<>();
			for(int i=0;i<tokenNumber;i++) {
				featureVectors.add(new TObjectDoubleHashMap<String>());
			}
		} else {
			featureVector = new TObjectDoubleHashMap<>();
		}*/
		featureVectors1 = new ArrayList<>();
		featureVectors2 = new ArrayList<>();
	}
}
