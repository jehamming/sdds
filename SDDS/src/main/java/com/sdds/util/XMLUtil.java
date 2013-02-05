package com.sdds.util;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;

public class XMLUtil {
	
    private static HashMap<String, XPathExpression> compiledExpressions = new HashMap<String, XPathExpression>();
    private static XStream xstream = new XStream();

    /**
     * Execute an xpath on the given XML
     * @param xml
     * @param expression
     * @return
     */
    public static synchronized String executeXPath(String xml, String expression) {
        String retVal = "";
        try {
            XPathExpression xpathExpression = compiledExpressions.get(expression);
            if (xpathExpression == null) {
                XPath xpath = XPathFactory.newInstance().newXPath();
                xpathExpression = xpath.compile(expression);
                compiledExpressions.put(expression, xpathExpression);
            }
            InputSource source = new InputSource(new StringReader(xml));
            retVal = xpathExpression.evaluate(source);
        } catch (XPathExpressionException ex) {
        	ex.printStackTrace();
        }
        return retVal;
    }
	
	/**
	 * Convert an object to XML
	 * @param o
	 * @return
	 */
	public static String toXML( Object o ) {
		return xstream.toXML(o);
	}
	
	/**
	 * Convert XML to an object instance
	 * @param xml
	 * @return
	 */
	public static Object fromXML( String xml ) {
		Object obj = null; 
		try {
			obj = xstream.fromXML(xml);
		} catch (Exception e) {
			Logger.info("XMLUtil.fromXML: Could not create Object from XML:" + e.getMessage());
		}
		return obj;
	}

}
