/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sdds.monitor.consumer;

import java.util.Calendar;

import com.sdds.impl.Message;
import com.sdds.monitor.MessageReciever;
import com.sdds.monitor.ReceivedMessage;
import com.sdds.util.Logger;
import com.sdds.util.XMLUtil;

/**
 *
 * @author Jan-Egbert
 */
public class ConsumerWindowHelper implements MessageReciever {

    private ConsumerWindow window;
    private String type;
    private XMLConsumer consumer;
    private String msgClassName = Message.class.getName(); 

    public ConsumerWindowHelper( ConsumerWindow w , String type) {
        this.window = w;
        this.type = type;
        initialize();
    }

    private long computeDelay(String xml) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long retVal = 0;
        String xpath = "/" +msgClassName+ "/header/timestamp";
        String s = XMLUtil.executeXPath(xml, xpath);
        long timeOfMessage = Long.valueOf(s);
        retVal = currentTime - timeOfMessage;
        return retVal;
    }

    private void initialize() {
        consumer = new XMLConsumer(this);
        // Make this consumer specific
        String xpath = "/" +msgClassName+ "/content[@class='" + type + "']";
        consumer.addXPathQuery(xpath);
        window.getManager().getConsumerManager().addConsumer(consumer);
    }

	@Override
	public void messageReceived(ReceivedMessage message) {
        long delay = computeDelay(message.getXml());
        ConsumedItem item = new ConsumedItem();
        String managerSourceId = XMLUtil.executeXPath(message.getXml(), "/" +msgClassName+ "/header/managerId"); 
        item.setManagerSourceId(managerSourceId);
        item.setTime( Calendar.getInstance().getTimeInMillis() );
        item.setXml(message.getXml());
        item.setDelay( delay );
        window.addConsumedItem( item );
	}

	public void stop() {
		Logger.info(this, "Closing window - Removing consumer");
		window.getManager().getConsumerManager().removeConsumer(consumer);
	}


}
