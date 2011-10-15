package com.uri;

import com.sdds.Consumer;
import com.sdds.Manager;
import com.sdds.util.Logger;

public class SimpleConsumer extends Consumer<TestObject> implements Runnable {

	private TestObject prev;

	@Override
	public void consume(TestObject o) {
		if ( prev != null && prev.equals(o) ) {
			Logger.info(this, "Consumed AGAIN :" + o.getName()  );
		} else {
			Logger.info(this, "Consumed :" + o.getName()  );
		}
		prev = o;
	}

	@Override
	public void run() {
		Manager m = Manager.getInstance();
		m.getConsumerManager().addConsumer(this);
		while ( true ) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SimpleConsumer c = new SimpleConsumer();
		Thread t = new Thread(c);
		t.start();
	}

}
