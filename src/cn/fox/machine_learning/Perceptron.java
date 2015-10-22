package cn.fox.machine_learning;

import gnu.trove.TDoubleArrayList;
import gnu.trove.TIntArrayList;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectIntHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import cc.mallet.types.SparseVector;
import cn.fox.math.Matrix;

public abstract class Perceptron implements Serializable{
	private static final long serialVersionUID = -6988787945862648789L;
	public static final String EMPTY = "#N#"; // empty type
	protected SparseVector w1; 
	protected SparseVector w2; 
	public ArrayList<PerceptronFeatureFunction> featureFunctions1; 
	public ArrayList<PerceptronFeatureFunction> featureFunctions2; 
	// For the joint model, "alphabet1" is the type set of the first object, like entity.
	// And "alphabet2" is the type set of the second object, like relation.
	public ArrayList<String> alphabet1;
	public ArrayList<String> alphabet2;
	
	public TObjectIntHashMap<String> featureAlphabet; // store the feature name and its index in the feature vector
	public boolean isAlphabetStop; // when training finished, set this true
	
	public float convergeThreshold;
	public double weightMax;
	
	public double learningRate = 1;
	
	public Perceptron(ArrayList<String> alphabet1, ArrayList<String> alphabet2, float convergeThreshold, double weightMax) {
		if(alphabet1!=null) {
			this.alphabet1 = new ArrayList<String>();
			for(int i=0;i<alphabet1.size();i++) {
				this.alphabet1.add(alphabet1.get(i));
			}
		}
		if(alphabet2!=null) {
			this.alphabet2 = new ArrayList<String>();
			for(int i=0;i<alphabet2.size();i++) {
				this.alphabet2.add(alphabet2.get(i));
			}
		}
		
		this.featureAlphabet = new TObjectIntHashMap<>();
		
		this.convergeThreshold = convergeThreshold;
		this.weightMax = weightMax;
	}
	
	public void setFeatureFunction(ArrayList<PerceptronFeatureFunction> featureFunctions1, ArrayList<PerceptronFeatureFunction> featureFunctions2) {
		this.featureFunctions1 = featureFunctions1;
		this.featureFunctions2 = featureFunctions2;
	}
	
	public abstract void buildFeatureAlphabet(ArrayList<PerceptronInputData> inputDatas, ArrayList<PerceptronOutputData> outputDatas, Object other);
	
	public SparseVector getW1() {
		return w1;
	}
	public SparseVector getW2() {
		return w2;
	}
	
	public void normalizeWeight() {
		// norm
		double norm1 = w1.twoNorm();
		for(int j=0;j<w1.getIndices().length;j++) {
			w1.setValueAtLocation(j, w1.valueAtLocation(j)/norm1);
		}
		double norm2 = w2.twoNorm();
		for(int j=0;j<w2.getIndices().length;j++) {
			w2.setValueAtLocation(j, w2.valueAtLocation(j)/norm2);
		}
	}

