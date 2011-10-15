package com.sdds.impl;

import java.util.List;

import com.sdds.Consumer;
import com.sdds.Manager;
import com.sdds.util.Logger;

/**
 * The XML dispatcher is used mainly by the Monitor. In this case, the Monitor can be started 
 * without knowing the .class files of the produced objects.
 * @author jehamming
 *
 */
public class InternalXMLDispatcher extends Dispatcher {

	private Manager manager;

	/**
	 * Constructor
	 * @param manager
	 */
	public InternalXMLDispatcher(Manager manager) {
		super("Internal XML Dispatcher");
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * Deliver XML (String) to the XML Consumers
	 * @see com.uri.impl.Dispatcher#deliver(java.lang.Object)
	 */
	@Override
	public synchronized void deliver(Object o) {
		if (o instanceof String) {
			String xml = (String) o;
			List<Consumer<String>> listOfXMLConsumers = manager.getConsumerManager().getXmlConsumers();
			for (Consumer<String> consumer : listOfXMLConsumers) {
				consumer.consume(xml);
			}
			if (listOfXMLConsumers.size() > 1) {
				assert Logger.info(this, "Dispatched object to " + listOfXMLConsumers.size() + " consumers");
			}
		}
	}

}
