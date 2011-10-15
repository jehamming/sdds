/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdds.monitor.producer;

import com.sdds.Manager;

/**
 *
 * @author Jan-Egbert
 */
public class ProducerWindowHelper {

    @SuppressWarnings("unused")
	private ProducerWindow window;
    @SuppressWarnings("unused")
	private Manager manager;

    public ProducerWindowHelper(ProducerWindow w, Manager manager) {
        this.window = w;
        this.manager = manager;
    }

    public void produce(String xml, String type) {
        //TODO How to produce XML?
    	//manager.produce(o);
    }
    
}