	public void trainOnce(int beamSize, ArrayList<PerceptronInputData> input, ArrayList<PerceptronOutputData> output, Object other) {
		try {		

			long startTime = System.currentTimeMillis();
			for(int j=0;j<input.size();j++) {
				PerceptronInputData x = input.get(j);
				PerceptronOutputData y = output.get(j);
				// get the best predicted answer
				PerceptronStatus status = beamSearch(x, y, true, beamSize, other);
				
				if(!status.z.isIdenticalWith(x, y, status)) {
					// if the predicted answer are not identical to the gold answer, update the model.

					if(status.step==1) {
						SparseVector fxy  = f(x, status, y, other).sv1;
						SparseVector fxz = f(x, status, status.z, other).sv1;
						SparseVector temp = fxy.vectorAdd(fxz, -1);
						w1 = w1.vectorAdd(temp, learningRate);
					}
					else if(status.step==2) {
						FReturnType rtFxy = f(x, status, y, other);
						FReturnType rtFxz = f(x, status, status.z, other);
						
						SparseVector fxy1  = rtFxy.sv1;
						SparseVector fxz1 = rtFxz.sv1;
						SparseVector temp1 = fxy1.vectorAdd(fxz1, -1);
						w1 = w1.vectorAdd(temp1, learningRate);
						
						SparseVector fxy2  = rtFxy.sv2;
						SparseVector fxz2 = rtFxz.sv2;
						SparseVector temp = fxy2.vectorAdd(fxz2, -1);
						w2 = w2.vectorAdd(temp, learningRate);
					}
					else if(status.step==3) {
						FReturnType rtFxy = f(x, status, y, other);
						FReturnType rtFxz = f(x, status, status.z, other);
						
						SparseVector fxy1  = rtFxy.sv1;
						SparseVector fxz1 = rtFxz.sv1;
						SparseVector temp1 = fxy1.vectorAdd(fxz1, -1);
						w1 = w1.vectorAdd(temp1, learningRate);
						
						SparseVector fxy2  = rtFxy.sv2;
						SparseVector fxz2 = rtFxz.sv2;
						SparseVector temp = fxy2.vectorAdd(fxz2, -1);
						w2 = w2.vectorAdd(temp, learningRate);
					} else
						throw new Exception();
					
					
				}
			}
			// check weight
			if(weightMax!=0) {
				double values1[] = w1.getValues();
				for(int j=0;j<values1.length;j++) {
					if(values1[j]>weightMax)
						values1[j] = weightMax;
					else if(values1[j]<-weightMax)
						values1[j] = -weightMax;
				}
				double values2[] = w2.getValues();
				for(int j=0;j<values2.length;j++) {
					if(values2[j]>weightMax)
						values2[j] = weightMax;
					else if(values2[j]<-weightMax)
						values2[j] = -weightMax;
				}
			}
			
			
			long endTime = System.currentTimeMillis();
			System.out.println("train finished "+(endTime-startTime)+" ms");

		} catch(Exception e) {
			e.printStackTrace();
		}
		

		return;
	}
	
	/*
	 * "T" is the maximum of iterator times.
	 * "beamSize" is the size of beam.
	 * input: the training data
	 * output: the gold answers
	 * other: any information or tools which are used in the feature functions.
	 */
	public void trainPerceptron(int T, int beamSize, ArrayList<PerceptronInputData> input, ArrayList<PerceptronOutputData> output, Object other) {
		try {		
			for(int i=0;i<T;i++) {
				SparseVector old1 = (SparseVector)w1.cloneMatrix();
				SparseVector old2 = (SparseVector)w2.cloneMatrix();
				
				long startTime = System.currentTimeMillis();
				//Matrix w_copy = w.copy();
				for(int j=0;j<input.size();j++) {
					PerceptronInputData x = input.get(j);
					PerceptronOutputData y = output.get(j);
					// get the best predicted answer
					PerceptronStatus status = beamSearch(x, y, true, beamSize, other);
					
					if(!status.z.isIdenticalWith(x, y, status)) {
						// if the predicted answer are not identical to the gold answer, update the model.

						if(status.step==1) {
							SparseVector fxy  = f(x, status, y, other).sv1;
							SparseVector fxz = f(x, status, status.z, other).sv1;
							SparseVector temp = fxy.vectorAdd(fxz, -1);
							w1 = w1.vectorAdd(temp, learningRate);
						}
						else if(status.step==2) {
							FReturnType rtFxy = f(x, status, y, other);
							FReturnType rtFxz = f(x, status, status.z, other);
							
							SparseVector fxy1  = rtFxy.sv1;
							SparseVector fxz1 = rtFxz.sv1;
							SparseVector temp1 = fxy1.vectorAdd(fxz1, -1);
							w1 = w1.vectorAdd(temp1, learningRate);
							
							SparseVector fxy2  = rtFxy.sv2;
							SparseVector fxz2 = rtFxz.sv2;
							SparseVector temp = fxy2.vectorAdd(fxz2, -1);
							w2 = w2.vectorAdd(temp, learningRate);
						}
						else if(status.step==3) {
							FReturnType rtFxy = f(x, status, y, other);
							FReturnType rtFxz = f(x, status, status.z, other);
							
							SparseVector fxy1  = rtFxy.sv1;
							SparseVector fxz1 = rtFxz.sv1;
							SparseVector temp1 = fxy1.vectorAdd(fxz1, -1);
							w1 = w1.vectorAdd(temp1, learningRate);
							
							SparseVector fxy2  = rtFxy.sv2;
							SparseVector fxz2 = rtFxz.sv2;
							SparseVector temp = fxy2.vectorAdd(fxz2, -1);
							w2 = w2.vectorAdd(temp, learningRate);
						} else
							throw new Exception();
						
						
					}
				}
				// check weight
				if(weightMax!=0) {
					double values1[] = w1.getValues();
					for(int j=0;j<values1.length;j++) {
						if(values1[j]>weightMax)
							values1[j] = weightMax;
						else if(values1[j]<-weightMax)
							values1[j] = -weightMax;
					}
					double values2[] = w2.getValues();
					for(int j=0;j<values2.length;j++) {
						if(values2[j]>weightMax)
							values2[j] = weightMax;
						else if(values2[j]<-weightMax)
							values2[j] = -weightMax;
					}
				}
				
				// check convergence
				/*System.out.println("w1 "+w1.toString(true));
				System.out.println("w2 "+w2.toString(true));*/
				
				float dist1 = (float)old1.vectorAdd(w1, -1).twoNorm();
				float dist2 = (float)old2.vectorAdd(w2, -1).twoNorm();
				float dist = dist1+dist2;
				
				
				if(dist<convergeThreshold) {
					System.out.println("converged, quit training");
					normalizeWeight();
					/*System.out.println("w1 "+w1.toString(true));
					System.out.println("w2 "+w2.toString(true));*/
					return;
				} 
				else {
					long endTime = System.currentTimeMillis();
					System.out.println((i+1)+" train finished "+(dist1+dist2)+" "+(endTime-startTime)+" ms");
				}

			}
			System.out.println("achieve max training times, quit");
		} catch(Exception e) {
			e.printStackTrace();
		}
		// norm
		normalizeWeight();
		/*System.out.println("w1 "+w1.toString(true));
		System.out.println("w2 "+w2.toString(true));*/
		return;
	}
	
