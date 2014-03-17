package com.weasel.spring.mongodb.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="_user")
public class User {
	
	@Id
	private String id;
	
	private String name;
	
	private String passwd;

	public String getId() {
		return id;
	}

	public User setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public String getPasswd() {
		return passwd;
	}

	public User setPasswd(String passwd) {
		this.passwd = passwd;
		return this;
	}
	
}
