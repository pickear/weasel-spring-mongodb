package com.weasel.mongodb;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.weasel.lang.helper.GodHands;

/**
 * 
 * @author Dylan
 * @time 2013-5-10 上午11:36:16
 */
public abstract class MongoRepositorySupport<ID extends Serializable,T> {

	private final MongoOperations operations;
	private Class<T> entityClass;
	private String idName = "_id";

	@SuppressWarnings("unchecked")
	protected MongoRepositorySupport(MongoOperations _operations) {
		Validate.notNull(_operations, "MongoOperations must not be null,you can config MongoTemplate in spring xml ");
		entityClass = (Class<T>) GodHands.genericsTypes(getClass())[1];
		this.operations = _operations;
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				this.idName = field.getName();
			}
		}
	}

	/**
	 * find all documents from collection for page
	 * @param pageable
	 * @return
	 */
	public Page<T> findAll(Page<T> page) {

		List<T> result = getOperations().find(new Query().with(page), entityClass, getCollectionName());
		page.setResult(result).setTotal(count());
		return page;
	}

	/**
	 * persisty the document to collection,if already exit then update,otherwise insert
	 * @param entity
	 * @return
	 */
	public <S extends T> S save(S entity) {

		Validate.notNull(entity, "entity must not be null...");

		getOperations().save(entity, getCollectionName());
		return entity;
	}

	/**
	 * find a document by id
	 * @param id
	 * @return
	 */
	public T findOne(ID id) {

		Validate.notNull(id, "id must not be null...");
		return getOperations().findById(id, entityClass, getCollectionName());
	}

	/**
	 * check there is a document holde this id or not
	 * @param id
	 * @return
	 */
	public boolean exists(ID id) {

		Validate.notNull(id, "id must not be null...");
		final Query query = idQuery(id);
		query.fields();

		return getOperations().findOne(query, entityClass, getCollectionName()) != null;
	}

	/**
	 * find some document who's id in ids
	 * @param ids
	 * @return
	 */
	public List<T> findAll(Iterable<ID> ids) {

		Validate.notNull(ids, "ids must not be null...");
		Set<ID> parameters = Sets.newHashSet();
		for (ID id : ids) {
			parameters.add(id);
		}
		return findAll(query(where(idName).in(parameters)));
	}

	/**
	 * get all count from collection
	 * @return
	 */
	public long count() {
		return getOperations().getCollection(getCollectionName()).count();
	}

	/**
	 * remove the document from collection by id
	 * @param id
	 */
	public void delete(ID id) {

		Validate.notNull(id, "id must not be null...");
		getOperations().remove(idQuery(id), entityClass);
	}
	
	/**
	 * remove the document from collection what you give me
	 * @param entity
	 */
	public void delete(T entity) {

		Validate.notNull(entity, "entity must not be null...");
		getOperations().remove(entity, getCollectionName());
	}

	/**
	 * remove all documents from collection what you give me
	 * @param entities
	 */
	public void delete(Iterable<? extends T> entities) {

		Validate.notNull(entities, "entities must not be null...");

		for (T entity : entities) {
			delete(entity);
		}
	}

	/**
	 * remove all documents from collection
	 */
	public void deleteAll() {
		
		getOperations().remove(new Query(), getCollectionName());
	}

	/**
	 * save all documents to collection you give me,if exit then updata,otherwise insert
	 * @param entites
	 * @return
	 */
	public <S extends T> List<S> save(Iterable<S> entities) {

		Validate.notNull(entities, "entities must not be null...");

		List<S> result = Lists.newArrayList();
		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	/**
	 * get all documents from collection
	 * @return
	 */
	public List<T> findAll() {
		return findAll(new Query());
	}

	/**
	 * get all documents from collection,at this time,it will be sorted
	 * @param sort
	 * @return
	 */
	public List<T> findAll(Sort sort) {

		Validate.notNull(sort, "sort must not be null...");
		return findAll(new Query().with(sort));
	}

	/**
	 * get all documents from collection
	 * @param query
	 * @return
	 */
	public List<T> findAll(Query query) {

		if (null == query) {
			return Collections.emptyList();
		}
		return getOperations().find(query, entityClass, getCollectionName());
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	protected Query idQuery(ID id){
		return query(where(idName).is(id));
	}

	/**
	 * 
	 * @return
	 */
	protected String getCollectionName(){
		Document document = GodHands.getAccessibleAnnotation(entityClass, Document.class);
		String collectionName = "";
		if(null != document){
			collectionName = document.collection();
		}
		if(StringUtils.isEmpty(collectionName)){
			collectionName = entityClass.getSimpleName().toLowerCase();
		}
		return collectionName;
	}
	
	/**
	 * 
	 * @return
	 */
	protected final MongoOperations getOperations() {
		return operations;
	}

}
