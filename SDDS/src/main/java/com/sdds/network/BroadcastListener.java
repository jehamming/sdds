package com.sdds.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

import com.sdds.Manager;
import com.sdds.util.Logger;
import com.sdds.util.NetUtil;

/**
 * The Broadcast listener listens on a broadcast group to do the initial handshaking with other JVMs
 * @author jehamming
 *
 */
public class BroadcastListener implements Runnable {
	boolean running = false;
	private Manager manager;
	private Thread theThread;

	/**
	 * Constructor
	 * @param manager
	 */
	public BroadcastListener(Manager manager) {
		this.manager = manager;
		theThread = new Thread(this);
		theThread.setDaemon(true);
		theThread.setName(getClass().getName());
		theThread.start();
	}

	public void stop() {
		if (running) {
			running = false;
		}
	}

	/* (non-Javadoc)
	 * Do the work:
	 * Step 1: Send an ALIVE broadcast message
	 * Step 2: Start listening
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {

			MulticastSocket socket;
			DatagramPacket packet;
			InetAddress address;

			address = InetAddress.getByName(NetUtil.JVM_MULTICAST);
			socket = new MulticastSocket(NetUtil.JVM_PORT);
			//TODO LoopbackMode to true should but sometimes doesnt work! 
			socket.setLoopbackMode(false);
			socket.joinGroup(address);
			byte[] data = new byte[999];

			packet = new DatagramPacket(data, data.length);
			Logger.info(this,"Start");

			// Broadcast a message that we are alive
			NetUtil.broadcast(BroadcastProtocol.INIT_MSG + manager.getRealm() + ":" + manager.getServerPort() + ":" + manager.getUuid());
			
			running = true;
			while (running) {
				// receive the packets
				socket.receive(packet);

				String text = new String(packet.getData());
				text = text.substring(0, packet.getLength());
				Logger.info(this, "Received data ("+ text +") from : " + packet.getAddress().getHostAddress());

				if ( text.startsWith(BroadcastProtocol.INIT_MSG)) {
					// Subtract the String containing the REALM and PORT
					String complete = text.substring(BroadcastProtocol.INIT_MSG.length()).trim();
					String[] parts = complete.split(":");
					// Find out the realm
					String realm = parts[0];
					if ( manager.getRealm().equals(realm) ) {
						String txtPort = parts[1];
						String txtUUID = parts[2];
						int port = Integer.valueOf(txtPort);
						if ( NetUtil.isAllowed(port, manager.getServerPort(), packet.getAddress().getHostAddress() )) {
							Logger.info(this, "Adding a dispatcher and receiver to " + packet.getAddress().getHostAddress() + ":" + port );
							Socket extSocket = new Socket(packet.getAddress().getHostAddress(), port);
							manager.getDispatchManager().addExternalDispatcher(extSocket, txtUUID);
							manager.getDispatchManager().addExternalReceiver(extSocket);
						}
					} else {
						// Add the REALM to the known realms
						manager.addRealm(realm);
						Logger.info(this, "Ignored realm '" + realm + "', (!= " + manager.getRealm()+ ") but added to the known list");
						// Send a broadcast reply with this realm
						NetUtil.broadcast(BroadcastProtocol.INIT_REALM + manager.getRealm());
					}
				} else if ( text.startsWith(BroadcastProtocol.INIT_REALM)) {
					String realm = text.substring(BroadcastProtocol.INIT_REALM.length()).trim();
					if ( !realm.equals(manager.getRealm() )) {
						// Add the REALM to the known realms
						manager.addRealm(realm);
						Logger.info(this, "Added realm '" + realm + "' to the known realms");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isRunning() {
		return running;
	}

}
