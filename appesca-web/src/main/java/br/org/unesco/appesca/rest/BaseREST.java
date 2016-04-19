package br.org.unesco.appesca.rest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @author yesus
 *
 */
public class BaseREST {

	public String getXML(Object objectWriter)  {
		try {
			XStream xStream = new XStream(new DomDriver());
			String xml = xStream.toXML(objectWriter);
			
			return xml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
