package com.sdds.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetUtil {
	public static final int JVM_PORT = 1200;
	public static final int MULTICASTSOCKET = 6789;
	public static final String JVM_MULTICAST = "228.255.255.255";	
	

	/**
	 * Find a free port to start a server with;
	 * @return
	 */
	public static int findFreePort() {
		int port = -1;
		ServerSocket server;
		try {
			server = new ServerSocket(0);
			port = server.getLocalPort();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return port;
	}

	/**
	 * Broadcast a single string to the broadcastgroup
	 * @param message
	 */
	public static void broadcast(String message) {
		try {
			InetAddress address = InetAddress.getByName(JVM_MULTICAST);
			MulticastSocket socket = new MulticastSocket(MULTICASTSOCKET);
			socket.setTimeToLive(100);
			socket.setLoopbackMode(false);
			socket.joinGroup(address);
			byte[] data = null;
			data = message.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, JVM_PORT);

			// Sends the packet
			try {
				socket.send(packet);
			} catch (IOException exc) {
				exc.printStackTrace();
			}
			// Stop the server
			socket.leaveGroup(address);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A rather elaborate method to determine the current ipaddress of this machine and match 
	 * it against a given ipaddress
	 * @param ipaddress
	 * @return
	 */
	public static boolean isSameAsLocalhost( String ipaddress ) {
		//TODO Refactor
		try {
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface iface : Collections.list(ifaces)) {
				// Virtual
				Enumeration<NetworkInterface> virtualIfaces = iface
						.getSubInterfaces();
				for (NetworkInterface viface : Collections.list(virtualIfaces)) {					
					Enumeration<InetAddress> vaddrs = viface.getInetAddresses();
					for (InetAddress vaddr : Collections.list(vaddrs)) {
						if ( vaddr.getHostAddress().equals(ipaddress)) {
							return true;
						}
						
					}
				}
				// Real
				Enumeration<InetAddress> raddrs = iface.getInetAddresses();
				for (InetAddress raddr : Collections.list(raddrs)) {
					if ( raddr.getHostAddress().equals(ipaddress)) {
						return true;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * If the given data matches this Manager/JVM instance, do not allow it.
	 * @param incomingPort
	 * @param serverPort
	 * @param ipaddress
	 * @return
	 */
	public static boolean isAllowed(int incomingPort, int serverPort, String ipaddress) {
		boolean returnValue = true;
		boolean samePort = (incomingPort == serverPort);
		boolean sameHost = NetUtil.isSameAsLocalhost( ipaddress );
		if (  samePort && sameHost )  {
			returnValue = false;
		}
		return returnValue;			
	}
}
