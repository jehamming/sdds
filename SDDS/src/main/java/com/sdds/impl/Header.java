package com.sdds.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * The header used for creating a message
 * 
 * @author jehamming
 *
 */
public class Header {

    private String version;
    private String timestamp;
    private String managerId;
    /**
     * The id of the produced object 
     */
    private int contentHash;
    private List<String> path;
    
    public Header() {
    	path = new ArrayList<String>();
	}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public List<String> getPath() {
		return path;
	}
    
    public void setPath(List<String> path) {
		this.path = path;
	}
    
    public void addToPath( String pathElement ) {
    	path.add(pathElement);
    }
    
    public String getManagerId() {
		return managerId;
	}
    
    public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public int getContentHash() {
		return contentHash;
	}

	public void setContentHash(int contentHash) {
		this.contentHash = contentHash;
	}
    
    
}