	// This method is different depending on various questions.
	public abstract PerceptronStatus beamSearch(PerceptronInputData x, PerceptronOutputData y, boolean isTrain, int beamSize, Object other)throws Exception;

	// Find the current best predicted answers and put them from "buf" into "beam".
	public void kBest(PerceptronInputData x, PerceptronStatus status, ArrayList<PerceptronOutputData> beam, ArrayList<PerceptronOutputData> buf, int beamSize, Object other)throws Exception {
		// compute all the scores in the buf
		TDoubleArrayList scores = new TDoubleArrayList();
		for(PerceptronOutputData y:buf) {
			FReturnType ret = f(x,status,y, other);
			if(status.step==1) {
				scores.add(w1.dotProduct(ret.sv1));
			} else if(status.step==2) {
				scores.add(w1.dotProduct(ret.sv1)+w2.dotProduct(ret.sv2));
			} else 
				throw new Exception();
			
		}
		
		// assign k best to the beam, and note that buf may be more or less than beamSize.
		int K = buf.size()>beamSize ? beamSize:buf.size();
		PerceptronOutputData[] temp = new PerceptronOutputData[K];
		Double[] tempScore = new Double[K];
		for(int i=0;i<buf.size();i++) {
			for(int j=0;j<K;j++) {
				if(temp[j]==null || scores.get(i)>tempScore[j]) {
					if(temp[j] != null) {
						for(int m=K-2;m>=j;m--) {
							temp[m+1] = temp[m];
							tempScore[m+1] = tempScore[m];
						}
					}
					
					temp[j] = buf.get(i);
					tempScore[j] = scores.get(i);
					break;
				}
			}
		}
		
		beam.clear();
		for(int i=0;i<K;i++) {
			beam.add(temp[i]);
		}
		
		return;
	}
	
