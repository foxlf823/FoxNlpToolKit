package cn.fox.machine_learning;

/*
 * You can extend this class, if you want to record some special status information.
 */
public class PerceptronStatus {
	// This is used by the inner methods and it may be null.
	public PerceptronOutputData z;
	// It's the token index of the input data and it also denotes the current token that the perceptron mentions.
	public int tokenIndex;
	// "step" means, eg. entity is step 1 and relation is step 2
	// this is used in PerceptronOutputData isIdenticalWith
	public int step;
	
	public PerceptronStatus(PerceptronOutputData z, int tokenIndex, int step) {
		super();
		this.z = z;
		this.tokenIndex = tokenIndex;
		this.step = step;
	}
}
