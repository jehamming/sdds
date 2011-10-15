
package com.sdds.impl;

/**
 * This is the message that gets sent over the network. The header contains metadata.
 * @author jehamming
 *
 */
public class Message {
    
    private Header header;
    private Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }    
    
    @Override
    public String toString() {
    	return "A Message containing:" + content.toString();
    }
}
