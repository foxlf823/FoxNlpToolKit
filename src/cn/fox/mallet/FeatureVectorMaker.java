package cn.fox.mallet;

import gnu.trove.TObjectDoubleHashMap;

import java.util.ArrayList;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;

public class FeatureVectorMaker {
	public static FeatureVector make(Alphabet features, TObjectDoubleHashMap<String> map) {
		ArrayList<Integer> featureIndices = new ArrayList<Integer>();
		ArrayList<Double> featureValues = new ArrayList<Double>();
		String[] keys = map.keys(new String[map.size()]);
		for(String featureName:keys) {
			int featureIndex = features.lookupIndex(featureName);
        	if (featureIndex >= 0) {
        		featureIndices.add(featureIndex);
        		featureValues.add(map.get(featureName));
        	}
		}
        int[] featureIndicesArr = new int[featureIndices.size()];
        double[] featureValuesArr = new double[featureValues.size()];
        for (int index = 0; index < featureIndices.size(); index++) {
        	featureIndicesArr[index] = featureIndices.get(index);
        	featureValuesArr[index] = featureValues.get(index);
        }
        return new FeatureVector(features, featureIndicesArr, featureValuesArr);
	}
}
