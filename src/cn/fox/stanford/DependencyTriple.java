package cn.fox.stanford;

public class DependencyTriple {
	String relation;
	String governor;
	String governorTokenIdx;
	String dependent;
	String dependentTokenIdx;
	
	public DependencyTriple(String relation, String governor,
			String governorTokenIdx, String dependent, String dependentTokenIdx) {
		super();
		this.relation = relation;
		this.governor = governor;
		this.governorTokenIdx = governorTokenIdx;
		this.dependent = dependent;
		this.dependentTokenIdx = dependentTokenIdx;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return relation+" "+governor+" "+dependent;
	}
	
}
