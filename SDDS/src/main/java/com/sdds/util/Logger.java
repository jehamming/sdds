package com.sdds.util;

import java.util.Calendar;

/**
 * A Simple Logger
 * @author jehamming
 *
 */
public class Logger {
    
    private Logger() {
        // Do nothing
    }
    
    public static boolean  info(Object o, String logMessage) {
        System.out.println(Calendar.getInstance().getTimeInMillis() + " - " + o.getClass().getName()+":" + logMessage);
        return true;
    }
    
    public static boolean  info(String logMessage) {
        System.out.println(Calendar.getInstance().getTimeInMillis() + " - " + logMessage);
        return true;
    }


}
