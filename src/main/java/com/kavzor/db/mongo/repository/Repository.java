package com.kavzor.mongo.repository;

import java.util.List;

import com.kavzor.mongo.translator.SearchCritera;

/**
 * All generic types stored in MongoDBRepository must implement MongoDBCritera plus 
 * overriding the default equals method to ensure consistency.
 * @author kavzor
 *
 * @param <E> generic type to be stored
 */
public interface Repository<E> {

	/**
	 * Uses mongodb bson to find stored object
	 * @param critera name-value paired
	 * @return generic object
	 */
	public E find(MongoDBCritera critera);
	
	/**
	 * Alternative for find with MongoDBCritera
	 * @param key key to filter
	 * @param value value to find
	 * @return found generic object
	 */
	default E find(String key, Object value) {
		SearchCritera searchCritera = new SearchCritera(key, value);
		return find(() -> searchCritera);
	}
	
	/**
	 * Finds all stored generic objects
	 * @return a collection of stored objects
	 */
	public List<E> findAll();
	
	/**
	 * Uses $set to override old data
	 * @param entity to override
	 */
	public void update(E entity);
	
	/**
	 * Inserts generic type, won't add if already exists
	 * @param entity entity to add
	 */
	public void add(E entity);
	
	/**
	 * Removes generic object, alternative form below
	 * @param critera to search upon
	 */
	public void remove(MongoDBCritera critera);
	
	/**
	 * Optional method for filtering objects
	 * @param key to search on
	 * @param value to find
	 */
	default void remove(String key, Object value) {
		SearchCritera searchCritera = new SearchCritera(key, value);
		remove(() -> searchCritera);
	}
}
