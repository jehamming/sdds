package com.sdds.management;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sdds.Manager;
import com.sdds.impl.Dispatcher;
import com.sdds.impl.ExternalDispatcher;
import com.sdds.impl.ExternalReceiver;
import com.sdds.impl.InternalDispatcher;
import com.sdds.impl.InternalXMLDispatcher;
import com.sdds.impl.Message;

/**
 * The DispatchManager managers all Internal/External Dispatchers/Receivers
 * @author jehamming
 *
 */
public class DispatchManager {
    
    private InternalDispatcher internalDispatcher = null;
    private InternalXMLDispatcher internalXMLDispatcher = null;
    private List<ExternalDispatcher> externalDispatchers = null;
    private List<ExternalReceiver> externalReceivers = null;
    private Manager manager;
    
	/**
	 * Constructor
	 * @param manager
	 */
	public DispatchManager(Manager manager) {
		this.manager = manager;
		this.externalDispatchers = new ArrayList<ExternalDispatcher>();
		this.externalReceivers = new ArrayList<ExternalReceiver>();
		this.internalDispatcher = new InternalDispatcher(manager);
		this.internalXMLDispatcher = new InternalXMLDispatcher(manager);
	}
    
    /**
     * Dispatch a message. If external is true, the message is also send to all connected JVMs
     * @param m
     * @param external
     */
    public void dispatch(Message m, boolean external) {
        // First dispatch internal
        internalDispatcher.dispatch(m);
        if ( external ) dispatchToExternalConnections( m );
    }
    
    /**
     * For management use like the SDDS Tool: dispatch XML to XML consumers
     * @param xml
     */
    public void dispatch( String xml ) {
    	internalXMLDispatcher.dispatch(xml);
    }
    
    /**
     * Relay a message: Add this managers uuid to the Message path and send it to all connected JVMs
     * @param m
     */
    public void relayMessage( Message m) {
    	// Add the manager to the path of the message to keep track
    	m.getHeader().addToPath(manager.getUuid());
        for ( ExternalDispatcher dispatcher: externalDispatchers ) {
        	// Only dispatch items to destinations that has not received this message before
        	if ( !m.getHeader().getPath().contains( dispatcher.getDestinationUUID() )) {
        		dispatcher.dispatch(m);
        	}
        }
    }
    
    private void dispatchToExternalConnections(Message o) {
        for ( Dispatcher dispatcher: externalDispatchers ) {
            dispatcher.dispatch(o);
        }
    }
    
    public void removeExternalDispatcher( Dispatcher dispatcher ) {
        externalDispatchers.remove(dispatcher);
    }

	public void addExternalDispatcher(Socket socket, String destinationUUID) {
		ExternalDispatcher dispatcher = new ExternalDispatcher(manager, socket, destinationUUID, destinationUUID!=null);
		externalDispatchers.add(dispatcher);
	}
	
	public void addExternalDispatcher(Socket socket) {
		addExternalDispatcher(socket, null);
	}
	
	public void addExternalReceiver(Socket socket) {
		ExternalReceiver receiver = new ExternalReceiver(manager, socket);
		externalReceivers.add( receiver );
	}

	/**
	 * Get the dispatcher that handles the specified socket. This is used in the process to identify both ends of a connection
	 * @param socket
	 * @return
	 */
	public ExternalDispatcher getExternalDispatcher(Socket socket) {
		ExternalDispatcher returnValue = null; 
		for ( ExternalDispatcher d: externalDispatchers ) {
			if ( d.getSocket().equals(socket) ) {
				returnValue = d;
				break;
			}
		}
		return returnValue;
	}


}
