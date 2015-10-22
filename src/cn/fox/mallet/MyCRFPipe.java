package cn.fox.mallet;

import java.util.ArrayList;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;

public class MyCRFPipe extends Pipe {

	private static final long serialVersionUID = -8701993172020021316L;

	public MyCRFPipe() {
		super (new Alphabet(), new LabelAlphabet());
	}
	
	/*
	 * "carrier" is made by LineGroupIterator, then we use "pipe" to fill information into "carrier".
	 * Several lines denotes a instance because this is a sequence tagging problem.
	 */
	public Instance pipe (Instance carrier)
    {
      Object inputData = carrier.getData();  // the string of several lines
      Alphabet features = getDataAlphabet(); // the name of features
      LabelAlphabet labels;
      LabelSequence target = null;
      /* the first dimension denotes a line, and the second denotes the tokens in the line.
       * A line contains features and tag(if train) splitted with a whitespace.
       */
      String [][] tokens = parseSentence((String)inputData);
      // each line is a FeatureVecr
      FeatureVector[] fvs = new FeatureVector[tokens.length];
      /*if (isTargetProcessing()) // if train, we take the tags from training data
      {*/
        labels = (LabelAlphabet)getTargetAlphabet();
        target = new LabelSequence (labels, tokens.length);
      /*}*/
      for (int l = 0; l < tokens.length; l++) { // for each line
        int nFeatures;
        /*if (isTargetProcessing()) // if train, the last token is a tag
        {*/
          nFeatures = tokens[l].length - 1;
          target.add(tokens[l][nFeatures]);
       /* }
        else nFeatures = tokens[l].length;*/
        // "featureIndices" and "featureValues" denote the non-zero features and their values
        // We use arraylist because we don't known how many before iteration
        ArrayList<Integer> featureIndices = new ArrayList<Integer>();
        ArrayList<Double> featureValues = new ArrayList<Double>();
        for (int f = 0; f < nFeatures; f++) { // for each token
        	int colonPosition = tokens[l][f].lastIndexOf(":");
        	String featureName = "";
        	if(colonPosition==-1) { // a binary feature
        		featureName = tokens[l][f];
        	} else { // a numerical feature
        		featureName = tokens[l][f].substring(0, colonPosition);
        	}
        	
        	// if "lookupIndex" find, return the index in the alphabet; else add it.
        	int featureIndex = features.lookupIndex(featureName); 
        	// If the data alphabet's growth is stopped, featureIndex
        	// will be -1.  Ignore these features.
        	if (featureIndex >= 0) {
        		featureIndices.add(featureIndex);
        		if(colonPosition==-1) { // a binary feature
        			featureValues.add(1.0);
            	} else { // a numerical feature
            	
            		Double featureValue = Double.parseDouble(tokens[l][f].substring(colonPosition+1));
            		featureValues.add(featureValue);
            	}
        	}
        }
        // copy the non-zero feature indices and their values 
        int[] featureIndicesArr = new int[featureIndices.size()];
        double[] featureValuesArr = new double[featureValues.size()];
        for (int index = 0; index < featureIndices.size(); index++) {
        	featureIndicesArr[index] = featureIndices.get(index);
        	featureValuesArr[index] = featureValues.get(index);
        }
        
       	//fvs[l] = new FeatureVector(features, featureIndicesArr);  // create a binary vector
        fvs[l] = new FeatureVector(features, featureIndicesArr, featureValuesArr);
      }
      carrier.setData(new FeatureVectorSequence(fvs));
      /*if (isTargetProcessing())*/
        carrier.setTarget(target);
     /* else
        carrier.setTarget(new LabelSequence(getTargetAlphabet()));*/
      return carrier;
    }
	
	private String[][] parseSentence(String sentence)
    {
      String[] lines = sentence.split("\n");
      String[][] tokens = new String[lines.length][];
      for (int i = 0; i < lines.length; i++)
        tokens[i] = lines[i].split(" ");
      return tokens;
    }
}