	public class FReturnType {
		public SparseVector sv1;
		public SparseVector sv2;
		public FReturnType(SparseVector sv1, SparseVector sv2) {
			super();
			this.sv1 = sv1;
			this.sv2 = sv2;
		}
	}
	// Compute the feature vector "f" based on the current status.
	protected FReturnType f(PerceptronInputData x, PerceptronStatus status, PerceptronOutputData y, Object other) throws Exception {	
		
		if(y.isGold) {
			if(status.step==0) { // initialize the feature vectors of gold output
				TObjectDoubleHashMap<String> map1 = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions1.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions1.get(j);
					featureFunction.compute(x, status, y, other, map1);
				}
				y.featureVectors1.add(hashMapToSparseVector(map1));
				
				TObjectDoubleHashMap<String> map2 = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions2.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions2.get(j);
					featureFunction.compute(x, status, y, other, map2);
				}
				y.featureVectors2.add(hashMapToSparseVector(map2));
				return new FReturnType(y.featureVectors1.get(status.tokenIndex), y.featureVectors2.get(status.tokenIndex));
			} else if(status.step==1) {
				return new FReturnType(y.featureVectors1.get(status.tokenIndex), null);
			} else if(status.step==2) {
				return new FReturnType(y.featureVectors1.get(status.tokenIndex), y.featureVectors2.get(status.tokenIndex));
			} else if(status.step==3) {
				return new FReturnType(y.featureVectors1.get(status.tokenIndex), y.featureVectors2.get(status.tokenIndex));
			} else
				throw new Exception();
			
		} else {
			if(status.step==1) {
				TObjectDoubleHashMap<String> map = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions1.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions1.get(j);
					featureFunction.compute(x, status, y, other, map);
				}
				y.featureVector1 = hashMapToSparseVector(map);
				return new FReturnType(y.featureVector1, null);
			} else if(status.step==2) {
				TObjectDoubleHashMap<String> map1 = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions1.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions1.get(j);
					featureFunction.compute(x, status, y, other, map1);
				}
				y.featureVector1 = hashMapToSparseVector(map1);
				
				TObjectDoubleHashMap<String> map = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions2.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions2.get(j);
					featureFunction.compute(x, status, y, other, map);
				}
				y.featureVector2 = hashMapToSparseVector(map);
				return new FReturnType(y.featureVector1, y.featureVector2);
			} else if(status.step==3) {
				TObjectDoubleHashMap<String> map1 = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions1.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions1.get(j);
					featureFunction.compute(x, status, y, other, map1);
				}
				y.featureVector1 = hashMapToSparseVector(map1);
				
				TObjectDoubleHashMap<String> map2 = new TObjectDoubleHashMap<>();
				for(int j=0;j<featureFunctions2.size();j++) {
					PerceptronFeatureFunction featureFunction = featureFunctions2.get(j);
					featureFunction.compute(x, status, y, other, map2);
				}
				y.featureVector2 = hashMapToSparseVector(map2);
				return new FReturnType(y.featureVector1, y.featureVector2);
			} else
				throw new Exception();
			
		}

	}
	
	public SparseVector hashMapToSparseVector(TObjectDoubleHashMap<String> map) {
		TIntArrayList featureIndices = new TIntArrayList();
		TDoubleArrayList featureValues = new TDoubleArrayList();
		String[] keys = map.keys( new String[ map.size() ] );
		for(String featureName:keys) {
			featureIndices.add(this.featureAlphabet.get(featureName));
    		featureValues.add(map.get(featureName));
		}
		
        int[] featureIndicesArr = new int[featureIndices.size()];
        double[] featureValuesArr = new double[featureValues.size()];
        for (int index = 0; index < featureIndices.size(); index++) {
        	featureIndicesArr[index] = featureIndices.get(index);
        	featureValuesArr[index] = featureValues.get(index);
        }
		
        SparseVector fxy = new SparseVector(featureIndicesArr, featureValuesArr, false);
        return fxy;
	}
	
	// make sure each element of fxy is no less than that of fxz
	// if true, means they satisfy this condition.
	private boolean check(Matrix fxy, Matrix fxz) {
		try {
			for(int i=0;i<fxy.getColNumber();i++) {
				if(fxy.getElement(0, i)<fxz.getElement(0, i))
					return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	

}
