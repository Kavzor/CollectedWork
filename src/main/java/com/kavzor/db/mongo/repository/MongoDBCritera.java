package com.kavzor.mongo.repository;

import com.kavzor.mongo.translator.SearchCritera;

/**
 * Must be implemented at objects stored in MongoDBRepository
 * @author kavzor
 *
 */
public interface MongoDBCritera {
	public SearchCritera getQueryCritera();

}
