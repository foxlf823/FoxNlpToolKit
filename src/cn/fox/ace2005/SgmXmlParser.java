package cn.fox.ace2005;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import cn.fox.nlp.PlaceHolder;
import cn.fox.utils.DTDEntityResolver;

public class SgmXmlParser {
	DocumentBuilderFactory dbf ; 
	DocumentBuilder db ;
	Document d;
	
	public static final String TEXT_BEGIN = "*#BEGIN#*";
	public static final String TEXT_END = "*#END#*";
	public static final String CHINESE_PARA_DELIMETER = "。"; // a fullwidth stop
	public static final String ENGLISH_PARA_DELIMETER = ".";
	
	public SgmXmlParser(String dtdPath) {
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db  = dbf.newDocumentBuilder();
			//db.setEntityResolver(new IgnoreDTDEntityResolver());  // We forbid validation using dtd.
			db.setEntityResolver(new DTDEntityResolver(dtdPath));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * specialFormat=false Transfer the xml file of sgm format to the plain text with offset keeping.
	 * 
	 * specialFormat=true
	 * Transfer the xml file of sgm format to the special format.
	 * This special format has TEXT_BEGIN & TEXT_END labels which denotes the range of tag "TEXT" that contains information about entities and events.
	 * This may influence the offset, so you can handle it as you wish.
	 * We do this because some entities may appear out of the <TEXT></TEXT>, but they don't count entities in the gold data.
	 * 
	 * For some sgm files, there may be more than one subtag paragraphs in tag "TEXT", so we replace the new line at the last
	 * of a subtag paragraph with a full stop because some file like blog texts may be not normalized.
	 * This may influence the appearence of the a file, but it won't influence the offset.
	 * 
	 * As to newline and white space in tag "TEXT", we handle them according to different languages.
	 * For Chinese, we replace them with PlaceHolder.NL and PlaceHolder.WS.
	 * For English, we keep them without any change.
	 */
	public String transferXmlToRaw(String filePath, boolean specialFormat) {
		String strText = null;
		try {
			d = db.parse(filePath);
			
			NodeList doc= d.getElementsByTagName("DOC");
			StringBuilder builder = new StringBuilder();
			if(!specialFormat)
				builder  = getChildrenNodes(doc.item(0), builder, filePath);
			else 
				builder  = getChildrenNodesWithSpecialFormat(doc.item(0), builder, filePath);

			strText = new String(builder);
			strText = strText.replaceAll("&", "and  ");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strText;
	}
	// This function has been deprecated and I don't make sure its correctness.
	private StringBuilder getChildrenNodes(Node parent, StringBuilder builder, String filePath) throws Exception {
		NodeList children = parent.getChildNodes();
		for(int i=0;i<children.getLength();i++) {
			if(i==0 && parent.getNodeName().equals("DOC")) continue; // "\n" is the first child of "DOC" and it should be ignore.
			short type = children.item(i).getNodeType();
			Node node = children.item(i);
			if(type == Node.ELEMENT_NODE) {
				builder = getChildrenNodes(node, builder, filePath);
			} else if(type == Node.TEXT_NODE) {
				builder.append(((Text)node).getWholeText());
			} else {
				throw new Exception("We get a unexpected node with type("+type+") in file "+filePath);
			}
		}
		return builder;
	}
	
	private StringBuilder getChildrenNodesWithSpecialFormat(Node parent, StringBuilder builder, String filePath) throws Exception {
		NodeList children = parent.getChildNodes();
		for(int i=0;i<children.getLength();i++) {
			//if(i==0 && parent.getNodeName().equals("DOC")) continue; // "\n" is the first child of "DOC" and it should be ignore.
			short type = children.item(i).getNodeType();
			Node node = children.item(i);
			if(type == Node.ELEMENT_NODE) {
				builder = getChildrenNodesWithSpecialFormat(node, builder, filePath);
			} else if(type == Node.TEXT_NODE) {
				// add the text begin label
				if(parent.getNodeName().equals("TEXT") && i==0) builder.append(TEXT_BEGIN);
				// find the sons or grandsons of text tag, we will do some special things just for them.
				// For example, we will replace whitespaces and newlines with PlaceHolder.NL and PlaceHolder.WS for Chinese.
				boolean thisNodeIsSonOfText = false;
				Node tempParent = parent;
				while(tempParent != null) {
					if(tempParent.getNodeName().equals("TEXT"))
					{	
						thisNodeIsSonOfText = true;
						break;
					}
					tempParent = tempParent.getParentNode();
				}
				if(thisNodeIsSonOfText) {
					String text = ((Text)node).getWholeText();
					// if it's the last paragraph, we replace the last NL to PARA_DELIMETER.
					boolean isEnglish = filePath.indexOf("English")!=-1 ? true:false;
					boolean isChinese = filePath.indexOf("Chinese")!=-1 ? true:false;
					boolean isNLReplacedWithFullStop = false;
					// chinese
					if(isChinese && filePath.indexOf("Chinese\\bn") != -1) {
						if(parent.getNodeName().equals("TURN") && i==(children.getLength()-1)) {
							isNLReplacedWithFullStop = true;
						}
					} else if(isChinese && filePath.indexOf("Chinese\\nw") != -1) {
						if(parent.getNodeName().equals("TEXT") && i==(children.getLength()-1)) {
							isNLReplacedWithFullStop = true;
						}
					} else if (isChinese && filePath.indexOf("Chinese\\wl") != -1) {
						if(parent.getNodeName().equals("POST") && i==(children.getLength()-1)) {
							isNLReplacedWithFullStop = true;
						}
					} 
					// english
					else if(isEnglish && (filePath.indexOf("English\\bc") != -1 ||
							filePath.indexOf("English\\bn") != -1 || filePath.indexOf("English\\cts") != -1)) {
						if(parent.getNodeName().equals("TURN") && i==(children.getLength()-1)) {
							isNLReplacedWithFullStop = true;
						}
					} 
					else if(isEnglish && filePath.indexOf("English\\nw") != -1) {
						if(parent.getNodeName().equals("TEXT") && i==(children.getLength()-1)) {
							isNLReplacedWithFullStop = true;
						}
					} 
					else if(isEnglish && (filePath.indexOf("English\\un") != -1 || filePath.indexOf("English\\wl") != -1)) {
						if(parent.getNodeName().equals("POST") && i==(children.getLength()-1)) {
							isNLReplacedWithFullStop = true;
						}
					} 
					else 
						throw new Exception("We get a unexpected filePath");
					
					if(isNLReplacedWithFullStop) {
						String text1 = text.substring(0, text.length()-1);
						if(isEnglish)
							text1 += ENGLISH_PARA_DELIMETER;
						else if(isChinese)
							text1 += CHINESE_PARA_DELIMETER;
						text = text1;
					}
					if(isChinese) {
						text = text.replaceAll(" ", PlaceHolder.WS);
						text = text.replaceAll("\n", PlaceHolder.NL);
						builder.append(text);
					}
					else if(isEnglish) {
						builder.append(text);
					}
					
				} else { // For the nodes out of <TEXT>, we just keep them without any change.
					builder.append(((Text)node).getWholeText());
				}

				if(parent.getNodeName().equals("TEXT") && i==(children.getLength()-1)) builder.append(TEXT_END);
			} else {
				throw new Exception("We get a unexpected node with type("+type+") in file "+filePath);
			}
		}
		return builder;
	}
	
	
	
	/*public String getTextFromFile(String filePath) {
		String strText = "";
		try {
			d = db.parse(filePath);
			
			String tag = "";
			if(filePath.indexOf("Chinese\\bn") != -1) {
				tag = "TURN";
				NodeList texts = d.getElementsByTagName(tag); 
				for(int i=0;i<texts.getLength();i++) {
					Element text = (Element)texts.item(i); // there is only one text.
					strText += text.getFirstChild().getNodeValue().replaceAll("\n", ""); // value may contains line characters
					strText += "。"; // because some documents are not normalized, or even punctuaions losed.
				}
				
			}
			else if(filePath.indexOf("Chinese\\nw") != -1) {
				tag = "TEXT";
				NodeList texts = d.getElementsByTagName(tag); 
				Element text = (Element)texts.item(0); // there is only one text.
				strText = text.getFirstChild().getNodeValue().replaceAll("\n", ""); // value may contains line characters
			}
			else if(filePath.indexOf("Chinese\\wl") != -1) {
				tag = "POST";
				NodeList nodes = d.getElementsByTagName(tag);
				for(int i=0;i<nodes.getLength();i++)  {
					String temp = nodes.item(i).getChildNodes().item(4).getNodeValue(); 
					strText += temp.replaceAll("\n", ""); // value may contains line characters
					strText += "。";
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strText;
	}*/
}
