package com.sdds.network;

/**
 * For JVM<-->JVM communication
 * 
 * @author jehamming
 *
 */
public class ManagementSourceId {
	
	private String sourceId;

	public ManagementSourceId( String uuid ) {
		this.sourceId = uuid;
	}
	
	public String getSourceId() {
		return sourceId;
	}
	
	@Override
	public String toString() {
		return sourceId;
	}
	
}
