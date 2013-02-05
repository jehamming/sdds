package com.uri;

import com.sdds.SDDSManager;
import com.sdds.util.Logger;

public class SimpleProducer implements Runnable {

	private String realm = null;
	
	@Override
	public void run() {
		SDDSManager manager; 
		if ( realm == null ) {
			manager = SDDSManager.getInstance();
		} else {
			manager = SDDSManager.getInstance(realm);
		}
		int count = 1;
		while ( true ) {
			TestObject t = new TestObject();
			t.setName("Item-" + count++);
			manager.produce(t);
			Logger.info(this, "Produced: " + t.getName() );
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	public SimpleProducer() {
		// Do Nothing
	}
	
	public SimpleProducer( String realm ) {
		this.realm = realm;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String realm = null;
		if ( args.length > 0 ) {
			realm = args[0];
		}
		SimpleProducer p = new SimpleProducer(realm);
		Thread t = new Thread(p);
		t.start();
	}
	
}
