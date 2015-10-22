package cn.fox.ace2005;




/*
 * Entity denotes the "entity_mention" tags in the *.apf.xml files.
 */
public class Entity extends Mention {

	private static final long serialVersionUID = -6664129960315410909L;
	public static final String ORG="ORG";
	public static final String GPE="GPE";
	public static final String LOC="LOC";
	public static final String FAC="FAC";
	public static final String PER="PER";
	public static final String WEA="WEA";
	public static final String VEH="VEH";
	
	public Entity(String value, int start, int end, String type, String id) {
		super(value, start, end, id, type);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "entity: "+type+" "+value+" "+id+" "+start+" "+end;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Entity))
			return false;
		Entity o = (Entity)obj;

		if(value.equals(o.value) && type.equals(o.type))
			return true;
		else 
			return false;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode()+type.hashCode();
	}*/
}
