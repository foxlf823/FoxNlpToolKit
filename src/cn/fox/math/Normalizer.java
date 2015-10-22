package cn.fox.math;


public class Normalizer {
	/*
	 * This function is to transfer a numeric value to 0~1.
	 * x[] = {-3,4,5}
	 * 0.04742587317756678 0.9820137900379085 0.9933071490757153
	 */
	public static double doSigmoid(double x) {
		return 1.0/(1+Math.exp(-x));
	}
	/*
	 * The Softmax function in logistic regression is to transfer
	 * each element in "x[]" to 0~1 and the sum of all elements to 1.
	 * x = {-3,4,5}
	 * 2.451827026375595E-4 0.2688754815854525 0.7308793357119101 
	 */
	public static void doSoftmax(double x[]) {                                     
        double max = Double.MIN_VALUE;                                                                
	    int n = x.length;
	    double sum = 0.0;  
	    
	    for(int i=0; i<n; i++) if(max < x[i]) max = x[i];                            
	    for(int i=0; i<n; i++) {                                                     
	    	x[i] = Math.exp(x[i] - max);                                                        
	    	sum += x[i];                                                                
	    } 
	    
	    for(int i=0; i<n; i++) x[i] /= sum;    
	    
	}  
	
	/*
	 * This function is to transfer each element in "x[]" to 0~1.
	 * x = {-3,4,5}
	 * 0.0 0.875 1.0
	 */
	public static void doMinMax(double x[]) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		int n = x.length;
		
		for(int i=0;i<n;i++) {
			if(min > x[i]) min = x[i];
			if(max < x[i]) max = x[i];
		}
		
		for(int i=0;i<n;i++) {
			x[i] = (x[i]-min)/(max-min);
		}
	    
	}
	
	/*
	 * It makes the norm of a vector be 1
	 */
	public static void doVectorNormalizing(Matrix m) throws Exception{
		double sum = 0;
		for(int i=0;i<m.getColNumber();i++) {
			sum += Math.pow(m.getElement(0, i), 2);
		}
		double norm = Math.sqrt(sum);
		for(int i=0;i<m.getColNumber();i++) {
			m.setElement(0, i, m.getElement(0, i)/norm);
		}
	}
}
