package cn.fox.ace2005;


import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import cn.fox.utils.DTDEntityResolver;

public class ApfXmlParser {
	DocumentBuilderFactory dbf ; 
	DocumentBuilder db ;
	Document d;
	public ApfXmlParser(String dtdPath) {
		super();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db  = dbf.newDocumentBuilder();
			//db.setEntityResolver(new IgnoreDTDEntityResolver());  // We forbid validation using dtd.
			db.setEntityResolver(new DTDEntityResolver(dtdPath));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Event> getEventsFromFile(String filePath) {
		ArrayList<Event> events = new ArrayList<Event>();
		try {
			d = db.parse(filePath);
			
			
			NodeList eventMentions = d.getElementsByTagName("event_mention"); 
			for(int i = 0; i < eventMentions.getLength(); i++) {
				Element eventMention = (Element)eventMentions.item(i); 
				// trigger
				NodeList anchors = eventMention.getElementsByTagName("anchor");
				Element anchor = (Element)anchors.item(0); // there is only one anchor. 
				NodeList charseqs = anchor.getElementsByTagName("charseq");
				Element charseq = (Element)charseqs.item(0); // there is only one charseq. 
				int start = Integer.parseInt(charseq.getAttribute("START"));
				int end = Integer.parseInt(charseq.getAttribute("END"));
				String value = charseq.getFirstChild().getNodeValue(); // value may contains line characters
				//value = value.replaceAll("\n", "");
				Trigger trigger = new Trigger(value, start, end);
				//System.out.println(trigger);
				// argument
				ArrayList<Argument> arguments = new ArrayList<Argument>();
				NodeList event_mention_arguments = eventMention.getElementsByTagName("event_mention_argument");
				for(int j=0;j<event_mention_arguments.getLength();j++) {
					Element event_mention_argument = (Element)event_mention_arguments.item(j);
					String refID = event_mention_argument.getAttribute("REFID");
					String role = event_mention_argument.getAttribute("ROLE");
					NodeList extents = event_mention_argument.getElementsByTagName("extent");
					NodeList charseqsArgument = ((Element)extents.item(0)).getElementsByTagName("charseq"); //there is only one extent
					Element charseqArgument=(Element)charseqsArgument.item(0); // there is only one charseq
					String valueArgument = charseqArgument.getFirstChild().getNodeValue();
					//valueArgument = valueArgument.replaceAll("\n", ""); // value may contains line characters
					int startArgument = Integer.parseInt(charseqArgument.getAttribute("START"));
					int endArgument = Integer.parseInt(charseqArgument.getAttribute("END"));
					// mention
					Mention mentionObject = null;
					Element mention = d.getElementById(refID);
					if(mention.getTagName().equals("entity_mention")) {
						NodeList heads = mention.getElementsByTagName("head");
						Element head = (Element)heads.item(0);
						NodeList charseqsEntity = head.getElementsByTagName("charseq");
						Element charseqEntity = (Element)charseqsEntity.item(0);
						int startEntity=Integer.parseInt(charseqEntity.getAttribute("START"));
						int endEntity=Integer.parseInt(charseqEntity.getAttribute("END"));
						String valueEntity = charseqEntity.getFirstChild().getNodeValue();
						//valueEntity = valueEntity.replaceAll("\n", "");
						String idEntity = mention.getAttribute("ID");
						Element entityParent = (Element)mention.getParentNode();
						String typeEntity = entityParent.getAttribute("TYPE");
						//String subTypeEntity = entityParent.getAttribute("SUBTYPE");
						mentionObject = new Entity(valueEntity, startEntity, endEntity, typeEntity, idEntity);
					} else if(mention.getTagName().equals("timex2_mention")) {
						NodeList heads = mention.getElementsByTagName("extent");
						Element head = (Element)heads.item(0);
						NodeList charseqsEntity = head.getElementsByTagName("charseq");
						Element charseqEntity = (Element)charseqsEntity.item(0);
						int startEntity=Integer.parseInt(charseqEntity.getAttribute("START"));
						int endEntity=Integer.parseInt(charseqEntity.getAttribute("END"));
						String valueEntity = charseqEntity.getFirstChild().getNodeValue();
						//valueEntity = valueEntity.replaceAll("\n", "");
						String idEntity = mention.getAttribute("ID");
						mentionObject = new Time(valueEntity, startEntity, endEntity, idEntity);
					} else if(mention.getTagName().equals("value_mention")) {
						NodeList heads = mention.getElementsByTagName("extent");
						Element head = (Element)heads.item(0);
						NodeList charseqsEntity = head.getElementsByTagName("charseq");
						Element charseqEntity = (Element)charseqsEntity.item(0);
						int startEntity=Integer.parseInt(charseqEntity.getAttribute("START"));
						int endEntity=Integer.parseInt(charseqEntity.getAttribute("END"));
						String valueEntity = charseqEntity.getFirstChild().getNodeValue();
						//valueEntity = valueEntity.replaceAll("\n", "");
						String idEntity = mention.getAttribute("ID");
						Element entityParent = (Element)mention.getParentNode();
						String typeEntity = entityParent.getAttribute("TYPE");
						mentionObject = new Value(valueEntity, startEntity, endEntity, typeEntity, idEntity);
					}

					Argument argument = new Argument(valueArgument, startArgument, endArgument, refID, role, mentionObject);
					arguments.add(argument);
					//System.out.println(argument);
				}
				// event
				Element event = (Element)eventMention.getParentNode();
				String type = event.getAttribute("TYPE");
				String subType = event.getAttribute("SUBTYPE");
				NodeList extentsEvent = eventMention.getElementsByTagName("extent");
				NodeList charseqsEvent = ((Element)extentsEvent.item(0)).getElementsByTagName("charseq");
				Element charseqEvent = (Element)charseqsEvent.item(0);
				int startEvent = Integer.parseInt(charseqEvent.getAttribute("START"));
				int endEvent = Integer.parseInt(charseqEvent.getAttribute("END"));
				String id = eventMention.getAttribute("ID");
				Event eventInstance = new Event(trigger, type, subType, startEvent, endEvent, id, arguments);
				//System.out.println(eventInstance);
				events.add(eventInstance);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return events;
	}
	
	public ArrayList<Entity> getEntitiesFromFile(String filePath) {
		ArrayList<Entity> entities = new ArrayList<>();
		try {
			d = db.parse(filePath);
			
			NodeList entityMentions = d.getElementsByTagName("entity_mention"); 
			for(int i = 0; i < entityMentions.getLength(); i++) {
				Element entityMention = (Element)entityMentions.item(i);
				NodeList heads = entityMention.getElementsByTagName("head");
				Element head = (Element)heads.item(0);
				NodeList charseqs = head.getElementsByTagName("charseq");
				Element charseq = (Element)charseqs.item(0);
				int start=Integer.parseInt(charseq.getAttribute("START"));
				int end=Integer.parseInt(charseq.getAttribute("END"));
				String value = charseq.getFirstChild().getNodeValue();
				//value = value.replaceAll("\n", "");
				String id = entityMention.getAttribute("ID");
				Element entityParent = (Element)entityMention.getParentNode();
				String type = entityParent.getAttribute("TYPE");
				//String subType = entityParent.getAttribute("SUBTYPE");
				Entity entity = new Entity(value, start, end, type, id);
				entities.add(entity);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return entities;
	}
	
}




