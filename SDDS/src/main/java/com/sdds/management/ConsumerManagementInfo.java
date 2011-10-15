package com.sdds.management;

import java.util.ArrayList;
import java.util.List;

import com.sdds.impl.Header;
import com.sdds.impl.Message;

/**
 * This class contains metadata for a consumer. Before an object is delivered to the consumer,
 * this class instance is checked if the object should be delivered at all. Reasons for not delivering:
 * - The consumer already consumed this object, so it was received using a quicker path
 * - More?
 * @author jehamming
 *
 */
public class ConsumerManagementInfo {

	//TODO Make filtering/checking already consumed items better/quicker
	
	private List<Header> receivedHeaders;
	
	public ConsumerManagementInfo() {
		receivedHeaders = new ArrayList<Header>();
	}
	
	
	/**
	 * Check if the given message has already been delivered
	 * @param m
	 * @return
	 */
	public boolean canReceive(Message m) {
		boolean alreadyReceived = find( m.getHeader() );		
		if ( !alreadyReceived ) {
			receivedHeaders.add(m.getHeader());
		}
		return !alreadyReceived;
	}

	
	/**
	 * Try to find a header in the list with the same<br>
	 * - Content Hash<br>
	 * - Manager Id<br>
	 * - Timestamp<br>
	 * 
	 * @param header
	 * @return
	 */
	private boolean find(Header header) {
		boolean found = false;
		for (Header h : receivedHeaders) {
			if (h.getContentHash() == header.getContentHash()
					&& h.getManagerId().equals(header.getManagerId())
					&& h.getTimestamp().equals(header.getTimestamp())) {
				found = true;
				break;
			}
		}
		return found;
	}
	

}
