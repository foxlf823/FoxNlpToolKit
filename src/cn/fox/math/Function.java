package cn.fox.math;

public class Function {
	public static double exp(double x) {
		if(x>50) x=50;
		else if(x<-50) x=-50;
		return Math.exp(x);
	}
	
	public static double tanh(double x) {
		return (exp(x)-exp(-x))/(exp(x)+exp(-x));
	}
	
	public static double sigmoid(double x) {
		return 1.0/(1+exp(-x));
	}
	
	public static double deriTanh(double x) {
		return 1-x*x;
	}
	
	public static double deriSigmoid(double x) {
		return x*(1-x);
	}
	
	public static double relu(double x) {
		return x>0 ? x : 0;
	}
	
	public static double deriRelu(double x) {
		return x>0 ? 1:0;
	}
}
