package com.sdds.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sdds.Manager;
import com.sdds.util.Logger;
import com.sdds.util.NetUtil;

/**
 * The ConnectionServer sets up direct connections (ExternalDispatchers, ExternalReceivers)
 * @author jehamming
 *
 */
public class ConnectionServer implements Runnable {
	private boolean stopServer = false;
	private int port;
	private Manager manager;
	private Thread theThread;

	/**
	 * The constructor
	 * 
	 * @param manager
	 * @param port
	 */
	public ConnectionServer(Manager manager, int port) {
		this.port = port;
		this.manager = manager;
		theThread = new Thread(this);
		theThread.setDaemon(true);
		theThread.setName(getClass().getName());
		theThread.start();
	}
	
	public void stop() {
		stopServer = true;
	}
	
	/* (non-Javadoc)
	 * Do the work: Listen for connections on the specified port
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			Logger.info(this, "Start listening on port : " + port);
		} catch (IOException e) {
			Logger.info(this, "Could not listen on port: " + port);
			System.exit(-1);
		}

		while (!stopServer) {
			try {
				Socket socket = serverSocket.accept();
				if ( NetUtil.isAllowed(socket.getPort(), manager.getServerPort(), socket.getInetAddress().getHostAddress())) {
					Logger.info(this, "A new Manager instance has connected, connect a dispatcher and receiver to it on port " + socket.getPort());
					manager.getDispatchManager().addExternalDispatcher(socket);
					manager.getDispatchManager().addExternalReceiver(socket);
				} else {
					Logger.info(this, "FIX THIS: Refuse connection to local: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Logger.info(this, "Stop");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
