package com.sdds.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.sdds.Manager;
import com.sdds.network.ManagementSourceId;
import com.sdds.util.Logger;
import com.sdds.util.XMLUtil;

/**
 *  An external receiver  is used by the ConnectionServer to setup direct connections
 * @author jehamming
 *
 */
public class ExternalReceiver implements Runnable {

	private Socket socket;
	private Manager manager;

	/**
	 * Constructor
	 * 
	 * @param manager
	 * @param socket
	 */
	public ExternalReceiver(Manager manager, Socket socket) {
		this.socket = socket;
		this.manager = manager;
		Thread t = new Thread(this);
		String name = socket.getInetAddress().getHostName();
		t.setName(getClass().getName() + "-" + name);
		t.start();
	}

	/* (non-Javadoc)
	 * Start receiving XML(Strings) from the socket and dispatch them internally and/or relaying them
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Refactor, reduce cyclo complexity
		Logger.info(this, "Start");
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			while (socket.isConnected() && !socket.isInputShutdown()) {
				String xml = (String) ois.readObject();
				// Convert XML to object
				Object o = XMLUtil.fromXML(xml);
				// Check for Message type
				if ( o != null ) {
					if( o instanceof Message ) {
					Message m = (Message) o;
						if ( !m.getHeader().getPath().contains(manager.getUuid() ) ) {
							// Dispatch Internal
							manager.getDispatchManager().dispatch(m, false);
							// Dispatch in XML form
							manager.getDispatchManager().dispatch(xml);
							// Relay message to other listeners
							Manager.getInstance().getDispatchManager().relayMessage(m);					
						} else {
							Logger.info(this, "Not relaying instance of " + m.getContent().getClass().getName() + ", already passed this Manager or originated from here");
						}
					} else if ( o instanceof ManagementSourceId) {
						ManagementSourceId m = (ManagementSourceId) o;
						Logger.info(this, "Got a ManagementSourceId:" + m);
						ExternalDispatcher d = manager.getDispatchManager().getExternalDispatcher( socket );
						d.setDestinationUUID(m.getSourceId());
					}
				}
			}
		} catch (IOException e) {
			// Logger.info(this, "Exception");
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Should not happen
			e.printStackTrace();
		}
		Logger.info(this, "Stopped, connection closed");
	}

}
