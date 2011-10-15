package com.sdds.impl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sdds.Manager;
import com.sdds.network.ManagementSourceId;
import com.sdds.util.Logger;
import com.sdds.util.XMLUtil;

/**
 * An external dispatcher is used by the ConnectionServer to setup direct connections
 * 
 * @author jehamming
 *
 */
public class ExternalDispatcher extends Dispatcher {
	private Socket socket;
	private Manager manager;
	private String destinationUUID;
	private ObjectOutputStream oos;
	private boolean sendSourceId;

	
    /**
     * The constructor. 
     * @param manager
     * @param socket
     * @param destinationUUID
     * @param sendSourceId
     */
    public ExternalDispatcher(Manager manager, Socket socket, String destinationUUID, boolean sendSourceId) {
        super("DataTxRx");
        this.socket = socket;
        this.manager = manager;
        this.destinationUUID = destinationUUID;
        this.sendSourceId = sendSourceId;
    }

    /* (non-Javadoc)
     * Step 1: send info about this manager to the other side.
     * Step 2: Start dispatching
     * 
     * @see com.uri.impl.Dispatcher#run()
     */
    @Override
    public void run() {
    	// First, set the UUID of the manager to the other side for management purposes
    	try {
    		// Create management object
			oos = new ObjectOutputStream(socket.getOutputStream());
    		if ( sendSourceId ) {
        		ManagementSourceId m = new ManagementSourceId(manager.getUuid());
    			String xml = XMLUtil.toXML(m);
    			oos.writeObject(xml);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// Now really start
    	super.run();
    }
    
    /* (non-Javadoc)
     * Start dispatching
     * @see com.uri.impl.Dispatcher#deliver(java.lang.Object)
     */
    @Override
	public void deliver(Object o) {
		Logger.info(this, "Deliver to " + socket.getInetAddress().getHostName()
				+ ":" + socket.getPort() + " an instance of "
				+ o.getClass().getName() + "(" + o.toString() + ")");
		// Step 1 : Convert to XML
		String xml = XMLUtil.toXML(o);
		// Step 2 : Send XML
		try {			
			oos.writeObject(xml);
		} catch (IOException e) {
			Logger.info(this, "Socket closed/disconnected/interrupted, Dispatcher will die");
			try {
				socket.close();
			} catch (IOException exc) {
				// Nothing
				exc.printStackTrace();
			}
			manager.getDispatchManager().removeExternalDispatcher(this);
			requestStop();
		}
	}
    
	/**
	 * Used for relaying messages to other managers (JVMs)
	 * @param m
	 */
	public void dispatch(Message m) {
		super.dispatch(m);
	}
	
	public String getDestinationUUID() {
		return destinationUUID;
	}
    
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setDestinationUUID(String destinationUUID) {
		this.destinationUUID = destinationUUID;
	}
}
