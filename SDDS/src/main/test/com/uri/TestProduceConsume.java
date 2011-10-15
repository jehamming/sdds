package com.uri;

public class TestProduceConsume {
	
	public static void main(String[] args) {
		SimpleProducer producer = new SimpleProducer();
		Thread t1 = new Thread(producer);
		t1.start();
		
		SimpleConsumer consumer = new SimpleConsumer();
		Thread t = new Thread(consumer);
		t.start();
		
		while ( true ) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
