package com.weasel.spring.mongodb.test;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;
import com.weasel.mongodb.Page;

public class MongoRepositoryTest {

	private ClassPathXmlApplicationContext applicationContext;
	private UserRepository repository;
	@Before
	public void before(){
		applicationContext = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		repository = applicationContext.getBean(UserRepository.class);
	}
	
	@Test
	public void save(){
		User user = new User().setName("张三").setPasswd("123");
		user = repository.save(user);
	}
	
	@Test
	public void saveAll(){
		User u1 = new User().setName("李四").setPasswd("234");
		User u2 = new User().setName("张三").setPasswd("123");
		List<User> entites = Lists.newArrayList();
		entites.add(u2);
		entites.add(u1);
		repository.save(entites);
		Assert.assertEquals(2, repository.count());
	}
	
	
	@Test
	public void findOne(){
		User user2 = repository.findOne("518e12854545f347b161e019");
		Assert.assertNotNull(user2);
	}
	
	@Test
	public void findAll(){
	//	List<User> users = repository.findAll();
		List<String> ids = Lists.newArrayList();
		ids.add("518d9b4d0c1b679c7e474e25");
		ids.add("518d9b4d0c1b679c7e474e26");
		List<User> users = repository.findAll(ids);
		Assert.assertTrue(null != users && users.size()==2);
	}
	
	@Test
	public void findPage(){
		
		/*Page<User> page = new Page<User>(0,1);
		page = repository.findAll(page);

		for(User user : page.getResult()){
			System.out.println(user.getName());
		}*/
		/*page = repository.findAll(page.nextPage());
		for(User user : page.getResult()){
			System.out.println(user.getName());
		}*/
		Page<User> page = new Page<User>(Page.DESC,"passwd");
		page = repository.findAll(page);
		for(User user : page.getResult()){
			System.out.println(user.getName());
		}
	}
	
	@Test
	public void update(){
		User user = repository.findOne("518d9b4d0c1b679c7e474e26");
		user.setPasswd("345");
		user.setName("王五");
		repository.save(user);
		Assert.assertTrue(StringUtils.equals(repository.findOne("518d9b4d0c1b679c7e474e26").getName(),"王五"));
	}
	
	@Test
	public void delete(){
		repository.delete("518e116845458eb53c81b7b3");
		Assert.assertEquals(1, repository.count());
	}
	@Test
	public void deleteAll(){
		
	//	repository.deleteAll();
	//	repository.delete(repository.findOne("518d99d90c1b13037173a736"));
		repository.delete(repository.findAll());
		Assert.assertEquals(0, repository.count());
	}
	
	@Test
	public void exit(){
		Assert.assertTrue(repository.exists("518da7370c1b3cd0fdb33461"));
	}
}
