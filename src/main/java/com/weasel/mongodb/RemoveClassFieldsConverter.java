package com.weasel.mongodb;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * when we save the entity to collection,thare is a "_class" fields be saved,if we don't want it,
 * we can offer this converter to MongoTemplate,instead of the default converter name MappingMongoConverter
 * @author Dylan
 * @time 2013-5-11 下午5:30:21
 */
public class RemoveClassFieldsConverter extends MappingMongoConverter {

	public RemoveClassFieldsConverter(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory, new MongoMappingContext());
		this.setTypeMapper(new DefaultMongoTypeMapper(null));
	}

}
