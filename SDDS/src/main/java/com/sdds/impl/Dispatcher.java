package com.sdds.impl;

import java.util.LinkedList;
import java.util.List;

import com.sdds.util.Logger;


/**
 * This is a generic dispatcher. The internal and external dispatchers are based on this Dispatcher.
 * The dispatcher uses a wait() notify() system to let the dispatch() method return immediately, 
 * you dont have to wait for the object to be really dispatched. 
 * 
 * @author jehamming
 */
public abstract class Dispatcher implements Runnable {
    
    private List<Object> bucket = null;
    private boolean running = false;

    /**
     * Constructor
     * @param name The name that will show up in the process list
     */
    public Dispatcher(String name) {
        this.bucket = new LinkedList<Object>();
        Thread t = new Thread(this);
        t.setName(getClass().getName() + "-" + name);
        t.start();
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     * 
     * This is where all the work of dispatching takes place. It uses a synchronized bucket, so
     * that a dispatch() method will return immediately, processing occurs in parrallel
     */
    @Override
    public void run() {
        Logger.info(this, "Start");
        running = true;
        while (running) {
            synchronized (bucket) {
                try {
                    if (bucket.size() == 0)
                        bucket.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while ( !bucket.isEmpty() ) {
                    // Start from the back (FIFO)
                    deliver(bucket.remove( bucket.size() - 1));
                }
            }
        }
        Logger.info(this, "Stopped");
    }

    /**
     * Dispatch an object
     * @param o the object
     */
    public void dispatch(Object o) {
        synchronized (bucket) {
            bucket.add(o);
            bucket.notify();
        }
    }

	/**
	 * Stop this dispatcher (Called when the connection breaks,etc)
	 */
	public void requestStop() {
        running = false;
    }
    
    /**
     * This is the method that needs specific code to do the real delivering
     */
    public abstract void deliver(Object o);


}
