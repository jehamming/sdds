package com.sdds.monitor;

/**
 * Standard listener concept. Get notified when a message is received
 * @author jehamming
 *
 */
public interface MessageReciever {
	
	public void messageReceived(ReceivedMessage message);

}
