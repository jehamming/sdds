package com.sdds;

import com.sdds.Consumer;
import com.sdds.management.ConsumerManager;
import com.sdds.management.DispatchManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SDDSManager {
	
	private static UUID uuid = UUID.randomUUID();
	private static SDDSManager instance = null;

	private DispatchManager dispatchManager = null;
	private ConsumerManager consumerManager = null;

	private List<String> realms = null;
	private String currentREALM = null;
	private static String DEFAULT_REALM = "DEFAULT";
	private static int SERVER_PORT = 9876;

	
	private static List<Consumer> consumers = new ArrayList<Consumer>();
	
	public static String getUUID() {
		return uuid.toString();
	}
	
	public static void addConsumer( Consumer c) {
		consumers.add(c);
	}
	
	public static void removeConsumer( Consumer c) {
		consumers.remove(c);
	}
	
	public static boolean containsConsumer( Consumer c ) {
		return consumers.contains(c);
	}
	
	public static List<Consumer> getConsumers() {
		return consumers;
	}

	 public static SDDSManager getInstance() {
		if (instance == null ) {
			instance = new SDDSManager();
		}
		return instance;
	 }

	public ConsumerManager getConsumerManager() {
		return consumerManager;
	}

	public DispatchManager getDispatchManager() {
		return dispatchManager;
	}

	private SDDSManager() {
		initialize();
	}

	private void initialize() {
		consumerManager = new ConsumerManager();
		dispatchManager = new DispatchManager(this);
		realms = new ArrayList<String>();
		realms.add( DEFAULT_REALM);
		currentREALM = DEFAULT_REALM;
	}


	public static String getUuid() {
		return uuid.toString();
	}

	public String getRealm() {
		return currentREALM;
	}

	public int getServerPort() {
		return SERVER_PORT;
	}

	public void addRealm(String realm) {
		if (! realms.contains(realm)) {
			realms.add(realm);
		}
	}
}
