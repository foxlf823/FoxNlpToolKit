package cn.fox.ace2005;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Event denote the "event_mention" tags in the *.apf.xml files.
 */
public class Event implements Serializable{

	private static final long serialVersionUID = 2321135204518540190L;
	public Trigger trigger;
	public String type;  // type and subtype are in the parent tag "event".
	public String subType;
	public int start;
	public int end;
	public String id;
	public ArrayList<Argument> arguments;
	public Event(Trigger trigger, String type, String subType, int start,
			int end, String id, ArrayList<Argument> arguments) {
		super();
		this.trigger = trigger;
		this.type = type;
		this.subType = subType;
		this.start = start;
		this.end = end;
		this.id = id;
		this.arguments = arguments;
	}
	
	public static final String BE_BORN = "Be-Born";
	public static final String MARRY = "Marry";
	public static final String DIVORCE = "Divorce";
	public static final String INJURE="Injure";
	public static final String DIE="Die";
	public static final String TRANSPORT="Transport";
	public static final String TRANSFER_OWNERSHIP="Transfer-Ownership";
	public static final String TRANSFER_MONEY="Transfer-Money";
	public static final String START_ORG="Start-Org";
	public static final String MERGE_ORG="Merge-Org";
	public static final String DECLARE_BANKRUPTCY="Declare-Bankruptcy";
	public static final String END_ORG="End-Org";
	public static final String ATTACK="Attack";
	public static final String DEMONSTRATE="Demonstrate";
	public static final String MEET="Meet";
	public static final String PHONE_WRITE="Phone-Write";
	public static final String START_POSITION="Start-Position";
	public static final String END_POSITION="End-Position";
	public static final String NOMINATE="Nominate";
	public static final String ELECT="Elect";
	public static final String ARREST_JAIL="Arrest-Jail";
	public static final String RELEASE_PAROLE="Release-Parole";
	public static final String TRIAL_HEARING = "Trial-Hearing";
	public static final String CHARGE_INDICT="Charge-Indict";
	public static final String SUE="Sue";
	public static final String CONVICT="Convict";
	public static final String SENTENCE="Sentence";
	public static final String FINE="Fine";
	public static final String EXECUTE="Execute";
	public static final String EXTRADITE="Extradite";
	public static final String ACQUIT="Acquit";
	public static final String PARDON="Pardon";
	public static final String APPEAL="Appeal";

	
	// Give a argument type, and judge the argument's type whether belongs to this kind of event.
	public static boolean isEventAndArgumentTypeMatch(String eventType, String argumentType) {
		if(eventType == null || argumentType == null)
			return false;
		boolean ret = false;
		String type = argumentType;
					
		switch(eventType) {
		case BE_BORN:
		case MARRY:
		case DIVORCE:
			if(type.equals(Entity.PER) || type.equals(Entity.GPE)|| type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					|| type.equals(Time.TIME))
				ret=true;
			break;

		case INJURE:
		case DIE:
		case ATTACK:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) || type.equals(Entity.WEA) ||type.equals(Entity.VEH) || type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME))
				ret=true;
			break;
		
		case TRANSPORT:
		case TRANSFER_OWNERSHIP:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) || type.equals(Entity.WEA) ||type.equals(Entity.VEH) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) 
					|| type.equals(Value.NUMERIC))
				ret=true;
			break;
		
		case TRANSFER_MONEY:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) 
					|| type.equals(Value.NUMERIC))
				ret=true;
			break;
			
		case START_ORG:
		case DECLARE_BANKRUPTCY:
		case DEMONSTRATE:
		case MEET:
		case PHONE_WRITE:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) )
				ret=true;
			break;
			
		case MERGE_ORG:
		case END_ORG:
			if(type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) )
				ret=true;
			break;
			
		case START_POSITION:
		case END_POSITION:
		case NOMINATE:
		case ELECT:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) 
					|| type.equals(Value.JOB_TITLE))
				ret=true;
			break;
			
		case ARREST_JAIL:
		case RELEASE_PAROLE:
		case TRIAL_HEARING:
		case CHARGE_INDICT:
		case SUE:
		case CONVICT:
		case EXECUTE:
		case EXTRADITE:
		case ACQUIT:
		case PARDON:
		case APPEAL:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) 
					|| type.equals(Value.CRIME))
				ret=true;
			break;

		case SENTENCE:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) 
					|| type.equals(Value.CRIME) || type.equals(Value.SENTENCE))
				ret=true;
			break;
			
		case FINE:
			if(type.equals(Entity.PER) || type.equals(Entity.ORG) || type.equals(Entity.GPE) ||type.equals(Entity.LOC)|| type.equals(Entity.FAC)
					||type.equals(Time.TIME) 
					|| type.equals(Value.CRIME) || type.equals(Value.NUMERIC))
				ret=true;
			break;
		
		}
		return ret;
	}
	
	@Override
	public String toString() {
		/*String ret = "event "+start+" "+end+" "+type+" "+subType+" "+id+"\n";
		ret += trigger.toString();
		for(Argument a:arguments) {
			ret +="\n"+a;
		}
		ret += "\n\n";*/
		String ret = "event: "+type+" "+subType+" "+id+" "+start+" "+end;
		return ret;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Event))
			return false;
		Event o = (Event)obj;

		if(!this.trigger.value.equals(o.trigger.value))
			return false;
		if(this.arguments.size()!= o.arguments.size())
			return false;
		for(int i=0;i<this.arguments.size();i++) {
			if(!this.arguments.get(i).value.equals(o.arguments.get(i).value))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash=trigger.value.hashCode();
	    for(int i = 0; i < arguments.size(); i++)  
	    	hash += (i+1)*(int)(arguments.get(i).value.hashCode());  
	    return hash;  
	}*/
}
