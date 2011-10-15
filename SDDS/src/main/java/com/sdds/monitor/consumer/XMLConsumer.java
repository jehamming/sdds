/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdds.monitor.consumer;

import java.util.ArrayList;
import java.util.List;

import com.sdds.Consumer;
import com.sdds.impl.Message;
import com.sdds.monitor.MessageReciever;
import com.sdds.monitor.ReceivedMessage;
import com.sdds.util.XMLUtil;

/**
 * 
 * @author A121916
 */
public class XMLConsumer extends Consumer<String> {

	private MessageReciever receiver;
	private List<String> xpathQueries;
    private String msgClassName = Message.class.getName(); 

	public XMLConsumer(MessageReciever receiver) {
		this.receiver = receiver;
		this.xpathQueries = new ArrayList<String>();
	}

	@Override
	public void consume(String xml) {
		boolean shouldProcess = true;
		// Check for special xpath queries
		if (xpathQueries.size() > 0) {
			shouldProcess = checkQueries(xml);
		}
		if (shouldProcess) {
			String name = XMLUtil.executeXPath(xml,
					"/" + msgClassName + "/content/@class");
			ReceivedMessage m = new ReceivedMessage();
			m.setName(name);
			m.setXml(xml);
			receiver.messageReceived(m);
		}

	}

	public boolean checkQueries(String xml) {
		boolean retval = true;
		if (getXPathQueries().size() == 0) {
			return retval;
		}
		for (String query : getXPathQueries()) {
			String result = XMLUtil.executeXPath(xml, query);
			if (result.equals("false") || result.equals("")) {
				retval = false;
				break;
			}
		}
		return retval;
	}

	public List<String> getXPathQueries() {
		return xpathQueries;
	}

	public void addXPathQuery(String q) {
		xpathQueries.add(q);
	}

}
