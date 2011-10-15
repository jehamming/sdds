/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdds.monitor.consumer;

import java.text.SimpleDateFormat;

/**
 *
 * @author Jan-Egbert
 */
public class ConsumedItem {

    private String xml;
    private String managerSourceId;
    private long time;
    private long delay;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SS");
    
    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
    
    public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	


	public String getManagerSourceId() {
		return managerSourceId;
	}

	public void setManagerSourceId(String managerSourceId) {
		this.managerSourceId = managerSourceId;
	}

	@Override
    public String toString() {
        return formatter.format(getTime());
    }
}
