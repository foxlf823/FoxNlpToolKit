package cn.fox.ace2005;

/*
 * Value denotes the "value_mention" tags in the *.apf.xml files.
 */
public class Value extends Mention{

	private static final long serialVersionUID = 3429766338357936187L;
	public static final String NUMERIC="Numeric";    
	public static final String CONTACT_INFO="Contact-Info";
	public static final String CRIME="Crime";
	public static final String SENTENCE="Sentence";
	public static final String JOB_TITLE="Job-Title";
	
	public Value(String value, int start, int end, String id, String type) {
		super(value, start, end, id, type);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "value: "+type+" "+value+" "+id+" "+start+" "+end;
	}
}
