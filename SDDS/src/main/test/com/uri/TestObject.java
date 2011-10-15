package com.uri;

public class TestObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } 
    
    @Override
    public String toString() {
    	return getName();
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj.toString().equals(toString());
    }
}
