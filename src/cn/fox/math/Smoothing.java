package cn.fox.math;

public class Smoothing {
	/*
	 * n is the total number of samples.
	 */
	public static double doLaplaceSmoothing(double numerator, double denominator, int n) {
		return (1+numerator)/(n+denominator);
	}
}
