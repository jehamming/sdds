package com.sdds.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sdds.Consumer;
import com.sdds.monitor.consumer.XMLConsumer;


/**
 * A ConsumerManager manages the consumers, xml consumers and for each consumer management information.
 * @author jehamming
 *
 */
public class ConsumerManager {
    
    @SuppressWarnings("rawtypes")
	private List<Consumer> consumers = null;
    private List<Consumer<String>> xmlConsumers;
    @SuppressWarnings("rawtypes")
    private Map<Consumer, ConsumerManagementInfo> consumerManagement;
    
    /**
     * Constructor
     */
    @SuppressWarnings("rawtypes")
	public ConsumerManager() {
        consumers = new ArrayList<Consumer>();
        xmlConsumers = new ArrayList<Consumer<String>>();
        consumerManagement = new HashMap<Consumer, ConsumerManagementInfo>();
    }
        
    /**
     * Obvious: add a consumer for an object. This consumer can be a standard (Object instance) consumer, 
     * or a XML consumer.
     * @param consumer
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addConsumer( Consumer consumer ) {
    	if ( consumer instanceof XMLConsumer ) {
    		addXMLConsumer(consumer);
    	} else {
    		addStandardConsumer(consumer);
    	}
    }

	/**
	 * Add a standard (Object instance) consumer
	 * @param consumer
	 */
	@SuppressWarnings("rawtypes")
	private void addStandardConsumer(Consumer consumer) {
		if (!consumers.contains(consumer)) {
			// Register a consumer management class
			ConsumerManagementInfo info = new ConsumerManagementInfo();
			consumerManagement.put(consumer, info);
			// Add the consumer to the list
			consumers.add(consumer);
		}
	}

	/**
	 * Obvious: remove a consumer for an object. This consumer can be a standard (Object instance) consumer, 
     * or a XML consumer.
	 * @param consumer
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeConsumer( Consumer consumer ) {
    	if ( consumer instanceof XMLConsumer ) {
    		removeXMLConsumer(consumer);
    	} else {
    		removeStandardConsumer(consumer);
    	}
    }
    
    /**
     * Remove a standard (Object instance) consumer
     * @param consumer
     */
    @SuppressWarnings("rawtypes")
	private void removeStandardConsumer(Consumer consumer) {
        if ( consumers.contains( consumer )) {
            consumers.remove( consumer );
            // Remove also from management
            consumerManagement.remove(consumer);
        }
	}

	/**
	 * Find all the consumers that are interested in instance of Class clazz
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Consumer> getConsumersFor(Class<? extends Object> clazz) {
        List<Consumer> list = new ArrayList<Consumer>();
        for ( Consumer consumer: consumers) {
          if ( consumer.getConsumedClass().equals( clazz) ) {
              list.add( consumer );
          }
        }
        return list;
    }
    
    
    /**
     * Get XML consumers
     * @return
     */
    public List<Consumer<String>> getXmlConsumers() {
		return xmlConsumers;
	}
    
    /**
     * Add a XML consumer
     * @param consumer
     */
    private void addXMLConsumer( Consumer<String> consumer) {
    	if ( !xmlConsumers.contains(consumer)) {
    		xmlConsumers.add(consumer);
    	}
    }
    
    /**
     * Remove a XML consumer
     * @param consumer
     */
    private void removeXMLConsumer( Consumer<String> consumer ) {
    	if ( xmlConsumers.contains(consumer)) {
    		xmlConsumers.remove(consumer);
    	}
    }

	/**
	 * Get the ManagementInfo (MetaData) for a consumer
	 * @param consumer
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ConsumerManagementInfo getManagementInfo(Consumer consumer) {
		return consumerManagement.get(consumer);
	}
    

}
