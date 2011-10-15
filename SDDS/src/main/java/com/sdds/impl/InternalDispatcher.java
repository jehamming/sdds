package com.sdds.impl;

import java.util.List;

import com.sdds.Consumer;
import com.sdds.Manager;
import com.sdds.management.ConsumerManagementInfo;
import com.sdds.util.Logger;

/**
 * This dispatcher is used to dispatch objects internally to Consumers inside the same JVM
 * @author jehamming
 *
 */
public class InternalDispatcher extends Dispatcher {

	private Manager manager;
	
    /**
     * Constructor
     * @param manager
     */
    public InternalDispatcher(Manager manager) {
        super("Internal Dispatcher");
        this.manager = manager;
    }
    
	/* (non-Javadoc)
	 * An internal dispatcher dispatches Message isntances
	 * @see com.uri.impl.Dispatcher#deliver(java.lang.Object)
	 */
	@Override
    public void deliver(Object o) {
    	if ( o instanceof Message ) {
    		deliver((Message)o);
    	}
    }

    /**
     * Deliver a message. Get a list of all consumers that are interested in the content of the message 
     * and let them consume it.
     * @param m
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void deliver(Message m) {
        List<Consumer> listOfSpecificConsumers =
            manager.getConsumerManager().getConsumersFor(m.getContent().getClass());
        for (Consumer consumer : listOfSpecificConsumers) {
        	ConsumerManagementInfo info = manager.getConsumerManager().getManagementInfo(consumer);
        	if ( info.canReceive(m) ) {
        		consumer.consume(m.getContent());
        	}
        }
        if ( listOfSpecificConsumers.size() > 1 ) {
        	assert Logger.info(this, "Dispatched object to " + listOfSpecificConsumers.size() + " consumers");
        }

    }

}
