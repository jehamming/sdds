package com.sdds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sdds.impl.Header;
import com.sdds.impl.Message;
import com.sdds.management.ConsumerManager;
import com.sdds.management.DispatchManager;
import com.sdds.network.BroadcastListener;
import com.sdds.network.ConnectionServer;
import com.sdds.util.Logger;
import com.sdds.util.NetUtil;
import com.sdds.util.UUID;

/**
 * The Manager class. This is the main entrypoint to product items and to register a consumer
 * @author jehamming
 *
 */
public class Manager {

	// The list of registered realms and their manager. For each realm 1 manager exists
	private static Map<String, Manager> managers = new HashMap<String, Manager>();
	@SuppressWarnings("unused")
	private BroadcastListener broadcastListener;
	@SuppressWarnings("unused")
	private ConnectionServer connectionServer;
	private DispatchManager dispatchManager;
	private ConsumerManager consumerManager;
	private int serverPort;
	private String uuid;
	private static String DEFAULT_REALM = "DEFAULT";
	private String realm;
	private List<RealmListener> realmListeners;

	/**
	 * Get the Manager for a specific realm. Note: Only one manager exists for each realm
	 * @param realm
	 */
	private Manager(String realm) {
		this.realm = realm;
	}
	
	/**
	 * Get the manager for the default realm. Note: only one manager exists for each realm
	 */
	public static Manager getInstance() {
		return Manager.getInstance(DEFAULT_REALM);
	}

	/**
	 * Get the manager for a realm. Note, only one manager exists for each realm
	 */
	public static Manager getInstance(String realm) {
		Manager manager = managers.get(realm);		
		if (manager == null) {
			manager = new Manager(realm);
			manager.init();
			managers.put(realm, manager);
		}
		return manager;
	}

	/**
	 * Get the consumer manager, here you can register a new Consumer
	 * @see Consumer
	 */
	public ConsumerManager getConsumerManager() {
		return consumerManager;
	}

	/**
	 * Internal framework use only, use dispatch instead
	 * @see dispatch()
	 */
	public DispatchManager getDispatchManager() {
		return dispatchManager;
	}

	/**
	 * Initialize the manager
	 */
	private void init() {
		// Get an UUID
		uuid = new UUID().toString();
		// Initialize the list of realmListeners
		realmListeners = new ArrayList<RealmListener>();
		// Start a server that external producers can connect to 
		serverPort = NetUtil.findFreePort();
		// Initialize the dispatchManager
		dispatchManager = new DispatchManager(this);
		// Initialize the consumerManager
		consumerManager = new ConsumerManager();
		// Initialize the direct connection server
		connectionServer = new ConnectionServer(this, serverPort);
		// Listen for Broadcast messages
		broadcastListener = new BroadcastListener(this);
		Logger.info(this, "Started");
		Logger.info(this, "Realm:" + realm);
		Logger.info(this, "UUID:" + uuid);
	}

	/**
	 * Send an object to the realm (or datacloud, or whatever)
	 * 
	 * @param o The object you want to send
	 */
	public void produce(Object o) {
		Message m = createMessage(o);
		dispatchManager.dispatch(m, true);
	}
	
	/**
	 * Get the server port
	 */
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * Get the unique ID of this manager
	 */
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * Get the real the Manager is currently in
	 */
	public String getRealm() {
		return realm;
	}
	
	/**
	 * Get the list of Realms the manager has detected  
	 */
	public static String[] getRealms() {
		String[] returnValue = new String[managers.keySet().size()];
		returnValue = managers.keySet().toArray(returnValue);
		return returnValue;
	}
	
	/**
	 * Add a realm listener to get notified of realm updates
	 * @param l
	 */
	public synchronized void addRealmListener( RealmListener l ) {
		if ( !realmListeners.contains(l) ) {
			realmListeners.add(l);
		}
	}
	
	/**
	 * Remove a realmlistener
	 */
	public synchronized void removeRealmListener( RealmListener l ){
		if ( realmListeners.contains(l) ) {
			realmListeners.remove(l);
		}		
	}
	
	/**
	 * Add a realm to the list of know realms (if not already present)
	 * @param realm
	 */
	public synchronized void addRealm( String realm ) {
		if ( !managers.keySet().contains(realm) ) {
			managers.put(realm, null);
			for ( RealmListener rl: realmListeners ) {
				rl.realmAdded(realm);
			}
		}
	}
	
	/**
	 * Construct a MessageObject from Object o
	 * @param o
	 * @return
	 */
	private Message createMessage(Object o) {
		Message m = new Message();
		m.setContent(o);
		Header header = new Header();
		header.setManagerId(uuid);
		header.addToPath(uuid);
		header.setTimestamp("" + Calendar.getInstance().getTimeInMillis());
		header.setVersion("0.1");
		header.setContentHash(System.identityHashCode(o));
		m.setHeader(header);
		return m;
	}

}
