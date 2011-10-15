package com.sdds.network;

public interface BroadcastProtocol {
	
	/**
	 * Initial ALIVE message
	 */
	public static final String INIT_MSG ="SDDS_INIT:";
	/**
	 * Communicate realms to eachother
	 */
	public static final String INIT_REALM ="SDDS_REALM:";

}
