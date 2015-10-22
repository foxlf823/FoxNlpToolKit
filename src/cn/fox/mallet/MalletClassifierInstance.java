package cn.fox.mallet;

import gnu.trove.TObjectDoubleHashMap;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Before training a mallet classifier , we need to transfer the plain text into the format which mallet needs.
 * So firstly, we need to preprocess the plain text and put all the information like features or labels into the "MalletClassifierInstance".
 * For classification, each "MalletClassifierInstance" denotes a instance.
 * Secondly, we will write all the "MalletClassifierInstance" into a file with the format which mallet needs.
 * Thirdly, we load the data from files and train according to the mallet examples.
 */
public class MalletClassifierInstance {
	public String target;
	public TObjectDoubleHashMap<String> data;
	public String name;
	
	public MalletClassifierInstance() {
		data = new TObjectDoubleHashMap<String>();
	}
	
	public static void writeAllInstances2File(ArrayList<MalletClassifierInstance> instances, String filePath) {
		try {
			OutputStreamWriter tempOsw = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
			DecimalFormat decimalFormat = new DecimalFormat("#.##########");
			for(int j=0;j<instances.size();j++) {
				MalletClassifierInstance instance = instances.get(j);
				tempOsw.write(instance.name+" ");
				tempOsw.write(instance.target+" ");
				String[] keys = instance.data.keys(new String[instance.data.size()]);
				for(String key:keys) {
					double value = instance.data.get(key);
					tempOsw.write(key+":"+decimalFormat.format(value)+" ");
				}
				tempOsw.write("\n");
			}
			tempOsw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
