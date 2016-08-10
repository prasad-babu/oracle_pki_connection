package com.plabs.datasource.utils;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class XMLTest {
	
	@Test
	public void testNULLXml() throws Exception {
		File fXmlFile = new File("D:\\EDRIVE\\workspace - Copy\\plabs_datasource\\src\\test\\resources\\staff.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("staff");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			Element ele = doc.createElement("test");
			CDATASection cdata = doc.createCDATASection(null);			
			ele.appendChild(cdata);
			nNode.appendChild(ele);			
		}
		
		DOMImplementationLS ls = (DOMImplementationLS) doc.getImplementation();
		LSOutput lsOutput =  ls.createLSOutput();
		lsOutput.setEncoding("UTF-8");
		Writer stringWriter = new StringWriter();
		lsOutput.setCharacterStream(stringWriter);
		LSSerializer lsSerializer = ls.createLSSerializer();
		lsSerializer.write(doc, lsOutput);   	
		System.out.println(stringWriter.toString());
		
//		doc.getDomConfig().setParameter("error-handler", new DOMErrorHandler() {
//			
//			public boolean handleError(DOMError error) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
		DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        writer.flush();
        System.out.println(writer.toString());
	}

}
