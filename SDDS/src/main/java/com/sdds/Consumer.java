package com.sdds;

import java.lang.reflect.ParameterizedType;

/**
 * A generic Abstract Consumer. Extend/Implement this class for specific
 * functionality when instances of Class 'T' are consumed.
 * 
 * Implement the consume method to handle instances of Class 'T'
 * 
 * @author J.E. Hamming
 * 
 * @param <T> The specific consumer will consume instances of Class 'T' 
 */
public abstract class Consumer<T> {
	
    /**
     * The real type of the class ('T') that this consumer consumes
     */
    private Class<T> clazz = figureOutPersistentClass();

    /**
     * Implement this method to create functionality that handles instances of
     * class 'T'
     * 
     * @param object
     */
    public abstract void consume(T object);

    /**
     * Get the type of Class ('T') that this consumer consumes
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
	public Class getConsumedClass() {
        return this.clazz;
    }

    /**
     * Find out the type of 'T' using reflection
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<T> figureOutPersistentClass() {
        Class<T> clazz =
                (Class<T>) ((ParameterizedType) (getClass().getGenericSuperclass()))
                        .getActualTypeArguments()[0];
        return clazz;
    }
    
}
