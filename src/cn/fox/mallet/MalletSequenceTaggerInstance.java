package cn.fox.mallet;

import gnu.trove.TObjectDoubleHashMap;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * Before training a mallet tagger, we need to transfer the plain text into the format which mallet needs.
 * So firstly, we need to preprocess the plain text and put all the information like features or labels into the "MalletSequenceTaggerInstance".
 * For tagging, each "MalletSequenceTaggerInstance" denotes a instance with a group of features and labels.
 * Secondly, we will write all the "MalletSequenceTaggerInstance" into a file with the format which mallet needs.
 * Thirdly, we load the data from files and train according to the mallet examples.
 */
import java.util.Iterator;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;
public class MalletSequenceTaggerInstance {
	public ArrayList<TObjectDoubleHashMap<String>> data;
	public ArrayList<String> target;

	
	public MalletSequenceTaggerInstance() {
		data = new ArrayList<TObjectDoubleHashMap<String>>();
		target = new ArrayList<String>();
	}
	
	public static void writeAllInstances2File(ArrayList<MalletSequenceTaggerInstance> instances, String filePath){
		try {
			OutputStreamWriter oswInstance = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"); 
			DecimalFormat decimalFormat = new DecimalFormat("#.##########");
			for(MalletSequenceTaggerInstance instance : instances) {
				for(int i=0;i<instance.data.size();i++) {
					TObjectDoubleHashMap<String> map = instance.data.get(i);
					String[] keys = map.keys(new String[map.size()]);
					for(String key:keys) {
						String featureName = key;
						double featureValue = map.get(featureName);
						oswInstance.write(featureName+":"+decimalFormat.format(featureValue)+" ");
					}
					String label = instance.target.get(i);
					oswInstance.write(label+"\n");
				}
				oswInstance.write("\n");
			}
			oswInstance.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
					
	}
	
	public FeatureVectorSequence toFeatureVectorSequence(Alphabet features) {
		FeatureVector[] fvs = new FeatureVector[data.size()];
		for(int i=0;i<fvs.length;i++) {
			FeatureVector fv = FeatureVectorMaker.make(features, data.get(i));
			fvs[i] = fv;
		}
		return new FeatureVectorSequence(fvs);
	}
	
	public LabelSequence toLabelSequence(Alphabet alphabet) {
	    LabelSequence ls = new LabelSequence ((LabelAlphabet)alphabet, this.target.size());
	    for(int i=0;i<ls.size();i++) {
	    	ls.add(this.target.get(i));
	    }
	    return ls;
	}
}
