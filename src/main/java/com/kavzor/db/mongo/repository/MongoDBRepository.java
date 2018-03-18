/*
 * Date: 2017-12-20
 * 
 * For additional questions refer to kavzor@gmail.com
 */
package com.kavzor.mongo.repository;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.kavzor.mongo.exception.EntityException;
import com.kavzor.mongo.translator.SearchCritera;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/*
 * @author Jakob Rolandsson
 * @see MongoDB
 * @see generics
 * @see interface
 */

public class MongoDBRepository<E extends MongoDBCritera>  implements Repository<E> {
	
	private Class<? extends E> repoType;
	private String collectionName;
	
	/**
	 * Basic database configurations, those are changeable through the Repository<E> object
	 */
	
	private final static String COLLECTION_EXTENSION = "s";
	private final static String DATABASE_ADRESS = "localhost";
	private final static int DATABASE_PORT = 27017;
	

	private MongoClient mongoClient = new MongoClient(DATABASE_ADRESS, DATABASE_PORT);
	private MongoDatabase database;
	private List<E> storedItems = new LinkedList<>();
	
	private Gson gson = new Gson();
	
	
	/**
	 * Boots the database.
	 * <p>
	 * You will find the database name same as lowercase class type for the generic object
	 * </p>
	 * <p>
	 * You will find the collection name to be the same as lowercase class type
	 * with s extension of the generic object
	 * </p>
	 * @param type Generic type to persist, e.g. User.class
	 */
	public MongoDBRepository(Class<E> type) {
		this.repoType = type;
		
		checkWellFormedEntity(type);
		
		this.database = mongoClient.getDatabase(type.getSimpleName().toLowerCase());
		this.collectionName = type.getSimpleName().toLowerCase() + COLLECTION_EXTENSION;
		this.storedItems = findAll();
	}
	
	/**
	 * Same as previous construct with optional collection name for the stored class object
	 * @param collectionName optional collection name
	 * @param type Generic type to store
	 */
	public MongoDBRepository(final String collectionName, Class<E> type) {
		this(type);
		this.collectionName = collectionName;	
    }
    
	/**
	 * Enables users to swap database at runtime, however, it's recommended to keep defaults
	 * @param url defaults localhost
	 * @param port defaults 27017
	 * @param databaseName defaults lower case of generic class type
	 */
	public void swapDatabase(String url, int port, String databaseName) {
		mongoClient = new MongoClient(url, port);
		database = mongoClient.getDatabase(databaseName);
	}
	
	/**
	 * Enables users to swap url and port 
	 * @param url defaults localhost
	 * @param port defaults 27107
	 */
	public void swapDatabase(String url, int port) {
		swapDatabase(url, port, repoType.getSimpleName().toLowerCase());
	}
	
	
    public void add(E entity) {
    	if(!contains(entity)) {
    		database.getCollection(collectionName).insertOne(Document.parse(gson.toJson(entity)));
        	storedItems.add(entity);
    	}
    }

    public void update(E entity) {
    	Document document = Document.parse(gson.toJson(entity));
    	Bson bsonArg = new Document("$set", document);
    	SearchCritera searchCritera = entity.getQueryCritera();
    	database.getCollection(collectionName).updateOne(Filters.eq(searchCritera.getKey(), searchCritera.getValue()), bsonArg);
    }
    
    public void remove(MongoDBCritera critera) {
    	E entity = find(critera);
    	SearchCritera searchCritera = entity.getQueryCritera();
    	database.getCollection(collectionName).deleteOne(Filters.eq(searchCritera.getKey(), searchCritera.getValue()));
    	storedItems.remove(entity);
    }

    public E find(MongoDBCritera mongoDBCritera) {
    	SearchCritera searchCritera = mongoDBCritera.getQueryCritera();
    	Document document = database.getCollection(collectionName).find(Filters.eq(searchCritera.getKey(), searchCritera.getValue())).first();
    	if(document == null) {
    		return null;
    	}
    	else {
    		return gson.fromJson(document.toJson(), repoType);
    	}
    	
    }
	
	public List<E> findAll(){
		Iterator<Document> iterator = database.getCollection(collectionName).find().iterator();
		List<E> list = new LinkedList<>();
		
		while(iterator.hasNext()) {
			Document document = iterator.next();
			E item = gson.fromJson(document.toJson(), repoType);
			list.add(item);
		}
		return list;
	}
	
	private boolean contains(E entity) {
		for(E item : storedItems) {
			if(item.equals(entity)) {
				return true;
			}
		}
		return false;
	}
	
	private void checkWellFormedEntity(Class<E> type){
		if(!IsEqualsImplemented(type))
			throw new EntityException(type);
			
	}
	private boolean IsEqualsImplemented(Class<E> type) {
		try {
			return repoType.getMethod("equals", Object.class).getDeclaringClass().equals(repoType);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}
}
