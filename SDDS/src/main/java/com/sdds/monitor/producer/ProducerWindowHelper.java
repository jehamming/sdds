/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdds.monitor.producer;

import com.sdds.SDDSManager;

/**
 *
 * @author Jan-Egbert
 */
public class ProducerWindowHelper {

    @SuppressWarnings("unused")
	private ProducerWindow window;
    @SuppressWarnings("unused")
	private SDDSManager manager;

    public ProducerWindowHelper(ProducerWindow w, SDDSManager manager) {
        this.window = w;
        this.manager = manager;
    }

    public void produce(String xml, String type) {
        //TODO How to produce XML?
    	//manager.produce(o);
    }
    
}
