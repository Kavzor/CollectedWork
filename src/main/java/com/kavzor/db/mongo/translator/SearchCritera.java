package com.kavzor.mongo.translator;

import com.kavzor.mongo.repository.MongoDBCritera;

/**
 * Translates mongos default bson filter object 
 * This class ensures that the user won't have to download any mongo dependencies
 * @author kavzor
 *
 */
public class SearchCritera implements MongoDBCritera {
	private String key;
	private Object value;
	
	/**
	 * Key value paired filter for finding objects
	 * @param key
	 * @param value
	 */
	public SearchCritera(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public SearchCritera getQueryCritera() {
		return new SearchCritera(key, value);
	}

	public String getKey() {
		return key;
	}
	public Object getValue() {
		return value;
	}
}
