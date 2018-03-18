package com.kavzor.mongo.exception;

public class EntityException extends RuntimeException {

	private static final long serialVersionUID = -6744295552541875366L;
	
	/**
	 * Thrown if equals method is not overridden
	 * @param type generic type
	 */
	public EntityException(Class<?> type) {
		super("All Entities must override the Object Equals method \n "
				+ "Entity " + type.toGenericString() + " does not override the equals method");
	}
}
