package com.weasel.spring.mongodb.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.weasel.mongodb.MongoRepositorySupport;

/**
 * 
 * @author Dylan
 * @time 2013-5-10 下午3:21:28
 */
public class UserRepository extends MongoRepositorySupport<String,User> {

	@Autowired
	public UserRepository(MongoOperations _operations) {
		super(_operations);
	}

}
