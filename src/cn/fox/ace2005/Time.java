package cn.fox.ace2005;

/*
 * Time denotes the "timex2_mention" tags in the *.apf.xml files.
 */
public class Time extends Mention {

	private static final long serialVersionUID = 798157965654436088L;
	public static final String TIME="TIME";
	
	public Time(String value, int start, int end, String id) {
		super(value, start, end, id, TIME);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "time: "+type+" "+value+" "+id+" "+start+" "+end;
	}
}
