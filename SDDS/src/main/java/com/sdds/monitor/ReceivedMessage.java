/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sdds.monitor;

/**
 *This class is used by the Monitor to store received messages in XML format
 * @author Jan-Egbert Hamming
 */
public class ReceivedMessage {
    
    private String xml; 
    private String name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
    
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReceivedMessage other = (ReceivedMessage) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.xml != null ? this.xml.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    

}
